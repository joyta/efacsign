/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package efacsign.sign;

/**
 *
 * @author desarrollador
 */
public class EfacObjectToSign extends es.mityc.javasign.xml.refs.AbstractObjectToSign {

    @Override
    public String getReferenceURI() {
        return "#comprobante";
    }
}
