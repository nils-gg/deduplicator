package com.nilsgg.postgresql;

import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by Nils Gerstner on 2017-06-13.
 */
class TreeWalker {

    private String dir;
    private File fileDir = new File(this.dir);
    private static int images = 0;
    private static int count = 0;
    private PostSQL hDB;

    TreeWalker(String dir, PostSQL hDB) {
        this.dir = dir;
        this.hDB = hDB;

    }

    void startWalk() {
        startWalk(fileDir);
    }

    private void startWalk(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                count++;
                if (!Files.isSymbolicLink(file.toPath())) {
                    if (file.isDirectory()) {
                        startWalk(file);
                    } else {
                        try {
//                            assert (getMimeType(file.getAbsolutePath()).getPrimaryType().toLowerCase().equals("image"));
                            if (getMimeType(file.
                                    getAbsolutePath()).
                                    getPrimaryType().
                                    toLowerCase().
                                    equals("image")) {
                                hDB.insertIntoTable(file.getAbsolutePath());
                                images++;
                            }
                        } catch (MimeTypeParseException e) {
//                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static MimeType getMimeType(String fileName) throws MimeTypeParseException {

        if (fileName == null) {
            return new MimeType("application", "octet");
        }
        MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();
        //if (mimeTypes == null) {
            mimeTypes = (MimetypesFileTypeMap) FileTypeMap.getDefaultFileTypeMap();
            mimeTypes.addMimeTypes("image/gif gif GIF");
            mimeTypes.addMimeTypes("image/ief ief");
            mimeTypes.addMimeTypes("image/jpeg jpeg jpg jpe JPG");
            mimeTypes.addMimeTypes("image/tiff tiff tif");
            mimeTypes.addMimeTypes("image/png png PNG");
        //}
        return new MimeType(mimeTypes.getContentType(fileName));

    }
    void print(){
        System.out.println(count + " files walked and " + images + " images identified.");
    }
}
