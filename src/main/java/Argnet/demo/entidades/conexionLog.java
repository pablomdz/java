/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Argnet.demo.entidades;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author pabli
 */
@Entity
@Table(name = "conexionLog")
public class conexionLog {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name= "uuid", strategy="uuid2")
    @Column(name="id")
    private String id;
    
    private String usuario;
    private String ip;
    private String forwarded;
    private String iphost;
    private String userAgent;
    private String controlador;
     
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar fecha; 

   

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
     * @return the user


    /**
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the forwarder
     */
    public String getForwarder() {
        return getForwarded();
    }

    /**
     * @param forwarder the forwarder to set
     */
    public void setForwarder(String forwarder) {
        this.setForwarded(forwarder);
    }

    /**
     * @return the host
     */


    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the fecha
     */
    public Calendar getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the estado
     */
    
    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the forwarded
     */
    public String getForwarded() {
        return forwarded;
    }

    /**
     * @param forwarded the forwarded to set
     */
    public void setForwarded(String forwarded) {
        this.forwarded = forwarded;
    }

    /**
     * @return the iphost
     */
    public String getIphost() {
        return iphost;
    }

    /**
     * @param iphost the iphost to set
     */
    public void setIphost(String iphost) {
        this.iphost = iphost;
    }

    /**
     * @return the controlador
     */
    public String getControlador() {
        return controlador;
    }

    /**
     * @param controlador the controlador to set
     */
    public void setControlador(String controlador) {
        this.controlador = controlador;
    }
}
