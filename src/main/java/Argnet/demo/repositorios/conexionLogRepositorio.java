/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argnet.demo.repositorios;


import Argnet.demo.entidades.Log;
import Argnet.demo.entidades.User;
import Argnet.demo.entidades.conexionLog;
import java.awt.print.Pageable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pabli
 */
@Repository
public interface conexionLogRepositorio  extends JpaRepository<conexionLog,String> {
   //TRAE EL ULTIMO REGISTRO DE ESA IP
    @Query(nativeQuery= true, value= "SELECT * FROM conexion_log p WHERE p.ip = :ip ORDER BY fecha DESC LIMIT 1")
    public conexionLog buscarIpDesc (@Param("ip") String ip);
    
   //trae el ultimo resgistro de ip con el controlador asignado
    @Query(nativeQuery= true, value= "SELECT * FROM conexion_log p WHERE p.ip = :ip AND p.controlador =:controlador ORDER BY fecha DESC LIMIT 1")
    public conexionLog buscarIpandControladorDescFecha (@Param("ip") String ip, @Param("controlador") String controlador);
   
   //busco las ultimas 5 conexiones al controlador "authError" entre el tiempo del sistema 
    // y 5 minutos antes
    @Query(nativeQuery= true, value="SELECT * FROM conexion_log p WHERE (p.fecha BETWEEN :inicio and :fin) and (p.ip = :ip) and (p.controlador= :controlador ) ORDER BY fecha DESC LIMIT 5")
    List <conexionLog> verificarIntentos(@Param("ip") String ip, @Param("inicio") Calendar inicio, @Param("fin") Calendar fin, @Param("controlador") String controlador) ;

    //selecionamos las ultimas 5 conexiones
    @Query(nativeQuery= true, value="SELECT * FROM conexion_log order by fecha desc LIMIT 5")
    List <conexionLog> ultimas();
    

}
