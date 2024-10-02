package android;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lineascc.LineasCC;
import pojoslineas.ExcepcionLineas;
import pojoslineas.LineaParada;
import pojoslineas.Parada;
import pojoslineas.ParadaFavorita;
import pojoslineas.Usuario;

/**
 * Clase para que actua como el simulador de android
 * @author Hancel
 */
public class SimuladorAndroid {

    public static void main(String[] args) 
    {    
        
        int resultado = 0;
        Usuario usuario = new Usuario();
        Parada parada = new Parada();
        ParadaFavorita pf = new ParadaFavorita();
        LineasCC c = new LineasCC("192.168.1.14");
        
//        usuario.setDni("24540700Z");
//        usuario.setNombre("Fancel");
//        usuario.setApellido1("Lbrines");
//        usuario.setApellido2("asallo");
//        usuario.setDireccion("a via 11");
//        usuario.setLocalidad("stro");
//        usuario.setProvincia("nta");
//        usuario.setCorreoElectronico("a@a.com");
//        usuario.setContrasena("contrasena");
//        usuario.setTelefono("653681831");
////        


//        usuario.setDni("72847290A");
//        usuario.setNombre("BUenas");
//        usuario.setApellido1("Tardes");
//        usuario.setApellido2("Vasallo");
//        usuario.setDireccion("Abrines");
//        usuario.setLocalidad("Urdiales");
//        usuario.setProvincia("Peru");
//        usuario.setCorreoElectronico("hfedo@gma.com");
//        usuario.setContrasena("ebf143495bd92ed3c61c52176e4530bd");
//        usuario.setTelefono("643681830");
        
        try 
        {
//            resultado = c.modificarUsuario(2, usuario);
//            resultado = c.insertarUsuario(usuario);
//            resultado = c.eliminarUsuario(30);
            
//            String direcion = "Juzgados";
//            
//            ArrayList<Parada> lc = c.leerParadas();
//            for(Parada registros : lc)
//            {
//                if(registros.getPDireccion() == direcion){
//                    System.out.println(registros.getIdParada());
//                    break;
//                }
//            }
            
            String direcion = "Juzgados";
            Integer idParada = 0;
//            Integer idParada = 0;
            
//            ArrayList<Parada> lc = c.leerParadas();
//            for(Parada registros : lc)
//            {
//                if(registros.getPDireccion().equals(direcion)){
//                    System.out.println(registros.getIdParada());
//                    idParada = registros.getIdParada();
//                    break;
//                }
//            }
            
//            ArrayList<LineaParada> lc2 = c.leerLineasParadas();
//            
//            for(LineaParada registros : lc2)
//            {                
//                if(registros.getParada().getPDireccion().equals(direcion)){
//                    System.out.println(registros.getParada().getIdParada());
//                    idParada = registros.getParada().getIdParada();
//                    break;
//                }
//            }
//            
//            for(LineaParada registros : lc2)
//            {                
//                if(registros.getParada().getIdParada() == idParada){
//                    System.out.println(registros);
//                }
//            }
            
//            ArrayList<ParadaFavorita> lc = c.leerParadasFavoritas();
//            int contador = 0;
//            
//            for(ParadaFavorita registro : lc){
//                if(registro.getUsuario().getIdUsuario() == 1){
//                    System.out.println(registro);
////                    if(registro.getParada().getIdParada() == 2){
////                        System.out.println("el usuario ya tiene eesa parada");
////                        break;
////                    } else {
////                        System.out.println("el usuario no tiene esa parada");
////                        break;
////                    }
//                    contador++;
//                }
//            }
//            
//            if(contador > 4){
//                System.out.println("no puedes añadir mas");
//            }
            
//            usuario = c.leerUsuario(1);
//            
//            usuario.setNombre("Fernando");

                ArrayList<Usuario> usuarios = c.leerUsuarios();
                
                for(Usuario exDni : usuarios){
                    if (exDni.getDni().equals("24540600S")){
                        System.out.println("ya hay unn usuario con ese dni");
                    }
                }
            
//            System.out.println(c.leerUsuario(1));


//            usuario.setIdUsuario(1);
//            parada.setIdParada(23);
//
//            pf.setUsuario(usuario);
//            pf.setParada(parada);
//            
//            c.insertarParadaFavorita(pf);


//            for (int i = 0; i < lc.size(); i++){
//                    System.out.println("" + lc.get(i).getLatitud() + "" + lc.get(i).getLongitud());
//            }
            
//            System.out.println(c.leerUsuario(1));
//            System.out.println(c.leerParadaFavorita(1));
            
//            int cantidad = c.insertarUsuario(u);
//            System.out.println(cantidad);
        } 
        catch (ExcepcionLineas ex) 
        {
            System.out.println(ex);
        }
        catch (NullPointerException e){
            System.out.println("no es posible conectarse con el servidor");
        }
        System.out.println(resultado);
        
//        try {
//            LineasCC a = new LineasCC("192.168.1.14");
//            
//            a.modificarUsuario(1, usuario);
//            
//            System.out.println("cambiado");
//        } catch (ExcepcionLineas ex) {
//            System.out.println(ex);
//        }
        
    }
}
