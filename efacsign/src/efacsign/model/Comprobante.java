/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author desarrollador
 */
@Entity
@Table(schema = "tributario")
public class Comprobante implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column
    private String numero;
    
    @Column
    private String estado;
    
    @Column
    private String tipo;
    
    @Column
    private String xml;
    
    @Column    
    private String clave_acceso;
    
    @Column
    private String numero_autorizacion;
    
    @Temporal(TemporalType.DATE)
    private Date fecha_autorizacion;
    
    @Column
    private String ambiente;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_send;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_email;
    
    @Column
    private String send_email;
    
    public Comprobante() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprobante)) {
            return false;
        }
        Comprobante other = (Comprobante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comprobante[ id=" + id + " ]";
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * @param xml the xml to set
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * @return the clave_acceso
     */
    public String getClave_acceso() {
        return clave_acceso;
    }

    /**
     * @param clave_acceso the clave_acceso to set
     */
    public void setClave_acceso(String clave_acceso) {
        this.clave_acceso = clave_acceso;
    }   

    /**
     * @return the numero_autorizacion
     */
    public String getNumero_autorizacion() {
        return numero_autorizacion;
    }

    /**
     * @param numero_autorizacion the numero_autorizacion to set
     */
    public void setNumero_autorizacion(String numero_autorizacion) {
        this.numero_autorizacion = numero_autorizacion;
    }

    /**
     * @return the fecha_autorizacion
     */
    public Date getFecha_autorizacion() {
        return fecha_autorizacion;
    }

    /**
     * @param fecha_autorizacion the fecha_autorizacion to set
     */
    public void setFecha_autorizacion(Date fecha_autorizacion) {
        this.fecha_autorizacion = fecha_autorizacion;
    }

    /**
     * @return the ambiente
     */
    public String getAmbiente() {
        return ambiente;
    }

    /**
     * @param ambiente the ambiente to set
     */
    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    /**
     * @return the last_send
     */
    public Date getLast_send() {
        return last_send;
    }

    /**
     * @param last_send the last_send to set
     */
    public void setLast_send(Date last_send) {
        this.last_send = last_send;
    }

    /**
     * @return the last_email
     */
    public Date getLast_email() {
        return last_email;
    }

    /**
     * @param last_email the last_email to set
     */
    public void setLast_email(Date last_email) {
        this.last_email = last_email;
    }

    /**
     * @return the send_email
     */
    public String getSend_email() {
        return send_email;
    }

    /**
     * @param send_email the send_email to set
     */
    public void setSend_email(String send_email) {
        this.send_email = send_email;
    }
    
}
