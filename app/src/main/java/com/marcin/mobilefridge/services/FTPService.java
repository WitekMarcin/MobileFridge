package com.marcin.mobilefridge.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.StrictMode;
import android.util.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FTPService {

    private String server = "ftp.192.168.0.241";
    private int port = 21;
    private String user = "FTP-User";
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

    public void sendPicture(String login, Bitmap photo, Context context) {
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
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return;
            } else {
                System.out.println("LOGGED IN SERVER");
//                ftpClient.changeWorkingDirectory("Projekt/uzytkownicy/");
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE, ftpClient.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(ftpClient.BINARY_FILE_TYPE);
                ftpClient.storeFile(login + ".png", bitmapToSend(photo));
                ftpClient.logout();
            }
        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(login + ".png", Context.MODE_PRIVATE);
            photo.compress(Bitmap.CompressFormat.PNG, 100, fos); // w miejscu 100 wpisujemy kompresje (mniejsza warto�� = silniejsza kompresja)
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private ByteArrayInputStream bitmapToSend(Bitmap photo) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

        return bs;
    }

    public void download(Context context, String folderToADD, String name) {
        File folder = new File(context.getFilesDir() +
                File.separator + folderToADD);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do so1mething on success
        } else {
            // Do something else on failure
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ByteArrayInputStream bs = null;
        ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);

            // using InputStream retrieveFileStream(String)
            String remoteFile2 = name + ".png";
            File downloadFile2 = new File(folder.getAbsolutePath(), name + ".png");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;

            //if(checkFileExists("/Projekt/miejsca/"+name+".png"))
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
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void downloadAll(Context context, String folderToADD, String name) {
        File folder = new File(context.getFilesDir() +
                File.separator + folderToADD);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            // Do so1mething on success
        } else {
            // Do something else on failure
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ByteArrayInputStream bs = null;
        ftpClient = new FTPClient();
        try {
            int ilosc_zdjec = Integer.parseInt(name.replaceAll("[\\D]", ""));
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            for (int i = 0; i <= ilosc_zdjec; i++) {
                try {
                    String remoteFile2 = "/Projekt/miejsca/" + i + ".png";
                    File downloadFile2 = new File(folder.getAbsolutePath(), i + ".png");
                    OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
                    InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
                    byte[] bytesArray = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                        outputStream2.write(bytesArray, 0, bytesRead);
                    }
                    success = ftpClient.completePendingCommand();
                    if (success) {
                        Log.d("Watek Zdjecia", "pobralem fote dla " + String.valueOf(i));
                    }
                    outputStream2.close();
                    inputStream.close();
                } catch (Exception e) {
                    Log.d("Watek Zdjecia", "nie mam dla " + String.valueOf(i));
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    boolean checkFileExists(String filePath) throws IOException {
        InputStream inputStream = ftpClient.retrieveFileStream(filePath);
        returnCode = ftpClient.getReplyCode();
        if (inputStream == null || returnCode == 550) {
            return false;
        }
        return true;
    }

}