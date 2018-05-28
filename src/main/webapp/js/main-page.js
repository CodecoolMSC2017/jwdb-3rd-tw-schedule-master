let taskButtonEl;
let scheduleButtonEl;

function onMainPageLoad(userDto) {
    clearMessages();
    if (getAuthorization().role !== 'admin') {
        showContents(['name-logout-content', 'logout-content', 'schedules-content', 'tasks-content', 'days-content']);
    } else {
        showContents(['admin-content', 'name-logout-content', 'logout-content', 'schedules-content', 'tasks-content', 'days-content']);
    }

    const nameField = document.getElementById("name-field");
    nameField.textContent = userDto.user.userName;

    const taskULEl = document.getElementById("tasksUl");
    if (taskULEl !== null) {
        taskULEl.remove();
    }

    const createTaskEl = document.getElementById("create-task");
    if (createTaskEl !== null) {
        createTaskEl.remove();
    }

    const scheduleULEl = document.getElementById("schedulesUl");
    if (scheduleULEl !== null) {
        scheduleULEl.remove();
    }

    const createScheduleEl = document.getElementById("create-schedule");
    if (createScheduleEl !== null) {
        createScheduleEl.remove();
    }
    createTaskDiv(userDto);
    taskButtonEl = document.getElementById('tasks-button');
    taskButtonEl.addEventListener('click', hideTasks);

    createScheduleDiv(userDto);
    scheduleButtonEl = document.getElementById('schedules-button');
    scheduleButtonEl.addEventListener('click', hideSchedule);

}

function createTaskDiv(userDto) {
    clearMessages();
    const taskEl = document.createElement("ul");
    taskEl.setAttribute("id","tasksUl");
    for (let i = 0; i < userDto.tasks.length; i++) {

        let taskLi = document.createElement("li");
        taskLi.setAttribute("id", userDto.tasks[i].id);
        taskLi.setAttribute("class", "task-li");
        taskLi.setAttribute('draggable', true);
        taskLi.setAttribute('ondragstart', 'drag_start(event)');

        let taskSpan = document.createElement("span");
        taskSpan.textContent = userDto.tasks[i].title;
        taskSpan.setAttribute("id", userDto.tasks[i].title);
        taskSpan.setAttribute('class', 'title-span');
        taskSpan.addEventListener('click', showTaskDescription);


        let taskDesc = document.createElement("p");
        taskDesc.textContent = userDto.tasks[i].description;
        taskDesc.classList.add("hidden");
        taskSpan.appendChild(taskDesc);

        let renameTaskButt = document.createElement("button");
        renameTaskButt.addEventListener('click', updateTask);
        renameTaskButt.setAttribute("class", "change-btn-min-absolute");


        let deleteTaskButt = document.createElement("button");
        deleteTaskButt.addEventListener('click', removeTask);
        deleteTaskButt.setAttribute("class", "delete-btn");

        taskLi.appendChild(taskSpan);
        taskLi.appendChild(renameTaskButt);
        taskLi.appendChild(deleteTaskButt);
        taskEl.appendChild(taskLi);
    }

    const taskCreateLiEl = document.createElement("li");

    const createImage = document.createElement("button");
    createImage.addEventListener('click', showCreateTask);
    createImage.setAttribute("id", "to-createTask-button");
    createImage.setAttribute("class", "create-min-btn");

    taskCreateLiEl.appendChild(createImage);
    taskEl.appendChild(taskCreateLiEl);
    taskDiv.appendChild(taskEl);

}

function createScheduleDiv(userDto){
    clearMessages();
    const scheduleEl = document.createElement("ul");
    scheduleEl.setAttribute("id","schedulesUl");

    for (let i = 0; i < userDto.schedules.length ; i++) {
        let scheduleLi = document.createElement("li");
        scheduleLi.setAttribute("id",userDto.schedules[i].id);
        scheduleLi.setAttribute("class", "schedule-li");


        let scheduleSpan = document.createElement("span");
        scheduleSpan.textContent = userDto.schedules[i].title;
        scheduleSpan.addEventListener('click',listingSchedules);
        scheduleSpan.setAttribute("class", "title-span");

        let deleteScheduleButt = document.createElement("button");
        deleteScheduleButt.addEventListener('click',removeSchedule);
        deleteScheduleButt.setAttribute("class", "delete-btn");


        scheduleLi.appendChild(scheduleSpan);
        scheduleLi.appendChild(deleteScheduleButt);
        scheduleEl.appendChild(scheduleLi);
    }
    const scheduleCreateLiEl = document.createElement('li');
    scheduleCreateLiEl.setAttribute("class", 'task-create-li');
    const createButton = document.createElement("button");

    createButton.addEventListener('click',showCreateSchedule);
    createButton.setAttribute("id","to-createSchedule-button");
    createButton.setAttribute("class", "create-min-btn");

    scheduleCreateLiEl.appendChild(createButton);
    scheduleEl.appendChild(scheduleCreateLiEl);
    scheduleDiv.appendChild(scheduleEl);
}


function drag_start(ev) {
    ev.dataTransfer.setData("text", ev.target.getAttribute('id'));
    ev.dataTransfer.dropEffect = "move";
}

