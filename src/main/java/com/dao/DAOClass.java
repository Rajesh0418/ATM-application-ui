package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOClass {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/atmapplication";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "chinnu1820";

    public Connection conn;

    public DAOClass() {
        initiateDBConnection();
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }

    void initiateDBConnection()  {
        try {
            this.conn = DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
}
