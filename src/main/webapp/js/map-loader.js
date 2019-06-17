function createMap() {
  fetch('/university').then((response) => {
    return response.json();
  }).then((universities) => {
    const map = new google.maps.Map(document.getElementById('map'), {
      zoom: 4,
      center: {lat: 39.50, lng: -98.35}
    });
    universities.forEach((university) => {
      var geocoder = new google.maps.Geocoder();
      geocodeAddress(geocoder, map, university);
    });
  });
}

function geocodeAddress(geocoder, resultsMap, address) {
  geocoder.geocode({'address': address}, function(results, status) {
    if (status === 'OK') {
      var marker = new google.maps.Marker({
        map: resultsMap,
        position: results[0].geometry.location
      });
    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });
}
