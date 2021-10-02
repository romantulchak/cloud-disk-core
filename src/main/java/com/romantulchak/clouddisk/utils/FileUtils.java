package com.romantulchak.clouddisk.utils;

public class FileUtils {

    public static String getFileExtension(String filename){
        String extension = "";

        int index = filename.lastIndexOf(".");
        if(index > 0){
            extension = filename.substring(index + 1);
        }
        return extension;
    }
}
