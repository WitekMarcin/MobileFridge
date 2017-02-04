package com.marcin.mobilefridge.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FTPService {

    private String server = "mobilefridge.cba.pl";
    private int port = 21;
    private String user = "mobilefridge";
    private String pass = "haslo1234";
    private FTPClient ftpClient;
    private int returnCode;

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public String sendPicture(String pictureId, Bitmap photo, Context context) {
        //policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);

            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return null;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return null;
            } else {
                System.out.println("LOGGED IN SERVER");
                ftpClient.changeWorkingDirectory("/mobilefridge.cba.pl/pictures");
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE, ftpClient.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(ftpClient.BINARY_FILE_TYPE);
                ftpClient.storeFile(pictureId + ".png", bitmapToSend(photo));
                ftpClient.logout();
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
            return null;
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(pictureId + ".png", Context.MODE_PRIVATE);
            photo.compress(Bitmap.CompressFormat.PNG, 100, fos); // w miejscu 100 wpisujemy kompresje (mniejsza warto�� = silniejsza kompresja)
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pictureId + ".png";

    }


    private ByteArrayInputStream bitmapToSend(Bitmap photo) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        return bs;
    }

    public Bitmap downloadRecipe(Context context, String folderToADD, String recipeId) throws IOException {
        File folder = new File(context.getFilesDir() +
                File.separator + folderToADD);
        boolean success = true;
        if (!folder.exists()) {
            folder.mkdir();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ByteArrayInputStream bs = null;
        ftpClient = new FTPClient();
        File downloadFile2 = new File(folder.getAbsolutePath(), "recipe" + recipeId + ".png");
        if (checkIfFileIsStoredLocally(downloadFile2.getAbsolutePath())) {
            return BitmapFactory.decodeFile(downloadFile2.getAbsolutePath());
        }
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.changeWorkingDirectory("/mobilefridge.cba.pl/pictures");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);

            // using InputStream retrieveFileStream(String)
            String remoteFile2 = "recipe" + recipeId + ".png";
            downloadFile2 = new File(folder.getAbsolutePath(), "recipe" + recipeId + ".png");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            {
                while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                    outputStream2.write(bytesArray, 0, bytesRead);
                }

                success = ftpClient.completePendingCommand();
                if (success) {
                    System.out.println("File #2 has been downloaded successfully.");
                }
                outputStream2.close();
                inputStream.close();
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            throw new IOException();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IOException();

            }
        }
        return BitmapFactory.decodeFile(downloadFile2.getAbsolutePath());
    }

    private boolean checkIfFileIsStoredLocally(String absolutePath) {
        try {
            if (BitmapFactory.decodeFile(absolutePath) == null)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean FileExists(String filePath) throws IOException {
        InputStream inputStream = ftpClient.retrieveFileStream(filePath);
        returnCode = ftpClient.getReplyCode();
        if (inputStream == null || returnCode == 550) {
            return false;
        }
        return true;
    }

}