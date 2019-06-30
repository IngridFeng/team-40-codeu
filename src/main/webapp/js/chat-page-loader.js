// Get ?chat=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterChat = urlParams.get('chat');
// if chat parameter not in url, redirect to home
if (!parameterChat) {
  window.location.replace('/');
}

// set the chat title to the saved chat name
function buildUI() {

  const url = '/chat?chatId=' + parameterChat;
  fetch(url).then((response) => {
    return response.json();
  }).then((chatInfo) => {
    console.log(chatInfo.description);
    document.getElementById('chat-name').innerHTML = chatInfo.name;
    document.getElementById('chat-description').innerHTML = chatInfo.description;

  });


  console.log('hi');


}



// send get request to chat endpoint

// pull up requested chat id
    // requires a getChat function in Datastore
    // should also implement a chat page that pulls all chats that have ids associated with user
