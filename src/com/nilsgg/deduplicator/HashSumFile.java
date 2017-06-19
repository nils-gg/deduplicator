package com.nilsgg.deduplicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nils Gerstner on 2017-06-12.
 */
class HashSumFile {
    private String pc, path, hashSum=null;
    private int id = 0;
    private static int count=0;
    private double timeSize = 0;

    private static MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    HashSumFile() throws IOException {
        this(1,"1","1");
    }

    HashSumFile(int id, String path, String pc) throws IOException {
        this.path = path;
        this.id = id;
        this.pc = pc;
    }

    int getId() {
        return id;
    }

    public double getTimeSize() {
        return timeSize;
    }

    String getHashSum() {
        return hashSum;
    }

//    String getPc(){
//        return pc;
//    }

//    String getPath(){
//        return path;
//    }

    void createFileChecksum() throws IOException {


        File file = new File(path);
        double fileSizeInbytes = file.length();
        double time = System.currentTimeMillis();
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bufferedInputStream =  new BufferedInputStream(fis);

        //Create byte array to read data in chunks
        byte[] byteArray;
        byteArray = new byte[1024];
        int bytesCount;

        //Read file data and update in message digest
        while ((bytesCount = bufferedInputStream.read(byteArray)) != -1) digest.update(byteArray, 0, bytesCount);

        //close the stream; We don't need it now.
        bufferedInputStream.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        count++;
        // filesize / (endingtime - startingtime)
        this.timeSize = fileSizeInbytes/(System.currentTimeMillis()-time);

        //return complete hash
        this.hashSum = sb.toString();
    }
    void print(){
        System.out.println(count + ". hashSums generated.");
    }
}
