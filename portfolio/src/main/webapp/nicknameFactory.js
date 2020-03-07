
/**
 * Fetches and displays relevant nickname information pertaining to the user from the nickname 
 * servlet.
 */
async function fetchAndDisplayNicknameInfo(){
  const response = await fetch('/nickname');
  const json = await response.json();
  const url = json.url;
  const loggedInStatus = json.loggedin;
  const nickname = json.nickname;
  displayLoginLogoutOrForm(nickname,url,loggedInStatus);
  
}

/** 
 * Displays the login link if not signed in, else displays the nickname form.
 * {@code nickname} is the user's nickname, {@code url} is the URL to display, and 
 * {@code loggedInStatus} is a boolean representing whether the user is logged in.
 */
function displayLoginLogoutOrForm(nickname,url,loggedInStatus){
  if (loggedInStatus){  
    document.getElementById("nickname-form").style.display="block";
    document.getElementById("nickname-input").value = nickname;
    document.getElementById("loginlogoutinfo").innerHTML = "Logout <a href=\"" + url + "\">here</a>.</p>"
  } else {
    document.getElementById("loginlogoutinfo").innerHTML = "Login <a href=\"" + url + "\">here</a>.</p>"
    document.getElementById("login-message").innerHTML = "Please login first.";
  }

}