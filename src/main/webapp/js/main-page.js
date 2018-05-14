function onMainPageLoad(userDto) {
    clearMessages();
    showContents(['schedules-content', 'logout-content', 'tasks-content']);

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
}


