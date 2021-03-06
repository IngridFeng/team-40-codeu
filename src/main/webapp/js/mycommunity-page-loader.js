/** Fetches users and adds them to the page. */
function loadUsers(){
  
  // fetch user
  const url = '/chats-list';
  fetch(url).then((response) => {
    return response.json();
  }).then((users) => {
    const list = document.getElementById('list');
    list.innerHTML = '';

    // build UI
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
  const userLink = document.createElement('a');
  userLink.setAttribute('href', '/user-page.html?user=' + user.email);
  userDiv.appendChild(userLink)
  userDiv.className = "user_card";
  const pictureDiv = document.createElement('div');
  pictureDiv.className = "user_card-picture";
  userLink.appendChild(pictureDiv);
  const contentDiv = document.createElement('div');
  contentDiv.className = "user_card-content";
  userLink.appendChild(contentDiv);

  // add profile pic
  const profilePic = document.createElement('img');
  const picUrl = user.imageUrl || "profilepic.png";
  profilePic.setAttribute("id","profilepic");
  profilePic.setAttribute("src", picUrl);
  profilePic.setAttribute("alt","Profile Picture");
  profilePic.setAttribute("class","profilepic");
  pictureDiv.appendChild(profilePic);

  // add profile name
  const nameElem = document.createElement('h3');
  const userName = user.nickName || user.email;
  nameElem.appendChild(document.createTextNode(userName));
  contentDiv.appendChild(nameElem)

  // add university
  const userInfoElem = document.createElement('p');
  if(!user.hasOwnProperty('universityName')){
    const universityName = 'Unknown';
    userInfoElem.appendChild(document.createTextNode('University: ' + universityName));
  } else {
    const universityName = user.universityName;
    if(universityName == ''){
        universityName = 'Unknown';
    }
    userInfoElem.appendChild(document.createTextNode('University: ' + universityName));
  }

  userInfoElem.appendChild(document.createElement('br'));

  // add major
  if(!user.hasOwnProperty('major')){
    const major = 'Unknown';
    userInfoElem.appendChild(document.createTextNode('Major: ' + major));
  } else {
    const major = user.major;
    if(major == ''){
        major = 'Unknown';
    }
    userInfoElem.appendChild(document.createTextNode('Major: ' + major));
  }

  userInfoElem.appendChild(document.createElement('br'));

  // add topic
  if(!user.hasOwnProperty('currentTopics')){
    const topic = '-';
    userInfoElem.appendChild(document.createTextNode('Fields of interest: ' + topic));
  } else {
    const topic = user.currentTopics;
    const topicStr = 'Fields of interest: ' + topic;
    if (topicStr.length > 45) {
      const topicSubStr = topicStr.substring(0, 45) + '...';
      userInfoElem.appendChild(document.createTextNode(topicSubStr));
    } else {
      userInfoElem.appendChild(document.createTextNode(topicStr));
    }
  }
  contentDiv.appendChild(userInfoElem);

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
  chatButton.setAttribute('value',"Go to Chat");
  //chatButton.classList.add("submit-button");
  //chatButton.classList.add("chat-with-me");
  chatButton.classList.add("user_card-chat_button");


  chatForm.appendChild(selectedUser);
  chatForm.appendChild(chatButton);
  contentDiv.appendChild(chatForm);

  return userDiv
}

/** Fetches data and populates the UI of the page. */
function buildUI(){
  loadUsers();
}
