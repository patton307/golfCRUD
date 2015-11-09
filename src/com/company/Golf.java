package com.company;

/**
 * Created by landonkail on 10/29/15.
 */
public class Golf {
    String username;
    int id;

    String driver;
    String irons;
    String wedge;
    String putter;
    String ball;

    public Golf(String driver, String irons, String wedge, String putter, String ball) {
        this.driver = driver;
        this.irons = irons;
        this.wedge = wedge;
        this.putter = putter;
        this.ball = ball;
    }

    public Golf(String username, String driver, String irons, String wedge, String putter, String ball) {
        this.username = username;
        this.driver = driver;
        this.irons = irons;
        this.wedge = wedge;
        this.putter = putter;
        this.ball = ball;
    }

    public Golf(String username, int id, String driver, String irons, String wedge, String putter, String ball) {
        this.username = username;
        this.id = id;
        this.driver = driver;
        this.irons = irons;
        this.wedge = wedge;
        this.putter = putter;
        this.ball = ball;
    }

    public Golf() {
    }
}
