package com.tinyws;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author : tiny
 * @version :
 * @description : FileUtils
 * @date : 2020/12/13 11:39 PM
 */
public class ZipFileUtils {
    public static void addZipEntry(ZipOutputStream zipOutputStream, ZipEntry zipEntry, InputStream inputStream) {
        try {
            zipOutputStream.putNextEntry(zipEntry);
            byte[] buffer = new byte[16 * 1024];
            int length = -1;
            do {
                length = inputStream.read(buffer, 0, buffer.length);
                if (length != -1) {
                    zipOutputStream.write(buffer, 0, length);
                    zipOutputStream.flush();
                } else {
                    break;
                }
            } while (true);


        } catch (Exception e) {
        } finally {
            closeQuietly(inputStream);
            try {
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }
}
