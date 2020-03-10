// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Displays a greeting to the front page.
 */
async function getGreeting(){
    const response = await fetch('/data');
    const greeting = await response.text();
    document.getElementById('title').innerText = greeting;
}

/**
 * Displays messages retrieved from the server to
 * the front page.
 */
async function getMessages(){
  const response = await fetch('/data');
  const json = await response.json();
  const container = document.getElementById('message-container');
  json.forEach(function(comment){
    container.appendChild(createListElement(comment));

  })
}

/**
 * Displays the comment form if the user is logged in alongside
 * a logout link. Displays a login link if the user is not loggedin.
 */
async function showCommentFormOrLoginLink(){
  const response = await fetch('/loginstatus');
  const json = await response.json();
  const isLoggedIn = json.loginstatus;
  const url = json.url;
  if (isLoggedIn) {
    document.getElementById("comment-form").style.display="block";
    displayLoginLogoutLink("logout", url);
    
  } else {
    displayLoginLogoutLink("login", url);

  }

}

/**
 * Displays the login link {@code url} and the message {@code msg} below 
 * the comment form.
 */
function displayLoginLogoutLink(msg,url){
  const container = document.getElementById("comment-login-info");
  container.innerHTML = msg+" <a href=\"" + url 
    + "\">here</a>. Change nickname <a href=\"nickname.html\">here</a></p>";
}

/**
 * Functions called when the page is initially loaded.
 */
function start(){
  getMessages();
  showCommentFormOrLoginLink();
}

/**
 * Creates a list element for the comment.
 */
function createListElement(comment){
  const element = document.createElement('li');
  element.className = "comment";
  element.innerText = comment.commenter + ": \n" + comment.comment +
    "\n \n Posted: " + comment.formattedTimeSubmitted;
  return element;
} 
