package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    // Database methods START START START START START START  DATABASE METHODS //////////////////////////////////////

    public static void createTable(Connection conn) throws SQLException {
        java.sql.Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS golf (id IDENTITY, user_id INT, driver VARCHAR, irons VARCHAR, wedge VARCHAR, putter VARCHAR, ball VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        User user = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            user = new User();
            user.id = results.getInt("id");
            user.password = results.getString("password");
        }
        return user;
    }

    public static void insertEntry(Connection conn, int userId, String driver, String irons, String wedge, String putter, String ball) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO golf VALUES (NULL, ?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, driver);
        stmt.setString(3, irons);
        stmt.setString(4, wedge);
        stmt.setString(5, putter);
        stmt.setString(6, ball);
        stmt.execute();
    }

    public static Golf selectEntry(Connection conn, int id) throws SQLException {
        Golf golf = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM golf INNER JOIN users ON golf.user_id = users.id WHERE golf.user_id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            golf = new Golf();
        }
        return golf;
    }

    public static ArrayList<Golf> selectEntries(Connection conn, int id) throws SQLException {
        ArrayList<Golf> entries = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM golf INNER JOIN users ON golf.user_id = users.id WHERE golf.user_id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            Golf golf = new Golf();
            golf.id = results.getInt("golf.id");
            golf.username = results.getString("users.name");
            golf.putter = results.getString("golf.putter");
            golf.ball = results.getString("golf.ball");
            golf.driver = results.getString("golf.driver");
            golf.irons = results.getString("golf.irons");
            golf.wedge = results.getString("golf.wedge");
            entries.add(golf);
        }
        return entries;
    }

    // END END END END END DATABASE METHODS  /////////////////////////////////////////////////////

    public static void main(String[] args) throws SQLException {
        // Create Database
        java.sql.Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTable(conn);


        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    String password = session.attribute("password");

                    ArrayList<Golf> bag = selectEntries(conn, 1);


                    HashMap m = new HashMap();
                    m.put("username", username);
                    m.put("password", password);
                    m.put("golfBag", bag);

                    return new ModelAndView(m, "bag.html");
                }),
                new MustacheTemplateEngine()
        );


        // Initial login - NEW USER
        Spark.post(
                "/login",
                ((request, response) -> {
                    Session session = request.session();
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");

                    if (username.isEmpty() || password.isEmpty()) {
                        Spark.halt(403);
                    }

                    User user = selectUser(conn, username);

                    if (user == null) {
                        insertUser(conn, username, password);
                    } else if (!password.equals(user.password)) {
                        Spark.halt(403);
                    }

                    Session session = request.session();
                    session.attribute("username", username);

                    response.redirect(request.headers("Referer"));
                    return "";
                })
        ),

        Spark.post(
                "/go-to",
                ((request, response) -> {
                    Session session = request.session();

                })
        );


        // Creating initial entry
        Spark.post(
                "/create-clubs",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if (username == null) {
                        Spark.halt(403);
                    }

                    String driver = request.queryParams("driver");
                    String irons = request.queryParams("irons");
                    String wedges = request.queryParams("wedges");
                    String putter = request.queryParams("putter");
                    String ball = request.queryParams("ball");

                    try {

                        User me = selectUser(conn, username);
                        insertEntry(conn, 1, driver, irons, wedges, putter, ball);

                    } catch (Exception e) {

                    }

                    HashMap m = new HashMap();
                    m.put("driver", driver);
                    m.put("irons", irons);
                    m.put("wedges", wedges);
                    m.put("putter", putter);
                    m.put("ball", ball);

                    response.redirect(request.headers("Referer"));
                    return "";
                })
        );

        // EDIT clubs / bag
        Spark.post(
                "/edit-clubs",
                ((request, response) -> {
                    Session session = request.session();

                })
        );

        Spark.post(
                "/go-to-edit",
                ((response, request) -> {

                })
        );

    }
}
