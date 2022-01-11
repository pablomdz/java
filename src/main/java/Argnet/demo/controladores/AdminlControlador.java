package Argnet.demo.controladores;
import Argnet.demo.entidades.Log;
import Argnet.demo.entidades.User;
import Argnet.demo.repositorios.LogRepositorio;
import Argnet.demo.repositorios.UsuarioRepositorio;
import Argnet.demo.servicios.conexionServicio;
import Argnet.demo.servicios.userServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping
@Controller
public class AdminlControlador {   
    @Autowired
    private userServicio usuarioServicio;
    private UsuarioRepositorio repo;
    @Autowired
    private LogRepositorio log;  
    private conexionServicio coneclog; 
    //----------------------INDEX---------------------   
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITADO','ROLE_USUARIO')")
    //en este controlador se muestran los usuarios y ultima acciones de cada uno de ellos
    //los roles USUARIOS e Invitados, solo podran ver las funciones pero no podran eliminar o editar
    //Solo los administradores pueden realizar modificaciones
    @GetMapping("/admin")
    //ninguno de los datos que se puede recibir son obligatorios
    //error=nos devuelve el codigo 404 403 etc, pero solo utlizo esos dos
    //pag= hace referencia que esta utilizando un numero de paginacion
    //dato= sirve para definir el orden, normalmente puede ser nombre apellido mail fecha de naciemiento
    //orden= nos indica que como mostras los resultados, si de forma ascendete o descedente
    //en caso de recibir el parametro error lo evaluamos y enviamos
    public String lista(@RequestParam(required = false) String error,@RequestParam(required = false) String pag,@RequestParam(required = false) String dato, @RequestParam(required = false) String orden, ModelMap modelo) throws Exception {		             
        //si el controlador error envia un metodo get /?error=403, enviamos un mensaje mediante el put al sitio
        if ("403".equals(error)){
             modelo.put("mensaje","Solo los administradores puede modificar o eliminar usuarios"); 
        }
        // en caso de no recibir el parametro "dato" utilizado para ordenar la lista, por default, mostramos una
        // ordenada por nombre de forma ascendente
        List<User> usuariosLista=null;
        if (dato==null ){
            // cargamos todos los usuarios a la lisa usuariosLista
            usuariosLista = usuarioServicio.ordenarPor("name","asc");
            
        }else {
            // cargamos todos los usuarios de la lisa por datos y en orde
            usuariosLista = usuarioServicio.ordenarPor(dato,orden);
        }
        
        ////---------LOGICAL DE CODIGO PARA MOSTRAR LOS RESUTADOS CON UN SISTEMA DE PAGINACION ----------//
            //lista de usuarios a presentar en la paginacion
            List<User> usuariosPaginacion = new ArrayList();      
            //modelo utilizado para enviar todos los usuarios modelo.addAttribute("usuarios",usuariosLista);
            // si los usuarios son menor a 5 los imprimo
            if (usuariosLista.size()<=5){
                modelo.addAttribute("usuarios",usuariosLista);  
                modelo.put("paginacionAnterior","#");
                modelo.put("paginacionPosterior","#");    
            }else{
                // si ?pag= es null "null" "" o "0" mostramos loa primeros 5
                // luego asignamos los valores anteriores y posteriores
                if (pag==null | "".equals(pag) | "null".equals(pag) | "0".equals(pag)){
                    for (int i = 0; i < 5; i++) {
                        User usuarioPaginacion=new User();
                        usuarioPaginacion=usuariosLista.get(i);
                        usuariosPaginacion.add(usuarioPaginacion);                           
                    }
                    modelo.addAttribute("usuarios",usuariosPaginacion);
                    modelo.put("paginacionAnterior","0");
                    modelo.put("paginacionPosterior","5");            
                }else{
                // en caso de existir un numero de paginacion    
                //recibimos el numero lo convertimos a entero
                    int pagina = Integer.parseInt(pag);
                    
                    //VERIFICAMOS que el numero recibido conincida con el minimo y el maximo
                    // si el numero que recibimos es menor o igual al numero todal menos 5, significa que puedo
                    //mostrar 5 resultamos mas sin error
                    
                    //verificamos que ingresen un numero menor o igual a la lista
                    if (pagina<=usuariosLista.size() ){
                        
                        // si el numero de la lista +  el numero de la proximapagina es menor a 5
                        // quiere decir que los usuarios a mmostrar son menos de 5
                        if(usuariosLista.size()-pagina<5){
                            //entonces recorremos la diferencia menor a 5
                            for (int i = pagina; i < usuariosLista.size(); i++) {
                                User usuarioPaginacion=new User();
                                usuarioPaginacion=usuariosLista.get(i);
                                usuariosPaginacion.add(usuarioPaginacion);                           
                            }
                        }else{
                            // en caso de que la diferencia se 5 o mayor, mostramos todos los usuarios
                            for (int i = pagina; i < pagina + 5; i++) {
                                User usuarioPaginacion=new User();
                                usuarioPaginacion=usuariosLista.get(i);
                                usuariosPaginacion.add(usuarioPaginacion);                           
                            }
                        }                            
                        //Enviamos todos los usuarios cargados en la lista usuarios Paginacion
                        modelo.addAttribute("usuarios",usuariosPaginacion);
                        //MODELAMOS EL AVANCE Y RETROCESO DE LOS USUARIOS
                        //RETROCESO DE PAGINA - en caso de que la cantidad de usuarios que se encuentra en la variable
                        //pagina sea menor a 0, quiere decir que se acabanon la listas, pero puede puede haber un resto 
                        // de almenos 4 usuarios. En este caso le pasamos el valor cero y pasa a la linea 63 aprox
                        // donde en caso de ser cero, muestra los primeros 5 
                        if(pagina-5<0){
                             modelo.put("paginacionAnterior","0");
                        //caso contrario de no ser nuemero negativo, restamos
                        }else{
                            modelo.put("paginacionAnterior",pagina-5);
                        }
                        //AVANCE DE PAGINA - verificamos que el proximo numero +5 no exeda la longitud total
                        // en caso de exceder enviamos al diferencia del total menos 7
                        if(pagina+5>usuariosLista.size()-5){
                            modelo.put("paginacionPosterior",usuariosLista.size()-5);
                        // caso contrario autamos en 5 la paginacion
                        }else{
                            modelo.put("paginacionPosterior",pagina+5);
                        }
                    }else{
                            modelo.put("mensaje","Ha ingresado un numero de paginacion no valido");                    
                    }                                            
                }
            }
        //----------------------------------FIN DE LA LOGICA DE PAGINACION------------------------------------//              
        //enviamos el nombre de usuario
        modelo.put("usuario", usuarioServicio.usuarioName());         
        //enviamos los ultimos 5 registros 
        List<Log> registros =log.ordenarPorfechaDESC();
        modelo.addAttribute("registros", registros);       
        return "index.html";
    }

    //-----------------------BUSCAR---------------------
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITADO','ROLE_USUARIO')")
    @PostMapping("/buscar") 
    public String buscar (@RequestParam String dato_buscar, @RequestParam String dato_tipo,  ModelMap modelo , RedirectAttributes redirectAttrs ) throws Exception{       
        // dato_buscar=null
        if ("".equals(dato_buscar)) {
             //con esta linea envio los mensajes a la pagina (https://parzibyte.me/blog/2019/08/26/agregar-mensajes-flash-redirigir-spring-boot-thymeleaf/)
            RedirectAttributes addFlashAttribute = redirectAttrs
            .addFlashAttribute("mensaje", "No ingreso ningun dato");
            return "redirect:/admin"; 
        }else{
        // resultad null corregir
            if (usuarioServicio.buscar(dato_buscar,dato_tipo)==null){
                //avisamos que no hay resultados
                RedirectAttributes addFlashAttribute = redirectAttrs
                .addFlashAttribute("mensaje", "Sin resultado");                          
                return "index.html"; 
        //resultado     
            }else{
                //Mostramos los resultados
                modelo.put("usuarios",usuarioServicio.buscar(dato_buscar,dato_tipo));               
                //enviamos todos los registros 
                List<Log> registros =log.findAll();
                modelo.addAttribute("registros", registros);                               
               return "index.html";
            }                                   
        }      
    }
    //--------------------------------EDITAR------------------------- 
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITADO','ROLE_USUARIO')")
    @PostMapping("/editar") 
    // buscamos el id a buscar
    public String editar (@RequestParam String id_editar, ModelMap modelo) throws Exception{       
        modelo.put("usuarios", usuarioServicio.buscarId(id_editar));
        return "editar.html";          
    }
    //guardamos el id con los cambios realizados
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/editarRegistro") 
    public String editarRegistro (@RequestParam String id, @RequestParam String name,@RequestParam String lastname,@RequestParam String email,@RequestParam String nacimiento, @RequestParam boolean activo) throws Exception{                         
        usuarioServicio.modificar(id, name, lastname, email, nacimiento, activo);     
        return "redirect:/admin";                
    }
    
    //--------------------------------EDITAR_pass------------------------- 
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITADO','ROLE_USUARIO')")
    @PostMapping("/editar_pass") 
    // buscamos el id a buscar
    public String editar_pass (@RequestParam String editar_pass, ModelMap modelo) throws Exception{       
        modelo.put("usuarios", usuarioServicio.buscarId(editar_pass));
        return "editar_pass.html";          
    }
    //guardamos el id con los cambios realizados
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/editarRegistrPass") 
    public String editarRegistroPass (@RequestParam String id,@RequestParam String pass) throws Exception{                         
        usuarioServicio.modificarPass(id,pass);     
        return "redirect:/admin";                
    }
        
    //--------------------------------ELIMINAR-------------------------
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar") 
    public String eliminar(@RequestParam String id) throws Exception{       
        usuarioServicio.eliminar(id);
        return "redirect:/admin" ; 
    } 
  
}
