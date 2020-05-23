/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciowebcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author nazaret
 */
public class ClienteUDP {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String dir_ip = "localhost";
            int puerto = 31415;
            
            if(args.length == 2){
                dir_ip = args[0];
                puerto = Integer.parseInt(args[1]);
            }
            
            ClienteUDP cliente = new ClienteUDP(dir_ip, puerto);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClienteUDP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClienteUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ClienteUDP(String dir_ip, int puerto) throws SocketException, UnknownHostException, IOException{
        // Creamos el socket para la conexión con el servidor
        DatagramSocket socket = new DatagramSocket();
        
        // Enviamos un mensaje al servidor para pedir la imagen
        byte []peticion = "Peticion".getBytes();
        DatagramPacket envio = new DatagramPacket(peticion, peticion.length, InetAddress.getByName(dir_ip), puerto);
        socket.send(envio);
        
        // Recibimos el número de datagramas que se va a recibir
        byte []bytes_imagen = new byte[4096];
        DatagramPacket imagen_recibida = new DatagramPacket(bytes_imagen, bytes_imagen.length);
        
        // Recibimos y almacenamos la información del servidor
        socket.receive(imagen_recibida);
        bytes_imagen = imagen_recibida.getData();
        
        // Creamos un stream de bytes ByteArrayOutputStream para ir leyendo la información recibida
        int num_datagramas = 0, filas = 0, col = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Usamos un array auxiliar para transformar los bytes a los pixeles de la
        // imagen para poder visualizarla luego
        byte []aux = new byte[4];
        
        for(int i=0; i<bytes_imagen.length; i+=4){
            aux[0] = bytes_imagen[i];
            aux[1] = bytes_imagen[i+1];
            aux[2] = bytes_imagen[i+2];
            aux[3] = bytes_imagen[i+3];
            
            switch(i) {
                case 0:
                    num_datagramas = byteArrayToInt(aux);
                    break;
                case 4:
                    filas = byteArrayToInt(aux);
                    break;
                case 8:
                    col = byteArrayToInt(aux);
                    break;
                default:
                    out.write(aux);
                    break;
            }
        }
        
        // Recibimos tantos paquetes como datagramas nos haya indicado el servidor
        for(int i=0; i<num_datagramas; i++){
            socket.receive(imagen_recibida);
            bytes_imagen = imagen_recibida.getData();
            out.write(bytes_imagen);
        }
        
        byte []array_bytes_imagen = out.toByteArray();
        int []imagen_reconstruida = new int[array_bytes_imagen.length/4];
        
        // Reconstruimos la imagen a partir de los datos que hemos calculado antes
        int j=0;
        
        for(int i=0; i<array_bytes_imagen.length; i+=4){
            aux[0] = array_bytes_imagen[i];
            aux[1] = array_bytes_imagen[i+1];
            aux[2] = array_bytes_imagen[i+2];
            aux[3] = array_bytes_imagen[i+3];
            
            imagen_reconstruida[j] = byteArrayToInt(aux);
            j++;
        }
        
        // Mostramos la imagen reconstruida
        BufferedImage imagen = new BufferedImage(col, filas, BufferedImage.TYPE_INT_RGB);
        imagen.setRGB(0, 0, col, filas, imagen_reconstruida, 0, col);
        
        JLabel label = new JLabel(new ImageIcon(imagen));
        JFrame frame = new JFrame();
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
    
    // Funcion que convierte un array de bytes a un entero
    public static int byteArrayToInt(byte[] b){
        if(b.length == 4)
            return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
        
        else if(b.length == 2)
            return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);
        
        else if(b.length == 1)
            return b[0];
        
        else
            return 0;
    }
}
