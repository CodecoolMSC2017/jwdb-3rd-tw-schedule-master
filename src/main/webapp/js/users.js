let usersTableEl;
let usersTableHeadEl;
let usersTableBodyEl;

function onUserListingLoad(userDto){
    clearMessages();
    showContents(['users-content','name-logout-content', 'logout-content']);
    createUsersTableHeader();
    usersTableBodyEl = usersTableEl.querySelector('tbody');
    appendUsers(users);
}

function appendUser(user) {
    const nameTdEl = document.createElement('td');
    nameTdEl.textContent = user.userName;

    const emailTdEl = document.createElement('td');
    emailTdEl.textContent = user.email;

    const roleTdEl = document.createElement('td');
    roleTdEl.textContent = user.role;

    const trEl = document.createElement('tr');
    trEl.setAttribute('id',user.id);
    trEl.addEventListener('click', showAsUser);
    trEl.appendChild(nameTdEl);
    trEl.appendChild(emailTdEl);
    trEl.appendChild(roleTdEl);
    usersTableBodyEl.appendChild(trEl);
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

    trEl.appendChild(nameThEl);
    trEl.appendChild(emailThEl);
    trEl.appendChild(roleThEl);
    usersTableHeadEl.appendChild(trEl);

}

function showAsUser(e){
    const id = e.target.id;
    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'login?' + params.toString());
    xhr.send();
}