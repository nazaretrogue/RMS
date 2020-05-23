/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciowebcam;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author nazaret
 */
public class SCTCP extends Thread{
    public SCTCP(Socket s){
        id = s;
    }
    
    public static void main(String[] args) {
        ServerSocket server_socket;
        
        try{
            server_socket = new ServerSocket(31415);
        
            do{
                new SCTCP(server_socket.accept()).start();
            }while(true);
        }catch(IOException ex){
            Logger.getLogger(SCTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        try{
            webcam.open();
            
            do{// Tomamos la imagen y la mandamos
                BufferedImage imagen = webcam.getImage();
                ImageIO.write(imagen, "png", id.getOutputStream());
              
                id.close();
                webcam.close();
            }while(true);
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    Socket id;
    private static Webcam webcam = Webcam.getDefault();
}
