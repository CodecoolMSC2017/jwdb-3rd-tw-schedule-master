<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="/icons/favicon.png" type="image/x-icon"/>
    <title>Schedule Master</title>
</head>
<body>
<div id="login-content" class="content">
    <div class="form-div">
        <form id="login-form" name="login" onsubmit="return false;">
            <div class="input-div">
                <h2 id="h2">Logging in</h2><br>E-mail:<br>
                <input type="email" name="email" class="form-el" id="email"><br> Password:<br>
                <input type=password name="password" class="form-el" id="loginPassword"><br>
                <input id="login-button" type="submit" value="Login" class="btn">
            </div>
        </form>
    </div>
</div>


</body>
</html>
