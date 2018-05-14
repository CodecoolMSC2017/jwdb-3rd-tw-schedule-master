let taskEl;

function showTasks(){
    taskEl = document.getElementById("tasksUl");
    taskEl.classList.remove('hidden');
    taskDiv.removeEventListener('click',showTasks);
    taskDiv.addEventListener('click',hideTasks);
}

function hideTasks(){
    taskEl = document.getElementById("tasksUl");
    taskEl.classList.add('hidden');
    taskDiv.removeEventListener('click',hideTasks);
    taskDiv.addEventListener('click',showTasks);
}

function renameTaskTitle(e){
    const liEl = e.source.parentElement;
    const buttonEl = e.source;
    const spanTask = e.source.parentElement.firstChild;
    liEl.remove(spanTask);
    const inputField = document.createElement("INPUT");
    inputField.setAttribute("type","text");
    buttonEl.removeEventListener('click',renameTaskTitle);
    buttonEl.addEventListener('click',setTaskTitle)
    liEl.insertBefore(inputField,buttonEl);

}

function setTaskTitle(e){
    const liEl = e.source.parentElement;
    const buttonEl = e.source;
    const inputField = e.source.parentElement.firstChild;
    const title = inputField.value;
    const params = new URLSearchParams();
    params.append('title',title);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load',onRenameResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT','protected/task');
    xhr.send(params);
}

function onRenameResponse(){
    if(this.status === OK){
        const userDto = JSON.parse(this.responseText);
        taskEl = document.getElementById("tasksUl");
        const parentEl = taskEl.parentElement;
        parentEl.remove(taskEl);
        onMainPageLoad(userDto);
    }else{
        onMessageResponse(taskContentDivEl,this);
    }
}

function showCreateTask(){
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
        taskEl = document.getElementById("create-task");
        const parentEl = taskEl.parentElement;
        parentEl.remove(taskEl);
        onMainPageLoad(userDto);
    }else{
        onMessageResponse(taskContentDivEl,this);
    }
}
