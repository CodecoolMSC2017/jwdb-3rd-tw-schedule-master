function onLogoutResponse() {
    if (this.status === OK) {
        setUnauthorized();
        const auth2 = gapi.auth2.getAuthInstance();
        if(auth2 != null) {
        auth2.disconnect();
        }
        clearMessages();

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
