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
        String inputLine;
        URL webURL = null;
        ServerSocket proxySocket = null;
        Socket communicationSocket = null;

        //crear conexion para proxy
        try {
            proxySocket = new ServerSocket(5000);
        }
        catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }  

        while (true) {
            try {
            	//primero el proxy debe de recibir la pagina solicitada
            	communicationSocket = proxySocket.accept(); 
        		BufferedReader in = new BufferedReader( new InputStreamReader( communicationSocket.getInputStream()));

        		//leer informacion del navegador
        		String urlLine = in.readLine();

        		//eliminar datos inecesarios y dejar solo URL
        		String[] words = urlLine.split(" ");
        		urlLine = words[1];

	            //crear URL y conexion con URL
	            webURL = new URL(urlLine);
	            URLConnection webConection = webURL.openConnection();
	            webConection.setRequestProperty("User-Agent","");
				webConection.connect();

	            //leer datos de URL y regresar a navegador datos
	            BufferedReader inURL = new BufferedReader(new InputStreamReader(webConection.getInputStream()));
	            PrintStream out = new PrintStream(communicationSocket.getOutputStream());
	           	
	            while ((inputLine = inURL.readLine()) != null) { 
	            	out.println(inputLine);
				} 

				in.close();
				inURL.close();
				out.close();
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
}
    
