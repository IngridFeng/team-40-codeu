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
@WebServlet("/bookchart")
public class ChartServlet extends HttpServlet {
  @Override
  public void init() {
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().println("yoyo");
  }

}
