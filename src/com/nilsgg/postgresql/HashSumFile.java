package com.nilsgg.postgresql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nils Gerstner on 2017-06-12.
 */
class HashSumFile {
    private String pc,path;
    private String hashSum=null;
    private int id = 0;
    private static int count=0;

    private static MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    HashSumFile(int id, String path, String pc) throws IOException {
        this.path = path;
        this.id = id;
        this.pc = pc;
        //this.hashSum = createFileChecksum(this.path);
    }

    int getId() {
        return id;
    }

    String getHashSum() {
        return hashSum;
    }

    String getPc(){
        return pc;
    }

    String getPath(){
        return path;
    }

    void createFileChecksum() throws IOException {


        File file = new File(path);
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray;
        byteArray = new byte[1024];
        int bytesCount;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) digest.update(byteArray, 0, bytesCount);

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        count++;
        //return complete hash
        this.hashSum = sb.toString();
    }
    void print(){
        System.out.println(count + ". hashSums generated.");
    }
}
