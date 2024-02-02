const form = document.getElementById('accountForm');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const phone = document.getElementById('phoneNumber');

$("#update").on("click", function (event) {
    alert("Button clicked!");
    //alert("Inside functionnnnnnn")
    event.preventDefault();
    $("#bdashboard-ta").hide();
    $("#account-tab").show();

    $("#orders-tab").hide();

    $("#address").hide();




});
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
    setSuccess(firstName);
    setSuccess(lastName);
    setSuccess(phone);


    const firstNameValue = firstName.value.trim();
    const lastNameValue = lastName.value.trim();
    const phoneValue = phone.value.trim();

    if(firstNameValue === '') {
        setError(firstName, 'Please enter first name', e);
        firstName.focus();
        return false;
    }
    else{
        setSuccess(firstName);
    }
    if(lastNameValue === '') {
        setError(lastName, 'Please enter last name', e);
        lastName.focus();
        return false;
    }
    else{
        setSuccess(lastName);
    }



    const phonenoRegex = /^\d{10}$/;
    if(phoneValue === ""){
        setError(phone, 'Enter phone number', e);
        phone.focus();
        return false;
    }
    else if(!phoneValue.match(phonenoRegex)){
        setError(phone, 'Enter valid number', e);
        phone.focus();
        return false;
    }
    else{
        setSuccess(phone);
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