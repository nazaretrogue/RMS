/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciowebcam;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author nazaret
 */
public class ClienteTCP {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            // Creamos un socket para la conexión con el servidor
            Socket socket = new Socket("localhost", 31415);
            
            // Recibimos la imagen de la tubería y la ponemos en una ventana para visualizarla
            BufferedImage imagen = ImageIO.read(socket.getInputStream());
            JLabel label = new JLabel(new ImageIcon(imagen));
            JFrame frame = new JFrame();
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
            
            // Cerramos la tubería
            socket.close();
        }catch(IOException ex) {
            Logger.getLogger(ClienteTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
