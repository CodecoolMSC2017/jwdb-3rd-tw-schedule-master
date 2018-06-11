<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="google-signin-client_id"
          content="144891204676-idis83roj0hnbo5vhpbo9mimppi5c0q0.apps.googleusercontent.com">
    <c:url value="/css/style.css" var="styleUrl"/>
    <c:url value="/js/index.js" var="indexScriptUrl"/>
    <c:url value="/js/login.js" var="loginScriptUrl"/>
    <c:url value="/js/logout.js" var="logoutScriptUrl"/>
    <c:url value="/js/register.js" var="registerScriptUrl"/>
    <c:url value="/js/main-page.js" var="mainPageScriptUrl"/>
    <c:url value="/js/task.js" var="taskScriptUrl"/>
    <c:url value="/js/schedule.js" var="scheduleScriptUrl"/>
    <c:url value="/js/users.js" var="usersScriptUrl"/>
    <c:url value="/js/google-user.js" var="googleUsersScriptUrl"/>
    <c:url value="https://apis.google.com/js/platform.js" var="googleScriptUrl"/>

    <link rel="stylesheet" type="text/css" href="${styleUrl}">
    <script src="${indexScriptUrl}"></script>
    <script src="${usersScriptUrl}"></script>
    <script src="${loginScriptUrl}"></script>
    <script src="${logoutScriptUrl}"></script>
    <script src="${registerScriptUrl}"></script>
    <script src="${scheduleScriptUrl}"></script>
    <script src="${taskScriptUrl}"></script>
    <script src="${mainPageScriptUrl}"></script>
    <script src="${googleUsersScriptUrl}"></script>
    <script src="${googleScriptUrl}" async defer></script>
    <link rel="shortcut icon" type="image/png" href="icons/favicon.png"/>
    <title>Schedule Master</title>
</head>
<body>
<div id="login-content" class="content">
    <div class="form-div">
        <form id="login-form" name="login" onsubmit="return false;">
            <div class="input-div">
                <h2 id="h2">Log in</h2><br>E-mail:<br>
                <input type="email" name="email" class="input">
                <br> Password:<br>
                <input type=password name="password" class="input"><br>
                <input id="login-button" type="submit" value="Login" class="btn">
            </div>
        </form>
        <p id="registration-para" class="register-paragraph">If you don't have an account <a
                class="register-link"
                href=javascript:void(0); onclick="toRegistration();">register</a>
            one</p>
        <div class="g-signin2" data-onsuccess="onSignIn"></div>
    </div>
</div>

<div id="register-content" class="content hidden">
    <div class="form-div">
        <form id="register-form" name="register" onsubmit="return false;">
            <div class="input-div">
                <h2>Registration</h2>Name:<br>
                <input type="text" name="name" class="input"><br>E-mail:<br>
                <input type="email" name="email" class="input"><br>Password:<br>
                <input type=password name="password" class="input"><br>Confirm Password:<br>
                <input type="password" name="password-again" class="input"><br>
                <input id="register-button" type="submit" value="Register" class="btn">
            </div>
        </form>
        <p class="register-paragraph">Already have an account? <a class="register-link"
                                                                  href=javascript:void(0);
                                                                  onclick="toLogin();">Login
            here</a></p>
    </div>
</div>

<div class="main-div">
    <div id="name-logout-content" class="content hidden">
        <table id="name-logout-table">
            <tr>
                <td id="name-td">
                    <h2 id="name-field"></h2>
                </td>
                <td id="logout-td">
                    <div id="logout-content" class="content hidden">
                        <img id="logout-button" src="css/img/logout.png" alt="logout"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>

    <div id="admin-content" class="content hidden">
        <button id="admin-button" class="btn">Go Back</button>
    </div>
    <div id="users-content" class="content hidden">
        <h1>Users</h1>
        <table id="users">
            <thead>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>


    <table class="content-table">
        <tr>
            <td valign="top" class="wing-td">
                <div id="schedules-content" class="content hidden">
                    <div id="schedules">
                        <button id="schedules-button" class="container">Schedules</button>
                    </div>
                </div>
                <div id="trash-td" class="hidden">
                    <section>
	                    <span id="trash" class="trash">
    	                <span></span>
    	                    <i></i>
                        </span>
                    </section>
                </div>
            </td>

            <td class="middle-td" valign="top">
                <div id="days-content" class="content hidden">
                    <div id="days">
                    </div>
                </div>
            </td>
            <td valign="top" class="wing-td">
                <div id="tasks-content" class="content hidden">
                    <div id="tasks">
                        <button id="tasks-button" class="container">Tasks</button>
                    </div>
                </div>
            </td>
        </tr>
    </table>

</div>


</body>
</html>
