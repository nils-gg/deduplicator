package com.nilsgg.postgresql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Nils Gerstner on 2017-06-13.
 */


public class Main {
        private String dir;
        private boolean walkFileTree;
        private String dataBase;
        private String dbUser;
        private String dbPassword;
        private boolean createNewDBTable;
        private boolean configFile=true;

    public void main(String[] args) throws IOException {

        loadProperties();
        if (!configFile)
            createNewDBTable=true;
        PostSQL hDB = null;
        try {
            hDB = new PostSQL(createNewDBTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TreeWalker treeWalker = new TreeWalker(dir, hDB);
        treeWalker.startWalk();
        treeWalker.print();

        assert hDB != null;
        while (hDB.getHashableEntry() != null) {
            HashSumFile hsf = hDB.getHashableEntry();
            hsf.createFileChecksum();
            hDB.updateHashSum(hsf.getHashSum(), hsf.getId());
        }
        hDB.print();
        new HashSumFile(1, "1", "1").print();
    }
    private void loadProperties() throws IOException {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            this.dir = prop.getProperty("dir");
            this.walkFileTree = prop.getProperty("walkFileTree").toLowerCase().equals("true");
            this.dataBase = prop.getProperty("database");
            this.dbUser = prop.getProperty("dbuser");
            this.dbPassword = prop.getProperty("dbpassword");
            this.createNewDBTable = prop.getProperty("createNewDBTable").toLowerCase().equals("true");
        } catch (FileNotFoundException a) {
            System.out.println("Config file not found...");
            new ConfigWizard();
            loadProperties();
            this.configFile=false;

        }
    }
}

