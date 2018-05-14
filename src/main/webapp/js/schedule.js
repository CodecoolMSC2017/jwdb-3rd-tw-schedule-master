let scheduleEl;

function showSchedule(){
    scheduleEl = document.getElementById("scheduleUl");
    scheduleEl.classList.remove('hidden');
    scheduleDiv.removeEventListener('click',showschedule);
    scheduleDiv.addEventListener('click',hideschedule);
}

function hideSchedule(){
    scheduleEl = document.getElementById("scheduleUl");
    scheduleEl.classList.add('hidden');
    scheduleDiv.removeEventListener('click',hideSchedule);
    scheduleDiv.addEventListener('click',showSchedule);
}

function renameScheduleTitle(e){
    const liEl = e.source.parentElement;
    const buttonEl = e.source;
    const spanSchedule = e.source.parentElement.firstChild;
    liEl.remove(spanSchedule);
    const inputField = document.createElement("INPUT");
    inputField.setAttribute("type","text");
    buttonEl.removeEventListener('click',renameScheduleTitle);
    buttonEl.addEventListener('click',setScheduleTitle)
    liEl.insertBefore(inputField,buttonEl);
}

function setScheduleTitle(e){
    const liEl = e.source.parentElement;
    const buttonEl = e.source;
    const inputField = e.source.parentElement.firstChild;
    const title = inputField.value;
    const params = new URLSearchParams();
    params.append('title',title);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load',onRenameScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT','protected/schedule');
    xhr.send(params);
}

function onRenameScheduleResponse(){
    if(this.status === OK){
        const userDto = JSON.parse(this.responseText);
        taskEl = document.getElementById("scheduleUl");
        const parentEl = taskEl.parentElement;
        parentEl.remove(taskEl);
        onMainPageLoad(userDto);
    }else{
        onMessageResponse(scheduleContentDivEl,this);
    }
}

function showCreateSchedule(){
    const createScheduleDiv= document.createElement("div");
    createScheduleDiv.setAttribute("id","create-schedule");
    const inputTitle = document.createElement("INPUT");
    inputTitle.setAttribute("type","text");
    inputTitle.setAttribute("id", "schedule-title");
    inputTitle.placeholder = "Title";
    const inputDescript = document.createElement("INPUT");
    inputDescript.setAttribute("type","text");
    inputDescript.setAttribute("id","schedule-desc");
    inputDescript.placeholder = "Description";
    const createScheduleButt = document.createElement("button");
    createScheduleButt.addEventListener('click',createSchedule);
    createScheduleButt.textContent = "Create";
    createScheduleDiv.appendChild(inputTitle);
    createScheduleDiv.appendChild(inputDescript);
    createScheduleDiv.appendChild(createScheduleButt);
    scheduleContentDivEl.appendChild(createScheduleDiv);
}

function createSchedule(){
    const scheduleTitleInputEl = document.getElementById("schedule-title");
    const scheduleDescInputEl = document.getElementById("schedule-desc");
    const title = scheduleTitleInputEl.value;
    const description = scheduleDescInputEl.value;
    const params = new URLSearchParams();
    params.append('title',title);
    params.append('description',description);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load',onCreateScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST','protected/schedule');
    xhr.send(params)
}

function onCreateScheduleResponse(){
    if(this.status === OK){
        const userDto = JSON.parse(this.responseText);
        scheduleEl = document.getElementById("create-schedule");
        const parentEl = scheduleEl.parentElement;
        parentEl.remove(scheduleEl);
        onMainPageLoad(userDto);
    }else{
        onMessageResponse(scheduleContentDivEl,this);
    }
}
