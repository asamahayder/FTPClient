package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            //  Connecting to server
            FTPClient client = getFtpClient();

            //Retrieving the second file
            StringBuilder text = getStringBuilder(client);
            BufferedReader bufferedReaderreader;
            String line;

            String data = text.toString();
            readingBothFiles(client, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readingBothFiles(FTPClient client, String data) throws IOException {
        BufferedReader bufferedReaderreader;
        StringBuilder text;
        String line;
        if (data.length() >= 1024) {
            data = data.substring(0, 1024);
        }
        System.out.println("###########################################################################################");
        System.out.println("#####################################Start#################################################");
        System.out.println("###########################################################################################");
        System.out.println(data);
        System.out.println("###########################################################################################");
        System.out.println("#######################################End#################################################");
        System.out.println("###########################################################################################");

        //READING file2 VIA BUFFEREDREADER
        bufferedReaderreader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/file2.txt"));

        text = new StringBuilder();

        while ((line = bufferedReaderreader.readLine()) != null) {
            text.append(line);
            text.append(System.lineSeparator());
        }

        data = text.toString();
        if (data.length() >= 1024) {
            data = data.substring(0, 1024);
        }

        System.out.println("###########################################################################################");
        System.out.println("#####################################Start#################################################");
        System.out.println("###########################################################################################");
        System.out.println(data);
        System.out.println("###########################################################################################");
        System.out.println("#######################################End#################################################");
        System.out.println("###########################################################################################");

        String message = "this is a test file for a ftp client";
        client.uploadTextFile("STOR incoming/testfilnummer1.txt", message);
    }

    private static StringBuilder getStringBuilder(FTPClient client) throws IOException {
        String file2 = client.receiveText("RETR pub/alt.quotations/README");
        try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.home") + "/Desktop/file2.txt")) {
            printWriter.println(file2);
        }

        //READING VIA BUFFEREDREADER
        BufferedReader bufferedReaderreader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/file1.txt"));
        StringBuilder text = new StringBuilder();
        String line;

        while ((line = bufferedReaderreader.readLine()) != null) {
            text.append(line);
            text.append(System.lineSeparator());
        }
        return text;
    }

    private static FTPClient getFtpClient() throws IOException {
        FTPClient client = new FTPClient();
        client.forbind("ftp.cs.brown.edu", "anonymous", "password");

        // Retrieving the first file
        String file1 = client.receiveText("RETR pub/gp/readme.txt");
        try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.home") + "/Desktop/file1.txt")) {
            printWriter.println(file1);
        }
        return client;
    }
}
