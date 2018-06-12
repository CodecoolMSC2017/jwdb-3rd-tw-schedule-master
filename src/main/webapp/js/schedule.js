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
    inputNumberOfDays.setAttribute("id", "schedule-days");
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
    let r = confirm("Press a button!\nEither OK or Cancel.");
    if (r === true) {

        const liEL = e.target.parentElement;
        const id = liEL.id;
        removeAllChildren(daysDiv);
        const title = liEL.children.item(1).textContent;
        const desc = liEL.children.item(1).id;
        const userId = document.getElementById("name-field").name;

        const data = JSON.stringify({"id": id, "userId": userId, "title": title, "description": desc});


        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onDeleteScheduleResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('DELETE', 'protected/schedule');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(data);
    }

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
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
        removeAllChildren(daysDiv);
        listingDays(userDto);
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    } else {
        onMessageResponse(mainDiv, this);
    }
}

function listingDays(userDto) {
    clearMessages();
    currentScheduleId = userDto.schedule.id;
    const table = document.createElement("table");
    table.setAttribute("class", "schedule-table");
    table.setAttribute("id", userDto.schedule.id);

    const updateButt = document.createElement("button");
    updateButt.addEventListener('click', updateScheduleFields);
    updateButt.setAttribute("class", "change-btn");

    const createTd = document.createElement("td");
    createTd.setAttribute("class", "change-sched-fields");
    createTd.rowSpan = "2";
    if (userDto.user != null) {
        createTd.appendChild(updateButt);
    }

    const titleRow = document.createElement("tr");
    titleRow.setAttribute("id", userDto.schedule.id);
    titleRow.setAttribute("class", "name-row");


    const descriptionRow = document.createElement("tr");
    descriptionRow.setAttribute("class", "desc-row");

    let numberOfDays = userDto.schedule.days.length;

    if (userDto.user == undefined) {
        numberOfDays += 1;
    }

    const titleTd = document.createElement("td");
    titleTd.colSpan = numberOfDays;

    const descriptionTd = document.createElement("td");
    descriptionTd.colSpan = numberOfDays;

    titleTd.textContent = userDto.schedule.title;
    descriptionTd.textContent = userDto.schedule.description;

    titleRow.appendChild(titleTd);
    if (userDto.user != null) {
        titleRow.appendChild(createTd);
    }
    descriptionRow.appendChild(descriptionTd);

    table.appendChild(titleRow);
    table.appendChild(descriptionRow);

    let tr = document.createElement("tr");
    tr.setAttribute("class", "day-row");
    tr.setAttribute('id', userDto.schedule.id);

    let hourFirstTd = document.createElement("td");
    hourFirstTd.setAttribute("class", "day-td");

    let hourFirstTable = document.createElement("table");
    hourFirstTable.setAttribute("class", "hours-table");


    for (let i = 0; i < 24; i++) {
        let hourFirstTr = document.createElement("tr");
        hourFirstTr.setAttribute("class", "hours-row");

        let hourFirstValueTd = document.createElement("td");
        hourFirstValueTd.setAttribute("class", "hours-td");

        if (i < 10) {
            hourFirstValueTd.textContent = "0" + i + ":00";
        } else {
            hourFirstValueTd.textContent = i + ":00";
        }
        hourFirstTr.appendChild(hourFirstValueTd);
        hourFirstTable.appendChild(hourFirstTr);
    }

    let hourButt = document.createElement("button");
    hourButt.setAttribute("id", "hour-clock");
    hourButt.setAttribute("class", "clock-btn-min");

    let parTitle = document.createElement("p");
    parTitle.innerText = "Hours";
    parTitle.setAttribute("class", "title-par");

    hourFirstTd.appendChild(parTitle);
    hourFirstTd.appendChild(hourButt);
    hourFirstTd.appendChild(hourFirstTable);

    tr.appendChild(hourFirstTd);

    for (let i = 0; i < userDto.schedule.days.length; i++) {

        let td = document.createElement("td");
        td.setAttribute("class", "day-td");
        let hoursTable = document.createElement("table");
        hoursTable.setAttribute("class", "hours-table");

        let titleParEl = document.createElement("p");
        titleParEl.textContent = userDto.schedule.days[i].title;
        titleParEl.setAttribute("id", userDto.schedule.days[i].id);
        titleParEl.setAttribute("class", "title-par");
        td.appendChild(titleParEl);

        if (userDto.schedule.days[i].dueDate != null) {
            titleParEl.classList.add("tooltip");
            let tooltipSpan = document.createElement("span");
            tooltipSpan.setAttribute("class", "tooltiptext");
            tooltipSpan.innerText = timeConverter(userDto.schedule.days[i].dueDate);
            titleParEl.appendChild(tooltipSpan);
        }

        let renameButt = document.createElement("button");
        renameButt.setAttribute("id", userDto.schedule.days[i].title);

        if (userDto.user != null) {
            renameButt.setAttribute("class", "change-btn-min");
            renameButt.addEventListener('click', renameDay);
        } else {
            renameButt.setAttribute("class", "calendar-btn-min")
        }
        td.appendChild(renameButt);


        for (let j = 0; j < userDto.schedule.days[i].hours.length; j++) {
            let hoursTr = document.createElement("tr");
            hoursTr.setAttribute("id", userDto.schedule.days[i].hours[j].id);
            hoursTr.setAttribute("class", "hours-row");

            let hoursTd = document.createElement("td");
            hoursTd.setAttribute("class", "hours-td");
            hoursTd.setAttribute("id", userDto.schedule.days[i].hours[j].value);

            if (userDto.schedule.days[i].hours[j].task != null && userDto.schedule.days[i].hours[j].task !== "deleted") {

                let task = userDto.schedule.days[i].hours[j].task;

                hoursTd.style.backgroundColor = task.color;
                hoursTd.setAttribute("name", task.id);

                if (userDto.user != null) {
                    hoursTd.classList.add("task-in-table");
                    hoursTd.setAttribute('draggable', true);
                    hoursTd.setAttribute('ondragstart', 'added_drag_start(event)');
                    hoursTd.setAttribute('ondragend', 'added_drag_end(event)');
                }

                hoursTd.textContent = task.title;

                let next = j + 1;
                let rowspan = 1;
                while (userDto.schedule.days[i].hours[next] != null && userDto.schedule.days[i].hours[next].task != null && userDto.schedule.days[i].hours[next].task.id === userDto.schedule.days[i].hours[j].task.id) {
                    rowspan++;
                    userDto.schedule.days[i].hours[next].task = "deleted";
                    next++;
                }

                hoursTd.classList.add("tooltip");
                let tooltipSpan = document.createElement("span");
                tooltipSpan.setAttribute("class", "tooltiptext");
                tooltipSpan.innerText = userDto.schedule.days[i].hours[j].task.description;
                hoursTd.appendChild(tooltipSpan);
                hoursTd.rowSpan = rowspan;
                let height = rowspan * 28;
                hoursTd.style.height = height.toString() + "px";
            }
            hoursTr.addEventListener('drop', add_task);
            hoursTr.addEventListener('dragenter', drag_enter_prevent);
            hoursTr.addEventListener('dragover', drag_over_prevent);

            if (userDto.schedule.days[i].hours[j].task == null || userDto.schedule.days[i].hours[j].task !== "deleted") {
                hoursTr.appendChild(hoursTd);
            }
            hoursTable.appendChild(hoursTr);

        }
        td.appendChild(hoursTable);
        tr.appendChild(td);
    }
    table.appendChild(tr);
    daysDiv.appendChild(table);
    if (userDto.user != null) {

        const guestButton = document.createElement("button");
        guestButton.setAttribute("class", "btn");
        guestButton.innerText = "Create Guest Link";

        guestButton.addEventListener('click', sendId);

        daysDiv.appendChild(guestButton);
    }
}

function sendId(e) {
    e.target.removeEventListener('click', sendId);

    const scheduleId = e.target.parentElement.firstElementChild.id;

    const params = new URLSearchParams();
    params.append('scheduleId', scheduleId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', createLinkResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/encrypt');
    xhr.send(params);
}

function copyToClipBoard(e) {
    const copyText = document.getElementById("guest-link");
    copyText.select();
    document.execCommand("copy");
    e.target.firstElementChild.innerText = "Copied!";
}

function copyTextBack(e) {
    e.target.firstElementChild.innerText = "Copy to Clipboard";
}

function createLinkResponse() {
    if (this.status === OK) {
        const scheduleId = JSON.parse(this.responseText);
        createLink(scheduleId);
    }
    else {
        onMessageResponse(mainDiv, this);
    }
}

function createLink(scheduleId) {

    const linkInputField = document.createElement("INPUT");
    linkInputField.setAttribute("id", "guest-link");
    linkInputField.setAttribute("class", "input");
    linkInputField.setAttribute("type", "text");

    const copyButt = document.createElement('button');
    copyButt.setAttribute("class", "copy-btn");
    copyButt.classList.add("tooltip");
    copyButt.addEventListener('click', copyToClipBoard);
    copyButt.addEventListener('mouseleave', copyTextBack);

    const guestTable = document.createElement('table');
    guestTable.setAttribute("id", "guest-table");
    const guestTr = document.createElement('tr');
    const linkTd = document.createElement('td');
    const copyTd = document.createElement('td');


    let tooltipSpan = document.createElement("span");
    tooltipSpan.setAttribute("class", "tooltiptext");
    tooltipSpan.innerText = "Copy to Clipboard";
    copyButt.appendChild(tooltipSpan);

    const params = new URLSearchParams();
    params.append('scheduleId', scheduleId.message);
    linkInputField.value = document.documentURI + "guest?" + params.toString();

    linkTd.appendChild(linkInputField);
    copyTd.appendChild(copyButt);
    guestTr.appendChild(linkTd);
    guestTr.appendChild(copyTd);
    guestTable.appendChild(guestTr);
    daysDiv.appendChild(guestTable);
    const shareDiv = document.getElementById("share-div");
    shareDiv.classList.remove("hidden");
    shareDiv.firstElementChild.setAttribute("data-href", document.documentURI + "guest?" + params.toString());

}

function hideListingSchedules(e) {
    currentScheduleId = null;
    removeAllChildren(daysDiv);

    const xhr = new XMLHttpRequest();


    xhr.addEventListener('load', hideListingResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'protected/schedule?');
    xhr.send();
}

function hideListingResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    }
    else {
        onMessageResponse(mainDiv, this);
    }
}

function renameDay(e) {
    const buttonEl = e.target;
    const tdEl = buttonEl.parentElement;

    const titleEl = tdEl.firstChild;
    let oldDate = null;
    if (titleEl.firstElementChild != null) {
        oldDate = titleEl.firstElementChild.innerText;
    }
    const oldTitle = buttonEl.id;


    const id = titleEl.id;
    titleEl.remove();

    const newTitle = document.createElement("INPUT");
    newTitle.setAttribute("type", "text");
    newTitle.placeholder = oldTitle;
    newTitle.setAttribute("id", id);
    newTitle.setAttribute("class", "input-miniature");

    const newDate = document.createElement("INPUT");
    newDate.setAttribute("type", "date");
    newDate.setAttribute("id", "datepicker");
    if (oldDate == null) {
        newDate.value = getCurrentDate();
    } else {
        newDate.value = oldDate;
    }

    newDate.setAttribute("min", getCurrentDate());

    buttonEl.removeEventListener('click', renameDay);
    buttonEl.addEventListener('click', applyDayUpdates);

    tdEl.insertBefore(newTitle, buttonEl);
    tdEl.insertBefore(newDate, buttonEl);
    tdEl.insertBefore(document.createElement("br"), buttonEl);

}


function applyDayUpdates(e) {
    const tdEl = e.target.parentElement;
    const titleInputField = tdEl.firstChild;
    const scheduleTitleField = tdEl.parentElement.parentElement.firstChild;
    const dateField = titleInputField.parentElement.children.item(1);


    let title = titleInputField.value;
    const oldTitle = titleInputField.placeholder;
    const id = titleInputField.id;
    const scheduleId = scheduleTitleField.id;
    let date = dateField.value;

    if (title === "" || title === " ") {
        title = oldTitle;
    }


    const data = JSON.stringify({"id": id, "title": title, "scheduleId": scheduleId, "dueDate": date});
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdateDayResponse);
    xhr.addEventListener('error', onNetworkError);
    console.log(date);
    if (date === "") {
        xhr.open('DELETE', 'protected/date');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(data);
    } else if (date === getCurrentDate()) {
        xhr.open('POST', 'protected/date');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(data);
    } else {
        xhr.open('PUT', 'protected/date');
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(data);
    }


}

function getCurrentDate() {
    let today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1;
    let yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }

    today = yyyy + '-' + mm + '-' + dd;
    return today;
}

function timeConverter(UNIX_timestamp) {
    const a = new Date(UNIX_timestamp * 1000);
    const year = a.getFullYear();
    const month = a.getMonth();
    const date = a.getDate();
    const time = year + '-' + month + '-' + date;
    return time;
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

function updateScheduleFields(e) {
    const scheduleUpdateButt = e.target;
    const trEl = scheduleUpdateButt.parentElement.parentElement;
    const table = trEl.parentElement;

    const titleEl = trEl.firstChild;
    const descEl = table.children.item(1).firstChild;

    const oldTitle = titleEl.textContent;
    const oldDesc = descEl.textContent;
    const colspan = titleEl.colSpan;


    titleEl.remove();
    descEl.remove();

    const titleTd = document.createElement("td");
    titleTd.colSpan = colspan;
    const descTd = document.createElement("td");
    descTd.setAttribute("class", "desc-td");
    descTd.colSpan = colspan;

    const titleInputEl = document.createElement("INPUT");
    titleInputEl.setAttribute("type", "text");
    titleInputEl.setAttribute("class", "input-min");
    titleInputEl.placeholder = oldTitle;
    titleTd.appendChild(titleInputEl);

    const descInputEl = document.createElement("INPUT");
    descInputEl.setAttribute("type", "text");
    descInputEl.placeholder = oldDesc;
    descInputEl.setAttribute("class", "input-min");
    descTd.appendChild(descInputEl);


    scheduleUpdateButt.removeEventListener('click', updateScheduleFields);
    scheduleUpdateButt.addEventListener('click', applyScheduleUpdates);

    trEl.insertBefore(titleTd, scheduleUpdateButt.parentElement);
    table.children.item(1).appendChild(descTd);


}

function applyScheduleUpdates(e) {
    const scheduleUpdateButt = e.target;
    const trEl = scheduleUpdateButt.parentElement.parentElement;
    const table = trEl.parentElement;

    const titleInputEl = trEl.firstChild.firstChild;
    const descInputEl = table.children.item(1).firstChild.firstChild;

    const id = table.id;

    const userId = document.getElementById("name-field").name;

    let title = titleInputEl.value;
    const oldTitle = titleInputEl.placeholder;

    let desc = descInputEl.value;
    const oldDesc = descInputEl.placeholder;

    if (title == null || title === "" || title === " ") {
        title = oldTitle;
    }
    if (desc == null || desc === "" || desc === " ") {
        desc = oldDesc;
    }

    const data = JSON.stringify({"id": id, "description": desc, "title": title, "userId": userId});

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUpdateScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'protected/schedule');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(data);
}

function onUpdateScheduleResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        removeAllChildren(daysDiv);
        listingDays(userDto);
        document.getElementById("schedulesUl").remove();
        createScheduleDiv(userDto);
    } else {
        onMessageResponse(mainDiv, this);
    }

}

function add_task(ev) {
    ev.preventDefault();
    if (ev.target.firstChild != null) {
        newError(mainDiv, "You already added a task for this hour!");
        return false;
    }
    const hourValue = ev.target.id;
    const taskId = ev.dataTransfer.getData("text");
    let number = prompt("How long will it take to finish with the task?", "");
    let parsedNum = parseInt(number);
    if (number === "0") {
        newError(mainDiv, "Number can not be 0!");
    } else if (!isNumeric(parsedNum) || parsedNum === "") {
        newError(mainDiv, "Please enter a number!");
    } else if (parseInt(hourValue) + parsedNum > 24) {
        newError(mainDiv, "Not enough hours left for that day.");
    } else if (parsedNum < 0) {
        newError(mainDiv, "Number can not be negative!")
    } else {
        const hourId = ev.target.parentElement.id;
        const scheduleId = ev.target.parentElement.parentElement.parentElement.parentElement.id;
        const dayId = ev.target.parentElement.parentElement.parentElement.firstChild.id;
        const params = new URLSearchParams();
        params.append('taskId', taskId);
        params.append('hourId', hourId);
        params.append('dayId', dayId);
        params.append('scheduleId', scheduleId);
        params.append('number', parsedNum);

        const xhr = new XMLHttpRequest();
        xhr.addEventListener('load', onDragResponse);
        xhr.addEventListener('error', onNetworkError);
        xhr.open('POST', 'protected/taskHour');
        xhr.send(params);
    }
}

function onDragResponse() {
    if (this.status === OK) {
        const userDto = JSON.parse(this.responseText);
        removeAllChildren(daysDiv);
        document.getElementById("tasksUl").remove();
        createTaskDiv(userDto);
        listingDays(userDto);
    } else {
        onMessageResponse(mainDiv, this);
    }
}

function drag_enter_prevent(event) {
    event.preventDefault();
}

function drag_over_prevent(event) {
    event.preventDefault();
}

function isNumeric(n) {
    return !isNaN(n);
}

function added_drag_start(ev) {
    ev.dataTransfer.dropEffect = "move";
    ev.dataTransfer.setData("text", ev.target.getAttribute("name"));

    const trash = document.getElementById("trash-td");
    trash.classList.remove("hidden");

    ev.target.firstElementChild.removeAttribute("class");
    ev.target.firstElementChild.setAttribute("class", "hidden");
}

function added_drag_end(ev) {
    ev.preventDefault();
    ev.target.firstElementChild.removeAttribute("class");
    ev.target.firstElementChild.classList.add("tooltiptext");
    const trash = document.getElementById("trash-td");
    trash.classList.add("hidden");
}