const OK = 200;
const BAD_REQUEST = 400;
const UNAUTHORIZED = 401;
const NOT_FOUND = 404;
const CONFLICT = 409;
const INTERNAL_SERVER_ERROR = 500;

let loginContentDivEl;
let registerContentDivEl;
let logoutContentDivEl;
let profileContentDivEl;
let taskContentDivEl;
let taskDiv;


function newInfo(targetEl, message) {
    newMessage(targetEl, 'info', message);
}

function newError(targetEl, message) {
    newMessage(targetEl, 'error', message);
}

function newMessage(targetEl, cssClass, message) {
    clearMessages();

    const pEl = document.createElement('h2');
    pEl.classList.add('message');
    pEl.classList.add(cssClass);
    pEl.textContent = message;
    const firstChild = targetEl.firstElementChild;
    targetEl.insertBefore(pEl, firstChild);
}

function clearMessages() {
    const messageEls = document.getElementsByClassName('message');
    for (let i = 0; i < messageEls.length; i++) {
        const messageEl = messageEls[i];
        messageEl.remove();
    }
}

function showContents(ids) {
    const contentEls = document.getElementsByClassName('content');
    for (let i = 0; i < contentEls.length; i++) {
        const contentEl = contentEls[i];
        if (ids.includes(contentEl.id)) {
            contentEl.classList.remove('hidden');
        } else {
            contentEl.classList.add('hidden');
        }
    }
}

function removeAllChildren(el) {
    while (el.firstChild) {
        el.removeChild(el.firstChild);
    }
}

function onNetworkError(response) {
    document.body.remove();
    const bodyEl = document.createElement('body');
    document.appendChild(bodyEl);
    newError(bodyEl, 'Network error, please try reloading the page');
}

function onMessageResponse(targetEl, xhr) {
        const json = JSON.parse(xhr.responseText);
        if (xhr.status === INTERNAL_SERVER_ERROR) {
            newError(targetEl, `Server error: ${json.message}`);
        } else if (xhr.status === UNAUTHORIZED || xhr.status === BAD_REQUEST || xhr.status === CONFLICT || xhr.status === NOT_FOUND) {
            newError(targetEl, json.message);
        } else if (xhr.status === OK) {
            newInfo(targetEl, json.message);
        } else {
            newError(targetEl, `Unknown error: ${json.message}`);
        }
}

function hasAuthorization() {
    return sessionStorage.getItem('user') !== null;
}

function setAuthorization(user) {
    return sessionStorage.setItem('user', JSON.stringify(user));
}

function getAuthorization() {
    return JSON.parse(sessionStorage.getItem('user'));
}

function setUnauthorized() {
    return sessionStorage.removeItem('user');
}

function onLoad() {
    loginContentDivEl = document.getElementById('login-content');
    registerContentDivEl = document.getElementById('register-content');
    logoutContentDivEl = document.getElementById('logout-content');
    profileContentDivEl = document.getElementById('profile-content');
    taskContentDivEl = document.getElementById('tasks-content');

    const loginButtonEl = document.getElementById('login-button');
    loginButtonEl.addEventListener('click', onLoginButtonClicked);

    const registerButtonEl = document.getElementById('register-button');
    registerButtonEl.addEventListener('click', onRegisterButtonClicked);

    const logoutButtonEl = document.getElementById('logout-button');
    logoutButtonEl.addEventListener('click', onLogoutButtonClicked);

    taskDiv = document.getElementById("tasks");
    taskDiv.addEventListener('click',showTasks);

    if (hasAuthorization()) {
        onMainPageLoad(getAuthorization());
    }
}

document.addEventListener('DOMContentLoaded', onLoad);
