// Get ?chat=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterChat = urlParams.get('chat');
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
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.classList.add('padded');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));

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
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.classList.add('padded');
  headerDiv.appendChild(document.createTextNode(
      studySession.topic + ";" + studySession.time + ";" + studySession.location));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = studySession.description;

  const studySessionDiv = document.createElement('div');
  studySessionDiv.classList.add('message-div');
  studySessionDiv.classList.add('rounded');
  studySessionDiv.classList.add('panel');
  studySessionDiv.appendChild(headerDiv);
  studySessionDiv.appendChild(bodyDiv);

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
