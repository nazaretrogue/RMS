/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciowebcam;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nazaret
 */
public class ServidorUDP {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int puerto = 31415;
        if(args.length == 1){
            puerto = Integer.parseInt(args[0]);
        }
        try {
            ServidorUDP server = new ServidorUDP(puerto);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServidorUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ServidorUDP(int puerto) throws SocketException, IOException, InterruptedException{
        byte []peticion_recibida = new byte[4096];
        
        // Abrimos la cámara
        webcam.open();

        // Creamos el socket
        DatagramSocket server_socket = new DatagramSocket(puerto);

        // Esperamos un mensaje
        while(true) {
            // Almacenamos dir. IP y puerto en el datagrama a enviar
            DatagramPacket paquete_recibido = new DatagramPacket(peticion_recibida, peticion_recibida.length);
            server_socket.receive(paquete_recibido);

            System.out.println("Recibida petición de webcam");

            // Capturamos una imagen
            BufferedImage imagen = webcam.getImage();

            // Copiamos la imagen a un stream (ByteArrayOutputStream)
            // Para ello, añadimos primero el número de filas (getHeight),
            // luego el número de columnas (getWidth) y por último los
            // pixeles RGB (getRGB)         
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int filas = imagen.getHeight();
            int col = imagen.getWidth();
            int []pixeles = imagen.getRGB(0, 0, col, filas, null, 0, col);

            // Calculamos el número de datagramas para enviarlo al cliente
            int num_datagramas = (Integer.BYTES*2+pixeles.length*Integer.BYTES)/peticion_recibida.length;
            
            // Escribimos en la tubería los datagramas totales, las filas y las columnas de la imagen
            out.write(toBytes(num_datagramas));
            out.write(toBytes(filas));
            out.write(toBytes(col));

            // Convertimos los píxeles de la imagen a bytes para enviarlos
            for(int i=0; i<pixeles.length; i++)
                out.write(toBytes(pixeles[i]));

            while(out.size()%peticion_recibida.length != 0)
                out.write((byte)0);

            // Almacenamos en un array de bytes la información a transmitir
            byte []datos_imagen = out.toByteArray();
            byte []datagrama_envio;
            DatagramPacket fragmento_img;

            // Transmitimos el número de datagramas que se van a enviar y transmitimos 
            // los datagramas que contienen los datos de la imagen (se recomienda 
            // usar la función sleep entre cada envío)
            for(int i=0; i<datos_imagen.length; i+=peticion_recibida.length) {
                // Enviamos los datos en fragmentos de tamaño de la petición que hemos recibido
                datagrama_envio = Arrays.copyOfRange(datos_imagen, i, i+(peticion_recibida.length-1));
                
                // Enviamos los fragmentos de la imagen
                fragmento_img = new DatagramPacket(datagrama_envio, datagrama_envio.length, paquete_recibido.getAddress(), paquete_recibido.getPort());
                server_socket.send(fragmento_img);
                
                // Esperamos 2 segundos entre transmisiones
                Thread.sleep(2);
            }
        }
    }
    
    // Funcion que convierte un entero a bytes
    public static byte[] toBytes(int i){
        byte[] resultado = new byte[4];
        resultado[0] = (byte) (i >> 24);
        resultado[1] = (byte) (i >> 16);
        resultado[2] = (byte) (i >> 8);
        resultado[3] = (byte) (i /*>> 0*/);
        
        return resultado;
    }
    
    private static Webcam webcam = Webcam.getDefault();
}
