/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciowebcam;

import com.github.sarxos.webcam.Webcam;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author nazaret
 */
public class ServidorTCP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            // Creamos la tubería para conectarnos con el cliente
            ServerSocket server_socket = new ServerSocket(31415);
            
            // Abrimos la cámara
            webcam.open();
            
            // Tubería para enviar los datos al cliente
            Socket socket = null;
            
            do{
                socket = server_socket.accept();

                // Tomamos la imagen y la mandamos
                BufferedImage imagen = webcam.getImage();
                ImageIO.write(imagen, "png", socket.getOutputStream());
              
                socket.close();
            }while(true);
        }catch(IOException ex){
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Necesitamos la cámara como atributo de la clase
    private static Webcam webcam = Webcam.getDefault();
}
