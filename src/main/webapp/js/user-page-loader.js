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

/** Fetches the Blobstore upload URL (for images) */
function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      const messageForm = document.getElementById('message-form');
      messageForm.action = imageUploadUrl;
      document.getElementById('message-form-wrap').classList.remove('hidden');
    });
}

/** Fetches the profile picture upload URL */
function fetchProfilePicUrlAndShowForm() {
  fetch('/profilepic-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((profilePicUploadUrl) => {
      const profilepicForm = document.getElementById('profilepic-form');
      profilepicForm.action = profilePicUploadUrl;
      profilepicForm.classList.remove('hidden');
    });
}


/** Fetches s and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          if(!message.hasOwnProperty('chat')) {
            const messageDiv = buildMessageDiv(message);
            messagesContainer.appendChild(messageDiv);
          }
        });
      });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.classList.add('padded');
  headerDiv.appendChild(document.createTextNode(
      (new Date(message.timestamp)).toLocaleString()));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  if(message.hasOwnProperty('imageUrl')) {
    const img = document.createElement('img');
    img.src = message.imageUrl;
    bodyDiv.appendChild(img);
  }

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.classList.add('rounded');
  messageDiv.classList.add('panel');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

function fetchProfilePic() {
  const url = '/profilePic?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((profilePicUrl) => {
    const profileImg = document.getElementById('profilepic');
    if(profilePicUrl != ''){
      profileImg.src = profilePicUrl;
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

function fetchUniversityName(){
  const url = '/universityName?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((universityName) => {
    const universityNameContainer = document.getElementById('university-name-container');
    if(universityName == ''){
      universityName = 'Unknown';
    }
    universityNameContainer.innerHTML = 'University: ' + universityName;
  });
}

function fetchMajor(){
  const url = '/major?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((major) => {
    const majorContainer = document.getElementById('major-container');
    if(major == ''){
      major = 'Unknown';
    }
    majorContainer.innerHTML = 'Major: ' + major;
  });
}

function fetchTimeZone(){
  const url = '/timezone?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((timezone) => {
    const timezoneContainer = document.getElementById('timezone-container');
    if(timezone == ''){
      timezone = 'Unknown';
    }
    timezoneContainer.innerHTML = 'Time Zone: ' + timezone;
  });
}

function fetchStudyPace(){
  const url = '/studypace?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((studypace) => {
    const studyPaceContainer = document.getElementById('studypace-container');
    if(studypace == ''){
      studypace = 'Unknown';
    }
    studyPaceContainer.innerHTML = 'Weekly Study Hours: ' + studypace;
  });
}

function fetchTopics() {
  const topicContainer = document.getElementById('topic-container');
  const url = '/topic?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((topic) => {
    const topicContainer = document.getElementById('topic-container');
    if(topic == ''){
      topic = 'Unknown';
    }
    console.log(topic);
    topicContainer.innerHTML = 'Fields of Interest: ' + topic;
  });
}
 // get elements
 // pastTopicsDiv = document.getElementById('pastTopics-div');
 // pastTopicsInputs = pastTopicsDiv.getElementsByTagName('input');

 function setCurrentTopics() {
   currentTopicsDiv = document.getElementById('currentTopics-div');
   currentTopicsInputs = currentTopicsDiv.getElementsByTagName('input');

   // let pastTopics = [];
   let currentTopics = [];

   for (i = 0; i < currentTopicsInputs.length; i++) {
    //if checked, add to list
    //if (pastTopicsInputs[i].checked) {
    //  pastTopics.push(pastTopicsInputs[i].name);
    //}
     if (currentTopicsInputs[i].checked) {
       currentTopics.push(currentTopicsInputs[i].name);
     }
   }
   // send post request
   const params = new URLSearchParams();
    //params.append('pastTopics', pastTopics);
    params.append('currentTopics', currentTopics);

    fetch('/topic', {
      method: 'POST',
      body: params
     });

 }

/** Fetches data and populates the UI of the page. */
function buildUI() {
  showForms();
  fetchMessages();
  const config = {removePlugins: [ 'Heading', 'List' ]};
  ClassicEditor.create(document.getElementById('message-input'), config );
  fetchAboutMe();
  fetchProfilePic();
  fetchUniversityName();
  fetchMajor();
  fetchTimeZone();
  fetchStudyPace();
  fetchTopics();
}
