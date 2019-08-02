/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');
// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle(nickName, viewingSelf) {
  if (viewingSelf == true){
    document.getElementById('page-title').innerText = 'Welcome home! ' + nickName.replace(/(\n|\r|\r\n)/g, '') + ' ^_^';
  }
  else{
    if (nickName == ''){
      document.getElementById('page-title').innerText = 'Hello! My owner hasn\'t set my name yet! Please remind him/her to give me a name ^_^';
    }
    else{
      document.getElementById('page-title').innerText = 'Helloooo! This is ' + nickName.replace(/(\n|\r|\r\n)/g, '') + ". Welcome to my page ^_^";
    }
  }
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showForms() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        //not logged in
        if (!loginStatus.isLoggedIn){
          fetchNickName(false);
          document.getElementById('login-form').classList.remove('hidden');
        }
        //logged in and viewing self
        else if (loginStatus.username == parameterUsername) {
          // handle message forms
          document.getElementById('message-form-wrap').classList.remove('hidden');

          fetchBlobstoreUrlAndShowForm();
          fetchProfilePicUrlAndShowForm();
          document.getElementById('update-forms').classList.remove('hidden');
          fetchNickName(true);
        }
        //login and viewing others
        else {
          fetchNickName(false);
          const chatForm = document.getElementById('chat-form');
          chatForm.classList.remove('hidden');
          chatForm.firstElementChild.value = parameterUsername;
        }
      });
}



function fetchAboutMe(){
  const url = '/about?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((aboutMe) => {
    const aboutMeContainer = document.getElementById('about-me-container');
    if(aboutMe == ''){
      aboutMe = 'This user has not entered any aboutme information yet.';
    }
    aboutMeContainer.innerHTML = aboutMe;
  });
}

function fetchNickName(viewingSelf){
  const url = '/nickName?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((nickName) => {
    const nickNameContainer = document.getElementById('nick-name-container');
    if(nickName == ''){
      nickName = 'Unknown';
    }
    nickNameContainer.innerHTML = 'Nickname: ' + nickName;
    setPageTitle(nickName, viewingSelf);
  });
}


/** Fetches data and populates the UI of the page. */
function buildUI() {
  showForms();
  const config = {removePlugins: [ 'Heading', 'List' ]};
  ClassicEditor.create(document.getElementById('message-input'), config );
  fetchAboutMe();
}
