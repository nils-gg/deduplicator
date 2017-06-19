package com.nilsgg.deduplicator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Nils Gerstner on 2017-06-19.
 */
public class ThreadManager {
    PostSQL hDB;


    public ThreadManager(PostSQL hDB) {
        this.hDB = hDB;
    }

    private void test() throws IOException {
        assert hDB != null;
        while (hDB.getHashableEntry() != null) {
            HashSumFile hsf = hDB.getHashableEntry();
            hsf.createFileChecksum();
            hDB.updateHashSum(hsf.getHashSum(), hsf.getId());
        }
    }
    class hashQueue{
        Queue<HashSumFile> queue = new ConcurrentLinkedDeque<>();
    }



    /*
    Test if new threads minimize the time to create new hashes. Spawn or drain threads dynamically.
     */
}
