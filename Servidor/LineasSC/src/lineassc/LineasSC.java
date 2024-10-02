/**
 *
 * @author Hancel
 */
package lineassc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojoslineas.ExcepcionLineas;

/**
 * Clase que representa el servidor de comunicaciones de Linners.
 * @author Hancel Fernando Abrines Vasallo
 * @version 1.0
 * @since 1.0
 */
public class LineasSC {
    
    public static void main(String[] args) {

        try 
        {
            int puertoServidor = 30500;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            
            while (true) 
            {                
                Socket clienteConectado = socketServidor.accept();
                ManejadorPeticion mp = new ManejadorPeticion(clienteConectado, "192.168.1.14");
                mp.start();
            }
            
//            socketServidor.close();    
        } 
        catch (IOException ex) 
        {
            System.out.println(ex);
        }
    }
}
