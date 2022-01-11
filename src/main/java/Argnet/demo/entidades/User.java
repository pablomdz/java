/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argnet.demo.entidades;

import Argnet.demo.enumeracion.userRol;
import java.sql.Time;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author pabli
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name= "uuid", strategy="uuid2")
    @Column(name="id")
    private String id;
    
    private String name;
    private String lastname;
    private String email;
    private String password;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar altafecha; 
    @Temporal(TemporalType.TIME)
    private java.util.Date altahora;

   
    //@Temporal(TemporalType.DATE)
    private String nacimiento; 
    private boolean activo;
    
   @Enumerated(EnumType.STRING)
   private userRol rol; 

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the altafecha
     */
   

   
    /**
     * @return the nacimiento
     */
    public String getNacimiento() {
        return nacimiento;
    }

    /**
     * @param nacimiento the nacimiento to set
     */
    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the altahora
     */
    public Date getAltahora() {
        return altahora;
    }

    /**
     * @param altahora the altahora to set
     */
    public void setAltahora(Date altahora) {
        this.altahora = altahora;
    }

    public Comparator<? super User> compareTo(User o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the rol
     */
    public userRol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(userRol rol) {
        this.rol = rol;
    }

    public String getAltafecha(String dispatchTimeStamp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the altafecha
     */
    public Calendar getAltafecha() {
        return altafecha;
    }

    /**
     * @param altafecha the altafecha to set
     */
    public void setAltafecha(Calendar altafecha) {
        this.altafecha = altafecha;
    }

    /**
     * @return the altahora
     */
   

    /**
     * @return the altahora
     */

      
}
