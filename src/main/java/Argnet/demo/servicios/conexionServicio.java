package Argnet.demo.servicios;
import Argnet.demo.entidades.conexionLog;
import Argnet.demo.repositorios.conexionLogRepositorio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author pabli
 */
@Service
public class conexionServicio {
    @Autowired
    private conexionLogRepositorio repo;
   
    
    @Autowired
    private conexionServicio servicio;
        

//guardar los registros
   @Transactional
    public  void guardarRegistros(String RemoteAddr, String forwarded, String RemoteHost, String userAgent, String controlador) {  
        conexionLog registro=new conexionLog();
        registro.setIp(RemoteAddr);
        registro.setIphost(RemoteHost);
        registro.setUsuario("invitado");
        registro.setForwarder(forwarded);
        registro.setUserAgent(userAgent);
        registro.setControlador(controlador);
        Calendar fechaa = Calendar.getInstance();
        registro.setFecha(fechaa);
        repo.save(registro);  
  }
    //TRAE EL ULTIMO REGISTRO DE ESA IP
    public  Object listarIpdesc(String ip) {  
       conexionLog conec =new conexionLog();   
       conec=repo.buscarIpDesc(ip);
    return conec;
    }
    
    public Object ultimaConexionIpControlador (String ip, String controlador){
       conexionLog conec =new conexionLog();   
       conec=repo.buscarIpandControladorDescFecha(ip, controlador);
    return conec  ;  
    }
    
    public List verificarIntentos (String ip){
        List conexionesIntentos= new ArrayList();
        TimeZone timeZone = TimeZone.getTimeZone("America/Buenos_Aires");; 
        Calendar actual = Calendar.getInstance(); 
        actual.setTimeZone(timeZone);
        
        Calendar inicio = Calendar.getInstance(); 
        inicio.setTimeZone(timeZone);
        inicio.add(Calendar.MINUTE, -10);
        
        conexionesIntentos = repo.verificarIntentos(ip,inicio, actual, "/authError");
        
    return conexionesIntentos;  
    }
    
    public List ulti (){
        List<conexionLog> ultimasConexiones= new ArrayList(); 
        ultimasConexiones = repo.ultimas();   
    return ultimasConexiones;  
    }
}
