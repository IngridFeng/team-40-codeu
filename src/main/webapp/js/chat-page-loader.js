// Get ?chat=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterChat = urlParams.get('chat');
let cur_user = null;
fetch('/current-user').then((response) => {
  return response.text();
}).then((user) => {
  cur_user = user;
});

// if chat parameter not in url, redirect to home
if (!parameterChat) {
  window.location.replace('/');
}

// set the chat title to the saved chat name
function loadChatDetails() {

  const url = '/chat?chatId=' + parameterChat;
  fetch(url).then((response) => {
    return response.json();
  }).then((chatInfo) => {
    document.getElementById('chat-name').innerHTML = chatInfo.name;
    document.getElementById('chat-description').innerHTML = chatInfo.description;

  });

}

function buildMessageDiv(message) {
  /*
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.classList.add('padded');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));
  */

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  if(message.hasOwnProperty('imageUrl')) {
    const img = document.createElement('img');
    img.src = message.imageUrl;
    bodyDiv.appendChild(img);
  }

  const messageDiv = document.createElement('div');

  if (message.user.replace(/\s/g, "") == cur_user.replace(/\s/g, "")){
    messageDiv.classList.add('message-div-right');
  } else {
    messageDiv.classList.add('message-div-left');
  }

  const time = getTimeFromDate(message.timestamp);
  const timeDiv = document.createElement('div');
  timeDiv.classList = "time-div";
  timeDiv.appendChild(document.createTextNode(time));

  /*messageDiv.appendChild(headerDiv); */
  messageDiv.appendChild(bodyDiv);
  messageDiv.appendChild(timeDiv);

  return messageDiv;
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
      messageForm.classList.remove('hidden');
    });
}

/** Fetches messages and add them to the page. */
function fetchChatMessages() {
  const url = '/messages?chat=' + parameterChat;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>Start the conversation!</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
  }

function fetchStudySessions() {
  console.log("started fetching sessions");
  const url = '/study-session?chat=' + parameterChat;
  fetch(url)
      .then((response) => {
        console.log("response");
        return response.json();
      })
      .then((studySessions) => {
        console.log(studySessions);
        const studySessionsContainer = document.getElementById('studySession-container');
        if (studySessions.length == 0) {
          studySessionsContainer.innerHTML = '<p>Set a Study Session!</p>';
        } else {
          studySessionsContainer.innerHTML = '';
        }
        studySessions.forEach((studySession) => {
          const studySessionDiv = buildStudySessionDiv(studySession);
          studySessionsContainer.appendChild(studySessionDiv);
        });
      });
  }

function buildStudySessionDiv(studySession) {
  const topicDiv = document.createElement('div');
  topicDiv.classList.add('studySession_topic');
  const topic = document.createElement('h3');
  topic.appendChild(document.createTextNode(studySession.topic));
  const about = document.createElement('p');
  about.appendChild(document.createTextNode(studySession.description));
  topicDiv.appendChild(topic);
  topicDiv.appendChild(about);

  const logisticsDiv = document.createElement('div');
  logisticsDiv.classList.add('studySession_logistics');
  const timeIcon = document.createElement('i');
  timeIcon.classList = "fas fa-clock";
  const time = document.createElement('p');
  time.appendChild(document.createTextNode(studySession.time.slice(11,16)));
  const location = document.createElement('p')
  location.appendChild(document.createTextNode(studySession.location));
  const locationIcon = document.createElement('i');
  locationIcon.classList = "fas fa-map-marker-alt";;
  logisticsDiv.appendChild(timeIcon);
  logisticsDiv.appendChild(time);
  logisticsDiv.appendChild(locationIcon);
  logisticsDiv.appendChild(location);

  const studySessionDiv = document.createElement('div');
  studySessionDiv.classList.add('studySession-div');
  studySessionDiv.appendChild(topicDiv);
  studySessionDiv.appendChild(logisticsDiv);

  return studySessionDiv;
}


function setChatParam() {
  const messageForm = document.getElementById('message-form');
  const studySessionForm_chatInput = document.getElementById('studySessionForm_chatInput');
  messageForm.firstElementChild.value = parameterChat;
  studySessionForm_chatInput.value = parameterChat;
}

/** Opens modal to create study session **/
// Get the modal

function openStudySessionModal() {
  var modal = document.getElementById("studySessionModal");
  modal.style.display = "block";
}

function closeStudySessionModal() {
  var modal = document.getElementById("studySessionModal");
  modal.style.display = "none";
}

/* Helper to parse timestamp */
function pad(num) {
  return ("0"+num).slice(-2);
}
function getTimeFromDate(timestamp) {
  var date = new Date(timestamp * 1000);
  var hours = date.getHours();
  var minutes = date.getMinutes();
  var seconds = date.getSeconds();
  return pad(hours)+":"+pad(minutes);
}

/** Build page */
function buildUI() {
  loadChatDetails();
  const chatLoadTimer = setInterval(fetchChatMessages, 1000);
  // fetchChatMessages();
  setChatParam();
  fetchBlobstoreUrlAndShowForm();
  fetchStudySessions();
}



    // should also implement a chat page that pulls all chats that have ids associated with user
