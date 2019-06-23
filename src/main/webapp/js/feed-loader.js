// Fetch messages and add them to the page.

  /** fetches messages and populates the message container with messages */
  function fetchMessages(){
    const url = '/feed';
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
       const messageDiv = buildMessageDiv(message);
       messageContainer.appendChild(messageDiv);
      });
    });
  }
  
  /** creates a new message element from the message provided */
  function buildMessageDiv(message){
   const usernameDiv = document.createElement('div');
   usernameDiv.classList.add("left-align");
   usernameDiv.appendChild(document.createTextNode(message.user));
   
   const timeDiv = document.createElement('div');
   timeDiv.classList.add('right-align');
   timeDiv.appendChild(document.createTextNode(new Date(message.timestamp)));
   
   const headerDiv = document.createElement('div');
   headerDiv.classList.add('message-header');
   headerDiv.appendChild(usernameDiv);
   headerDiv.appendChild(timeDiv);
   
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
   
   const messageDiv = document.createElement('div');
   messageDiv.classList.add("message-div");
   messageDiv.appendChild(headerDiv);
   messageDiv.appendChild(bodyDiv);
   
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
  
  // Fetch data and populate the UI of the page.
  function buildUI(){
   fetchMessages();
  }