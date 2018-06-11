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
let scheduleContentDivEl;
let scheduleDiv;
let mainDiv;
let daysContentDivEl;
let daysDiv;
let userContentDivEl;
let trashEl;
let currentScheduleId = null;



function newInfo(targetEl, message) {
    newMessage(targetEl, 'info', message);
}

function newError(targetEl, message) {
    newMessage(targetEl, 'error', message);
}

function newMessage(targetEl, cssClass, message) {
    clearMessages();

    const pEl = document.createElement('h3');
    pEl.classList.add('message');
    pEl.classList.add(cssClass);
    pEl.textContent = message;
    const firstChild = targetEl.firstChild;
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

function drag_over_trash(ev) {
    ev.preventDefault();
    const trash = document.getElementById("trash").firstElementChild;
    trash.style.transform = "rotate(-45deg)";
    trash.style.transition = "transform 250ms";
}

function drag_leave(ev){
    ev.preventDefault();
    const trash = document.getElementById("trash").firstElementChild;
    trash.style.transform = "rotate(0deg)";
    trash.style.transition = "transform 250ms";
}

function remove_from_schedule(ev){
    ev.preventDefault();
    const taskId = ev.dataTransfer.getData('text');
    const scheduleId = document.getElementById("days").firstElementChild.id;

    const trash = document.getElementById("trash").firstElementChild;
    trash.removeAttribute("class");
    trash.style.transform = "rotate(0deg)";
    trash.style.transition = "transform 250ms";

    const data = JSON.stringify({"taskId": taskId, "scheduleId": scheduleId});
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onDeleteFromScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'protected/taskHour');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(data);
}

function onDeleteFromScheduleResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        removeAllChildren(daysDiv);
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
        listingDays(userDto);
    }
    else {
        onMessageResponse(mainDiv, this);
    }
}

function onLoad() {
    loginContentDivEl = document.getElementById('login-content');
    registerContentDivEl = document.getElementById('register-content');
    logoutContentDivEl = document.getElementById('logout-content');
    profileContentDivEl = document.getElementById('profile-content');
    taskContentDivEl = document.getElementById('tasks-content');
    taskDiv = document.getElementById("tasks");
    scheduleContentDivEl = document.getElementById('schedules-content');
    scheduleDiv = document.getElementById("schedules");
    daysContentDivEl = document.getElementById("days-content");
    daysDiv = document.getElementById("days");
    mainDiv = document.getElementById("name-logout-content");
    userContentDivEl = document.getElementById('users-content');

    trashEl = document.getElementById("trash");
    trashEl.addEventListener('drop',remove_from_schedule);
    trashEl.addEventListener('dragenter', drag_enter_prevent);
    trashEl.addEventListener('dragover', drag_over_trash);
    trashEl.addEventListener('dragleave', drag_leave);

    const loginButtonEl = document.getElementById('login-button');
    loginButtonEl.addEventListener('click', onLoginButtonClicked);

    const registerButtonEl = document.getElementById('register-button');
    registerButtonEl.addEventListener('click', onRegisterButtonClicked);

    const logoutButtonEl = document.getElementById('logout-button');
    logoutButtonEl.addEventListener('click', onLogoutButtonClicked);

    const adminGoBackButton = document.getElementById('admin-button');
    adminGoBackButton.addEventListener('click',adminGoBackButtonClicked);

    if (hasAuthorization()) {
        onMainPageLoad(getAuthorization());
    }

    if (document.documentURI.includes("guest")) {
        const xhr = new XMLHttpRequest();

        taskDiv.remove();
        scheduleDiv.remove();



        xhr.addEventListener('load', onGuestResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('POST', document.documentURI);
        xhr.send();
    }

    function onGuestResponse() {
        if (this.status === OK) {
            const dto = JSON.parse(this.responseText);
            listingDays(dto);
            showContents(['days-content']);
        } else {
            onMessageResponse(mainDiv, this);
        }
    }
}

document.addEventListener('DOMContentLoaded', onLoad);
