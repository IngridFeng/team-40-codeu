/** Fetches users and adds them to the page. */
function fetchUserList(){
  const url = '/user-list';
  fetch(url).then((response) => {
    return response.json();
  }).then((users) => {
    const list = document.getElementById('list');
    list.innerHTML = '';

    users.forEach((user) => {
     const userListItem = buildUserListItem(user);
     list.appendChild(userListItem);
   });
  });
}

/**
 * Builds a list element that contains a link to a user page, e.g.
 * <li><a href="/user-page.html?user=test@example.com">test@example.com</a></li>
 */
function buildUserListItem(user){
  // build div
  const userDiv = document.createElement('div');

  // build profile link
  const userLink = document.createElement('a');
  userLink.setAttribute('href', '/user-page.html?user=' + user.email);

  const userName = user.nickName || user.email;
  userLink.appendChild(document.createTextNode(userName));

  userDiv.appendChild(userLink);

  // build chat form
  const chatForm = document.createElement("form");
  chatForm.setAttribute("method","post");
  chatForm.setAttribute("action","/chat");

  // add selectedUser input
  const selectedUser = document.createElement("input"); //input element, Submit button
  selectedUser.setAttribute('name',"selectedUser");
  selectedUser.setAttribute('type',"hidden");
  selectedUser.setAttribute('value',user.email);

  // build chat button
  const chatButton = document.createElement("input");
  chatButton.setAttribute('type',"submit");
  chatButton.setAttribute('value',"Chat with Me!");

  chatForm.appendChild(selectedUser);
  chatForm.appendChild(chatButton);
  userDiv.appendChild(chatForm);


  /*
  const chatButton = document.createElement("input");
    chatButton.type = "button";
    chatButton.value = "Chat with Me!";
    chatButton.onclick = createChat(user.email);
    userDiv.appendChild(chatButton);
  */

    return userDiv
}

/*
function createChat(email) {
  fetch('/chat', {
    method: 'POST',
    body: params
  }).then(response => response.text())
  .then((translatedMessage) => {
    resultContainer.innerText = translatedMessage;
  });

}
*/

/** Fetches data and populates the UI of the page. */
function buildUI(){
  fetchUserList();
}
