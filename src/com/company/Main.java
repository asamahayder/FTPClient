package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            //  DOWNLOADING
            FTPClient client = new FTPClient();
            client.forbind("ftp.cs.brown.edu", "anonymous", "adgangskoden");


            String file1 = client.receiveText("RETR pub/gp/readme.txt");
            try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.home") + "/Desktop/file1.txt")) {
                printWriter.println(file1);
            }

            String file2 = client.receiveText("RETR pub/alt.quotations/README");
            try (PrintWriter printWriter = new PrintWriter(System.getProperty("user.home") + "/Desktop/file2.txt")) {
                printWriter.println(file2);
            }

            //READING VIA BUFFEREDREADER
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/file1.txt"));

            StringBuilder text = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append(System.lineSeparator());
            }

            String data = text.toString();
            if (data.length() >= 1024) {
                data = data.substring(0, 1024);
            }
            System.out.println("###########################################################################################");
            System.out.println("#####################################start#################################################");
            System.out.println("###########################################################################################");
            System.out.println(data);
            System.out.println("###########################################################################################");
            System.out.println("#######################################end#################################################");
            System.out.println("###########################################################################################");

            //READING file2 VIA BUFFEREDREADER
            reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/file2.txt"));

            text = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append(System.lineSeparator());
            }

            data = text.toString();
            if (data.length() >= 1024) {
                data = data.substring(0, 1024);
            }

            System.out.println("###########################################################################################");
            System.out.println("#####################################start#################################################");
            System.out.println("###########################################################################################");
            System.out.println(data);
            System.out.println("###########################################################################################");
            System.out.println("#######################################end#################################################");
            System.out.println("###########################################################################################");

            String message = "this is a test file for a ftp client";
            client.uploadTextFile("STOR incoming/testfilnummer1.txt", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
