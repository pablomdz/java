/*lINK DONDE SE EXPLICA EL CODIGO UTILIZADO
https://www.logicbig.com/tutorials/spring-framework/spring-boot/implementing-error-controller.htmlnd open the template in the editor.
 */
//en ese controlador optenemos todos los codigos de errores, pero por el momento solo utilizaremos dos
package Argnet.demo.controladores;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@RequestMapping
public class Error implements ErrorController {
     
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {
      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
      Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
      String mensajeError="";
     switch(statusCode){
         case 404:
             mensajeError="redirect:/welcome";
             break;
         case 403:
             mensajeError="redirect:/admin?error=403";
             break;   
    }     
      return mensajeError;
  }
  public String getErrorPath() {
      return "/error";
  }
}