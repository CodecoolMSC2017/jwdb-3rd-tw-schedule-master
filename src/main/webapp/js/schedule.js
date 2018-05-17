function showSchedule() {
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.remove('hidden');
    scheduleButtonEl.removeEventListener('click', showSchedule);
    scheduleButtonEl.addEventListener('click', hideSchedule);
}

function hideSchedule() {
    const scheduleEl = document.getElementById("schedulesUl");
    scheduleEl.classList.add('hidden');
    scheduleButtonEl.removeEventListener('click', hideSchedule);
    scheduleButtonEl.addEventListener('click', showSchedule);
}

function showCreateSchedule() {
    const toCreateScheduleButt = document.getElementById("to-createSchedule-button");
    toCreateScheduleButt.removeEventListener('click', showCreateSchedule);

    const createScheduleDiv = document.createElement("div");
    createScheduleDiv.setAttribute("id", "create-schedule");
    createScheduleDiv.setAttribute("class", "create-div");

    const closeScheduleButt = document.createElement("button");
    closeScheduleButt.addEventListener('click', closeCreateSchedule);
    closeScheduleButt.setAttribute("class", "close-btn");

    const inputTitle = document.createElement("INPUT");
    inputTitle.setAttribute("type", "text");
    inputTitle.setAttribute("id", "schedule-title");
    inputTitle.setAttribute("class", "input-min");
    inputTitle.placeholder = "Title";

    const inputDesc = document.createElement("INPUT");
    inputDesc.setAttribute("type", "text");
    inputDesc.setAttribute("id", "schedule-desc");
    inputDesc.setAttribute("class", "input-min");
    inputDesc.placeholder = "Description";

    const numOfDaysSpanEl = document.createElement("span");
    numOfDaysSpanEl.textContent = "Number of days :";

    const inputNumberOfDays = document.createElement("INPUT");
    inputNumberOfDays.setAttribute("type", "number");
    inputNumberOfDays.setAttribute("id", "schedule-days")
    inputNumberOfDays.setAttribute("class", "input-min");
    inputNumberOfDays.placeholder = "Number of Days";
    inputNumberOfDays.max = 7;
    inputNumberOfDays.min = 1;
    inputNumberOfDays.value = 3;

    const breakEl = document.createElement("br");

    const createScheduleButt = document.createElement("button");
    createScheduleButt.addEventListener('click', createSchedule);
    createScheduleButt.setAttribute("id", "schedule-create-btn");
    createScheduleButt.setAttribute("class", "create-btn");
    createScheduleButt.textContent = "Create";

    createScheduleDiv.appendChild(inputTitle);
    createScheduleDiv.appendChild(closeScheduleButt);
    createScheduleDiv.appendChild(inputDesc);
    createScheduleDiv.appendChild(breakEl);
    createScheduleDiv.appendChild(numOfDaysSpanEl);
    createScheduleDiv.appendChild(inputNumberOfDays);
    createScheduleDiv.appendChild(breakEl);
    createScheduleDiv.appendChild(createScheduleButt);
    scheduleContentDivEl.appendChild(createScheduleDiv);
}

function createSchedule() {
    const toCreateScheduleButt = document.getElementById("to-createSchedule-button");
    toCreateScheduleButt.addEventListener('click', showCreateSchedule);

    const scheduleTitleInputEl = document.getElementById("schedule-title");
    const scheduleDescInputEl = document.getElementById("schedule-desc");
    const scheduleNumberOfDays = document.getElementById("schedule-days");

    const title = scheduleTitleInputEl.value;
    const description = scheduleDescInputEl.value;
    const days = scheduleNumberOfDays.value;

    if (title !== "" && description !== "") {
        const params = new URLSearchParams();
        params.append('title', title);
        params.append('description', description);
        params.append('days', days);

        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onCreateScheduleResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('POST', 'protected/schedule');
        xhr.send(params)
    } else {
        newError(mainDiv, "Please fill all the fields");
    }
}

function onCreateScheduleResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        document.getElementById("create-schedule").remove();
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    } else {
        onMessageResponse(mainDiv, this);
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
        onMessageResponse(mainDiv, this);
    }
}

function listingSchedules(e) {
    e.target.removeEventListener('click', listingSchedules);
    e.target.addEventListener('click', hideListingSchedules);
    const idSchedule = e.target.parentElement.id;
    const xhr = new XMLHttpRequest();

    const params = new URLSearchParams();
    params.append("scheduleId", idSchedule);

    xhr.addEventListener('load', onListingResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedule?' + params.toString());
    xhr.send();
}

function onListingResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        listingDays(userDto);
    } else {
        onMessageResponse(mainDiv, this);
    }
}

function listingDays(userDto) {
    const table = document.createElement("table");
    table.setAttribute("id", "schedule-table");

    const titleRow = document.createElement("tr");
    titleRow.setAttribute("id", userDto.schedule.id);
    const descriptionRow = document.createElement("tr");

    const titleTd = document.createElement("td");
    const descriptionTd = document.createElement("td");

    titleTd.textContent = userDto.schedule.title;
    descriptionTd.textContent = userDto.schedule.description;

    titleRow.appendChild(titleTd);
    descriptionRow.appendChild(descriptionTd);

    table.appendChild(titleRow);
    table.appendChild(descriptionRow);

    let tr = document.createElement("tr");
    for (let i = 0; i < userDto.schedule.days.length; i++) {

        let td = document.createElement("td");
        let hoursTable = document.createElement("table");

        let titleSpan = document.createElement("span");
        titleSpan.textContent = userDto.schedule.days[i].title;
        titleSpan.setAttribute("id", userDto.schedule.days[i].id);
        td.appendChild(titleSpan);

        let renameButt = document.createElement("button");
        renameButt.setAttribute("id", userDto.schedule.days[i].title);
        renameButt.textContent = "Rename Day";
        renameButt.addEventListener('click', renameDay);
        td.appendChild(renameButt);


        for (let j = 0; j < userDto.schedule.days[i].hours.length; j++) {
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

function renameDay(e) {
    const buttonEl = e.target;
    const tdEl = buttonEl.parentElement;

    const titleEl = tdEl.firstChild;
    const oldTitle = buttonEl.id;


    const id = titleEl.id;
    titleEl.remove();

    const newTitle = document.createElement("INPUT");
    newTitle.setAttribute("type", "text");
    newTitle.placeholder = oldTitle;
    newTitle.setAttribute("id", id);

    buttonEl.removeEventListener('click', renameDay);
    buttonEl.addEventListener('click', applyDayUpdates);

    tdEl.insertBefore(newTitle, buttonEl);

}

function applyDayUpdates(e) {
    const tdEl = e.target.parentElement;
    const titleInputField = tdEl.firstChild;
    const scheduleTitleField = tdEl.parentElement.parentElement.firstChild;

    let title = titleInputField.value;
    const oldTitle = titleInputField.placeholder;
    const id = titleInputField.id;
    const scheduleId = scheduleTitleField.id;

    if (title === "" || title === " ") {
        title = oldTitle;
    }

    const data = JSON.stringify({"dayId": id, "title": title, "scheduleId": scheduleId});
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdateDayResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'protected/day');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(data);
}

function onUpdateDayResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        removeAllChildren(daysDiv);
        listingDays(userDto);
    }
    else {
        onMessageResponse(mainDiv, this);
    }
}

function closeCreateSchedule() {
    document.getElementById("create-schedule").remove();
    const toCreateTaskButt = document.getElementById("to-createSchedule-button");
    toCreateTaskButt.addEventListener('click', showCreateSchedule);
}