

function showSchedule(){
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.remove('hidden');
    scheduleButtonEl.removeEventListener('click',showSchedule);
    scheduleButtonEl.addEventListener('click',hideSchedule);
}

function hideSchedule(){
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.add('hidden');
    scheduleButtonEl.removeEventListener('click',hideSchedule);
    document.getElementById("create-schedule").remove();
    scheduleButtonEl.addEventListener('click',showSchedule);
}

function showCreateSchedule(){
    const toCreateScheduleButt = document.getElementById("to-createSchedule-button");
    toCreateScheduleButt.removeEventListener('click',showCreateSchedule);

    const createScheduleDiv= document.createElement("div");
    createScheduleDiv.setAttribute("id","create-schedule");

    const inputTitle = document.createElement("INPUT");
    inputTitle.setAttribute("type","text");
    inputTitle.setAttribute("id", "schedule-title");
    inputTitle.placeholder = "Title";

    const inputDesc = document.createElement("INPUT");
    inputDesc.setAttribute("type","text");
    inputDesc.setAttribute("id", "schedule-desc");
    inputDesc.placeholder = "Description";

    const createScheduleButt = document.createElement("button");
    createScheduleButt.addEventListener('click',createSchedule);
    createScheduleButt.textContent = "Create";

    createScheduleDiv.appendChild(inputTitle);
    createScheduleDiv.appendChild(inputDesc);
    createScheduleDiv.appendChild(createScheduleButt);
    scheduleContentDivEl.appendChild(createScheduleDiv);
}

function createSchedule(){
    const toCreateScheduleButt = document.getElementById("to-createSchedule-button");
    toCreateScheduleButt.addEventListener('click', showCreateSchedule);
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
        document.getElementById("create-schedule").remove();
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    }else{
        onMessageResponse(scheduleContentDivEl,this);
    }
}

function removeSchedule(e) {
    const liEL = e.target.parentElement;
    const id = liEL.id;

    const params = new URLSearchParams();
    params.append('scheduleId', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onDeleteScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'protected/schedule');
    xhr.send(params);
}

function onDeleteScheduleResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    } else {
        onMessageResponse(scheduleContentDivEl, this);
    }
}
