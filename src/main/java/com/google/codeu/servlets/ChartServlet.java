package com.google.codeu.servlets;

import java.io.IOException;

import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.Scanner;

/**
 * Handles fetching and saving user data.
 */
@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

  private JsonArray courseDetailsArray;

  // class can be separated from servlet if necessary
  private static class courseDetails{
    String title;
    String parent;
    int weight;
    int student_count;

    private courseDetails(String title, String parent, int weight, int student_count){
      this.title = title;
      this.parent = parent;
      this.weight = weight;
      this.student_count = student_count;
    }
  }

  @Override
  public void init() {
    courseDetailsArray = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/course-enrollment.csv"));
    scanner.nextLine(); // skip csv header
    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      String curTitle = cells[1];
      String curParent = cells[2];
      int curWeight = Integer.parseInt(cells[3]);
      int curStudentCount = Integer.parseInt(cells[4]);

      courseDetailsArray.add(gson.toJsonTree(new courseDetails(curTitle,curParent,curWeight,curStudentCount)));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(courseDetailsArray.toString());
  }

}
