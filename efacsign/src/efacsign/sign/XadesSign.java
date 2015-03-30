package efacsign.sign;

import efacsign.sign.pkstore.PassStoreKS;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.pkstore.CertStoreException;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import es.mityc.javasign.xml.refs.ObjectToSign;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XadesSign {

    public PrivateKey privateKey;
    public Provider provider;

    public byte[] firmarDocumentoXmlXades(byte[] entrada) {
        //logger.info("Inicia el proceso de firmado del documento");
        //X509Certificate certificate = LoadCertificate("C:\\Users\\Divusware\\workspace\\Firm\\cer\\diana_paulina_quille_camacho.p12", "corsYstel2011");		
        X509Certificate certificate = LoadCertificate("corsYstel2011");		
        

        //Si encontramos el certificado...  
        if (certificate != null) {
            //Crear datos a firmar  
            DataToSign dataToSign = new DataToSign();
            dataToSign.setXadesFormat(EnumFormatoFirma.XAdES_BES);
            dataToSign.setEsquema(XAdESSchemas.XAdES_132);
            dataToSign.setPolicyKey("facturae31");
            dataToSign.setAddPolicy(true);
            dataToSign.setXMLEncoding("UTF-8");
            dataToSign.setEnveloped(true);
            dataToSign.addObject(new ObjectToSign(new EfacObjectToSign(), "Descripcion del documento", null, "text/xml", null));
            dataToSign.setDocument(LoadXML(entrada));

            Object[] res = null;
            try {
                FirmaXML f = new FirmaXML();
                res = f.signFile(certificate, dataToSign, privateKey, provider);
                System.out.println("Xml firmado correctamente!!!");
            } catch (NoSuchMethodError ex) {
                //logger.info("error : "+ex.getStackTrace() + " ");
                ex.printStackTrace();

            } catch (Exception e) {
                //logger.info("Ha ocurrido una exceptio al momento de firmar el documento "+e.getStackTrace());
                e.printStackTrace();
                return null;
            }

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            // Guardamos el array de bytes firmados en un ByteArrayOutputStream.
            UtilidadTratarNodo.saveDocumentToOutputStream((Document) res[0], outStream, true);

            return outStream.toByteArray();
        }

        return null;
    }

    private X509Certificate LoadCertificate(final String password) {
        System.out.println("Va a cargar el certificado");
        X509Certificate certificate = null;
        provider = null;
        privateKey = null;

        try {
            //Cargar certificado de fichero PFX  
            KeyStore ks = KeyStore.getInstance("PKCS12");
            
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("efacsign/cert/diana_paulina_quille_camacho.p12");            
            
            ks.load(new BufferedInputStream(is), password.toCharArray());

            //ks.load(new BufferedInputStream(new FileInputStream(path)), password.toCharArray());

            IPKStoreManager storeManager = new KSStore(ks, new PassStoreKS(password));

            List<X509Certificate> certificates = storeManager.getSignCertificates();

            //Si encontramos el certificado...  
            if (certificates.size() >= 1) {
                certificate = certificates.get(0);

                // Obtenci�n de la clave privada asociada al certificado  
                privateKey = storeManager.getPrivateKey(certificate);

                // Obtenci�n del provider encargado de las labores criptogr�ficas  
                provider = storeManager.getProvider(certificate);
            }
            System.out.println("Certificado cargado correctamente");
        } catch (NoSuchAlgorithmException nsae) {
            //logger.info(nsae.getStackTrace());
            nsae.printStackTrace();
            return null;
        } catch (CertificateException ce) {
            ce.printStackTrace();
            //logger.info(ce.getStackTrace());
            return null;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            //logger.info(fnfe.getStackTrace());
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            //logger.info(ioe.getStackTrace());
            return null;
        } catch (KeyStoreException kse) {
            kse.printStackTrace();
            //logger.info(kse.getStackTrace());
            return null;
        } catch (CertStoreException cse) {
            cse.printStackTrace();
            //logger.info(cse.getStackTrace());
            return null;
        }

        return certificate;
    }

    private Document LoadXML(byte[] fileBytes) {
        System.out.println("Va a cargar el xml");
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(fileBytes));
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            //logger.info(ex.getStackTrace());
            return null;
        } catch (SAXException ex) {
            ex.printStackTrace();
            //logger.info(ex.getStackTrace());
            System.out.println(new String(fileBytes));
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            //logger.info(ex.getStackTrace());
            return null;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            //logger.info(ex.getStackTrace());
            return null;
        }
        System.out.println("Xml cargado correctamente");
        return doc;
    }

}

