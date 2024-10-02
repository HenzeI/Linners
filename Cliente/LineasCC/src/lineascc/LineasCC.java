/**
 *
 * @author Hancel
 */
package lineascc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojoslineas.Empresa;
import pojoslineas.ExcepcionLineas;
import pojoslineas.Linea;
import pojoslineas.LineaParada;
import pojoslineas.Operaciones;
import pojoslineas.Parada;
import pojoslineas.ParadaFavorita;
import pojoslineas.Peticion;
import pojoslineas.Respuesta;
import pojoslineas.Terminal;
import pojoslineas.Usuario;

/**
 * Clase que representa el cliente de comunicaciones de Linners.
 * @author Hancel Fernando Abrines Vasallo
 * @version 1.0
 * @since 1.0
 */
public class LineasCC {

    private Socket socketCliente;
    private String ipBD;
    
    /**
     * Constructor de la clase que establece una conexión con el servidor que aloja la base de datos Linners.
     * @param ipBD Pide por parámetro la dirección IP en la que se encuentra el servidor.
     */
    public LineasCC(String ipBD) {
        try 
        {
            System.out.println("Estableciendo la conexion con el servidor"); // Quitar
            int puertoServidor = 30500;
            socketCliente = new Socket(ipBD, puertoServidor);
            socketCliente.setSoTimeout(5000);    
        } 
        catch (IOException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Método que maneja la IOException, en el momento en el que se produzca una excepción de este tipo, se pasara por 
     * este método el cual tiene un determinado mensaje para luego ser arrojado a la clase ExcepcionLineas encargada de las excepciones.
     * @param ex Pide por parámetro una clase de tipo IOException.
     * @throws ExcepcionLineas arroja la excepción a la clase ExcepcionLineas.
     */
    private void manejadorIOException(IOException ex) throws ExcepcionLineas 
    {
        ExcepcionLineas e = new ExcepcionLineas();
        e.setMensajeUsuario("Fallo en las comunicaciones. Consulte con el administrador");
        e.setMensajeErrorBd(ex.getMessage());
        throw e;
    }
 
    /**
     * Método que maneja la ClassNotFoundException, en el momento en el que se produzca una excepción de este tipo, se pasara por
     * este método el cual tiene un determinado mensaje para luego ser arrojado a la clase ExcepcionLineas encargada de las excepciones.
     * @param ex Pide por parámetro uuna clase de tipo ClassNotFoundException.
     * @throws ExcepcionLineas Arroja la excepción a la clase ExcepcionLineas.
     */
    private void manejadorClassNotFoundException(ClassNotFoundException ex) throws ExcepcionLineas 
    {
        ExcepcionLineas e = new ExcepcionLineas();
        e.setMensajeUsuario("Error general en el sistema. Consulte con el administrador");
        e.setMensajeErrorBd(ex.getMessage());
        throw e;
    }
    
    /**
     * Método que inserta un usuario en la tabla USUARIO de la base de datos Linners a través de una petición al servidor.
     * @param usuario Pide por parámetro un objeto de tipo usuario con los datos del nuevo usuario.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarUsuario(Usuario usuario) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_USUARIO);
        p.setEntidad(usuario);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }   
    
    /**
     * Método que inserta una empresa en la tabla EMPRESA de la base de datos Linners a través de una petición al servidor.
     * @param empresa Pide por parámetro un objeto de tipo empresa con los datos de la nueva empresa.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarEmpresa(Empresa empresa) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_EMPRESA);
        p.setEntidad(empresa);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que inserta una parada en la tabla PARADA de la base de datos Linners a través de una petición al servidor.
     * @param parada Pide por parámetro un objeto de tipo parada con los datos de la nueva parada.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarParada(Parada parada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_PARADA);
        p.setEntidad(parada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }

    /**
     * Método que inserta una parada favorita en la tabla PARADA_FAVORITA de la base de datos Linners a través de una petición al servidor.
     * @param paradaFavorita Pide por parámetro un objeto de tipo parada favorita con los datos de la nueva parada favorita.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarParadaFavorita(ParadaFavorita paradaFavorita) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_PARADAFAVORITA);
        p.setEntidad(paradaFavorita);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que inserta una linea en la tabla LINEA de la base de datos Linners a través de una petición al servidor.
     * @param linea Pide por parámetro un objeto de tipo linea con los datos de la nueva linea.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarLinea(Linea linea) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_LINEA);
        p.setEntidad(linea);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que inserta una linea parada en la tabla LINEA_PARADA de la base de datos Linners a través de una petición al servidor.
     * @param lineaParada Pide por parámetro un objeto de tipo linea parada con los datos de la nueva linea parada.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarLineaParada(LineaParada lineaParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_LINEAPARADA);
        p.setEntidad(lineaParada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que inserta una terminal en la tabla TERMINAL de la base de datos Linners a través de una petición al servidor.
     * @param terminal Pide por parámetro un objeto de tipo terminal con los datos de la nueva terminal.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int insertarTerminal(Terminal terminal) throws ExcepcionLineas 
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.INSERTAR_TERMINAL);
        p.setEntidad(terminal);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica un usuario de la tabla USUARIO de la base de datos Linners a través de una petición al servidor.
     * @param idUsuario Pide por parámetro el ID del usuario a modificar.
     * @param usuario Pide por parámetro un objeto de tipo usuario con los datos del nuevo usuario.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarUsuario(Integer idUsuario, Usuario usuario) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_USUARIO);
        p.setIdEntidad(idUsuario);
        p.setEntidad(usuario);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica una empresa de la tabla EMPRESA de la base de datos Linners a través de una petición al servidor.
     * @param idEmpresa Pide por parámetro el ID de la empresa a modificar.
     * @param empresa Pide por parámetro un objeto de tipo empresa con los datos de la nueva empresa.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarEmpresa(Integer idEmpresa, Empresa empresa) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_EMPRESA);
        p.setIdEntidad(idEmpresa);
        p.setEntidad(empresa);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica una parada de la tabla PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idParada Pide por parámetro el ID de la parada a modificar.
     * @param parada Pide por parámetro un objeto de tipo parada con los datos de la nueva parada.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarParada(Integer idParada, Parada parada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_PARADA);
        p.setIdEntidad(idParada);
        p.setEntidad(parada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }

    /**
     * Método que modifica una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners a través de una petición al servidor.
     * @param idPFavorita Pide por parámetro el ID de la parada favorita a modificar.
     * @param parada Pide por parámetro un objeto de tipo parada con los datos de la nueva parada.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarParadaFavorita(Integer idPFavorita, Parada parada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_PARADAFAVORITA);
        p.setIdEntidad(idPFavorita);
        p.setEntidad(parada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica una linea de la tabla LINEA de la base de datos Linners a través de una petición al servidor.
     * @param idLinea Pide por parámetro el ID de la linea a modificar.
     * @param linea Pide por parámetro un objeto de tipo linea con los datos de la nueva linea.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarLinea(Integer idLinea, Linea linea) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_LINEA);
        p.setIdEntidad(idLinea);
        p.setEntidad(linea);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica una linea parada de la tabla LINEA_PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idLineaParada Pide por parámetro el ID de la linea parada a modificar.
     * @param lineaParada Pide por parámetro un objeto de tipo linea parada con los datos de la nueva linea parada.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarLineaParada(Integer idLineaParada, LineaParada lineaParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_LINEAPARADA);
        p.setIdEntidad(idLineaParada);
        p.setEntidad(lineaParada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que modifica una terminal de la tabla TERMINAL de la base de datos Linners a través de una petición al servidor.
     * @param idTerminal Pide por parámetro el ID de la terminal a modificar.
     * @param empresa Pide por parámetro un objeto de tipo empresa con los datos de la nueva empresa.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int modificarTerminal(Integer idTerminal, Empresa empresa) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ACTUALIZAR_TERMINAL);
        p.setIdEntidad(idTerminal);
        p.setEntidad(empresa);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina un usuario de la tabla USUARIO de la base de datos Linners a través de una petición al servidor.
     * @param idUsuario Pide por parámetro el ID del usuario a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarUsuario(Integer idUsuario) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_USUARIO);
        p.setIdEntidad(idUsuario);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una empresa de la tabla EMPRESA de la base de datos Linners a través de una petición al servidor.
     * @param idEmpresa Pide por parámetro el ID de la empresa a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarEmpresa(Integer idEmpresa) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_EMPRESA);
        p.setIdEntidad(idEmpresa);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una parada de la tabla PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idParada Pide por parámetro el ID de la parada a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarParada(Integer idParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_PARADA);
        p.setIdEntidad(idParada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners a través de una petición al servidor.
     * @param idPFavorita Pide por parámetro el ID de la parada favorita a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarParadaFavorita(Integer idPFavorita) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_PARADAFAVORITA);
        p.setIdEntidad(idPFavorita);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una linea de la tabla LINEA de la base de datos Linners a través de una petición al servidor.
     * @param idLinea Pide por parámetro el ID de la linea a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarLinea(Integer idLinea) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_LINEA);
        p.setIdEntidad(idLinea);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una linea parada de la tabla LINEA_PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idLineaParada Pide por parámetro el ID de la linea parada a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarLineaParada(Integer idLineaParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_LINEAPARADA);
        p.setIdEntidad(idLineaParada);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que elimina una terminal de la tabla TERMINAL de la base de datos Linners a través de una petición al servidor.
     * @param idTerminal Pide por parámetro el ID de la terminal a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public int eliminarTerminal(Integer idTerminal) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.ELIMINAR_TERMINAL);
        p.setIdEntidad(idTerminal);
        Respuesta r = null;
        int cantidad = 0;
        
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getCantidad() != null)
                cantidad = r.getCantidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return cantidad;
    }
    
    /**
     * Método que lee un usuario de la tabla USUARIO de la base de datos Linners a través de una petición al servidor.
     * @param idUsuario Pide por parámetro el ID del usuario a leer.
     * @return Devuelve el toString() del objeto usuario.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public Usuario leerUsuario(Integer idUsuario) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_USUARIO);
        p.setIdEntidad(idUsuario);
        Respuesta r = null;
        Usuario usuario = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                usuario = (Usuario) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return usuario;
    }
    
    /**
     * Método que lee una empresa de la tabla EMPRESA de la base de datos Linners a través de una petición al servidor.
     * @param idEmpresa Pide por parámetro el ID de la empresa a leer.
     * @return Devuelve el toString() del objeto empresa.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public Empresa leerEmpresa(Integer idEmpresa) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_EMPRESA);
        p.setIdEntidad(idEmpresa);
        Respuesta r = null;
        Empresa empresa = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                empresa = (Empresa) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return empresa;
    }
    
    /**
     * Método que lee una parada de la tabla PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idParada Pide por parámetro el ID de la parada a leer.
     * @return Devuelve el toString() del objeto parada.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public Parada leerParada(Integer idParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_PARADA);
        p.setIdEntidad(idParada);
        Respuesta r = null;
        Parada parada = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                parada = (Parada) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return parada;
    }
    
    /**
     * Método que lee una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners a través de una petición al servidor.
     * @param idPFavorita Pide por parámetro el ID de la parada favorita a leer.
     * @return Devuelve el toString() del objeto parada favorita.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ParadaFavorita leerParadaFavorita(Integer idPFavorita) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_PARADAFAVORITA);
        p.setIdEntidad(idPFavorita);
        Respuesta r = null;
        ParadaFavorita paradaFavorita = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                paradaFavorita = (ParadaFavorita) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return paradaFavorita;
    }
    
    /**
     * Método que lee una linea de la tabla LINEA de la base de datos Linners a través de una petición al servidor.
     * @param idLinea Pide por parámetro el ID de la linea a leer.
     * @return Devuelve el toString() del objeto linea.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public Linea leerLinea(Integer idLinea) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_LINEA);
        p.setIdEntidad(idLinea);
        Respuesta r = null;
        Linea linea = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                linea = (Linea) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return linea;
    }
    
    /**
     * Método que lee una linea parada de la tabla LINEA_PARADA de la base de datos Linners a través de una petición al servidor.
     * @param idLineaParada Pide por parámetro el ID de la linea parada a leer.
     * @return Devuelve el toString() del objeto linea parada.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public LineaParada leerLineaParada(Integer idLineaParada) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_LINEAPARADA);
        p.setIdEntidad(idLineaParada);
        Respuesta r = null;
        LineaParada lineaParada = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                lineaParada = (LineaParada) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return lineaParada;
    }
    
    /**
     * Método que lee una terminal de la tabla TERMINAL de la base de datos Linners a través de una petición al servidor.
     * @param idTerminal Pide por parámetro el ID de la terminal a leer.
     * @return Devuelve el toString() del objeto terminal.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public Terminal leerTerminal(Integer idTerminal) throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_TERMINAL);
        p.setIdEntidad(idTerminal);
        Respuesta r = null;
        Terminal terminal = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                terminal = (Terminal) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE();
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return terminal;
    }
    
    /**
     * Método que lee todos los usuario de la tabla USUARIO de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto usuario.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<Usuario> leerUsuarios() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_USUARIOS);
        Respuesta r = null;
        ArrayList<Usuario> listaUsuarios = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaUsuarios = (ArrayList<Usuario>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaUsuarios;
    }
    
    /**
     * Método que lee todas las empresas de la tabla EMPRESA de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto empresa.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<Empresa> leerEmpresas() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_EMPRESAS);
        Respuesta r = null;
        ArrayList<Empresa> listaEmpresas = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaEmpresas = (ArrayList<Empresa>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaEmpresas;
    }
    
    /**
     * Método que lee todas las paradas de la tabla PARADA de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto parada.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<Parada> leerParadas() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_PARADAS);
        Respuesta r = null;
        ArrayList<Parada> listaParadas = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaParadas = (ArrayList<Parada>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaParadas;
    }

    /**
     * Método que lee todas las paradas favoritas de la tabla PARADA_FAVORITA de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto paradaFavorita.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<ParadaFavorita> leerParadasFavoritas() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_PARADASFAVORITAS);
        Respuesta r = null;
        ArrayList<ParadaFavorita> listaParadasFavoritas = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaParadasFavoritas = (ArrayList<ParadaFavorita>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaParadasFavoritas;  
    }
    
    /**
     * Método que lee todas las lineas de la tabla LINEAS de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto linea.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<Linea> leerLineas() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_LINEAS);
        Respuesta r = null;
        ArrayList<Linea> listaLineas = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaLineas = (ArrayList<Linea>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaLineas;  
    }
    
    /**
     * Método que lee todas las lineas paradas de la tabla LINEAS_PARADAS de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto lineaParada.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<LineaParada> leerLineasParadas() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_LINEASPARADAS);
        Respuesta r = null;
        ArrayList<LineaParada> listaLineasParadas = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaLineasParadas = (ArrayList<LineaParada>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaLineasParadas;  
    }
    
    /**
     * Método que lee todas las terminales de la tabla LINEAS_PARADAS de la base de datos Linners a través de una petición al servidor.
     * @return Devuelve un listado de toString() del objeto terminal.
     * @throws ExcepcionLineas Cuando se produce algún tipo de error se arroja a la clase ExcepcionLineas y este te devuelve el mensaje de error.
     */
    public ArrayList<Terminal> leerTerminales() throws ExcepcionLineas
    {
        Peticion p = new Peticion();
        p.setIdOperacion(Operaciones.LEER_TERMINALES);
        Respuesta r = null;
        ArrayList<Terminal> listaTerminales = null;
        
        try 
        {    
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(p);
            
            ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
            r = (Respuesta) ois.readObject();

            ois.close();
            oos.close();
            
            socketCliente.close();
            
            if(r.getEntidad() != null)
                listaTerminales = (ArrayList<Terminal>) r.getEntidad();
            else if(r.getE() != null)
                throw r.getE(); 
        } 
        catch (IOException ex) 
        {
            manejadorIOException(ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
                    
        return listaTerminales; 
    } 
}
