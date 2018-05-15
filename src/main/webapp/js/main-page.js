let taskButtonEl;
let scheduleButtonEl;

function onMainPageLoad(userDto) {
    clearMessages();
    showContents(['schedules-content', 'logout-content', 'tasks-content']);

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
    const taskEl = document.createElement("ul");
    taskEl.setAttribute("id","tasksUl");
    for (let i = 0; i < userDto.tasks.length; i++) {
        let taskLi = document.createElement("li");
        taskLi.setAttribute("id", userDto.tasks[i].id);

        let taskSpan = document.createElement("span");
        taskSpan.textContent = userDto.tasks[i].title;
        taskSpan.setAttribute("id", userDto.tasks[i].title);
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
    const goBackButton = document.createElement("button");

    createButton.addEventListener('click',showCreateTask);
    createButton.setAttribute("id", "to-createTask-button");
    createButton.textContent = "Create Task";

    goBackButton.addEventListener('click', onGobackClicked);
    goBackButton.setAttribute("id", "go-back-button");
    goBackButton.textContent = "Revert Changes";


    taskCreateLiEl.appendChild(createButton);
    taskCreateLiEl.appendChild(goBackButton);
    taskEl.appendChild(taskCreateLiEl);
    taskDiv.appendChild(taskEl);

}

function createScheduleDiv(userDto){
    const scheduleEl = document.createElement("ul");
    scheduleEl.setAttribute("id","schedulesUl");

    for (let i = 0; i < userDto.schedules.length ; i++) {
        let scheduleLi = document.createElement("li");
        scheduleLi.setAttribute("id",userDto.schedules[i].id);

        let scheduleSpan = document.createElement("span");
        scheduleSpan.textContent = userDto.schedule[i].title;
        //add eventListener

        let deleteScheduleButt = ducoment.createElement("button");
        deleteScheduleButt.addEventListener('click',removeSchedule);
        deleteScheduleButt.textContent = "Remove";

        scheduleLi.appendChild(scheduleSpan);
        scheduleLi.appendChild(deleteScheduleButt);
        scheduleEl.appendChild(scheduleLi);
    }

    const scheduleCreateLiEl = document.createElement('li');
    const createButton = document.createElement("button");
    const goBackButton = document.createElement("button");

    createButton.addEventListener('click',showCreateSchedule);
    createButton.setAttribute("id","to-createSchedule-button");
    createButton.textContent = "Create Schedule";

    goBackButton.addEventListener('click',onGobackClicked);
    goBackButton.setAttribute("id","go-schedule-back-button");
    goBackButton.textContent = "Revert Changes";



    scheduleCreateLiEl.appendChild(createButton);
    scheduleCreateLiEl.appendChild(goBackButton);
    scheduleEl.appendChild(scheduleCreateLiEl);
    scheduleDiv.appendChild(scheduleEl);
}
