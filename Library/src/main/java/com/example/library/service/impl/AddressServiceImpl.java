package com.example.library.service.impl;

import com.example.library.dto.AddressDto;
import com.example.library.model.Address;
import com.example.library.model.Customer;
import com.example.library.repository.AddressRepository;
import com.example.library.service.AddressService;
import com.example.library.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private CustomerService customerService;
    private AddressRepository addressRepository;


    public AddressServiceImpl(CustomerService customerService, AddressRepository addressRepository){
        this.customerService = customerService;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(AddressDto addressDto, String username) {
        Customer customer = customerService.findByUsername(username);

        Address address = new Address();
        address.setAddressType(addressDto.getAddressType());
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setState(addressDto.getState());
        address.setDistrict(addressDto.getDistrict());
        address.setPin_code(addressDto.getPin_code());
        address.setContactNumber(addressDto.getContactNumber());
        address.setCustomer(customer);
        address.setActive(true);

        return addressRepository.save(address);
    }

    @Override
    public Address update(AddressDto addressDto) {
        long id = addressDto.getId();
        Address address = addressRepository.findById(id);
        address.setAddressType(addressDto.getAddressType());
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setState(addressDto.getState());
        address.setDistrict(addressDto.getDistrict());
        address.setPin_code(addressDto.getPin_code());
        address.setContactNumber(addressDto.getContactNumber());
        return addressRepository.save(address);
    }

    @Override
    public AddressDto findById(long id) {
        Address address = addressRepository.findById(id);
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setAddressType(address.getAddressType());
        addressDto.setAddressLine1(address.getAddressLine1());
        addressDto.setAddressLine2(address.getAddressLine2());
        addressDto.setState(address.getState());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setPin_code(address.getPin_code());
        addressDto.setContactNumber(address.getContactNumber());
        return addressDto;
    }

    @Override
    public void deleteAddress(long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public void enable(long id) {
        Address address = addressRepository.findById(id);
        address.setActive(true);

        addressRepository.save(address);
    }

    @Override
    public void disable(long id) {
        Address address = addressRepository.findById(id);
        address.setActive(false);

        addressRepository.save(address);
    }

    @Override
    public Address findDefaultAddress(long customer_id) {
        return addressRepository.findByActivatedTrue(customer_id);
    }

    @Override
    public Address findByIdOrder(long id) {
        System.out.println("checked addressService findBy id");
        return addressRepository.findById(id);
    }
}
