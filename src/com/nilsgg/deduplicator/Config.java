package com.nilsgg.deduplicator;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Nils Gerstner on 6/16/17.
 */
class Config {


    private String dir = "/";
    private boolean walkFileTree = true;
    private String dataBase = "localhost:5432";
    private String dbUser = "username";
    private String dbPassword = "password";
    private boolean createNewDBTable = true;

    Config() {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void load() throws IOException {
        Properties prop = new Properties();
        InputStream input;
        try {
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            this.dir = prop.getProperty("dir");
            this.walkFileTree = prop.getProperty("walkFileTree").toLowerCase().equals("true");
            this.dataBase = prop.getProperty("dataBase");
            this.dbUser = prop.getProperty("dbUser");
            this.dbPassword = prop.getProperty("dbPassword");
            this.createNewDBTable = prop.getProperty("createNewDBTable").toLowerCase().equals("true");
        } catch (FileNotFoundException a) {
            System.out.println("Config file not found...");
            new Wizard();
            load(); //load the new config file
        }
    }


    String getDir() {
        return dir;
    }

    boolean isWalkFileTree() {
        return walkFileTree;
    }

    String getDataBase() {
        return dataBase;
    }

    String getDbUser() {
        return dbUser;
    }

    String getDbPassword() {
        return dbPassword;
    }

    boolean isCreateNewDBTable() {
        return createNewDBTable;
    }


    private void setDir(String dir) {
        this.dir = dir;
    }

    private void setWalkFileTree(boolean walkFileTree) {
        this.walkFileTree = walkFileTree;
    }

    private void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    private void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    private void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    private void setCreateNewDBTable(boolean createNewDBTable) {
        this.createNewDBTable = createNewDBTable;
    }

    class Wizard {
        private Scanner sc = new Scanner(System.in);
        Wizard() throws IOException {
            System.out.println(
                    "\u001B[32m       L E T S  C R E A T E\n" +
                            "                A\n" +
                            "C O N F I G U R A T I O N  F I L E\n" +
                            "         T O G E T H E R\u001B[0m");

            System.out.println("What folder should i examine?  [" + dir + "] ");
            setDir(sc.nextLine());

            System.out.println("Do you want me to examine it at every run? true/false [" + walkFileTree + "] ");
            setWalkFileTree(Boolean.parseBoolean(sc.nextLine()));

            System.out.println("please enter the location of your PostgresqlDB [" + dataBase + "] ");
            setDataBase(sc.nextLine());

            System.out.println("Name of your DB user [" + dbUser + "] ");
            setDbUser(sc.nextLine());

            System.out.println("Enter " + dbUser + "s password [" + dbPassword + "] ");
            setDbPassword(sc.nextLine());

            System.out.println("do you want to write a new DB-table everytime you run the program?\n" +
                    "\u001B[31mIf set to true, this destroys all previous obtained data!!!\n" +
                    "\u001B[0mCreate new table? true/false [" + createNewDBTable + "] ");
            setCreateNewDBTable(Boolean.parseBoolean(sc.nextLine()));

            System.out.println("\u001b[31mKeep in mind that your input is not tested for faulty input...\u001b[0m");
            System.out.println("Your configuration is beeing saved to config.properties");
            System.out.println("You can change it by changing the file directly or invoking this wizard\n" +
                    "with 'java -jar deduplicator.jar -z'");
            write(); //write config to file
        }

        private void write() {
            Properties prop = new Properties();
            OutputStream output = null;
            try {
                output = new FileOutputStream("config.properties");

                // set the properties value
                prop.setProperty("dir", getDir());
                prop.setProperty("walkFileTree", String.valueOf(isWalkFileTree()));
                prop.setProperty("dataBase", getDataBase());
                prop.setProperty("dbUser", getDbUser());
                prop.setProperty("dbPassword", getDbPassword());
                prop.setProperty("createNewDBTable", String.valueOf(isCreateNewDBTable()));

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
