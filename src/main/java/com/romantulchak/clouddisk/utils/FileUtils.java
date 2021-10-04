package com.romantulchak.clouddisk.utils;

import java.util.Locale;

public class FileUtils {

    public static String getFileExtension(String filename){
        String extension = "";
        int index = filename.lastIndexOf(".");
        if(index > 0){
            extension = filename.substring(index + 1);
        }
        return extension.toLowerCase(Locale.ROOT);
    }
}
