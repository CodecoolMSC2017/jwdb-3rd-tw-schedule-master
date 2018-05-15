let taskButtonEl;

function onMainPageLoad(userDto) {
    clearMessages();
    showContents(['schedules-content', 'logout-content', 'tasks-content']);
    const taskULEl = document.getElementById("tasksUl");
    if (taskULEl !== null) {
        taskULEl.remove();
    }
    createTaskDiv(userDto);
    taskButtonEl = document.getElementById('tasks-button');
    taskButtonEl.addEventListener('click', hideTasks);
    //createScheduleDiv(userDto);

}

function createTaskDiv(userDto) {
    const taskEl = document.createElement("ul");
    taskEl.setAttribute("id","tasksUl");
    for (let i = 0; i < userDto.tasks.length; i++) {
        let taskLi = document.createElement("li");
        taskLi.setAttribute("id", userDto.tasks[i].id);

        let taskSpan = document.createElement("span");
        taskSpan.textContent = userDto.tasks[i].title;
        taskSpan.addEventListener('click', showTaskDescription);

        let taskDesc = document.createElement("p");
        taskDesc.textContent = userDto.tasks[i].description;
        taskDesc.classList.add("hidden");
        taskSpan.appendChild(taskDesc);

        let renameTaskButt = document.createElement("button");
        renameTaskButt.addEventListener('click', updateTask);
        renameTaskButt.textContent = "Update";
        //taskButt.style.backgroundImage = "url('/icons/pen.png')";

        let deleteTaskButt = document.createElement("button");
        deleteTaskButt.addEventListener('click', removeTask);
        deleteTaskButt.textContent = "Remove";


        taskLi.appendChild(taskSpan);
        taskLi.appendChild(renameTaskButt);
        taskLi.appendChild(deleteTaskButt);
        taskEl.appendChild(taskLi);
    }

    const taskCreateLiEl = document.createElement("li");
    const createButton = document.createElement("button");

    createButton.addEventListener('click',showCreateTask);
    createButton.setAttribute("id", "to-createTask-button");
    createButton.textContent = "Create Task";

    taskCreateLiEl.appendChild(createButton);
    taskEl.appendChild(taskCreateLiEl);
    taskDiv.appendChild(taskEl);
}

function createScheduleDiv(userDto){
    const scheduleEl = document.createElement("ul");
    scheduleEl.setAttribute("id","scheduleUl");
    scheduleEl.classList.add('hidden');
    for (let i = 0; i < userDto.schedules.length ; i++) {
        let scheduleLi = document.createElement("li");
        let scheduleSpan = document.createElement("span");
        scheduleSpan.textContent = userDto.schedule[i].title;
        let scheduleButt = document.createElement("button");
        scheduleButt.addEventListener('click',renameScheduleTitle);
        scheduleLi.appendChild(scheduleSpan);
        scheduleLi.appendChild(scheduleButt);
        scheduleEl.appendChild(scheduleLi);
    }
    scheduleDiv.appendChild(scheduleEl);
    const createButton = document.createElement("button");
    createButton.addEventListener('click',showCreateSchedule);
    createButton.textContent = "Create Schedule";
    scheduleDiv.appendChild(createButton);
    const scheduleButtonEl = document.getElementById('schedule-button');
    scheduleButtonEl.addEventListener('click',showSchedule);
}
