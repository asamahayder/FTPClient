package com.company;

import java.io.*;
import java.net.*;
import java.util.*;


/** Denne klasse er af pladshensyn skrevet meget kompakt. Den beste mÃ¥de at
 forstÃ¥ den er at prÃ¸ve den fra et program, f.eks BenytFtpForbindelse, og
 bruge trinvis gennemgang til at fÃ¸lge med i hvordan den fungerer. */

public class FTPClient
{
    private Socket kontrol;
    private PrintStream ud;
    private BufferedReader ind;

    /** Modtager værtens svar, der godt kan lÃ¸be over flere linjer. Sidste linje
     er en svarkode pÃ¥ tre cifre, uden en bindestreg '-' pÃ¥ plads nummer 4 */
    private String svar() throws IOException
{
    while (true) {
        String s = ind.readLine();
        System.out.println("modt: "+s);
        if (s.length()>=3 && s.charAt(3)!='-' && Character.isDigit(s.charAt(0))
                && Character.isDigit(s.charAt(1)) && Character.isDigit(s.charAt(2)))
            return s;   // afslut lÃ¸kken og returner sidste linje med statuskode
    }
}

    public String sendKommando(String kommando) throws IOException
    {
        System.out.println("send: "+kommando);
        ud.println(kommando);
        ud.flush();         // sÃ¸rg for at data sendes til værten fÃ¸r vi lÃ¦ser svar
        return svar();
    }

    public void forbind(String vært, String bruger, String kode)throws IOException
    {
        kontrol = new Socket(vært,21);
        ud  = new PrintStream(kontrol.getOutputStream());
        ind = new BufferedReader(new InputStreamReader(kontrol.getInputStream()));
        svar();                     // LÃ¦s velkomst fra vært
        sendKommando("USER "+bruger);  // Send brugernavn
        sendKommando("PASS "+kode);    // Send adgangskode
    }

    /** FÃ¥ en forbindelse beregnet til at overfÃ¸re data (filer) til/fra værten */
    private Socket skafDataforbindelse() throws IOException
    {
        String maskineOgPortnr = sendKommando("PASV");
        StringTokenizer st = new StringTokenizer(maskineOgPortnr, "(,)");
        if (st.countTokens() < 7) throw new IOException("Ikke logget ind");
        st.nextToken(); // spring over 5 bidder fÃ¸r portnummer kommer
        st.nextToken(); st.nextToken(); st.nextToken(); st.nextToken();
        int portNr = 256*Integer.parseInt(st.nextToken())
                + Integer.parseInt(st.nextToken());
        return new Socket(kontrol.getInetAddress(), portNr); // forbind til porten
    }

    public void receiveText(String kommando, String data) throws IOException
    {
        Socket df = skafDataforbindelse();
        PrintStream dataUd = new PrintStream( df.getOutputStream() );
        sendKommando(kommando);        // f.eks STOR fil.txt
        dataUd.print(data);
        dataUd.close();
        df.close();
        svar();
    }

    public String receiveText(String kommando) throws IOException
    {
        Socket df = skafDataforbindelse();
        BufferedReader dataInd = new BufferedReader(new InputStreamReader(
                df.getInputStream()));
        sendKommando(kommando); // f.eks LIST eller RETR fil.txt
        StringBuilder sb = new StringBuilder();
        String s = dataInd.readLine();
        while (s != null) {
            System.out.println("data: "+s);
            sb.append(s+"\n");
            s = dataInd.readLine();
        }
        dataInd.close();
        df.close();
        svar();
        return sb.toString(); // returnÃ©r en streng med de data vi fik fra værten
    }
}
