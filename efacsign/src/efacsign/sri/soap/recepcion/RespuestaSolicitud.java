/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.sri.soap.recepcion;

import efacsign.model.Comprobante;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author desarrollador
 */
public class RespuestaSolicitud {
    
    private String estado;    
    private List<Comprobante> comprobantes;

    public RespuestaSolicitud(String estado) {
        this.estado = estado;
        this.comprobantes = new ArrayList<Comprobante>();
    }

    @Override
    public String toString() {
        return "RespuestaSolicitud["+this.estado+"]";
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
     * @return the comprobantes
     */
    public List<Comprobante> getComprobantes() {
        return comprobantes;
    }

    /**
     * @param comprobantes the comprobantes to set
     */
    public void setComprobantes(List<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }
}
