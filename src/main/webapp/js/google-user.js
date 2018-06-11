function onSignIn(googleUser) {
  const profile = googleUser.getBasicProfile();

  const params = new URLSearchParams();
  params.append('name', profile.getName());
  params.append('email', profile.getEmail());

  const xhr = new XMLHttpRequest();

  xhr.addEventListener('load', onLoginResponse);
  xhr.addEventListener('error', onNetworkError);
  xhr.open('POST', '/googleLogin');
  xhr.send(params);

}