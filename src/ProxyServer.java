/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mauro
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyServer {
    public static void main(String args[]) throws IOException {
        //variables
        ServerSocket proxySocket = null;
        Socket communicationSocket = null;
        boolean read = true;

        try{
		    proxySocket = new ServerSocket(5000);

            while(read){
	         	communicationSocket = proxySocket.accept();
	         	new Threads(communicationSocket).start();
            }

		    proxySocket.close();
		} catch (IOException e){
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}


class Threads extends Thread{
	//variables
    private String inputLine;
    private URL webURL = null;
    private String urlLine;
    private String[] words;
	private BufferedReader inURL;
    private BufferedReader in;
    private PrintStream out;
    private Socket communicationSocket = null;
    private URLConnection webConection;

    public Threads(Socket communicationSocket){
        this.communicationSocket = communicationSocket;
    }
	public void run(){
	    try{
            in = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
            out = new PrintStream(communicationSocket.getOutputStream());

            urlLine = in.readLine();

            words = urlLine.split(" ");
        	urlLine = words[1];

        	webURL = new URL(urlLine);
	        webConection = webURL.openConnection();
	        webConection.connect();

            inURL = new BufferedReader(new InputStreamReader(webConection.getInputStream()));

            while ((inputLine = inURL.readLine()) != null) { 
            	//System.out.println(inputLine);
            	out.println(inputLine);
			} 

            in.close();
			inURL.close();
			out.close();
			communicationSocket.close();
        }
        catch (UnsupportedEncodingException e) { 
            System.err.println(e);
        }
        catch (MalformedURLException e) {
            System.err.println(e); 
        }
        catch (IOException e) { 
            System.err.println(e);
        }
	}
}