let map;
/* Editable marker that displays when a user clicks in the map. */
let editMarker;
function createMap(){
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 38.5949, lng: -94.8923},
    zoom: 4
  });
  var geocoder = new google.maps.Geocoder();

  document.getElementById('submit').addEventListener('click', function() {
    geocodeAddress(geocoder, map);
  });
  fetchMarkers();
}

/** Geocode Address the User inputted and calls to create a marker */
function geocodeAddress(geocoder, resultsMap) {
  var address = document.getElementById('address').value;
  geocoder.geocode({'address': address}, function(results, status) {
    if (status === 'OK') {
      var location = results[0].geometry.location;
      var infowindow = new google.maps.InfoWindow;
      resultsMap.setCenter(location);
      geocodeLatLng(address, location, geocoder, resultsMap, infowindow);
    } else {
      alert('Geocode failed due to: ' + status);
    }
  });
}
/** Geocode LagLng to got the university name to display on the infowindow and store to datastore */
function geocodeLatLng(universityName, latlng, geocoder, map, infowindow) {
  geocoder.geocode({'location': latlng}, function(results, status) {
    if (status === 'OK') {
      if (results[0]) {
        map.setZoom(11);
        var marker = new google.maps.Marker({
          position: latlng,
          map: map
        });
        var universityAddress = universityName.concat(" @", results[0].formatted_address);
        // TODO: university name verification
        // if (universityAddress.includes("University") || universityAddress.includes("university")
        // || universityAddress.includes("College") || universityAddress.includes("college")
        // || universityAddress.includes("School") || universityAddress.includes("school")
        // || universityAddress.includes("Institute") || universityAddress.includes("institute") ) {
        createMarkerForEdit(latlng.lat(), latlng.lng(), universityAddress);
        // }
        // else {
        //   window.alert('Ooops! To my knowledge, you might not entered a university/college/school. Please correct me if I am wrong:)');
        // }
      } else {
        window.alert('No results found');
      }
    } else {
      window.alert('Geocoder failed due to: ' + status);
    }
  });
}

/** Fetches markers from the backend and adds them to the map. */
function fetchMarkers(){
  fetch('/markers').then((response) => {
    return response.json();
  }).then((markers) => {
    markers.forEach((marker) => {
     createMarkerForDisplay(marker.lat, marker.lng, marker.content)
     console.log(marker.content);
    });
  });
}
/** Creates a marker that shows a read-only info window when clicked. */
function createMarkerForDisplay(lat, lng, content){
  const marker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map
  });
  var infoWindow = new google.maps.InfoWindow({
    content: content
  });
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}
/** Sends a marker to the backend for saving. */
function postMarker(lat, lng, content){
  const params = new URLSearchParams();
  params.append('lat', lat);
  params.append('lng', lng);
  params.append('content', content);
  fetch('/markers', {
    method: 'POST',
    body: params
  });
}
/** Creates a marker that shows a textbox the user can edit. */
function createMarkerForEdit(lat, lng, universityAddress){
  // If we're already showing an editable marker, then remove it.
  if (editMarker) {
   editMarker.setMap(null);
  }
  editMarker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map
  });
  const infoWindow = new google.maps.InfoWindow({
    content: buildInfoWindowInput(lat, lng, universityAddress)
  });
  // When the user closes the editable info window, remove the marker.
  google.maps.event.addListener(infoWindow, 'closeclick', () => {
    editMarker.setMap(null);
  });
  infoWindow.open(map, editMarker);
}
/** Builds and returns HTML elements that show an editable textbox and a submit button. */
function buildInfoWindowInput(lat, lng, universityAddress){
  const textBox = document.createElement('textarea');
  textBox.setAttribute("class", "mapInfoBox");
  const button = document.createElement('button');
  button.appendChild(document.createTextNode('Shoutout to Everyone from Your University!'));
  button.onclick = () => {
    const fullContent = textBox.value + "<br>" + "--from " + universityAddress;
    postMarker(lat, lng, fullContent);
    createMarkerForDisplay(lat, lng, fullContent);
    editMarker.setMap(null);
  };
  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);
  return containerDiv;
}
