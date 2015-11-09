package com.company;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by landonkail on 11/3/15.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTable(conn);
        return conn;
    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE golf");
        conn.close();
    }

    ///////    TEST METHODS TEST METHODS TEST METHODS //////////////////////

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Landon", "");
        User user = Main.selectUser(conn, "Landon");
        endConnection(conn);

        assertTrue(user != null);
    }

    @Test
    public void testEntry() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Landon", "");
        Main.insertEntry(conn, 1, "Taylormade", "Taylormade", "Taylormade", "Taylormade", "Taylormade");
        Golf golf = Main.selectEntry(conn, 1);
        endConnection(conn);

        assertTrue(golf != null);
    }

    @Test
    public void testEntries() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Landon", "");
        Main.insertEntry(conn, 1, "Taylormade", "Taylormade", "Taylormade", "Taylormade", "Taylormade");
        ArrayList<Golf> entries = Main.selectEntries(conn, 1);
        endConnection(conn);

        assertTrue(entries.size() == 1);
    }

}