/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba;

import cadlineas.CadLineas;
import java.util.ArrayList;
import pojoslineas.Empresa;

import pojoslineas.ExcepcionLineas;
import pojoslineas.Linea;
import pojoslineas.LineaParada;
import pojoslineas.Parada;
import pojoslineas.ParadaFavorita;
import pojoslineas.Terminal;
import pojoslineas.Usuario;

/**
 *
 * @author DAM201
 */
public class prueba {
 
    public static void main(String[] args) {
        
        int ra = 0; // Registros afectados

        // Creacion
        CadLineas cad = null;
        
        Usuario usuario = new Usuario();
        Empresa empresa = new Empresa();
        Parada parada = new Parada();

        ParadaFavorita pf = new ParadaFavorita();
        Linea linea = new Linea();
        LineaParada lp = new LineaParada();
        Terminal terminal = new Terminal();
        
        try 
        {
            //cad = new CadLineas("172.16.201.69:1521"); // Clase
            //cad = new CadLineas("192.168.1.40"); // Casa
            cad = new CadLineas("86.50.95.240"); // Casa
        } 
        catch (ExcepcionLineas ex) 
        {
            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
        }    
            
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    


        // Insertar Usuario
//        usuario.setDni("24840145Z");
//        usuario.setNombre("Hugo");
//        usuario.setApellido1("Velez");
//        usuario.setApellido2("Velez");
//        usuario.setDireccion("Paco");
//        usuario.setLocalidad("Castro");
//        usuario.setProvincia("Cantabria");
//        usuario.setCorreoElectronico("tutuyoloo@gmail.com");
//        usuario.setContrasena("cf03844fb2072c8beb5e90dc8450365c");
//        usuario.setTelefono("796969696");
//
//        try 
//        {
//           ra = cad.insertarUsuario(usuario);
//        } catch (ExcepcionLineas ex) {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);
            
        
        // Insertar Empresa
//        empresa.setNif("Z24540600");
//        empresa.setNombre("SLHancel");
//        empresa.setDireccion("Calle la via");
//        empresa.setCorreoElectronico("hancel@gmail.com");
//        empresa.setTelefonoFijo("900000001");
//
//        try 
//        {
//s            ra = cad.insertarEmpresa(empresa);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);
            
            
        // Insertar Parada
//        parada.setDireccion("Valdemoros");
//        parada.setLocalidad("Gijón");
//        parada.setProvincia("Asturias");
//        parada.setLatitud("Asturias");
//        parada.setLongitud("Asturias");
//        parada.setCp("33208");
//
//        try 
//        {
//            ra = cad.insertarParada(parada);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Insertar Parada Favorita
//        usuario.setIdUsuario(2);
//        parada.setIdParada(2);
//
//        pf.setUsuario(usuario);
//        pf.setParada(parada);
//
//        try 
//        {
//            ra = cad.insertarParadaFavorita(pf);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);

        
        // Insertar Linea
//        empresa.setIdEmpresa(1);
//        
//        linea.setEmpresa(empresa);
//        linea.setPrecio(3.466);
//
//        try 
//        {
//            ra = cad.insertarLinea(linea);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);        
        
        
        // Insertar Linea Parada
//        parada.setIdParada(1);
//        linea.setIdLinea(1);
//        
//        lp.setParada(parada);
//        lp.setLinea(linea);
//
//        try 
//        {
//            ra = cad.insertarLineaParada(lp);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);            
        
        
        // Insertar Terminal
//        parada.setIdParada(2);
//        empresa.setIdEmpresa(3);
//        
//        terminal.setParada(parada);
//        terminal.setEmpresa(empresa);
//
//        try 
//        {
//            ra = cad.insertarTerminal(terminal);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);        
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            

        // Modificar usuario
//        usuario.setDni("72847290S");
//        usuario.setNombre("A");
//        usuario.setApellido1("Abrines");
//        usuario.setApellido2("Vasallo");
//        usuario.setDireccion("A SAber");
//        usuario.setLocalidad("Urdiales");
//        usuario.setProvincia("Peru");
//        usuario.setCorreoElectronico("hfernando@gmail.com");
//        usuario.setContrasena("ebf143495bd92ed3c61c52176e4530bd");
//        usuario.setTelefono("643681839");
//
//        try 
//        {
//            ra = cad.modificarUsuario(1, usuario);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Empresa
//        empresa.setNif("B82059478");
//        empresa.setNombre("AA");
//        empresa.setDireccion("Abrines");
//        empresa.setCorreoElectronico("asesoria@alsa.es");
//        empresa.setTelefonoFijo("902422242");
//
//        try 
//        {
//            ra = cad.modificarEmpresa(1, empresa);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Parada
//        parada.setDireccion("B82059478");
//        parada.setLocalidad("Hancel");
//        parada.setProvincia("a");
//        parada.setCp("3700");
//
//        try 
//        {
//            ra = cad.modificarParada(2, parada);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Parada Favorita
//        parada.setIdParada(2);
//
//        try 
//        {
//            ra = cad.modificarParadaFavorita(2, parada);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Linea
//        empresa.setIdEmpresa(2);
//        
//        linea.setEmpresa(empresa);
//        linea.setPrecio(2.00);
//        
//        try 
//        {
//            ra = cad.modificarLinea(2, linea);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Linea Parada
//        parada.setIdParada(2);
//        linea.setIdLinea(2);
//        
//        lp.setParada(parada);
//        lp.setLinea(linea);
//        
//        try 
//        {
//            ra = cad.modificarLineaParada(2,lp);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Modificar Terminal
//        parada.setIdParada(2);
//        empresa.setIdEmpresa(2);
//        
//        terminal.setParada(parada);
//        terminal.setEmpresa(empresa);
//        
//        try 
//        {
//            ra = cad.modificarTerminal(2, terminal);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);
           

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Eliminar Usuario, Empresa y Parada
//        try 
//        {
//            ra += cad.eliminarUsuario(23);
//            ra += cad.eliminarEmpresa(15);
//            ra += cad.eliminarParada(14);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);


        // Eliminar Parada favorita, linea, linea parada y terminal
//        try 
//        {
//            ra += cad.eliminarParadaFavorita(34);
//            ra += cad.eliminarLinea(10);
//            ra += cad.eliminarLineaParada(7);
//            ra += cad.eliminarTerminal(2);
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println(ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
//        System.out.println("Registros afectados " + ra);
            
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Leer Usuario, Empresa y Parada
//        try 
//        {
//            System.out.println(cad.leerUsuario(1));
//            System.out.println(cad.leerEmpresa(1));
//            System.out.println(cad.leerParada(1));
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


        // Leer Parada favorita, linea, linea parada y terminal
//        try 
//        {
//            System.out.println(cad.leerLineaParada(1));
//            System.out.println(cad.leerParadaFavorita(1));
//            System.out.println(cad.leerLinea(1));
//            System.out.println(cad.leerTerminal(1));
//        } 
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
            

            
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Leer Usuarios
//        try 
//        {
//            ArrayList lc = cad.leerUsuarios();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }
        

        // Leer Empresas
//        try 
//        {
//            ArrayList lc = cad.leerEmpresas();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


        // Leer Paradas
//        try 
//        {
//            ArrayList lc = cad.leerParadas();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


        // Leer Lineas
//        try 
//        {
//            ArrayList lc = cad.leerLineas();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


        // Leer Lineas paradas
//        try 
//        {
//            ArrayList lc = cad.leerLineasParadas();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


        // Leer Terminales
//        try 
//        {
//            ArrayList lc = cad.leerTerminales();
//            for(Object registros : lc)
//            {
//                System.out.println(registros);
//            }
//        }  
//        catch (ExcepcionLineas ex) 
//        {
//            System.out.println("Mensaje usuario: " + ex.getMensajeUsuario());
//            System.out.println("Log: "+ ex.getCodigoErrorBd() + " - " + ex.getMensajeErrorBd() + " - " + ex.getSetenciaSql());
//        }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     
    }
    
}
