let usersTableEl;
let usersTableHeadEl;
let usersTableBodyEl;

function onUserListingLoad(adminDto) {
    clearMessages();
    showContents(['users-content','name-logout-content', 'logout-content']);
    createUsersTableHeader();
    usersTableBodyEl = usersTableEl.querySelector('tbody');
    appendUsers(adminDto.users);

    const nameField = document.getElementById("name-field");
    nameField.textContent = 'admin';
}

function appendUser(user) {
    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = user.userName;

    const emailTdEl = document.createElement('td');
    emailTdEl.textContent = user.email;
    emailTdEl.setAttribute("class", "email-td-el");

    const roleTdEl = document.createElement('td');
    roleTdEl.textContent = user.role;

    const removeButt = document.createElement('td');
    const removeUserButt = document.createElement("button");
    removeUserButt.addEventListener('click', ConfirmRemoveUser);
    removeUserButt.setAttribute("class", "delete-btn-min");
    removeButt.appendChild(removeUserButt);

    const trEl = document.createElement('tr');
    trEl.setAttribute('id',user.id);
    emailTdEl.addEventListener('click', showAsUser);

    if(roleTdEl.textContent !== 'admin'){
        trEl.appendChild(nameTdEl);
        trEl.appendChild(emailTdEl);
        trEl.appendChild(roleTdEl);
        trEl.appendChild(removeButt);
        usersTableBodyEl.appendChild(trEl);
    }
}

function appendUsers(users) {
    removeAllChildren(usersTableBodyEl);

    for (let i = 0; i < users.length; i++) {
        const user = users[i];
        appendUser(user);
    }
}

function createUsersTableHeader() {
    usersTableEl = document.getElementById('users');
    usersTableHeadEl = usersTableEl.querySelector('thead');
    removeAllChildren(usersTableHeadEl);

    const trEl = document.createElement('tr');

    const nameThEl = document.createElement('th');
    nameThEl.textContent = 'Name';

    const emailThEl = document.createElement('th');
    emailThEl.textContent = 'Email';

    const roleThEl = document.createElement('th');
    roleThEl.textContent = 'Role';

    const removeThEl = document.createElement('th');
    removeThEl.textContent = 'Remove';

    trEl.appendChild(nameThEl);
    trEl.appendChild(emailThEl);
    trEl.appendChild(roleThEl);
    trEl.appendChild(removeThEl);
    usersTableHeadEl.appendChild(trEl);

}

function showAsUser(e){
    const id = e.target.parentElement.id;
    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAdminLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'login?' + params.toString());
    xhr.send();
}

function ConfirmRemoveUser(e) {
    const name = e.target.parentElement.parentElement.firstElementChild.textContent;
    Confirm('Are you sure to delete this user?', 'The user named ' + name + ' will be deleted.', 'OK', 'Cancel', function () {
        removeUser(e);
    });
}
function removeUser(e) {
        const trEl = e.target.parentElement.parentElement;
        const id = trEl.id;

        const data = JSON.stringify({"userId": id});
        trEl.remove();


        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onDeleteUserResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('DELETE', 'login');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(data);
}

function onDeleteUserResponse(){
    if (this.status === OK) {
        const adminDto = JSON.parse(this.responseText);
        onUserListingLoad(adminDto);
    } else {
        onMessageResponse(userContentDivEl, this);
    }
}

function onAdminLoginResponse() {
    if (this.status === OK) {
        const dto = JSON.parse(this.responseText);
        removeAllChildren(daysDiv);
        onMainPageLoad(dto);
    } else {
        onMessageResponse(userContentDivEl, this);
    }
}

function adminGoBackButtonClicked(){

    const nameField = document.getElementById("name-field");
    nameField.textContent = 'admin';

    const params = new URLSearchParams();

    params.append('email', getAuthorization().email);
    params.append('password', getAuthorization().password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'login');
    xhr.send(params);
}