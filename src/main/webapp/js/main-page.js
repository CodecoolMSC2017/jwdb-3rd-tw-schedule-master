function onMainPageLoad(userDto) {
    clearMessages();
    showContents(['schedules-content', 'logout-content', 'tasks-content', 'schedules-content']);
    createTaskDiv(userDto);
    createScheduleDiv(userDto);

}

function createTaskDiv(userDto){
    const taskEl = document.createElement("ul");
     taskEl.setAttribute("id","tasksUl");
     taskEl.classList.add('hidden');
     for (let i = 0; i < userDto.tasks.length ; i++) {
          let taskLi = document.createElement("li");
          let taskSpan = document.createElement("span");
          taskSpan.textContent = userDto.tasks[i].title;
          let taskButt = document.createElement("button");
          taskButt.addEventListener('click',renameTaskTitle);
          taskLi.appendChild(taskSpan);
          taskLi.appendChild(taskButt);
          taskEl.appendChild(taskLi);
     }
     taskDiv.appendChild(taskEl);
     const createButton = document.createElement("button");
     createButton.addEventListener('click',showCreateTask);
     createButton.textContent = "Create Task";
     taskDiv.appendChild(createButton);
     const taskButtonEl = document.getElementById('tasks-button');
     taskButtonEl.addEventListener('click',showTasks);
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
