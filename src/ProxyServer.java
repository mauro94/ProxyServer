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
        	//crear socket
		    proxySocket = new ServerSocket(5000);

            while(read){
            	//aceptar conexion y crear thread
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
    private URL webURL = null;
    private String line;
    private String urlLine;
    private String requestMethod;
    private String[] words;
    private BufferedReader in;
    private InputStream inHTTP = null;
    private BufferedReader inURL;
    private DataOutputStream out;
    private Socket communicationSocket = null;
    private URLConnection webConection;
    private HttpURLConnection webHttpConnection;
    private byte b[];
    private int index;

    public Threads(Socket communicationSocket){
        this.communicationSocket = communicationSocket;
    }

	public void run(){
	    try{
	    	//crear accesos de lectura y escritura
            in = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
            out = new DataOutputStream(communicationSocket.getOutputStream());

            //leer linea de navegador
            line = in.readLine();
            
            //obtener SOLO el URL y grabarlo en URLline
            words = line.split(" ");
            requestMethod = words[0];
        	urlLine = words[1];

        	//revisar que el request method es GET, es el unico permitido
        	if (requestMethod.equals("GET")) {
        		//crear URL
	        	webURL = new URL(urlLine);
	        	checkError400(webURL.toString());
	        	//conectarse con el URL
		        webConection = webURL.openConnection();

		        //crear URL HTTP
                HttpURLConnection webHttpConnection = (HttpURLConnection)webConection;
                //crear acceso de lectura con el URL HTTP
                //webHttpConnection.setRequestProperty("User-Agent","");
                //webHttpConnection.setRequestMethod("GET");
		        webHttpConnection.connect();
            	inHTTP = webHttpConnection.getInputStream();
            	inURL = new BufferedReader(new InputStreamReader(inHTTP));


	            //interpretar los datos del URL y mandarlos al navegador
	            b = new byte[1024];
                index = inHTTP.read(b, 0, 1024);

                while (index != -1) {
                  out.write(b, 0, index);
                  index = inHTTP.read(b, 0, 1024);
                }
                out.flush();
			}

			else {
				error501(line);
			}

			//cerrar conexiones
			if (inHTTP != null) {
				inHTTP.close();
			}
			if (inURL != null) {
				inURL.close();
			}
			if (in != null) {
          	  in.close();
			}
			if (out != null) {
				out.close();

			}
			if (communicationSocket != null) {
				communicationSocket.close();
			}
			
        }
        catch (UnsupportedEncodingException e) { 
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (IOException e) { 
            System.out.println(e);
        }
	}

	/*public static void checkError400(String urlString) throws MalformedURLException, IOException {
    	URL u = new URL(urlString); 
    	HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
    	huc.setRequestMethod("GET"); 
    	huc.connect(); 
    	System.out.println(huc.getResponseCode());
	}*/


	public static void error501(String line) {
		System.out.println();
		System.out.println("------------------------------------------");
		System.out.println(line);
		System.out.println("NOT IMPLEMENTED");
		System.out.println("ERROR 501");
		System.out.println("------------------------------------------");
		System.out.println();
	}

	public static void error400(String line) {
		System.out.println();
		System.out.println("------------------------------------------");
		System.out.println(line);
		System.out.println("BAD REQUEST");
		System.out.println("ERROR 400");
		System.out.println("------------------------------------------");
		System.out.println();
	}
}