// Fetch messages and add them to the page.

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterKey = urlParams.get('search');
// URL must include ?user=XYZ parameter. If not, display all messages.
var url = '/feed';
if (parameterKey) {
  url = '/feedSearch?key=' + parameterKey;
}

  /** fetches messages and populates the message container with messages */
  function fetchMessages(){
    //const url = '/feed';
    fetch(url).then((response) => {
      return response.json();
    }).then((messages) => {
      const messageContainer = document.getElementById('message-container');
      if(messages.length == 0){
        messageContainer.innerHTML = '<p>There are no posts yet.</p>';
      }
      else{
        messageContainer.innerHTML = '';
      }
      messages.forEach((message) => {
        if(!message.hasOwnProperty('chat')) {
          const messageDiv = buildMessageDiv(message);
          messageContainer.appendChild(messageDiv);
        }
      });
    });
  }

  /** creates a new message element from the message provided */
  function buildMessageDiv(message){
    // build div
    const messageDiv = document.createElement('div');
    const pictureDiv = document.createElement('div');
    const contentDiv = document.createElement('div');
    pictureDiv.className = "user_card-picture";
    contentDiv.className = "user_card-content";
    messageDiv.className = "user_card";
    messageDiv.appendChild(pictureDiv);
    messageDiv.appendChild(contentDiv);

    // build picture div
    const profilePic = document.createElement('img');

    var profilePicUrl = "profilepic.png";
    if(message.hasOwnProperty('profilePic')) {
      profilePicUrl = message.profilePic;
    }

    var picUrl = message.profilePic || "profilepic.png";
    if(picUrl && picUrl.style) {
     picUrl.style.height = '75px';
     picUrl.style.width = '75px';
     picUrl.style.borderRadius = '50%';
    }
    profilePic.setAttribute("id","profilepic");
    profilePic.setAttribute("src", profilePicUrl)
    profilePic.setAttribute("alt", "Profile picture")
    profilePic.setAttribute("class", "profilepic")

    pictureDiv.appendChild(profilePic);

    // build content div
    const bodyDiv = document.createElement('div');
    bodyDiv.classList.add('message-body');
    const body = document.createElement('div');
    body.classList.add('message-body');
    body.classList.add('message-text');
    body.innerHTML = message.text;
    const bodyTranslated = document.createElement('div');
    bodyTranslated.classList.add('message-body');
    bodyTranslated.classList.add('message-translated');
    bodyTranslated.classList.add('hidden');
    bodyDiv.appendChild(body);
    bodyDiv.appendChild(bodyTranslated);

    if(message.hasOwnProperty('imageUrl')) {
      const img = document.createElement('img');
      img.src = message.imageUrl;
      bodyDiv.appendChild(img);
    }
    contentDiv.appendChild(bodyDiv);

    // build footer div
    const footerDiv = document.createElement('div');
    footerDiv.className = "user-card_footer-div";

    const usernameDiv = document.createElement('div');
    usernameDiv.classList.add("left-align");
    usernameDiv.style.textAlign = "left";
    const userLink = document.createElement('a');
    const url = '/nickName?user=' + message.user;
    fetch(url).then((response) => {
      return response.text();
    }).then((nickName) => {
      const userName = nickName || message.user;
      const userLinkText = document.createTextNode(userName);
      userLink.appendChild(userLinkText);
    });

    userLink.href = "/user-page.html?user=" + message.user
    usernameDiv.appendChild(userLink);
    footerDiv.appendChild(usernameDiv);

    // time div
    const timeDiv = document.createElement('div');
    timeDiv.classList.add('right-align');
    timeDiv.appendChild(document.createTextNode(new Date(message.timestamp).toLocaleString()));
    footerDiv.appendChild(timeDiv);
    contentDiv.appendChild(footerDiv);

    return messageDiv;
  }

  /** translates all of the messages to the selected language */
  function requestTranslation() {
    var messageTexts = document.getElementsByClassName("message-text");
    var messageContainers = document.getElementsByClassName("message-translated");
    const languageCode = document.getElementById('language').value;
    if(languageCode == "orig"){
      for (var i = 0; i < messageTexts.length; i++) {
        messageTexts[i].classList.remove('hidden');
        messageContainers[i].classList.add('hidden');
      }
    } else {
      for (var i = 0; i < messageTexts.length; i++) {
        translateElement(messageTexts[i], messageContainers[i], languageCode);
      }
    }
  }

  /** translates a single message by hiding the original message and revealing the translated message */
  function translateElement(textElement, messageContainer, languageCode) {
    const text = textElement.innerText;

    const resultContainer = messageContainer;
    resultContainer.innerText = 'Loading...';
    textElement.classList.add('hidden');
    messageContainer.classList.remove('hidden');

    const params = new URLSearchParams();
    params.append('text', text);
    params.append('languageCode', languageCode);

    fetch('/translate', {
      method: 'POST',
      body: params
    }).then(response => response.text())
    .then((translatedMessage) => {
      resultContainer.innerText = translatedMessage;
    });

  }

  function searchRedirect() {
    var searchKey = document.getElementById('search-key').value;
    if (searchKey == "") {
      window.location.replace("/feed.html");
    } else {
      window.location.replace("/feed.html?search=" + searchKey);
    }
  }

  // Fetch data and populate the UI of the page.
  function buildUI(){
   fetchMessages();
  }
