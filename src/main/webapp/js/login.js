function onLoginResponse() {
    if (this.status === OK) {
        const dto = JSON.parse(this.responseText);
        setAuthorization(dto.user);
        if(dto.user.role === 'admin'){
            onUserListingLoad(dto);
        }else{
            onMainPageLoad(dto);
        }
    } else {
        onMessageResponse(loginContentDivEl, this);
    }
}

function onLoginButtonClicked() {

    const loginFormEl = document.forms['login-form'];

    const emailInputEl = loginFormEl.querySelector('input[name="email"]');
    const passwordInputEl = loginFormEl.querySelector('input[name="password"]');

    const email = emailInputEl.value;
    const password = passwordInputEl.value;
    if (email == null || email === "") {
        newError(loginFormEl, "Enter an e-mail address!");
        passwordInputEl.value = "";
        return false;
    }
    if (password == null || password === "") {
        newError(loginFormEl, "Enter a password!");
        return false;
    }

    const params = new URLSearchParams();
    params.append('email', email);
    params.append('password', password);

    localStorage.setItem('email', email);
    localStorage.setItem('password', password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'login');
    xhr.send(params);
}

function toRegistration() {
    clearMessages();
    showContents(['register-content']);
}
