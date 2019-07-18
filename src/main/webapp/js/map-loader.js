let map;
/* Editable marker that displays when a user clicks in the map. */
let editMarker;
function createMap(){
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 38.5949, lng: -94.8923},
    zoom: 4,
    styles: [
      {
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#f5f5f5"
          }
        ]
      },
      {
        "elementType": "labels.icon",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#616161"
          }
        ]
      },
      {
        "elementType": "labels.text.stroke",
        "stylers": [
          {
            "color": "#f5f5f5"
          }
        ]
      },
      {
        "featureType": "administrative.land_parcel",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#bdbdbd"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#eeeeee"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#757575"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#e5e5e5"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#9e9e9e"
          }
        ]
      },
      {
        "featureType": "road",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#ffffff"
          }
        ]
      },
      {
        "featureType": "road.arterial",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#757575"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#dadada"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#616161"
          }
        ]
      },
      {
        "featureType": "road.local",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#9e9e9e"
          }
        ]
      },
      {
        "featureType": "transit.line",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#e5e5e5"
          }
        ]
      },
      {
        "featureType": "transit.station",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#eeeeee"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#c9c9c9"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#9e9e9e"
          }
        ]
      }
    ]
  });
  var geocoder = new google.maps.Geocoder();

  document.getElementById('submit').addEventListener('click', function() {
    geocodeAddress(geocoder, map);
    scrollTo(document.documentElement, document.getElementById("map").offsetTop, 600);
  });
  fetchMarkers();
}

/** Smooth scrolling to target element */
function scrollTo(element, to, duration) {
    if (duration <= 0) return;
    var difference = to - element.scrollTop;
    var perTick = difference / duration * 10;

    setTimeout(function() {
        element.scrollTop = element.scrollTop + perTick;
        if (element.scrollTop === to) return;
        scrollTo(element, to, duration - 10);
    }, 10);
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
        createMarkerForEdit(latlng.lat(), latlng.lng(), universityAddress);
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
     createMarkerForDisplay(marker.lat, marker.lng, marker.content + "<br>" + "--from " + marker.universityAddress, false);
    });
  });
}
/** Creates a marker that shows a read-only info window when clicked. */
function createMarkerForDisplay(lat, lng, content, show){
  const marker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map
  });
  var infoWindow = new google.maps.InfoWindow({
    content: content.replace(/(\n|\r|\r\n)/g, '<br>')
  });
  if (show) {
    infoWindow.open(map, marker);
  }
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}
/** Sends a marker to the backend for saving. */
function postMarker(lat, lng, content, universityAddress){
  const params = new URLSearchParams();
  params.append('lat', lat);
  params.append('lng', lng);
  params.append('content', content);
  params.append('universityAddress', universityAddress);
  // params.append('timestamp', System.currentTimeMillis());
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
  button.appendChild(document.createTextNode('Add Your Voice!'));
  button.onclick = () => {
    const fullContent = textBox.value + "<br>" + "--from " + universityAddress;
    postMarker(lat, lng, textBox.value, universityAddress);
    createMarkerForDisplay(lat, lng, fullContent, true);
    editMarker.setMap(null);
  };
  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);
  return containerDiv;
}
