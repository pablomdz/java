
package Argnet.demo.repositorios;


import Argnet.demo.entidades.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio  extends JpaRepository<User,String>{
    //-------------------BUSQUEDAS-------------------//
    @Query("SELECT p FROM User p WHERE p.id = :id")
    public User buscarPorId(@Param("id") String id);
    
    @Query("SELECT p FROM User p WHERE p.id = :id")
    List <User> Id(@Param("id") String id);
    
    @Query("SELECT p FROM User p WHERE p.name = :name")
    List <User> buscarPorname(@Param("name") String name);
    
    @Query("SELECT p FROM User p WHERE p.name = :name")
    public User buscarPorname2(@Param("name") String name);
    
    @Query("SELECT p FROM User p WHERE p.lastname = :lastname")
    List <User> buscarPorlastname(@Param("lastname") String lastname);
    
    
    @Query("SELECT p FROM User p WHERE p.email = :email")
    List <User> buscarPoremail(@Param("email") String email);
      
    @Query("SELECT p FROM User p WHERE p.email = :email")
    public User buscarPorMail(@Param("email") String email);
    

     //--------------------ORDEN------------------------//       
    @Query("SELECT p FROM User p order by p.name asc ")
    List <User> ordenarPornameasc();
    @Query("SELECT p FROM User p order by p.name desc ")
    List <User> ordenarPornamedesc();
    //----------------------------------------------------
    @Query("SELECT p FROM User p order by p.lastname asc ")
    List <User> ordenarPorlastnameasc();
        @Query("SELECT p FROM User p order by p.lastname desc ")
    List <User> ordenarPorlastnamedesc();
    //------------------------------------------------------
    @Query("SELECT p FROM User p order by p.nacimiento asc ")
    List <User> ordenarPornacimientoasc();
    @Query("SELECT p FROM User p order by p.nacimiento desc ")
    List <User> ordenarPornacimientodesc();
    //------------------------------------------------------
    @Query("SELECT p FROM User p where p.activo=true order by p.altafecha asc")
    List <User> ordenarPoractivoasc();  
    @Query("SELECT p FROM User p where p.activo=true order by p.altafecha desc")
    List <User> ordenarPoractivodesc();  
    @Query("SELECT p FROM User p where p.activo=false order by p.altafecha asc")
    List <User> ordenarPorinactivoasc();   
    @Query("SELECT p FROM User p where p.activo=false order by p.altafecha desc")
    List <User> ordenarPorinactivodesc();
    //------------------------------------------------------
    
   
    
}
