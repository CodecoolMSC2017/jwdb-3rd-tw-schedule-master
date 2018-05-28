function onLogoutResponse() {
    if (this.status === OK) {
        setUnauthorized();
        clearMessages();
        localStorage.clear();
        showContents(['login-content']);
        onMessageResponse(loginContentDivEl, this);
    } else {
        onMessageResponse(logoutContentDivEl, this);
    }
}

function onLogoutButtonClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLogoutResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'logout');
    xhr.send();
}
