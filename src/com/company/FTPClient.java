package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

public class FTPClient
{
    private Socket clientSocket;
    private PrintStream output;
    private BufferedReader input;

    private String replyFromServer() throws IOException
{
    while (true) {
        String reply = input.readLine();
        System.out.println("Server: " + reply);
        if (reply.length()>=3 && reply.charAt(3)!='-' && Character.isDigit(reply.charAt(0))
                && Character.isDigit(reply.charAt(1)) && Character.isDigit(reply.charAt(2)))
            return reply;
    }
}

    public String commandToServer(String commandLine) throws IOException
    {
        System.out.println("Client: " + commandLine);
        output.println(commandLine);
        output.flush();
        return replyFromServer();
    }

    public void forbind(String vært, String username, String password)throws IOException
    {
        clientSocket = new Socket(vært,21);
        output = new PrintStream(clientSocket.getOutputStream());
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        replyFromServer();
        commandToServer("USER " + username);
        commandToServer("PASS " + password);
    }


    private Socket getDataConnection() throws IOException
    {
        String newSocketPortConnection = commandToServer("PASV");
        StringTokenizer stringToken = new StringTokenizer(newSocketPortConnection, "(,)");
        if (stringToken.countTokens() < 7) throw new IOException("Not logged in...");
        stringToken.nextToken();
        stringToken.nextToken(); stringToken.nextToken(); stringToken.nextToken(); stringToken.nextToken();
        int portNumber = 256*Integer.parseInt(stringToken.nextToken())
                + Integer.parseInt(stringToken.nextToken());
        return new Socket(clientSocket.getInetAddress(), portNumber);
    }

    public void uploadTextFile (String commandLine, String textLines) throws IOException
    {
        Socket dataConnection = getDataConnection();
        PrintStream dataOutPut = new PrintStream( dataConnection.getOutputStream() );
        commandToServer(commandLine);
        dataOutPut.print(textLines);
        dataOutPut.close();
        dataConnection.close();
        replyFromServer();
    }

    public String receiveText(String commandLine) throws IOException
    {
        Socket dataConnection = getDataConnection();
        BufferedReader dataInput = new BufferedReader(new InputStreamReader(
                dataConnection.getInputStream()));
        commandToServer(commandLine);
        StringBuilder stringBuilder = new StringBuilder();
        String nextString = dataInput.readLine();
        while (nextString != null) {
            System.out.println("Server: "+nextString);
            stringBuilder.append(nextString+"\n");
            nextString = dataInput.readLine();
        }
        dataInput.close();
        dataConnection.close();
        replyFromServer();
        return stringBuilder.toString();
    }
}
