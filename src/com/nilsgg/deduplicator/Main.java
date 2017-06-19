package com.nilsgg.deduplicator;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Nils Gerstner on 2017-06-13.
 */


public class Main {

    public static void main(String[] args) throws IOException {

        Config config = new Config();

        String dir = config.getDir();
        boolean walkFileTree = config.isWalkFileTree();
        String dataBase = config.getDataBase();
        String dbUser = config.getDbUser();
        String dbPassword = config.getDbPassword();
        boolean createNewDBTable = config.isCreateNewDBTable();


        PostSQL hDB = null;
        try {
            hDB = new PostSQL(dataBase, dbUser, dbPassword, createNewDBTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (walkFileTree) {
            TreeWalker treeWalker = new TreeWalker(dir, hDB);
            treeWalker.startWalk();
            treeWalker.print();
        }else
            System.out.println("Not walking the fileTree!");

        assert hDB != null;
        while (hDB.getHashableEntry() != null) {
            HashSumFile hsf = hDB.getHashableEntry();
            hsf.createFileChecksum();
            hDB.updateHashSum(hsf.getHashSum(), hsf.getId());
        }
        hDB.print();
        new HashSumFile().print();
    }
}

