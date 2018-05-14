<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <c:url value="/css/style.css" var="styleUrl"/>
    <c:url value="/js/index.js" var="indexScriptUrl"/>
    <c:url value="/js/login.js" var="loginScriptUrl"/>
    <c:url value="/js/logout.js" var="logoutScriptUrl"/>
    <c:url value="/js/register.js" var="registerScriptUrl"/>
    <c:url value="/js/main-page.js" var="mainPageScriptUrl"/>
    <link rel="stylesheet" type="text/css" href="${styleUrl}">
    <script src="${indexScriptUrl}"></script>
    <script src="${loginScriptUrl}"></script>
    <script src="${logoutScriptUrl}"></script>
    <script src="${registerScriptUrl}"></script>
    <script src="${mainPageScriptUrl}"></script>
    <link rel="shortcut icon" type="image/png" href="icons/favicon.png"/>
    <title>Schedule Master</title>
</head>
<body>
    <div id="login-content" class="content">
        <div>
            <form id="login-form" name="login" onsubmit="return false;">
                <div class="input-div">
                    <h2 id="h2">Log in</h2><br>E-mail:<br>
                    <input type="email" name="email">
                    <br> Password:<br>
                        <input type=password name="password"><br>
                        <input id="login-button" type="submit" value="Login">
                </div>
            </form>
            <p><a id="register-button" href="toRegistration()">Register</a> an account</p>
        </div>
    </div>

    <div id="register-content" class="content hidden">
        <div>
            <form id="register-form" name="register" onsubmit="return false;">
                <div>
                    <h2>Registration</h2>Name:<br>
                    <input type="text" name="name"><br>E-mail:<br>
                    <input type="email" name="email"><br>Password:<br>
                    <input type=password name="password"><br>Confirm Password:<br>
                    <input type="password" name="password-again">
                </div>
            </form>
            <button onclick="toLogin()"></button>
        </div>
    </div>

    <div id="schedules-content" class="content hidden">
        <h2>Schedules</h2>
        <p>Welcome <span id="user-welcome"></span>!</p>
    </div>

    <div id="tasks-content" class="content hidden">
        <div id="tasks">
            <p>Tasks</p>
        </div>

    </div>

    <div id="logout-content" class="content hidden">
        <button id="logout-button">Logout</button>
    </div>

</body>
</html>
