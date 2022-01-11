/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argnet.demo.repositorios;

import Argnet.demo.entidades.Log;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pabli
 */
@Repository
public interface LogRepositorio extends JpaRepository<Log,String>{
        @Query(nativeQuery= true, value="SELECT * FROM Log order by fecha desc LIMIT 5")
        List <Log> ordenarPorfechaDESC();        
}
