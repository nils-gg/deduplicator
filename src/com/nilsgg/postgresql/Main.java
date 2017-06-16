package com.nilsgg.postgresql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by Nils Gerstner on 2017-06-13.
 */


public class Main {
    public static void main(String[] args) throws IOException {
        String dir = "/";
        boolean createTable = false;
        if (Arrays.asList(args).contains("c")) {
            createTable = true;
            if (args.length > 1)
                dir = args[0];
        } else if (args.length>0)
            dir = args[0];

        PostSQL hDB = null;
        try {
            hDB = new PostSQL(createTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        TreeWalker treeWalker = new TreeWalker(dir, hDB);
        treeWalker.startWalk();
        treeWalker.print();

        while (hDB.getHashableEntry() != null) {
            HashSumFile hsf = hDB.getHashableEntry();
            hsf.createFileChecksum();
            hDB.updateHashSum(hsf.getHashSum(), hsf.getId());
        }
        hDB.print();
        new HashSumFile(1,"12","12").print();
    }
}
