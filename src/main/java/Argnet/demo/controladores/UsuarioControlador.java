package Argnet.demo.controladores;
import Argnet.demo.servicios.userServicio;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping
@Controller

public class UsuarioControlador {
    private userServicio usuarioServicio;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO','ROLE_INVITADO')")
    @GetMapping("/welcome") 
    public String welcome(@RequestParam(required = false) String error, ModelMap modelo, ModelMap modelo2) throws Exception{       
        if ("404".equals(error) ){
            modelo.put("mensaje","Not Found");
        }else{
            modelo.put("mensaje","");
        }    
    return "welcome.html" ;
    } 
   
}
