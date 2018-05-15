

function showTasks(){
    const taskEl = document.getElementById("tasksUl");
    taskEl.classList.remove('hidden');
    taskButtonEl.removeEventListener('click', showTasks);
    taskButtonEl.addEventListener('click', hideTasks);
}

function hideTasks(){
    const taskEl = document.getElementById("tasksUl");
    taskEl.classList.add('hidden');
    const taskRem = document.getElementById("create-task");
    taskButtonEl.removeEventListener('click', hideTasks);
    if(taskRem !== null) {
        taskRem.remove();
    }
    taskButtonEl.addEventListener('click', showTasks);
}

function updateTask(e) {
    const buttonEl = e.target;
    const liEl = buttonEl.parentElement;
    const spanTask = liEl.firstElementChild;
    const descParEl = spanTask.firstElementChild;

    const oldTitle = spanTask.id;
    const oldDesc = descParEl.textContent;

    spanTask.remove();
    descParEl.remove();

    const titleInputEl = document.createElement("INPUT");
    titleInputEl.setAttribute("type", "text");
    titleInputEl.placeholder = oldTitle;

    const descInputEl = document.createElement("INPUT");
    descInputEl.setAttribute("type", "text");
    descInputEl.placeholder = oldDesc;

    buttonEl.removeEventListener('click', updateTask);
    buttonEl.addEventListener('click', applyTaskUpdates);

    liEl.insertBefore(titleInputEl, buttonEl);
    liEl.insertBefore(descInputEl, buttonEl);

}

function applyTaskUpdates(e) {
    const liEl = e.target.parentElement;
    const titleInputField = liEl.firstChild;
    const descInputField = liEl.children.item(1);


    const title = titleInputField.value;
    const desc = descInputField.value;
    const id = liEl.id;

    const data = JSON.stringify({"taskId": id, "description": desc, "title": title});

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdateTaskResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT','protected/task');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(data);
}

function onUpdateTaskResponse() {
    if(this.status === OK){
        const userDto = JSON.parse(this.responseText);
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
    }else{
        onMessageResponse(taskContentDivEl,this);
    }
}

function showCreateTask(){
    const toCreateTaskButt = document.getElementById("to-createTask-button");
    toCreateTaskButt.removeEventListener('click', showCreateTask);

    const createTaskDiv= document.createElement("div");
    createTaskDiv.setAttribute("id","create-task");

    const inputTitle = document.createElement("INPUT");
    inputTitle.setAttribute("type","text");
    inputTitle.setAttribute("id", "task-title");
    inputTitle.placeholder = "Title";

    const inputDescript = document.createElement("INPUT");
    inputDescript.setAttribute("type","text");
    inputDescript.setAttribute("id","task-desc");
    inputDescript.placeholder = "Description";

    const createTaskButt = document.createElement("button");
    createTaskButt.addEventListener('click',createTask);
    createTaskButt.textContent = "Create";

    createTaskDiv.appendChild(inputTitle);
    createTaskDiv.appendChild(inputDescript);
    createTaskDiv.appendChild(createTaskButt);
    taskContentDivEl.appendChild(createTaskDiv);
}

function createTask(){
    const toCreateTaskButt = document.getElementById("to-createTask-button");
    toCreateTaskButt.addEventListener('click', showCreateTask);
    const taskTitleInputEl = document.getElementById("task-title");
    const taskDescInputEl = document.getElementById("task-desc");
    const title = taskTitleInputEl.value;
    const description = taskDescInputEl.value;
    const params = new URLSearchParams();
    params.append('title',title);
    params.append('description',description);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load',onCreateTaskResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST','protected/task');
    xhr.send(params)
}

function onCreateTaskResponse(){
    if(this.status === OK){
        const userDto = JSON.parse(this.responseText);
        document.getElementById("create-task").remove();
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
    }else{
        onMessageResponse(taskContentDivEl,this);
    }
}

function showTaskDescription(e) {
    e.target.firstElementChild.classList.remove("hidden");
    e.target.removeEventListener('click', showTaskDescription);
    e.target.addEventListener('click', hideTaskDescription);
}

function hideTaskDescription(e) {
    e.target.firstElementChild.classList.add("hidden");
    e.target.removeEventListener('click', hideTaskDescription);
    e.target.addEventListener('click', showTaskDescription);
}

function removeTask(e) {
    const liEL = e.target.parentElement;
    const id = liEL.id;

    const params = new URLSearchParams();
    params.append('taskId', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onDeleteTaskResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'protected/task');
    xhr.send(params);
}

function onDeleteTaskResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
    } else {
        onMessageResponse(taskContentDivEl, this);
    }
}

function onGobackClicked() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLoginResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/task');
    xhr.send();
}

