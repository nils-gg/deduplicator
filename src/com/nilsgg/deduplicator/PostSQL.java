package com.nilsgg.deduplicator;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by Nils Gerstner on 2017-06-12.
 */
class PostSQL {

    /*
    private String dataBase = "localhost:5432";
    private String dbUser = "username";
    private String dbPassword = "password";
    */

    private Connection conn;
    private static int count = 0;
    PostSQL(String dataBase, String dbUser, String dbPassword, boolean createTable) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", dbPassword);
        String DB_URL = "jdbc:postgresql://" + dataBase + "/filedb";
        this.conn = DriverManager.getConnection(DB_URL, props);
        if (createTable||isTable())
          createTable();
    }

    private void createTable() {
        try {
            if(isTable())
                dropTable();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE Files (path VARCHAR(4351) UNIQUE ,hashsum CHAR(128),pc CHAR(100),time TIMESTAMP NOT NULL DEFAULT now(),id SERIAL PRIMARY KEY)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void dropTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE Files");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    HashSumFile getHashableEntry(){
        String sql = "SELECT * FROM files WHERE hashsum='' ORDER BY id ASC LIMIT 1";
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();
            rs.next();
            int id = rs.getInt("id");
            String path = rs.getString("path");
            String hashSum = rs.getString("hashsum");
            String pc = rs.getString("pc");
            return new HashSumFile(id,path,pc);
        } catch (SQLException | IOException e){
        }
        return null;
    }

    void updateHashSum(String hashSum, int id){
        String sql = "UPDATE files SET hashsum='"+ hashSum +"' WHERE id = " + id;
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            count++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void print(){
        System.out.println(count + " HashSums updated in DB.");
    }

    private boolean isTable() {
        String sql = "SELECT EXISTS(SELECT * FROM pg_catalog.pg_tables WHERE tablename='files')";
//        String sql = "SELECT exists(SELECT * FROM Files WHERE path='" + path + "')";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();
            rs.next();
            return rs.getBoolean("exists");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void insertIntoTable(String path) {
        this.insertIntoTable(path, "");
    }

    private void insertIntoTable(String path, String hashSum) {
        try {
            String sql = "INSERT INTO Files (path,hashsum)  VALUES(?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, path);
            pstmt.setString(2, hashSum);
            pstmt.executeUpdate();

        } catch (SQLException e) {
        }
    }
}
