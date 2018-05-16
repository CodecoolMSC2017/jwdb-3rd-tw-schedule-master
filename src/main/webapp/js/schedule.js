

function showSchedule() {
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.remove('hidden');
    scheduleButtonEl.removeEventListener('click',showSchedule);
    scheduleButtonEl.addEventListener('click',hideSchedule);
}

function hideSchedule() {
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.add('hidden');
    const sched = document.getElementById("create-schedule");
    scheduleButtonEl.removeEventListener('click',hideSchedule);
    if(sched !== null) {
         sched.remove();
    }
    scheduleButtonEl.addEventListener('click',showSchedule);
}

function showCreateSchedule() {
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

    const inputNumberOfDays = document.createElement("INPUT");
    inputNumberOfDays.setAttribute("type","number");
    inputNumberOfDays.setAttribute("id", "schedule-days");
    inputNumberOfDays.placeholder ="Number of Days";
    inputNumberOfDays.max = 7;
    inputNumberOfDays.min = 1;
    inputNumberOfDays.value = 3;

    const createScheduleButt = document.createElement("button");
    createScheduleButt.addEventListener('click',createSchedule);
    createScheduleButt.textContent = "Create";

    createScheduleDiv.appendChild(inputTitle);
    createScheduleDiv.appendChild(inputDesc);
    createScheduleDiv.appendChild(inputNumberOfDays);
    createScheduleDiv.appendChild(createScheduleButt);
    scheduleContentDivEl.appendChild(createScheduleDiv);
}

function createSchedule(){
    const toCreateScheduleButt = document.getElementById("to-createSchedule-button");
    toCreateScheduleButt.addEventListener('click', showCreateSchedule);

    const scheduleTitleInputEl = document.getElementById("schedule-title");
    const scheduleDescInputEl = document.getElementById("schedule-desc");
    const scheduleNumberOfDays = document.getElementById("schedule-days");

    const title = scheduleTitleInputEl.value;
    const description = scheduleDescInputEl.value;
    const days = scheduleNumberOfDays.value;

    if(title !== "" && description !== ""){
        const params = new URLSearchParams();
        params.append('title',title);
        params.append('description',description);
        params.append('days',days);

        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load',onCreateScheduleResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('POST','protected/schedule');
        xhr.send(params)
    }else{
        newError(scheduleContentDivEl,"Please fill all the fields");
    }
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
    removeAllChildren(daysDiv);
    const data = JSON.stringify({"scheduleId": id});


    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onDeleteScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'protected/schedule');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(data);
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

function listingSchedules(e){
    e.target.removeEventListener('click', listingSchedules);
    e.target.addEventListener('click', hideListingSchedules);
    const idSchedule = e.target.parentElement.id;
    const xhr = new XMLHttpRequest();

    const params = new URLSearchParams();
    params.append("scheduleId",idSchedule);

    xhr.addEventListener('load', onListingResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedule?' + params.toString());
    xhr.send();
}

function onListingResponse(){
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        listingDays(userDto);
    } else {
        onMessageResponse(daysContentDivEl, this);
    }
}

function listingDays(userDto){
    const table = document.createElement("table");
    table.setAttribute("id", "schedule-table");
    let tr = document.createElement("tr");
    for(let i = 0; i < userDto.schedule.days.length ; i++){
        let td = document.createElement("td");
        let hoursTable = document.createElement("table");
        td.textContent = userDto.schedule.days[i].title;
        for(let j = 0; j < userDto.schedule.days[i].hours.length ; j++){
            let hoursTr = document.createElement("tr");
            let hoursTd = document.createElement("td");
            hoursTd.textContent = userDto.schedule.days[i].hours[j].value;
            hoursTr.appendChild(hoursTd);
            hoursTable.appendChild(hoursTr);

        }
        td.appendChild(hoursTable);
        tr.appendChild(td);
    }
    table.appendChild(tr);
    daysDiv.appendChild(table);
}

function hideListingSchedules(e) {
    e.target.removeEventListener('click', hideListingSchedules);
    e.target.addEventListener('click', listingSchedules);
    removeAllChildren(daysDiv);
}
