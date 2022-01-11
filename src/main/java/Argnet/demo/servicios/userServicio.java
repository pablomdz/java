package Argnet.demo.servicios;
import Argnet.demo.entidades.Rol;
import Argnet.demo.repositorios.RolRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import Argnet.demo.entidades.User;
import Argnet.demo.enumeracion.userRol;
import Argnet.demo.errores.ErrorServicio;
import Argnet.demo.repositorios.UsuarioRepositorio;
import Argnet.demo.servicios.userServicio;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

@Service
public class userServicio  implements UserDetailsService{   

//Instancia los objetos de manera automatica ( remplaza  esto   UsuarioRepositorio userrepo =new UsuarioRepositorio();)
    @Autowired
    private UsuarioRepositorio userrepo;
    @Autowired
    private RolRepositorio rolrepo;
    @Autowired
    private userLog logServicio;
    @Autowired
    private userServicio usuarioServicio;
    @Autowired
    private conexionServicio conexion;  
    
//REGISTRO DE NUEVO USUARIO
    public String validarRegistro (String name, String lastname, String password, String email, String nacimiento){
        String aviso="";
        //control de name y verificar que no este este agregado como correo
        if (name==null){
            aviso=aviso +", no ingreso un nombre";
        }

        if("".equals(name)){
            aviso=aviso +", nombre vacio";

        }

        if(userrepo.buscarPorname2(name)!=null ){
            aviso=aviso +", debe cambiar el nombre a registrar";
        }    
        //control de lastname
        if (lastname==null){
            aviso=aviso +", no ingreso un apellido";
        }

        if("".equals(lastname)){
            aviso=aviso +", apellido vacio";

        }
        //control de password
        if (password==null){
            aviso=aviso +", no ingreso una clave";
        }

        if("".equals(password)){
            aviso=aviso +", clave vacia";

        }
        //control de email y verificacion de que no este en uso
        if (email==null){
            aviso=aviso +", no ingreso un correo";
        }

        if("".equals(email)){
            aviso=aviso +", debe ingresar un correo";
        }

        if(userrepo.buscarPorMail(email)!=null ){
            aviso=aviso +", su correo ya se encuentra registrado";
        }    
        //control nacimiento
        //
        System.out.println("****************AVISO " + aviso);
    return aviso;
    }
    
    @Transactional
    public String registrar(String name, String lastname, String password, String email, String nacimiento) throws ErrorServicio, Exception{
        String resultado="";
        if ("".equals(usuarioServicio.validarRegistro(name, lastname, password, email, nacimiento))){
            User usuario = new User();
            usuario.setName(name);
            usuario.setLastname(lastname);
            usuario.setEmail(email);
            //fix usuario.setPassword(password); 
            usuario.setNacimiento(nacimiento);        
            usuario.setActivo(true);

            usuario.setRol(userRol.USUARIO);

            String encriptada = new BCryptPasswordEncoder().encode(password);
            usuario.setPassword(encriptada);

            Calendar alta = Calendar.getInstance();
            usuario.setAltafecha(alta);

            Date altahora = new Date();
            usuario.setAltahora(altahora);

            userrepo.save(usuario);

            //REGISTRO
            if (usuarioName()!=null){
                User idagregado= userrepo.buscarPorMail(email);
                logServicio.addLog("Crear", idagregado.getId(), usuarioId());
            } 
            resultado="Registrado con exito";
            System.out.println(resultado);
        }else{
            System.out.println(resultado);
            resultado="¡ATENCION!"+usuarioServicio.validarRegistro(name, lastname, password, email, nacimiento);
           
        }
        return resultado;
    
    }
       
//----------------------MODIFICADOR DE USUARIO-------------------
    @Transactional
    public User modificar(String id, String name, String lastname, String email, String nacimiento, boolean activo) throws ErrorServicio, Exception{
        /*FALTA VALIDAR*/
        User usuario = userrepo.getOne(id);
        usuario.setName(name);
        usuario.setLastname(lastname);
        usuario.setEmail(email);
        usuario.setNacimiento(nacimiento);
        usuario.setActivo(activo);          
        logServicio.addLog("Modificar", id, usuarioId());
        return userrepo.save(usuario);
    }
    
    //----------------------MODIFICADOR password-------------------
    @Transactional
    public User modificarPass(String id, String password) throws ErrorServicio, Exception{
        /*FALTA VALIDAR*/
        User usuario = userrepo.getOne(id);
        //usuario.setPassword(password);    
                    String encriptada = new BCryptPasswordEncoder().encode(password);
            usuario.setPassword(encriptada);
        logServicio.addLog("Modificar pass", id, usuarioId());
        return userrepo.save(usuario);
    }
    
    
//----------------------ELIMINAR USUARIO-------------------
    public void eliminar (String id) throws ErrorServicio, Exception{
        userrepo.deleteById(id);
        logServicio.addLog("Eliminar", id, usuarioId());
    }
   
//----------------------BUSCAR POR ID -------------------
    public User buscarId (String id) throws ErrorServicio, Exception{  
        //logServicio.addLog("Busqueda", id, usuarioId());
        return userrepo.buscarPorId(id);
    }
    
//----------------------BUSCAR POR PARAMETRO----------------
    public List <User> buscar (String dato_buscar, String dato_tipo) throws ErrorServicio, Exception{                
        String i=dato_tipo;
        switch(i)
        {
           case "id":
             System.out.println("Case1 id: " + dato_buscar );
              return userrepo.Id(dato_buscar);
           case "name":               
             return userrepo.buscarPorname(dato_buscar);
           case "lastname":
            return userrepo.buscarPorlastname(dato_buscar);
           case "email":
             return userrepo.buscarPoremail(dato_buscar);
           case "":
            //esto es para la carga automatica
             break;
        }
        //logServicio.addLog("Busqueda: dato "+dato_buscar+", parametro "+dato_tipo, "", usuarioId());
        return null;   
    }
    
//----------------------LISTAR TODOS LOS USUARIOS----------------
    @Transactional
    public List<User> listarTodos() {
        //logServicio.addLog("Lista solicitada", "", usuarioId());
        return userrepo.findAll();
    }
    
//----------------------ORDENAR POR PARAMETRO----------------
    public List <User> ordenarPor (String dato, String orden) throws ErrorServicio, Exception{                
        String datoOrdenar=dato;
        String ordenOrdenar=orden;
        //logServicio.addLog("Ordenar dato "+dato+" por "+orden, "", usuarioId());
        switch(datoOrdenar)
        {
           case "name": 
               if ("asc".equals(ordenOrdenar)){
                 return userrepo.ordenarPornameasc();
               }else{
                 return userrepo.ordenarPornamedesc();
               }
           case "lastname":
               if ("asc".equals(ordenOrdenar)){
                 return userrepo.ordenarPorlastnameasc();
               }else{
                 return userrepo.ordenarPorlastnamedesc();
               }
           case "nacimiento":
               if ("asc".equals(ordenOrdenar)){
                 return userrepo.ordenarPornacimientoasc();
               }else{
                 return userrepo.ordenarPornacimientodesc();
               }
            case "actividad":
               if ("asc".equals(ordenOrdenar)){
                 return userrepo.ordenarPoractivoasc();
               }else{
                 return userrepo.ordenarPoractivodesc();
               }
          
            case "inactividad":
               if ("asc".equals(ordenOrdenar)){
                 return userrepo.ordenarPorinactivoasc();
               }else{
                  return userrepo.ordenarPorinactivodesc();
               }          
        }
     return null;
     }
    
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {    	
        User usuario = userrepo.buscarPorMail(mail);
        //System.out.println(usuario);        
        if (usuario != null) {       	
            List<GrantedAuthority> permisos = new ArrayList<>();                      
            //Creo una lista de permisos! 
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol());
            permisos.add(p1);
        
            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);            
            session.setAttribute("usuariosession", usuario); // llave + valor
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(), permisos);
            //logServicio.addLog("Conexion", "", usuarioId());
     
            return user;

        } else {
           
            System.out.println("el usuario no existe");
            return null;
        }
    }
//----------------------RECUPERAMOS EL NOMBRE DE USUARIO----------------   
    public String usuarioName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();      
        if (principal instanceof UserDetails) {      
            return ((UserDetails) principal).getUsername();
        }  
        return null;
    }
    

    public Collection<? extends GrantedAuthority> usuarioRol(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();      
        if (principal instanceof UserDetails) {      
            return ((UserDetails) principal).getAuthorities();
        }  
        return null;
    }
    
    

//----------------------RECUPERAMOS EL NOMBRE DE USUARIO----------------   
    public String usuarioId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();      
        if (principal instanceof UserDetails) {      
            User usuario = userrepo.buscarPorMail(((UserDetails) principal).getUsername());
            return usuario.getId();
        }else{
            return "Sin registrar";
        }  
    }
    
//Envio de correo
    public void enviarConGMail(String destinatario, String asunto, String cuerpo) {
        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
        String remitente = "correo@coreo.com";  //Para la dirección nomcuenta@gmail.com
        String clave = "contraseña";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO,  new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
            message.setSubject(asunto);
            message.setText(cuerpo);
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect("smtp.gmail.com", remitente, clave);
                transport.sendMessage(message, message.getAllRecipients());
                System.out.println("enviado");
            }
    }
    catch (MessagingException me) {
        System.out.println("Error");        
    }
    }
//Generador del Token usuario   
   public String generadorTokenU(String email) throws Exception {
        // generamos un token en base del correo y la hora que serviar como usuario
        String value = email+LocalDateTime.now();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(value.getBytes("utf8"));
        String usuarioNuevo = String.format("%040x", new BigInteger(1, digest.digest()));                        
        return usuarioNuevo;
   }
//Generador del Token password   
    public String generadorTokenP(String email) throws Exception {
         //generamos un token con el (usuario+fecha) mas un numero aleatorio
        String usuarioNuevo=usuarioServicio.generadorTokenU(email);
        String value2 = LocalDateTime.now()+usuarioNuevo;
        MessageDigest digest2 = MessageDigest.getInstance("SHA-1");
        digest2.reset();
        digest2.update(value2.getBytes("utf8"));
        String passwordNuevo = String.format("%040x", new BigInteger(1, digest2.digest()));               
        return passwordNuevo;                   
    }
//Carga de usurio rapido y generacion de link    
    public String userToken(String email, String tokenU, String tokenP) throws Exception {
       /// aca tengo que ver si existe actualizo-----------------------------------------------------------------------------------------------------
        if (userrepo.buscarPorname2(email)==null){                                 
            // Cargamos el usuario a la base de datos
            User usuario = new User();
            usuario.setName(email);
            usuario.setLastname("");
            usuario.setEmail(tokenU);
            String encriptada = new BCryptPasswordEncoder().encode(tokenP);
            usuario.setPassword(encriptada); 
            usuario.setNacimiento("");        
            usuario.setActivo(true);
            usuario.setRol(userRol.INVITADO);             
            Calendar alta = Calendar.getInstance();
            usuario.setAltafecha(alta);
            Date altahora = new Date();
            usuario.setAltahora(altahora);
            userrepo.save(usuario);
            
        }else{
            //actualizamos el usuario
            User usuario = new User();
            usuario=userrepo.buscarPorname2(email);
            usuario.setEmail(tokenU);
            String encriptada = new BCryptPasswordEncoder().encode(tokenP);
            usuario.setPassword(encriptada); 
            usuario.setRol(userRol.INVITADO);             
            Calendar alta = Calendar.getInstance();
            usuario.setAltafecha(alta);
            Date altahora = new Date();
            usuario.setAltahora(altahora);
            userrepo.save(usuario);
            
            System.out.println("Actualizar usuario");
            }
        
        return "usted puede ingresar con el siguiente link: " + "http://localhost:8080/visitas/?tokenU="+tokenU+"&tokenP="+tokenP;
    }
    
//fecha de registro de usuario
    public Calendar fechaRegistro(String email) throws Exception {
        User aux = new User();                                  
        //recordamos que el usernamename es el email, pero si se registra un logueo rapido
        //el username se transforma en un token aleatorio, 
        aux =userrepo.buscarPorname2(email);
        TimeZone timeZone = TimeZone.getTimeZone("America/Buenos_Aires");; 
        Calendar fecha = Calendar.getInstance();       
        // recordar configurar el aplication propietary useTimezone=true&serverTimezone=GMT-3
        fecha= aux.getAltafecha();
        fecha.setTimeZone(timeZone);
        return fecha;
    }
//fecha de registro del sistema
    public Calendar fechaSistema() throws Exception {
         TimeZone timeZone = TimeZone.getTimeZone("America/Buenos_Aires");; 
        Calendar fecha = Calendar.getInstance(); 
        fecha.setTimeZone(timeZone);
        return fecha;
    }
//diferencia entre registro(email) y fecha actual
//sirve para verificacion de seguridad(es utilizado para el tiempo de vida de un token) 
    public int fechaDiferencia(String email) throws Exception {
        int dif = (int) (usuarioServicio.fechaSistema().getTimeInMillis()-usuarioServicio.fechaRegistro(email).getTimeInMillis());        
        return (dif/(1000*60));
    }
    


        
  
//FIN DE LA CLASE userServicio        
}
