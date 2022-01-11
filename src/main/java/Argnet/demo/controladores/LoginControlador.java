package Argnet.demo.controladores;
import Argnet.demo.entidades.User;
import Argnet.demo.entidades.conexionLog;
import Argnet.demo.errores.ErrorServicio;
import Argnet.demo.repositorios.LogRepositorio;
import Argnet.demo.repositorios.UsuarioRepositorio;
import Argnet.demo.servicios.conexionServicio;
import Argnet.demo.servicios.userServicio;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;

@RequestMapping
@Controller
public class LoginControlador { 
    @Autowired
    private userServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio repo;
    @Autowired
    private LogRepositorio log;
    @Autowired
    private conexionServicio conexion;
    
    @GetMapping("/login")
    public String login(HttpServletRequest request,@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        List intentos=new ArrayList(); 
        //cargamos a la lista la cantidad de intentos
        intentos=conexion.verificarIntentos(request.getRemoteAddr());
         // en caso de superar los 5 intentos en menos de  10 minutos
         // cambio el login de la pagina    
        if(intentos.size()<5){
                
                //mostramos el numero de intentos fallidos
                if (error != null) {                                     
                    intentos=conexion.verificarIntentos(request.getRemoteAddr());
                    int intentosFallidos=5-intentos.size();
                    model.put("error", "Usuario o clave incorrectos." + "le quedan "+ String.valueOf(intentosFallidos)+ " intentos, luego se bloqueara el sistema");
                }
                //mostramos cuando cerramos correctamente la sesion
                if (logout != null) {
                    model.put("logout", "Ha salido correctamente.");
                     conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/logOut");
                }
                //guadamos el ingreso del controlador
                conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/login");
                return "login.html";
        // en caso de que exceda la cantidad de conexiones bloquemos el acceso por qo minutos
        }else{
              conexionLog tiempo=new conexionLog();
              tiempo=(conexionLog) intentos.get(1);
              TimeZone timeZone = TimeZone.getTimeZone("America/Buenos_Aires");; 
              Calendar fecha = Calendar.getInstance(); 
              fecha.setTimeZone(timeZone);
              int tiempoDesbloqueo=(int) (10-(((fecha.getTimeInMillis() - tiempo.getFecha().getTimeInMillis())/1000)/60));
              model.put("bloqueo", "Debe esperar " + tiempoDesbloqueo  + " minutos");
         return "bloqueado.html";
        }        
    }
    
    @GetMapping("/loginFail")
    public String loginFail(HttpServletRequest request, ModelMap model) {     
        //configure en el apartado security la ruta por si el login falla
        //guardo el fallo con el controlador "/authError"
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/authError");
        List intentos=new ArrayList(); 
       //redirecciono a login con el parametro get error
        return "redirect:login?error"
                + ""
                + "";
    }
    
    @GetMapping("/loginSpeed")
    public String loginSpeed(HttpServletRequest request,@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {     
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/loginSpeed");
        return "loginSpeed.html";
    }
    
    //REGISTRO
    @PostMapping("/cargarRegistro") 
    public String cargarRegistro(HttpServletRequest request,@RequestParam String name,@RequestParam String lastname,@RequestParam String email,@RequestParam String password, @RequestParam String nacimiento, @RequestParam String rol, RedirectAttributes redirectAttrs) throws Exception{        
        try {
             
        //guardamos y obtenemos el resultado
        String resultado = usuarioServicio.registrar(name, lastname, password, email ,nacimiento);
        RedirectAttributes addFlashAttribute = redirectAttrs
        .addFlashAttribute("error", resultado); 
        
        RedirectAttributes addFlashAttribute2 = redirectAttrs
        .addFlashAttribute("mensaje", resultado);
                   
        } catch (ErrorServicio ex) {
            Logger.getLogger(AdminlControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        //guardamos el ingreso al cotrolador
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/cargarRegistro");
        //aviso        
        System.out.println(usuarioServicio.usuarioName());       
        //verificamos roles, y si es administrador lo argamos a la variable retorno
        // luego evaluamos la variable retorno
       String retorno="";     
       if (usuarioServicio.usuarioRol()!=null){
           for (GrantedAuthority variable : usuarioServicio.usuarioRol()) 
            { 
                System.out.println(variable.getAuthority());
                if ("ROLE_ADMIN".equals(variable.getAuthority())){
                    retorno="redirect:/admin";
                }
            }
           if("".equals(retorno)){
              retorno="redirect:/login"; 
            }         
           
       }else{
            retorno="redirect:/login";
       }      
        return retorno;
    }

    @GetMapping("/")
    public String indexUsuario(HttpServletRequest request, @RequestParam(required = false) String dato, @RequestParam(required = false) String orden, ModelMap modelo) throws Exception {		                                   
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/");      
        return "redirect:/loginSpeed";
    }
    
    @PostMapping("/mail")
    public String mail(HttpServletRequest request,@RequestParam String email, RedirectAttributes redirectAttrs ) throws Exception {		                             
        //controladore de tiempo para enviar correos cada dos minutos por ip 
        int controlador =0;
        //verifico si la ip ha realizado una peticion de envio
        // en caso de no existir registro de esa IP, colocamos el controlador en 121 segundos
        // para que envia realice el envio del correo
        if (conexion.ultimaConexionIpControlador(request.getRemoteAddr(), "/mail")==null){
            controlador=121;
        }else{
        // en caso de no existir el registro obtenemos la fecha del sistema
        //que deberia cambiarla por una funcion mas a adelante
            TimeZone timeZone = TimeZone.getTimeZone("America/Buenos_Aires");; 
            Calendar fecha = Calendar.getInstance(); 
            fecha.setTimeZone(timeZone);
        // creamos el objeto conexon y le asignamos los valores del ultimo registro
        // de la ip del visitante
            conexionLog logConexion = new conexionLog();
            logConexion=(conexionLog) conexion.ultimaConexionIpControlador(request.getRemoteAddr(), "/mail");
        // cargamos la variable controlador con la difrencia en segundos    
            int minutos =(int) (fecha.getTimeInMillis()-logConexion.getFecha().getTimeInMillis());
            controlador=minutos/1000;
        }
    //--------------------------------//       
        if (("".equals(email))){
            RedirectAttributes addFlashAttribute = redirectAttrs
            .addFlashAttribute("correoEnviado", "Debe ingresar un correo");                     
        }else{
            //utilizamos el valor del controlador para ejecutar los envios           
             //Creamos el objeto                  
             User aux = new User();                                                        
             // en esta instancia se coloco el correo en el campo name, y en email el usuario codificado
             //todo esto es que decidi utilizar el correo como campo de calidacio             
             if (repo.buscarPorname2(email)==null){   
            //si la busqueda de correo no tiene resultado  
            //generamos cargamos el usuario y generamos el link
                
                if (controlador <120){
                     RedirectAttributes addFlashAttribute = redirectAttrs
                    .addFlashAttribute("correoEnviado", "Debe esperar dos minutos para reenviar el correo nuevamente");               
                }else{
                    String link= usuarioServicio.userToken(email, usuarioServicio.generadorTokenU(email), usuarioServicio.generadorTokenP(email));
                    //enviamos el correo
                    usuarioServicio.enviarConGMail(email, "Autorizacion de acceso  - Pablo Figueroa", link);
                     //enviamos el mensaje
                    RedirectAttributes addFlashAttribute = redirectAttrs
                    .addFlashAttribute("correoEnviado", "Se ha enviado el link de acceso a su correo");               
                }            
            }             
            else{
            // si existe el username(email), evaluamos tiempo de registro
            //si es menor a 40 minutos avisamos que el usuario ya se cuentra con un token habilitado
                if (controlador<120){
                     RedirectAttributes addFlashAttribute = redirectAttrs
                    .addFlashAttribute("correoEnviado", "Debe esperar dos minutos para enviar el correo ");     
                }else{                               
                    int minutos =40;
                    if ((usuarioServicio.fechaDiferencia(email)) < minutos){
                        RedirectAttributes addFlashAttribute = redirectAttrs
                        .addFlashAttribute("correoEnviado", "El link de acceso ya ha sido enviado, revise su correo");   
                    }else{
                        //si es mayor a 40, actualizamos el usuario con un nuevo token
                        String link= usuarioServicio.userToken(email, usuarioServicio.generadorTokenU(email), usuarioServicio.generadorTokenP(email));
                        usuarioServicio.enviarConGMail(email, "Autorizacion de acceso  - Pablo Figueroa", link);
                        RedirectAttributes addFlashAttribute = redirectAttrs    
                        .addFlashAttribute("correoEnviado", "El link de acceso se ha reenviado a su correo");                 
                    }               
                }                      
            }
        }
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/mail");
        return "redirect:/loginSpeed";
    }
    
    @GetMapping("/visitas")
    public String visitas (HttpServletRequest request,@RequestParam String tokenU,@RequestParam String tokenP, RedirectAttributes redirectAttrs ) throws MalformedURLException, IOException {
        if (tokenU!=null){
            if ((tokenP!=null)){            
                System.out.println(tokenU+"--"+tokenP);
                RedirectAttributes addFlashAttribute = redirectAttrs
                .addFlashAttribute("username", tokenU); 
                RedirectAttributes addFlashAttribute2 = redirectAttrs
                .addFlashAttribute ("password", tokenP); 
                 RedirectAttributes addFlashAttribute3 = redirectAttrs
                .addFlashAttribute ("boton", "btn-denger"); 
                return "redirect:/login";
            }
        }
        conexion.guardarRegistros(request.getRemoteAddr(),request.getHeader("X-Forwarded-For"),request.getRemoteHost(),request.getHeader("User-Agent"),"/visitas");
        return "redirect:/welcome";
    }
}

