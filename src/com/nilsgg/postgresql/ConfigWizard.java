package com.nilsgg.postgresql;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Nils Gerstner on 6/16/17.
 */
class ConfigWizard {

    private static Scanner sc = new Scanner(System.in);

    private String dir="/";
    private boolean walkFileTree=true;
    private String dataBase="localhost:5432";
    private String dbUser="username";
    private String dbPassword="password";
    private boolean createNewDBTable=true;

    ConfigWizard() throws IOException {
        System.out.println(
                "\u001B[32m       L E T S  C R E A T E\n" +
                        "                A\n" +
                        "C O N F I G U R A T I O N  F I L E\n" +
                        "         T O G E T H E R\u001B[0m");

        System.out.println("What folder should i examine?  [" + dir + "] ");
        this.dir = sc.nextLine();

        System.out.println("Do you want me to examine it at every run? true/false [" + walkFileTree + "] ");
        this.walkFileTree = sc.nextBoolean();

        System.out.println("please enter the location of your PostgresqlDB [" + dataBase + "] ");
        this.dataBase = sc.nextLine();

        System.out.println("Name of your DB user [" + dbUser + "] ");
        this.dbUser = sc.nextLine();

        System.out.println("Enter " + dbUser + "s password [" + dbPassword + "] ");
        this.dbPassword = sc.nextLine();

        System.out.println("do you want to create a new DB-table everytime you run the program?\n" +
                "\u001B[31mIf set to true, this destroys all previous obtained data!!!\n" +
                "\u001B[0mCreate new table? true/false [" + createNewDBTable + "] ");
        this.createNewDBTable = sc.nextBoolean();

        System.out.println("\u001b[31mKeep in mind that your input is not tested for faulty input...\u001b[0m");
        System.out.println("Your configuration is beeing saved to config.properties");
        System.out.println("You can change it by changing the file directly or invoking this wizard\n" +
                "with 'java -jar deduplicator.jar -z'");
        create(); //write config to file
    }

    private void create() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("dir", dir);
            prop.setProperty("walkFileTree", String.valueOf(walkFileTree));
            prop.setProperty("dataBase", dataBase);
            prop.setProperty("dbUser", dbUser);
            prop.setProperty("dbPassword", dbPassword);
            prop.setProperty("createNewDBTable", String.valueOf(createNewDBTable));

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
