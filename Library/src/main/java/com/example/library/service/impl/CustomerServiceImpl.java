package com.example.library.service.impl;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Address;
import com.example.library.model.Customer;
import com.example.library.model.EmailDetails;
import com.example.library.repository.CustomerRepository;
import com.example.library.repository.RoleRepository;
import com.example.library.service.CustomerService;
import com.example.library.service.EmailService;
import com.example.library.service.WalletService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private EmailService emailService;
    private WalletService walletService;

    public CustomerServiceImpl(CustomerRepository customerRepository, RoleRepository roleRepository, EmailService emailService, WalletService walletService){
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.walletService = walletService;
    }

    private static final long OTP_VALID_DURATION = 1 * 60 * 1000;

    public Date getOtpRequestedTime(){
        return getOtpRequestedTime();
    }

    public void setOtpRequestedTime(Date otpRequestedTime){
        this.otpRequestedTime = otpRequestedTime;
    }

    private Date otpRequestedTime;

    long otpRequestedTimeMillis = 0;

    @Override
    public CustomerDto save(CustomerDto customerDto){
        Customer customer=new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setUsername(customerDto.getUsername());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setRoles(Arrays.asList(roleRepository.findByName("CUSTOMER")));
        String enteredReferral = customerDto.getReferralCode();

        if (enteredReferral != null) {
            try {
                Customer referralOwnerCustomer = customerRepository.findByReferralCode(enteredReferral);
                if (referralOwnerCustomer != null) {
                    boolean status = walletService.saveReferralOfer(200.00, referralOwnerCustomer);
                    if (!status) {
                        throw  new RuntimeException("Referral offer transaction failed");
                    }
                }
            }catch (NullPointerException e) {
                throw new RuntimeException("No Referral found");
            }
        }
        customer.setReferralCode(referralCodeGenerator());
        Customer customerSave=customerRepository.save(customer);
        return mapperDto(customerSave);
    }
    @Override
    public Customer findById(long id) {
        System.out.println("Customer findBy id");
        return customerRepository.findById(id);
    }
    @Override
    public Customer findByUsername(String username){
        return customerRepository.findByUsername(username);
    }
    @Override
    public CustomerDto getCustomer(String username){
        CustomerDto customerDto=new CustomerDto();
        Customer customer=customerRepository.findByUsername(username);
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        // customerDto.setAddresses(customer.getAddresses().toString());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        return customerDto;
    }

    @Override
    public Customer changePass(Customer customer){
        Customer username=customerRepository.findByUsername(customer.getUsername());
        username.setPassword(username.getPassword());
        return customerRepository.save(username);
    }
    @Override
    public Customer update(CustomerDto dto) {
        Customer customer = customerRepository.findByUsername(dto.getUsername());
        // customer.setAddresses(dto.getAddresses());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return customerRepository.save(customer);
    }
    @Override
    public CustomerDto updateAccount(CustomerDto customerDto,String email) {
        Customer customer= findByUsername(email);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customerRepository.save(customer);
        CustomerDto customerDtoUpdated = convertEntityToDto(customer);
        return customerDtoUpdated;
    }
    public CustomerDto convertEntityToDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setBlocked(customer.isBlocked());
        customerDto.setPassword(customer.getPassword());

        return customerDto;
    }

    @Override
    public CustomerDto findByEmailCustomerDto(String email) {
        Customer customer = customerRepository.findByUsername(email);
        CustomerDto customerDto=new CustomerDto();
        customerDto.setUsername(customer.getUsername());
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setAddresses(customer.getAddresses().toString());
        customerDto.setPassword(customer.getPassword());
        customerDto.setBlocked(customer.isBlocked());

        return customerDto;
    }


    @Override
    public List<CustomerDto> findAll() {
        List<CustomerDto>  customerDtoList = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();
        for(Customer customer:customers){
            CustomerDto customerDto = new CustomerDto();
            customerDto.setId(customer.getId());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setLastName(customer.getLastName());
            customerDto.setUsername(customer.getUsername());
            customerDto.setPhoneNumber(customer.getPhoneNumber());
             customerDto.setBlocked(customer.isBlocked());
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    @Override
    public void save(Customer customer, Address address) {

    }

    @Override
    public String generateOTPAndSendOnEmail(String username) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            return "customer not found";
        }
        int otp = (int) (Math.random() * 9000) + 1000;
        customer.setOtp(otp);
        customerRepository.save(customer);
        setOtpRequestedTime(new Date());
        otpRequestedTimeMillis = otpRequestedTime.getTime();
        return emailService.sendSimpleMail(new EmailDetails(username, "Your OTP for verification is" + otp, "Verify with OTP"));
    }

    @Override
    public boolean verifyOTP(long otp, String username) {
        Customer customer = customerRepository.findByUsername(username);
        long currentTimeInMillis = System.currentTimeMillis();
        System.out.println("currentTimeInMillis:" + currentTimeInMillis);
        System.out.println("otpRequestedTimeMillis" + otpRequestedTimeMillis);
        if (otpRequestedTimeMillis + OTP_VALID_DURATION > currentTimeInMillis){
            if (otp == customer.getOtp())
                return true;
            else
                return false;
        }
        else {
            return false;
        }
    }

    @Override
    public String generateOTPAndSendOnMobile(Customer customer) {
       return null;
    }

    @Override
    public String shareReferralCode(String referralCode, String emailAddress) {
        return emailService.sendSimpleMail(new EmailDetails(emailAddress,"Hey, I wanted to share my referral code for Italymuss watches with you:" + referralCode, "check out my Italymuss watches referral code!"));
    }

    @Override
    public Customer findByReferralCode(String referralCode) {
        return customerRepository.findByReferralCode(referralCode);
    }


    private CustomerDto mapperDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customerDto.getLastName());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        return customerDto;
    }
//    @Override
//    public void blockUser(Long id){
//        Customer customer=customerRepository.getReferenceById(id);
//        if(customer.is_blocked()){
//            customer.set_blocked(false);
//        }else{
//            customer.set_blocked(true);
//        }
//        customerRepository.save(customer);
//    }

    @Override
    public void blockById(Long id) {
        Customer customer=customerRepository.getReferenceById(id);
        customer.setBlocked(true);
        customerRepository.save(customer);
    }

    @Override
    public void unblockById(Long id) {
        Customer customer=customerRepository.getReferenceById(id);
        customer.setBlocked(false);
        customerRepository.save(customer);
    }

    private String referralCodeGenerator() {
        char chars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
