/**
 *
 * @author Hancel
 */
package cadlineas;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import pojoslineas.ParadaFavorita;
import pojoslineas.*;

/**
 * Esta clase es la clase que implementa el Componente de Acceso a Datos a la base de datos Linners.
 * @author Hancel Fernando Abrines Vasallo
 * @version 1.0
 * @since 1.0
 */
public class CadLineas {
    
    private Connection conexion;
    private String ipBD;

    /**
     * Constructor de la clase CadLineas que carga el jdbc y los parámetros para la conexión con la base de datos.
     * @param pIpBD Parámetro que pide las opciones para establecer la conexión la base de datos de Oracle
     * @throws ExcepcionLineas La excepción se produce cuando hay un problema en la carga del jdbc
     */
    public CadLineas(String pIpBD) throws ExcepcionLineas 
    {
        this.ipBD = pIpBD;
        
        try 
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } 
        catch (ClassNotFoundException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setMensajeErrorBd(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        } 
    }
    
    /**
     * Método que establece la conexión con la base de datos Linners.
     * @throws ExcepcionLineas La excepción se produce cuando no es posible establecer la conexión con la base de datos Linners
     */
    private void conectar() throws ExcepcionLineas
    {
        try 
        {
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@" + ipBD + ":1521:xe", "LINEAS", "kk");
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    }
    
    
    /**
     * Método que permite insertar un usuario en la tabla USUARIO de la base de datos Linners.
     * @param usuario Objeto usuario que guarda los datos sobre el usuario a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarUsuario(Usuario usuario) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String llamada = "call INSERTAR_USUARIO(?,?,?,?,?,?,?,?,?,?)";
        
        try 
        {
            CallableStatement sentenciaLlamable = conexion.prepareCall(llamada);
            
            sentenciaLlamable.setString(1, usuario.getDni());
            sentenciaLlamable.setString(2, usuario.getNombre());
            sentenciaLlamable.setString(3, usuario.getApellido1());
            sentenciaLlamable.setString(4, usuario.getApellido2());
            sentenciaLlamable.setString(5, usuario.getDireccion());
            sentenciaLlamable.setString(6, usuario.getLocalidad());
            sentenciaLlamable.setString(7, usuario.getProvincia());
            sentenciaLlamable.setString(8, usuario.getCorreoElectronico());
            sentenciaLlamable.setString(9, usuario.getContrasena());
            sentenciaLlamable.setString(10, usuario.getTelefono());
            
            sentenciaLlamable.executeUpdate();
            
            sentenciaLlamable.close();
            conexion.close();
            
            registrosAfectados = 1;
        }
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(llamada);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe un usuario con ese campo.");
                    break;
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El DNI tiene que tener 8 letras y 1 número,\nEl Correo electrónico tiene que tener la siguiente estructurá nombre@dominio.es,\nEl Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 6 o 7.");
                    break;
                case 12899:
                    // En en el caso de que el numero de telefono tenga mas caracteres del indicado
                    e.setMensajeUsuario("El Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 6 o 7.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una empresa en la tabla EMPRESA de la base de datos Linners
     * @param empresa Objeto empresa que guarda los datos sobre la empresa a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarEmpresa(Empresa empresa) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String llamada = "call INSERTAR_EMPRESA(?,?,?,?,?)";
        
        try 
        {
            CallableStatement sentenciaLlamable = conexion.prepareCall(llamada);
            
            sentenciaLlamable.setString(1, empresa.getNif());
            sentenciaLlamable.setString(2, empresa.getNombre());
            sentenciaLlamable.setString(3, empresa.getDireccion());
            sentenciaLlamable.setString(4, empresa.getCorreoElectronico());
            sentenciaLlamable.setString(5, empresa.getTelefonoFijo());
            
            sentenciaLlamable.executeUpdate();
            
            sentenciaLlamable.close();
            conexion.close();
            
            registrosAfectados = 1;
        }
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(llamada);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe una empresa con ese campo.");
                    break;
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El NIF tiene que tener 1 letras y 8 número,\nEl Correo electrónico tiene que tener la siguiente estructurá nombre@dominio.es,\nEl Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 9.");
                    break;
                case 12899:
                    // En en el caso de que el numero de telefono tenga mas caracteres del indicado
                    e.setMensajeUsuario("El Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 9.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una parada en la tabla PARADA de la base de datos Linners.
     * @param parada Objeto parada que guarda los datos sobre la parada a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarParada(Parada parada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String llamada = "call INSERTAR_PARADA(?,?,?,?,?,?)";
        
        try 
        {
            CallableStatement sentenciaLlamable = conexion.prepareCall(llamada);
            
            sentenciaLlamable.setString(1, parada.getPDireccion());
            sentenciaLlamable.setString(2, parada.getPLocalidad());
            sentenciaLlamable.setString(3, parada.getPProvincia());
            sentenciaLlamable.setString(4, parada.getLatitud());
            sentenciaLlamable.setString(5, parada.getLongitud());
            sentenciaLlamable.setString(6, parada.getCp());
            
            sentenciaLlamable.executeUpdate();
            
            sentenciaLlamable.close();
            conexion.close();
            
            registrosAfectados = 1;
        }
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(llamada);
            switch(ex.getErrorCode()) 
            {
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El código postal solo puede tener un máximo de 5 dígitos.");
                    break;
                case 12899:
                    // En en el caso de que el codigo portal tenga mas caracteres del indicado
                    e.setMensajeUsuario("El código postal solo puede tener un máximo de 5 dígitos.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una parada favorita en la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param paradaFavorita Objeto paradaFavorita que guarda los datos sobre la parada favorita a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarParadaFavorita(ParadaFavorita paradaFavorita) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "insert into PARADA_FAVORITA (ID_PFAVORITA, ID_USUARIO, ID_PARADA) "
                + "values (SECUENCIA_PARADA_FAVORITA_ID.nextval, ?, ?)";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, paradaFavorita.getUsuario().getIdUsuario(), Types.INTEGER);
            sentenciaPreparada.setObject(2, paradaFavorita.getParada().getIdParada(), Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una linea en la tabla LINEA de la base de datos Linners tabla que tiene una relación directa
     * con la tabla EMPRESA.
     * @param linea Objeto linea que guarda los datos sobre la linea a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarLinea(Linea linea) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "insert into LINEA (ID_LINEA, ID_EMPRESA, PRECIO) values (SECUENCIA_LINEA_ID.nextval, ?, ?)";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, linea.getEmpresa().getIdEmpresa(), Types.INTEGER);
            sentenciaPreparada.setObject(2, linea.getPrecio(), Types.DOUBLE);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una linea parada en la tabla LINEA_PARADA de la base de datos Linners tabla que tiene una relación
     * directa con las tablas PARADA y LINEA.
     * @param lineaParada Objeto lineaParada que guarda los datos sobre la linea parada a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarLineaParada(LineaParada lineaParada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "insert into LINEA_PARADA (ID_PARADA, ORDEN_PARADA, ID_LINEA) values (?, SECUENCIA_LINEA_PARADA_ID.nextval, ?)";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, lineaParada.getParada().getIdParada(), Types.INTEGER);
            sentenciaPreparada.setObject(2, lineaParada.getLinea().getIdLinea(), Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite insertar una terminal en la tabla TERMINAL de la base de datos Linners tabla que tiene una relación
     * directa con las tablas PARADA y EMPRESA.
     * @param terminal Objeto terminal que guarda los datos sobre la linea parada a insertar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int insertarTerminal(Terminal terminal) throws ExcepcionLineas 
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "insert into TERMINAL (ID_TERMINAL, ID_EMPRESA) values (?, ?)";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, terminal.getParada().getIdParada(), Types.INTEGER);
            sentenciaPreparada.setObject(2, terminal.getEmpresa().getIdEmpresa(), Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe una terminal con ese campo.");
                    break;
                case 1400:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;        
    }
    
    /**
     * Método que permite modificar un usuario en la tabla USUARIO de la base de datos Linners.
     * @param idUsuario Parámetro que pide el ID del usuario a modificar.
     * @param usuario Objeto que guarda los nuevos datos de usuario a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarUsuario(Integer idUsuario, Usuario usuario) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "update USUARIO set DNI=?, NOMBRE=?, APELLIDO1=?, APELLIDO2=?, DIRECCION=?, LOCALIDAD=?, PROVINCIA=?, CORREO_ELECTRONICO=?, CONTRASENA=?, TELEFONO=? where ID_USUARIO=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setString(1, usuario.getDni());
            sentenciaPreparada.setString(2, usuario.getNombre());
            sentenciaPreparada.setString(3, usuario.getApellido1());
            sentenciaPreparada.setString(4, usuario.getApellido2());
            sentenciaPreparada.setString(5, usuario.getDireccion());
            sentenciaPreparada.setString(6, usuario.getLocalidad());
            sentenciaPreparada.setString(7, usuario.getProvincia());
            sentenciaPreparada.setString(8, usuario.getCorreoElectronico());
            sentenciaPreparada.setString(9, usuario.getContrasena());
            sentenciaPreparada.setString(10, usuario.getTelefono());
            
            sentenciaPreparada.setObject(11, idUsuario, Types.INTEGER);
                        
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe un usuario con ese campo.");
                    break;
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El DNI tiene que tener 8 letras y 1 número,\nEl Correo electrónico tiene que tener la siguiente estructurá nombre@dominio.es,\nEl Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 6 o 7.");
                    break;
                case 12899:
                    // En en el caso de que el numero de telefono tenga mas caracteres del indicado
                    e.setMensajeUsuario("El Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 6 o 7.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una empresa en la tabla EMPRESA de la base de datos Linners.
     * @param idEmpresa Parámetro que pide el ID de la emprea a modificar.
     * @param empresa Objeto que guarda los nuevos datos de la empresa a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas La excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarEmpresa(Integer idEmpresa, Empresa empresa) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "update EMPRESA set NIF=?, NOMBRE=?, DIRECCION=?, CORREO_ELECTRONICO=?, TELEFONO_FIJO=? where ID_EMPRESA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setString(1, empresa.getNif());
            sentenciaPreparada.setString(2, empresa.getNombre());
            sentenciaPreparada.setString(3, empresa.getDireccion());
            sentenciaPreparada.setString(4, empresa.getCorreoElectronico());
            sentenciaPreparada.setString(5, empresa.getTelefonoFijo());
            
            sentenciaPreparada.setObject(6, idEmpresa, Types.INTEGER);
                        
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe una empresa con ese campo.");
                    break;
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El NIF tiene que tener 1 letras y 8 número,\nEl Correo electrónico tiene que tener la siguiente estructurá nombre@dominio.es,\nEl Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 9.");
                    break;
                case 12899:
                    // En en el caso de que el numero de telefono tenga mas caracteres del indicado
                    e.setMensajeUsuario("El Teléfono tiene que tener 9 dígitos y el primero tiene que empezar por 9.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una Parada en la tabla PARADA de la base de datos Linners.
     * @param idParada Parámetro que pide el ID de la empresa a modificar.
     * @param parada Objeto que guarda los nuevos datos de la parada a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarParada(Integer idParada, Parada parada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "update PARADA set DIRECCION=?, LOCALIDAD=?, PROVINCIA=?, LATITUD=?, LONGITUD=?, CP=? where ID_PARADA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setString(1, parada.getPDireccion());
            sentenciaPreparada.setString(2, parada.getPLocalidad());
            sentenciaPreparada.setString(3, parada.getPProvincia());
            sentenciaPreparada.setString(4, parada.getLatitud());
            sentenciaPreparada.setString(5, parada.getLongitud());
            sentenciaPreparada.setString(6, parada.getCp());
            
            sentenciaPreparada.setObject(7, idParada, Types.INTEGER);
                        
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 2290:
                    // En caso inclumpla una CC
                    e.setMensajeUsuario("El código postal solo puede tener un máximo de 5 dígitos.");
                    break;
                case 12899:
                    // En en el caso de que el codigo portal tenga mas caracteres del indicado
                    e.setMensajeUsuario("El código postal solo puede tener un máximo de 5 dígitos.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una parada favorita en la tabla PARADA_FAVORITA de la base de datos Linners tabla que tiene
     * una relación directa con la tabla USUARIO.
     * @param idPFavorita Parámetro que pide el ID de la parada favorita a modificar.
     * @param parada Objeto que guarda los nuevos datos de usuario a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones espeficicadas en el SQLException.
     */
    public int modificarParadaFavorita(Integer idPFavorita, Parada parada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "UPDATE PARADA_FAVORITA SET ID_PARADA=? WHERE ID_PFAVORITA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, parada.getIdParada(), Types.INTEGER);
            
            sentenciaPreparada.setObject(2, idPFavorita, Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una linea en la tabla LINEA de la base de datos Linners tabla que tiene una relación directa
     * con la tabla EMPRESA.
     * @param idLinea Parámetro que pide el ID de la linea a modificar.
     * @param linea Objeto que guarda los nuevos datos de la linea a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarLinea(Integer idLinea, Linea linea) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "UPDATE LINEA SET ID_EMPRESA=?, PRECIO=? WHERE ID_LINEA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, linea.getEmpresa().getIdEmpresa(), Types.INTEGER);
            sentenciaPreparada.setObject(2, linea.getPrecio(), Types.DOUBLE);
            
            sentenciaPreparada.setObject(3, idLinea, Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una linea parada en la tabla LINEA_PARADA de la base de datos Linners tabla que tiene una relación
     * directa con las tablas PARADA y LINEA.
     * @param idLineaParada Parámetro que pide el ID de la linea a modificar.
     * @param lineaParada Objeto que guarda los nuevos datos de linea parada a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarLineaParada(Integer idLineaParada, LineaParada lineaParada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "UPDATE LINEA_PARADA SET ID_PARADA=?, ID_LINEA=? WHERE ORDEN_PARADA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, lineaParada.getParada().getIdParada(), Types.INTEGER);
            sentenciaPreparada.setObject(2, lineaParada.getLinea().getIdLinea(), Types.INTEGER);
            
            sentenciaPreparada.setObject(3, idLineaParada, Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite modificar una terminal en la tabla TERMINAL de la base de datos Linners tabla que tiene una relación
     * directa con las tablas PARADA y EMPRESA.
     * @param idTerminal Parámetro que pide el ID de la terminal a modificar.
     * @param terminal Objeto que guarda los nuevos datos de la terminal a modificar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int modificarTerminal(Integer idTerminal, Terminal terminal) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "UPDATE TERMINAL SET ID_TERMINAL=?, ID_EMPRESA=? WHERE ID_TERMINAL=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, terminal.getParada().getIdParada(), Types.INTEGER);
            sentenciaPreparada.setObject(2, terminal.getEmpresa().getIdEmpresa(), Types.INTEGER);
            
            sentenciaPreparada.setObject(3, idTerminal, Types.INTEGER);
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 1:
                    // En caso de que uno de los campos se repita
                    e.setMensajeUsuario("El campo indicado no se puede repetir. Ya existe una terminal con ese campo.");
                    break;
                case 1407:
                    // En caso de que el valor introducido sea nulo le indica que el campo debe ser obligatorio
                    e.setMensajeUsuario("El campo en blanco es obligatorio.");
                    break;
                case 1403:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                case 2291:
                    // En el caso de el registro seleccionado no exista
                    e.setMensajeUsuario("El registro no existe.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar un usuario de la base de datos Linners.
     * @param idUsuario Parámetro que pide el ID del usuario a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarUsuario(Integer idUsuario) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM USUARIO where ID_USUARIO=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idUsuario, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 2292:
                    // En el caso de que se intente eliminar un registro del cual dependen otras tablas
                    e.setMensajeUsuario("No se puede eliminar este usuario, existen paradas favoritas que dependen de él");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una empresa de la base de datos Linners.
     * @param idEmpresa Parámetro que pide el ID de la empresa a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarEmpresa(Integer idEmpresa) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM EMPRESA where ID_EMPRESA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idEmpresa, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 2292:
                    // En el caso de que se intente eliminar un registro del cual dependen otras tablas
                    e.setMensajeUsuario("No se puede eliminar esta empresa, existen otros registros que dependen de esta.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una parada de la base de datos Linners.
     * @param idParada Parámetro que pide el ID de la parada a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarParada(Integer idParada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM PARADA where ID_PARADA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idParada, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                case 2292:
                    // En el caso de que se intente eliminar un registro del cual dependen otras tablas
                    e.setMensajeUsuario("No se puede eliminar esta parada, existen otros registros que dependen de esta.");
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una parada favorita de la base de datos Linners.
     * @param idPFavorita Parámetro que pide el ID de la parada favorita a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarParadaFavorita(Integer idPFavorita) throws ExcepcionLineas
    {        
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM PARADA_FAVORITA where ID_PFAVORITA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idPFavorita, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una linea de la base de datos Linners.
     * @param idLinea Parámetro que pide el ID de la linea a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarLinea(Integer idLinea) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM LINEA where ID_LINEA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idLinea, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una linea parada de la base de datos Linners.
     * @param idLineaParada Parámetro que pide el ID de la linea parada a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarLineaParada(Integer idLineaParada) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM LINEA_PARADA where ORDEN_PARADA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idLineaParada, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite eliminar una terminal de la base de datos Linners.
     * @param idTerminal Parámetro que pide el ID de la terminal a eliminar.
     * @return Devuelve 1 o más si hay registros afectados y 0 si no los hay.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public int eliminarTerminal(Integer idTerminal) throws ExcepcionLineas
    {
        conectar();
        int registrosAfectados = 0;
        String dml = "delete FROM TERMINAL where ID_TERMINAL=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setObject(1, idTerminal, Types.INTEGER);

            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dml);
            switch(ex.getErrorCode()) 
            {
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }
    
    /**
     * Método que permite leer un usuario de la tabla USUARIO de la base de datos Linners.
     * @param idUsuario Parámetro que pide el ID del usuario a leer.
     * @return Devuelve el toString() del objeto usuario.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public Usuario leerUsuario(Integer idUsuario) throws ExcepcionLineas
    {
        conectar();
        Usuario leerUsuario = new Usuario();
        String dql = "select * from USUARIO where ID_USUARIO=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idUsuario, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                leerUsuario.setIdUsuario(((BigDecimal)resultado.getObject("ID_USUARIO")).intValue());
                leerUsuario.setDni(resultado.getString("DNI"));
                leerUsuario.setNombre(resultado.getString("NOMBRE"));
                leerUsuario.setApellido1(resultado.getString("APELLIDO1"));
                leerUsuario.setApellido2(resultado.getString("APELLIDO2"));
                leerUsuario.setDireccion(resultado.getString("DIRECCION"));
                leerUsuario.setLocalidad(resultado.getString("LOCALIDAD"));
                leerUsuario.setProvincia(resultado.getString("PROVINCIA"));
                leerUsuario.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                leerUsuario.setContrasena(resultado.getString("CONTRASENA"));
                leerUsuario.setTelefono(resultado.getString("TELEFONO"));
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        }
        return leerUsuario;
    }
    
    /**
     * Método que permite leer una empresa de la tabla EMPRESA de la base de datos Linners.
     * @param idEmpresa Parámetro que pide el ID de la empresa a leer.
     * @return Devuelve el toString() del objeto empresa.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public Empresa leerEmpresa(Integer idEmpresa) throws ExcepcionLineas
    {
        conectar();
        Empresa leerEmpresa = new Empresa();
        String dql = "select * from EMPRESA where ID_EMPRESA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idEmpresa, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                leerEmpresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                leerEmpresa.setNif(resultado.getString("NIF"));
                leerEmpresa.setNombre(resultado.getString("NOMBRE"));
                leerEmpresa.setDireccion(resultado.getString("DIRECCION"));
                leerEmpresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                leerEmpresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        }
        return leerEmpresa;
    }
    
    /**
     * Método que permite leer una parada de la tabla PARADA de la base de datos Linners.
     * @param idParada Parámetro que pide el ID de al parada a leer.
     * @return Devuelve el toString() del objeto parada.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public Parada leerParada(Integer idParada) throws ExcepcionLineas
    {
        conectar();
        Parada leerParada = new Parada();
        String dql = "select * from PARADA where ID_PARADA=?";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idParada, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                leerParada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                leerParada.setPDireccion(resultado.getString("PDIRECCION"));
                leerParada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                leerParada.setPProvincia(resultado.getString("PPROVINCIA"));
                leerParada.setLatitud(resultado.getString("LATITUD"));
                leerParada.setLongitud(resultado.getString("LONGITUD"));
                leerParada.setCp(resultado.getString("CP"));
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador.");
            throw e;
        }
        return leerParada;
    }
    
    /**
     * Método que permite leer una parada favorita de la tabla PARADA_FAVORITA de la base de datos Linners.
     * @param idPFavorita Parámetro que pide el ID de la parada favorita a leer.
     * @return Devuelve el toString() del objeto paradaFavorita.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ParadaFavorita leerParadaFavorita(Integer idPFavorita) throws ExcepcionLineas
    {
        conectar();
        ParadaFavorita leerParadaFavorita = new ParadaFavorita();
        Usuario usuario = new Usuario();
        Parada parada = new Parada();
        String dql = "select * from PARADA_FAVORITA pf, USUARIO u, PARADA pa "
                + "WHERE pf.ID_USUARIO = u.ID_USUARIO and pf.ID_PARADA = pa.ID_PARADA "
                + "and pf.ID_PFAVORITA=?";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idPFavorita, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                leerParadaFavorita.setIdPFavorita(((BigDecimal)resultado.getObject("ID_PFAVORITA")).intValue());

                usuario.setIdUsuario(((BigDecimal)resultado.getObject("ID_USUARIO")).intValue());
                usuario.setDni(resultado.getString("DNI"));
                usuario.setNombre(resultado.getString("NOMBRE"));
                usuario.setApellido1(resultado.getString("APELLIDO1"));
                usuario.setApellido2(resultado.getString("APELLIDO2"));
                usuario.setDireccion(resultado.getString("DIRECCION"));
                usuario.setLocalidad(resultado.getString("LOCALIDAD"));
                usuario.setProvincia(resultado.getString("PROVINCIA"));
                usuario.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                usuario.setContrasena(resultado.getString("CONTRASENA"));
                usuario.setTelefono(resultado.getString("TELEFONO"));
                leerParadaFavorita.setUsuario(usuario);
                
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                leerParadaFavorita.setParada(parada);    
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return leerParadaFavorita; 
    }
    
    /**
     * Método que permite leer una linea de la tabla LINEA de la base de datos Linners.
     * @param idLinea Parámetro que pide el ID de la linea a leer.
     * @return Devuelve el toString() del objeto linea.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public Linea leerLinea(Integer idLinea) throws ExcepcionLineas
    {
        conectar();
        Linea leerLinea = new Linea();
        Empresa empresa = new Empresa();
        String dql = "select * from LINEA ln, EMPRESA em WHERE ln.ID_EMPRESA = em.ID_EMPRESA and ln.ID_LINEA=?";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idLinea, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                leerLinea.setIdLinea(((BigDecimal)resultado.getObject("ID_LINEA")).intValue());

                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                leerLinea.setEmpresa(empresa);
                
                leerLinea.setPrecio(((BigDecimal)resultado.getObject("PRECIO")).doubleValue());
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return leerLinea; 
    }
    
    /**
     * Método que permite leer una linea parada de la tabla LINEA_PARADA de la base de datos Linners.
     * @param idLineaParada Parámetro que pide el ID de la linea parada a leer.
     * @return Devuelve el toString() del objeto lineaParada.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public LineaParada leerLineaParada(Integer idLineaParada) throws ExcepcionLineas
    {
        conectar();
        LineaParada leerLineaParada = new LineaParada();
        Linea linea = new Linea();
        Parada parada = new Parada();
        Empresa empresa = new Empresa();
        String dql = "select * from LINEA_PARADA lp, LINEA li, EMPRESA em, PARADA pa "
                + "WHERE lp.ID_LINEA = li.ID_LINEA and li.ID_EMPRESA = em.ID_EMPRESA and lp.ID_PARADA = pa.ID_PARADA and lp.ORDEN_PARADA=?";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idLineaParada, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                linea.setIdLinea(((BigDecimal)resultado.getObject("ID_LINEA")).intValue());
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                linea.setEmpresa(empresa);
                linea.setPrecio(((BigDecimal)resultado.getObject("PRECIO")).doubleValue());
                leerLineaParada.setLinea(linea);
                
                leerLineaParada.setOrdenParada(((BigDecimal)resultado.getObject("ORDEN_PARADA")).intValue());
                
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                leerLineaParada.setParada(parada);    
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return leerLineaParada; 
    }
    
    /**
     * Método que permite leer una Terminal de la tabla TERMINAL de la base de datos Linners.
     * @param idTerminal Parámetro que pide el ID de la terminal a leer.
     * @return Devuelve el toString() del objeto terminal.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public Terminal leerTerminal(Integer idTerminal) throws ExcepcionLineas
    {
        conectar();
        Terminal leerTerminal = new Terminal();
        Parada parada = new Parada();
        Empresa empresa = new Empresa();
        String dql = "select * from TERMINAL tm, PARADA pa, EMPRESA em WHERE tm.ID_TERMINAL = pa.ID_PARADA and tm.ID_EMPRESA = em.ID_EMPRESA and tm.ID_TERMINAL=?";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            sentenciaPreparada.setObject(1, idTerminal, Types.INTEGER);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                leerTerminal.setParada(parada);  
                
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                leerTerminal.setEmpresa(empresa);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return leerTerminal; 
    }
    
    /**
     * Método que lee todos los usuarios registrados en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto usuario.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<Usuario> leerUsuarios() throws ExcepcionLineas
    {
        conectar();
        ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
        String dql = "select * from USUARIO";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            
            while (resultado.next()) 
            {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(((BigDecimal)resultado.getObject("ID_USUARIO")).intValue());
                usuario.setDni(resultado.getString("DNI"));
                usuario.setNombre(resultado.getString("NOMBRE"));
                usuario.setApellido1(resultado.getString("APELLIDO1"));
                usuario.setApellido2(resultado.getString("APELLIDO2"));
                usuario.setDireccion(resultado.getString("DIRECCION"));
                usuario.setLocalidad(resultado.getString("LOCALIDAD"));
                usuario.setProvincia(resultado.getString("PROVINCIA"));
                usuario.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                usuario.setContrasena(resultado.getString("CONTRASENA"));
                usuario.setTelefono(resultado.getString("TELEFONO"));
                
                listaUsuarios.add(usuario);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    
        return listaUsuarios;   
    }
    
    /**
     * Método que lee todas las empresas registradas en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto empresa.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<Empresa> leerEmpresas() throws ExcepcionLineas
    {
        conectar();
        ArrayList<Empresa> listaEmpresas = new ArrayList<Empresa>();
        String dql = "select * from EMPRESA";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            
            while (resultado.next()) 
            {
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                
                listaEmpresas.add(empresa);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    
        return listaEmpresas;  
    }
    
    /**
     * Método que lee todas las paradas registradas en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto parada.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<Parada> leerParadas() throws ExcepcionLineas
    {
        conectar();
        ArrayList<Parada> listaParadas = new ArrayList<Parada>();
        String dql = "select * from PARADA";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            
            while (resultado.next()) 
            {
                Parada parada = new Parada();
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                
                listaParadas.add(parada);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    
        return listaParadas; 
    }
    
    /**
     * Método que lee todas las paradas favoritas registradas en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto paradaFavorita.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<ParadaFavorita> leerParadasFavoritas() throws ExcepcionLineas
    {
        conectar();
        ArrayList<ParadaFavorita> listaPFavorita = new ArrayList<ParadaFavorita>();
        String dql = "select * from USUARIO u, PARADA_FAVORITA pf, PARADA pa "
                + "WHERE u.ID_USUARIO = pf.ID_USUARIO and pa.ID_PARADA = pf.ID_PARADA";
        
        try 
        {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            
            while (resultado.next()) 
            {
                ParadaFavorita paradaFavorita = new ParadaFavorita();
                paradaFavorita.setIdPFavorita(((BigDecimal)resultado.getObject("ID_PFAVORITA")).intValue());
                
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(((BigDecimal)resultado.getObject("ID_USUARIO")).intValue());
                usuario.setDni(resultado.getString("DNI"));
                usuario.setNombre(resultado.getString("NOMBRE"));
                usuario.setApellido1(resultado.getString("APELLIDO1"));
                usuario.setApellido2(resultado.getString("APELLIDO2"));
                usuario.setDireccion(resultado.getString("DIRECCION"));
                usuario.setLocalidad(resultado.getString("LOCALIDAD"));
                usuario.setProvincia(resultado.getString("PROVINCIA"));
                usuario.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                usuario.setContrasena(resultado.getString("CONTRASENA"));
                usuario.setTelefono(resultado.getString("TELEFONO"));
                paradaFavorita.setUsuario(usuario);
                
                Parada parada = new Parada();
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                paradaFavorita.setParada(parada);
                
                listaPFavorita.add(paradaFavorita);
            }
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaPFavorita;  
    }
    
    /**
     * Método que lee todas las lineas registradas en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto linea.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<Linea> leerLineas() throws ExcepcionLineas
    {
        conectar();
        ArrayList<Linea> listaLineas = new ArrayList<Linea>();
        String dql = "select * from LINEA ln, EMPRESA em WHERE ln.ID_EMPRESA = em.ID_EMPRESA";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                Linea linea = new Linea();
                linea.setIdLinea(((BigDecimal)resultado.getObject("ID_LINEA")).intValue());

                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                linea.setEmpresa(empresa);
                
                linea.setPrecio(((BigDecimal)resultado.getObject("PRECIO")).doubleValue());
                
                listaLineas.add(linea);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaLineas;
    }
    
    /**
     * Método que lee todas las linea paradas en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto lineaParada.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<LineaParada> leerLineasParadas() throws ExcepcionLineas
    {
        conectar();
        ArrayList<LineaParada> listaLineasParadas = new ArrayList<LineaParada>();
        String dql = "select * from LINEA_PARADA lp, LINEA li, EMPRESA em, PARADA pa "
                + "WHERE lp.ID_LINEA = li.ID_LINEA and li.ID_EMPRESA = em.ID_EMPRESA and lp.ID_PARADA = pa.ID_PARADA";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                LineaParada lineaParada = new LineaParada();
                
                Linea linea = new Linea();
                linea.setIdLinea(((BigDecimal)resultado.getObject("ID_LINEA")).intValue());
                
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                linea.setEmpresa(empresa);
                linea.setPrecio(((BigDecimal)resultado.getObject("PRECIO")).doubleValue());
                lineaParada.setLinea(linea);
                
                lineaParada.setOrdenParada(((BigDecimal)resultado.getObject("ORDEN_PARADA")).intValue());
                
                Parada parada = new Parada();
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                lineaParada.setParada(parada);    
                
                listaLineasParadas.add(lineaParada);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaLineasParadas; 
    }
    
    /**
     * Método que lee todas las terminales en la base de datos Linners.
     * @return Devuelve un listado de toString() del objeto terminal.
     * @throws ExcepcionLineas ExcepcionLineas la excepción se produce cuando se viola alguna de las restricciones especificadas en el SQLException.
     */
    public ArrayList<Terminal> leerTerminales() throws ExcepcionLineas
    {
        conectar();
        ArrayList<Terminal> listaTerminales = new ArrayList<Terminal>();
        String dql = "select * from TERMINAL tm, PARADA pa, EMPRESA em WHERE tm.ID_TERMINAL = pa.ID_PARADA and tm.ID_EMPRESA = em.ID_EMPRESA";
        
        try 
        {   
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dql);
            ResultSet resultado = sentenciaPreparada.executeQuery();
                            
            while(resultado.next())
            {
                Terminal terminal = new Terminal();
                Parada parada = new Parada();
                parada.setIdParada(((BigDecimal)resultado.getObject("ID_PARADA")).intValue());
                parada.setPDireccion(resultado.getString("PDIRECCION"));
                parada.setPLocalidad(resultado.getString("PLOCALIDAD"));
                parada.setPProvincia(resultado.getString("PPROVINCIA"));
                parada.setLatitud(resultado.getString("LATITUD"));
                parada.setLongitud(resultado.getString("LONGITUD"));
                parada.setCp(resultado.getString("CP"));
                terminal.setParada(parada);  
                
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(((BigDecimal)resultado.getObject("ID_EMPRESA")).intValue());
                empresa.setNif(resultado.getString("NIF"));
                empresa.setNombre(resultado.getString("NOMBRE"));
                empresa.setDireccion(resultado.getString("DIRECCION"));
                empresa.setCorreoElectronico(resultado.getString("CORREO_ELECTRONICO"));
                empresa.setTelefonoFijo(resultado.getString("TELEFONO_FIJO"));
                terminal.setEmpresa(empresa);
                
                listaTerminales.add(terminal);
            }
            
            resultado.close();
            sentenciaPreparada.close();
            conexion.close();
        } 
        catch (SQLException ex) 
        {
            ExcepcionLineas e = new ExcepcionLineas();
            e.setCodigoErrorBd(ex.getErrorCode());
            e.setMensajeErrorBd(ex.getMessage());
            e.setSetenciaSql(dql);
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaTerminales; 
    }
}
