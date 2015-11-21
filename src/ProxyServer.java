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

public class ProxyServer {
    public static void main(String args[]) throws IOException {

        //primero el proxy debe recibir la pagina solicitada
        String input;
        URL webURL = null;

        //variables de conexion tipo servidor
        ServerSocket connectionSocket = null;
        Socket communicationSocket = null;

        //crear conexion
        try {
            connectionSocket = new ServerSocket(5000);
        }
        catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }  
        //iniciar conexion
        try { 
           communicationSocket = connectionSocket.accept(); 
        } 
        catch (IOException e) { 
            System.err.println("Accept failed."); 
            System.exit(1); 
        }

        //lectura de datos
        BufferedReader in = new BufferedReader( new InputStreamReader( communicationSocket.getInputStream()));

        while ((input = in.readLine()) != null) {
            try {
                String webLine;
                //String input2 = URLEncoder.encode(input, "UTF-8");
                webURL = new URL(input);
                URLConnection webConection = webURL.openConnection(); 
                webConection.connect();
                
                BufferedReader inputHTML = new BufferedReader(new InputStreamReader(webConection.getInputStream()));
                
                while ((webLine = inputHTML.readLine()) != null) { 
                    System.out.println(webLine);
                }
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
    
