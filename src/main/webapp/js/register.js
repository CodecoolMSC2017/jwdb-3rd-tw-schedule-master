function onRegisterButtonClicked() {
    const registerFormEl = document.forms['register-form'];

    const nameInputEl = registerFormEl.querySelector('input[name="name"]');
    const emailInputEl = registerFormEl.querySelector('input[name="email"]');
    const passwordInputEl = registerFormEl.querySelector('input[name="password"]');
    const passwordAgainInputEl = registerFormEl.querySelector('input[name="password-again"]');

    const name = nameInputEl.value;
    const email = emailInputEl.value;
    const password = passwordInputEl.value;
    const passwordAgain = passwordAgainInputEl.value;

    if (name == null || name == "") {
        newError(registerFormEl, "Enter a name!");
        passwordInputEl.value = "";
        passwordAgainInputEl.value = "";
        return false;
    }
    if (email == null || email == "") {
        newError(registerFormEl, "Enter an e-mail address!");
        passwordInputEl.value = "";
        passwordAgainInputEl.value = "";
        return false;
    }
    if (password == null || password == "") {
        newError(registerFormEl, "Enter a password!");
        passwordAgainInputEl.value = "";
        return false;
    }
    if (password.length < 8) {
        newError(registerFormEl, "Password must be longer than 8 character!");
        return false;
    }
    if (passwordAgain == null || passwordAgain == "") {
        newError(registerFormEl, "Please enter the password again!");
        passwordInputEl.value = "";
        return false;
    }
    if (password !== passwordAgain) {
        newError(registerFormEl, "The two password must match!");
        return false;
    }

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('email', email);
    params.append('password', password);
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onRegisterResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'register');
    xhr.send(params);
}

function onRegisterResponse() {
    if (this.status === OK) {
        clearMessages();
        showContents(['login-content']);
        onOtherResponse(loginContentDivEl, this);
    } else {
        onOtherResponse(registerContentDivEl, this);
    }
}

function toLogin() {
    clearMessages();
    showContents(['login-content']);
}