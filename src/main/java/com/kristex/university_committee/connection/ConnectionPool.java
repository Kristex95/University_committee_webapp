package com.kristex.university_committee.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static ConnectionPool cp = new ConnectionPool();
    private final String URL = "jdbc:postgresql://localhost:5432/committee_db";
    private final String USER = "postgres";
    private final String PASSWORD = "13579010";
    private final int MAX_CONNECTION = 8;
    private BlockingQueue<Connection> connections;

    private ConnectionPool(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found");
        }
        connections = new LinkedBlockingQueue<>(MAX_CONNECTION);
        try {
            for (int i = 0; i < MAX_CONNECTION; i++) {
                connections.put(DriverManager.getConnection(URL, USER, PASSWORD));
            }
        }catch (SQLException e){
            System.out.println("Trouble " + e);
        }catch (InterruptedException e){
            System.out.println("Connection was interrupted");
        }
    }

    public static ConnectionPool getConnectionPool(){
        return cp;
    }

    public Connection getConnection() throws InterruptedException, SQLException {
        Connection c = connections.take();
        if (c.isClosed()){
            c = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return c;
    }
    public void releaseConnection(Connection c) throws InterruptedException {
        connections.put(c);
    }
}
