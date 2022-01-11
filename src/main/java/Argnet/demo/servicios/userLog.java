/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argnet.demo.servicios;

import Argnet.demo.entidades.Log;
import Argnet.demo.repositorios.LogRepositorio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pabli
 */
@Service
public class userLog {
    @Autowired
    private LogRepositorio logrepo;
     @Autowired
    private userServicio userservicio;
    
    @Transactional
    public void addLog(String type, String id, String idmodificado){         
         Log registro = new Log();        
         registro.setIdUser(idmodificado);
         registro.setIdModificado(id);
         registro.setLog(type);        
         Date fecha = new Date();
         registro.setFecha(fecha);
         logrepo.save(registro);
}
    
}
