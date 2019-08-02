package io.happycoding.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import io.happycoding.data.Marker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching and saving markers data.
 */
@WebServlet("/markers")
public class MarkerServlet extends HttpServlet {

  /** Responds with a JSON array containing marker data. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    List<Marker> markers = getMarkers();
    Gson gson = new Gson();
    String json = gson.toJson(markers);

    response.getOutputStream().println(json);
  }

  /** Accepts a POST request containing a new marker. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    double lat = Double.parseDouble(request.getParameter("lat"));
    double lng = Double.parseDouble(request.getParameter("lng"));
    String content = Jsoup.clean(request.getParameter("content"), Whitelist.none());
    String universityAddress = Jsoup.clean(request.getParameter("universityAddress"), Whitelist.none());
    // Long timestamp = Long.parseLong(request.getParameter("timestamp"));

    Marker marker = new Marker(lat, lng, content, universityAddress);
    storeMarker(marker);
  }

  /** Fetches markers from Datastore. */
  private List<Marker> getMarkers() {
    Boolean printWholeList = false;
    List<Marker> markers = new ArrayList<>();
    Map<List<Double>, List<String>> markersAsDict = new HashMap<>();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Marker");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      Double lat = (Double) entity.getProperty("lat");
      Double lng = (Double) entity.getProperty("lng");
      String content = (String) entity.getProperty("content");
      String universityAddress = (String) entity.getProperty("universityAddress");

      List<Double> markerKey = Arrays.asList(lat, lng);
      if (printWholeList) {
        if (markersAsDict.containsKey(markerKey)) {
          List<String> markerValue = Arrays.asList(markersAsDict.get(markerKey).get(0) + "\n" + content, universityAddress);
          markersAsDict.put(markerKey, markerValue);
        }
        else {
          markersAsDict.put(markerKey, Arrays.asList(content, universityAddress));
        }
      }
      else {
        markersAsDict.put(markerKey, Arrays.asList(content, universityAddress));
      }
    }

    for (Map.Entry<List<Double>, List<String>> entry : markersAsDict.entrySet()) {
      List<Double> key = entry.getKey();
      List<String> value = entry.getValue();
      Marker marker = new Marker(key.get(0), key.get(1), value.get(0), value.get(1));
      markers.add(marker);
    }
    return markers;
  }

  /** Stores a marker in Datastore. */
  public void storeMarker(Marker marker) {
    Entity markerEntity = new Entity("Marker");
    markerEntity.setProperty("lat", marker.getLat());
    markerEntity.setProperty("lng", marker.getLng());
    markerEntity.setProperty("content", marker.getContent());
    markerEntity.setProperty("universityAddress", marker.getUniversityAddress());
    // markerEntity.setProperty("timestamp", marker.getTimestamp());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(markerEntity);
  }
}
