// Get ?chat=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterChat = urlParams.get('chat');
// if chat parameter not in url, redirect to home
if (!parameterChat) {
  window.location.replace('/');
}

// get chat details


// set the chat title to the saved chat name
function setChatName() {
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



// send get request to chat endpoint

// pull up requested chat id
    // requires a getChat function in Datastore
    // should also implement a chat page that pulls all chats that have ids associated with user
