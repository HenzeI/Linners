/**
 *
 * @author Hancel
 */
package lineassc;

import cadlineas.CadLineas;
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
 * Clase que representa el manejador de peticiones realizadas por el cliente de comunicaciones.
 * @author Hancel Fernando Abrines Vasallo
 * @version 1.0
 * @since 1.0
 */
public class ManejadorPeticion extends Thread{
    
    private Socket clienteConectado;
    private String ipCad;
    
    /**
     * Constructor de la clase que establece una conexión con el cliente para recibir las peticiones de este.
     * @param clienteConectado Pide por parámetro el Socket del cliente.
     */
    public ManejadorPeticion(Socket clienteConectado, String ipCad) 
    {
        this.clienteConectado = clienteConectado;
        this.ipCad = ipCad;
    }
    
    @Override
    /**
     * Método run que capta las petición del cliente.
     */
    public void run()
    {
        ObjectInputStream ois = null;
        
        try 
        {          
            ois = new ObjectInputStream(clienteConectado.getInputStream());
            Peticion p = (Peticion) ois.readObject();
            System.out.println(p);
            
            switch(p.getIdOperacion())
            {
                // Insertar
                case Operaciones.INSERTAR_USUARIO:
                    insertarUsuario(p);
                    break;
                    
                case Operaciones.INSERTAR_EMPRESA:
                    insertarEmpresa(p);
                    break;
                    
                case Operaciones.INSERTAR_PARADA:
                    insertarParada(p);
                    break;
                    
                 case Operaciones.INSERTAR_PARADAFAVORITA:
                    insertarParadaFavorita(p);
                    break;
                    
                case Operaciones.INSERTAR_LINEA:
                    insertarLinea(p);
                    break;
                    
                case Operaciones.INSERTAR_LINEAPARADA:
                    insertarLineaParada(p);
                    break;
                    
                case Operaciones.INSERTAR_TERMINAL:
                    insertarTerminal(p);
                    break;
                    
                // Modificar
                case Operaciones.ACTUALIZAR_USUARIO:
                    modificarUsuario(p);
                    break;
                    
                case Operaciones.ACTUALIZAR_EMPRESA:
                    modificarEmpresa(p);
                    break;
                    
                case Operaciones.ACTUALIZAR_PARADA:
                    modificarParada(p);
                    break;
                    
                 case Operaciones.ACTUALIZAR_PARADAFAVORITA:
                     modificarParadaFavorita(p);
                    break;
                    
                case Operaciones.ACTUALIZAR_LINEA:
                    modificarLinea(p);
                    break;
                    
                case Operaciones.ACTUALIZAR_LINEAPARADA:
                    modificarLineaParada(p);
                    break;
                    
                case Operaciones.ACTUALIZAR_TERMINAL:
                    modificarTerminal(p);
                    break;
                    
                    
                // Eliminar
                case Operaciones.ELIMINAR_USUARIO:
                    eliminarUsuario(p);
                    break;
                    
                case Operaciones.ELIMINAR_EMPRESA:
                    eliminarEmpresa(p);
                    break;
                    
                case Operaciones.ELIMINAR_PARADA:
                    eliminarParada(p);
                    break;
                    
                 case Operaciones.ELIMINAR_PARADAFAVORITA:
                     eliminarParadaFavorita(p);
                    break;
                    
                case Operaciones.ELIMINAR_LINEA:
                    eliminarLinea(p);
                    break;
                    
                case Operaciones.ELIMINAR_LINEAPARADA:
                    eliminarLineaParada(p);
                    break;
                    
                case Operaciones.ELIMINAR_TERMINAL:
                    eliminarTerminal(p);
                    break;
                    
                // Leer uno
                case Operaciones.LEER_USUARIO:
                    leerUsuario(p);
                    break;
                    
                case Operaciones.LEER_EMPRESA:
                    leerEmpresa(p);
                    break;
                    
                case Operaciones.LEER_PARADA:
                    leerParada(p);
                    break;
                    
                 case Operaciones.LEER_PARADAFAVORITA:
                     leerParadaFavorita(p);
                    break;
                    
                case Operaciones.LEER_LINEA:
                    leerLinea(p);
                    break;
                    
                case Operaciones.LEER_LINEAPARADA:
                    leerLineaParada(p);
                    break;
                    
                case Operaciones.LEER_TERMINAL:
                    leerTerminal(p);
                    break;
                    
                    
                // Leer todos
                case Operaciones.LEER_USUARIOS:
                    leerUsuarios(p);
                    break;
                    
                case Operaciones.LEER_EMPRESAS:
                    leerEmpresas(p);
                    break;
                    
                case Operaciones.LEER_PARADAS:
                    leerParadas(p);
                    break;
                    
                case Operaciones.LEER_PARADASFAVORITAS:
                    leerParadasFavoritas(p);
                    break;
                    
                case Operaciones.LEER_LINEAS:
                    leerLineas(p);
                    break;
                    
                case Operaciones.LEER_LINEASPARADAS:
                    leerLineasParadas(p);
                    break;
                    
                case Operaciones.LEER_TERMINALES:
                    leerTerminales(p);
                    break;
            }
            
            clienteConectado.close();    
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionsOIS(ex, ois);
        } 
        catch (ClassNotFoundException ex) 
        {
            manejadorClassNotFoundException(ex);
        }
    }
    
    /**
     * Método que maneja la petición de inserción de un usuario en la tabla USUARIO de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarUsuario(Peticion peticion)
    {        
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Usuario usuario = (Usuario) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarUsuario(usuario);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una empresa en la tabla EMPRESA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarEmpresa(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Empresa empresa = (Empresa) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarEmpresa(empresa);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una parada en la tabla PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Parada parada = (Parada) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarParada(parada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarParadaFavorita(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            ParadaFavorita paradaFavorita = (ParadaFavorita) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarParadaFavorita(paradaFavorita);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una linea de la tabla LINEA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarLinea(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Linea linea = (Linea) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarLinea(linea);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una linea parada de la tabla LINEA_PARADA de la base de datos Linnes
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarLineaParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            LineaParada lineaParada = (LineaParada) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarLineaParada(lineaParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de inserción de una terminal de la tabla TERMINAL de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int insertarTerminal(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Terminal terminal = (Terminal) peticion.getEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.insertarTerminal(terminal);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de un usuario de la tabla USUARIO de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarUsuario(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Usuario usuario = (Usuario) peticion.getEntidad();
            int idUsuario = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarUsuario(idUsuario, usuario);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una empresa de la tabla EMPRESA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarEmpresa(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Empresa empresa = (Empresa) peticion.getEntidad();
            int idEmpresa = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarEmpresa(idEmpresa, empresa);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una parada de la tabla PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Parada parada = (Parada) peticion.getEntidad();
            int idParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarParada(idParada, parada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarParadaFavorita(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Parada parada = (Parada) peticion.getEntidad();
            int idParadaFavorita = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarParadaFavorita(idParadaFavorita, parada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una linea de la tabla LINEA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarLinea(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Linea linea = (Linea) peticion.getEntidad();
            int idLinea = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarLinea(idLinea, linea);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una linea parada de la tabla LINEA_PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarLineaParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            LineaParada lineaParada = (LineaParada) peticion.getEntidad();
            int idLineaParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarLineaParada(idLineaParada, lineaParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de modificación de una terminal de la tabla TERMINAL de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int modificarTerminal(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            Terminal terminal = (Terminal) peticion.getEntidad();
            int idTerminal = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.modificarTerminal(idTerminal, terminal);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de un usuario de la tabla USUARIO de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarUsuario(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idUsuario = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarUsuario(idUsuario);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una empresa de la tabla EMPRESA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarEmpresa(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idEmpresa = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarEmpresa(idEmpresa);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una parada de la tabla PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarParada(idParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarParadaFavorita(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idParadaFavorita = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarParadaFavorita(idParadaFavorita);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una linea de la tabla LINEA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarLinea(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idLinea = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarLinea(idLinea);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una linea parada de la tabla LINEA_PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarLineaParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idLineaParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarLineaParada(idLineaParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de eliminación de una terminal de la tabla TERMINAL de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada. 
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     */
    private int eliminarTerminal(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        int registrosAfectados = 0;
        
        try 
        {
            int idTerminal = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            registrosAfectados = cad.eliminarTerminal(idTerminal);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setCantidad(registrosAfectados);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
        return registrosAfectados;
    }
    
    /**
     * Método que maneja la petición de lectura de un usuario de la tabla USUARIO de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerUsuario(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idUsuario = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            Usuario usuario = cad.leerUsuario(idUsuario);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(usuario);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una empresa de la tabla EMPRESA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerEmpresa(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idEmpresa = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            Empresa empresa = cad.leerEmpresa(idEmpresa);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(empresa);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una parada de la tabla PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            Parada parada = cad.leerParada(idParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(parada);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerParadaFavorita(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idParadaFavorita = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            ParadaFavorita paradaFavorita = cad.leerParadaFavorita(idParadaFavorita);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(paradaFavorita);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una linea de la tabla LINEA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerLinea(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idLinea = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            Linea linea = cad.leerLinea(idLinea);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(linea);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una linea parada de la tabla LINEA_PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerLineaParada(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idLineaParada = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            LineaParada lineaParada = cad.leerLineaParada(idLineaParada);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(lineaParada);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de una terminal de la tabla TERMINAL de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerTerminal(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            int idTerminal = peticion.getIdEntidad();
            
            CadLineas cad = new CadLineas(ipCad);
            Terminal terminal = cad.leerTerminal(idTerminal);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(terminal);
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        } 
    }
    
    /**
     * Método que maneja la petición de lectura de todos los usuarios de la tabla USUARIO de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerUsuarios(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerUsuarios());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las empresas de la tabla EMPRESA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerEmpresas(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerEmpresas());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las paradas de la tabla PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerParadas(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerParadas());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las paradas favoritas de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerParadasFavoritas(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerParadasFavoritas());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las lineas de la tabla LINEA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerLineas(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerLineas());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las lineas paradas de la tabla LINEA_PARADA de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerLineasParadas(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerLineasParadas());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que maneja la petición de lectura de todas las terminales de la tabla TERMINAL de la base de datos Linners.
     * @param peticion Pide por parámetro un objeto de tipo Peticion con la petición almacenada.
     */
    private void leerTerminales(Peticion peticion)
    {
        ObjectOutputStream oos = null;
        
        try 
        {
            CadLineas cad = new CadLineas(ipCad);
            
            Respuesta r = new Respuesta();
            r.setIdOperacion(peticion.getIdOperacion());
            r.setEntidad(cad.leerTerminales());
            
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            
            oos.writeObject(r);
            oos.close();
        } 
        catch (IOException ex) 
        {
            manejadorIOExceptionOOS(ex, oos);
        } 
        catch (ExcepcionLineas ex) 
        {
            manejadorExcepcionLineas(ex);            
        }
    }
    
    /**
     * Método que gestiona la IOExceptions de entrada.
     * @param ex Pide un objeto de tipo IOExceptions.
     * @param ois Pide un objeto de tipo ObjectInputStream.
     */
    private void manejadorIOExceptionsOIS(IOException ex, ObjectInputStream ois)
    {
        System.out.println(ex);
//        if(ois != null) 
//        {
//            ExcepcionLineas e = new ExcepcionLineas();
//            e.setMensajeUsuario("Error en la comunicación. Consulte con el administrador");
//            e.setMensajeErrorBd(ex.getMessage());
//            Respuesta r = new Respuesta();
//            r.setE(e);
//            try 
//            {
//                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
//                oos.writeObject(r);
//                oos.close();
//            } 
//            catch(IOException ex1) 
//            {
//                System.out.println(ex1);
//            }
//        }
    }
    
    /**
     * Método que gestiona IOExceptions de salida.
     * @param ex Pide un objeto de tipo IOExceptions.
     * @param oos Pide un objeto de tipo ObjectOutputStream.
     */
    private void manejadorIOExceptionOOS(IOException ex, ObjectOutputStream oos)
    {
        System.out.println(ex);
//        if(oos != null) 
//        {
//            ExcepcionLineas e = new ExcepcionLineas();
//            e.setMensajeUsuario("Error en la comunicación. Consulte con el administrador");
//            e.setMensajeErrorBd(ex.getMessage());
//            Respuesta r = new Respuesta();
//            r.setE(e);
//            try 
//            {
//                ObjectOutputStream os = new ObjectOutputStream(clienteConectado.getOutputStream());
//                os.writeObject(r);
//                os.close();
//            } 
//            catch(IOException ex1) 
//            {
//                System.out.println(ex1);
//            }
//        }
    }
    
    /**
     * Método que gestiona las excepciones de tipo ClassNotFoundException.
     * @param ex Pide por parámetro un objeto de tipo ClassNotFoundException.
     */
    private void manejadorClassNotFoundException(ClassNotFoundException ex)
    {
        System.out.println(ex);
        
//        ExcepcionLineas e = new ExcepcionLineas();
//        e.setMensajeUsuario("Error en la comunicacion. Consulte con el administrador");
//        e.setMensajeErrorBd(ex.getMessage());
//        Respuesta r = new Respuesta();
//        r.setE(e);
//        ObjectOutputStream oos = null;
//        try{
//            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
//            oos.writeObject(r);
//            oos.close();
//        } catch (IOException ex2) {
//            manejadorIOExceptionOOS(ex2, oos);
//        }
        
    }
    
    /**
     * Método que gestiona la excepciones producidades por el cad.
     * @param e Pide por parámetro un objeto de tipo ExcepcionLineas.
     */
    private void manejadorExcepcionLineas(ExcepcionLineas e)
    {
        System.out.println(e);
        Respuesta r = new Respuesta();
        r.setE(e);
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(clienteConectado.getOutputStream());
            oos.writeObject(r);
            oos.close();
            clienteConectado.close();
        } 
        catch (IOException ex2) 
        {
            manejadorIOExceptionOOS(ex2, oos);
        }
    }
}
