function onMainPageLoad(user) {
    clearMessages();
    showContents(['schedules-content', 'logout-content']);

    const userWelcomeSpanEl = document.getElementById('user-welcome');

    userWelcomeSpanEl.textContent = user.userName;
}