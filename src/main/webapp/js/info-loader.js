function buildReq() {
  // get elements
  pastTopicsDiv = document.getElementById('pastTopics-div');
  pastTopicsInputs = pastTopicsDiv.getElementsByTagName('input');

  currentTopicsDiv = document.getElementById('currentTopics-div');
  currentTopicsInputs = currentTopicsDiv.getElementsByTagName('input');


  // initialize returns
  let pastTopics = [];
  let currentTopics = [];

  // for each input option
  for (i=0; i < pastTopicsInputs.length; i++) {
    // if checked, add to list
    if (pastTopicsInputs[i].checked) {
      pastTopics.push(pastTopicsInputs[i].name);
    }

    if (currentTopicsInputs[i].checked) {
      currentTopics.push(currentTopicsInputs[i].name);
    }
  }

  // set hidden values
  //realInputsDiv = document.getElementById('realInputs-div');
  //realInputsDiv.children[0].value = pastTopics;
  //realInputsDiv.children[1].value = currentTopics;

  // send info

  const params = new URLSearchParams();
  params.append('pastTopics', pastTopics);
  params.append('currentTopics', currentTopics);

  fetch('/info', {
       method: 'POST',
       body: params
  });



}
