const form = document.getElementById('addressForm');
const addressType=document.getElementById('billingAddressType')
const addressLine1 = document.getElementById('addressLine1');
const addressLine2 = document.getElementById('addressLine2');
const state = document.getElementById('state');
const district = document.getElementById('district');
const pin_Code = document.getElementById('pin_code');
const contactNumber=document.getElementById('contactNumber');


const setError = (element, message, e) => {
    console.log(element)
    e.preventDefault();
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success');
};
const setSuccess = element => {
    console.log(element)
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.error');
    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
};


function validateInputs(e) {
    setSuccess(addressType)
    setSuccess(addressLine1);
    setSuccess(addressLine2);
    setSuccess(state);
    setSuccess(district);
    setSuccess(pin_Code);
    setSuccess(contactNumber)

    const addressType_Value=addressType.value.trim();
    const addressLine1_Value = addressLine1.value.trim();
    const addressLine2_Value = addressLine2.value.trim();
    const stateValue = state.value.trim();
    const districtValue = district.value.trim();
    const pin_codeValue = pin_Code.value.trim();
    const contactNumber_Value=contactNumber.value.trim();

    if(addressLine1_Value === '') {
        setError(addressLine1, 'Please enter address line 1', e);
        addressLine1.focus();
        return false;
    }
    else{
        setSuccess(addressLine1);
    }

    if(addressLine2_Value === '') {
        setError(addressLine2, 'Please enter address line 2', e);
        addressLine2.focus();
        return false;
    }
    else{
        setSuccess(addressLine2);
    }

    if(stateValue === '') {
        setError(state, 'Please enter City', e);
        state.focus();
        return false;
    }
    else{
        setSuccess(state);
    }

    if(districtValue === '') {
        setError(district, 'Please enter Country', e);
        district.focus();
        return false;
    }
    else{
        setSuccess(district);
    }
    if(addressType_Value === '') {
        setError(addressType, 'Enter the AddressType', e);
        addressType.focus();
        return false;
    }
    else{
        setSuccess(addressType);
    }



    const pinCodeRegex = /^\d{6}$/;
    if(pin_codeValue === ""){
        setError(pin_Code, 'Enter pinCode', e);
        pin_Code.focus();
        return false;
    }
    else if(!pin_codeValue.match(pinCodeRegex)){
        setError(pin_Code, 'Enter valid pinCode', e);
        pin_Code.focus();
        return false;
    }
    else{
        setSuccess(pin_Code);
    }
    const contactNumberRegex = /^\d{9}$/;
    if(contactNumber_Value === ""){
        setError(contactNumber, 'Enter Contact Number', e);
        contactNumber.focus();
        return false;
    }
    else if(!contactNumber_Value.match(contactNumberRegex)){
        setError(contactNumber, 'Enter Contact Number', e);
        contactNumber.focus();
        return false;
    }
    else{
        setSuccess(contactNumber);
    }


    return true;
}

form.addEventListener('submit', function(e) {
    console.log("hello");
    // e.preventDefault()

    if(validateInputs(e)){
        console.log("VALIDATION Success");
    }
})