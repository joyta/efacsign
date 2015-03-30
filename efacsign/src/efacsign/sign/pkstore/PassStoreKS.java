package efacsign.sign.pkstore;

import java.security.cert.X509Certificate;

import es.mityc.javasign.pkstore.IPassStoreKS;

public class PassStoreKS implements IPassStoreKS {
	
	private transient String password;
	
	public PassStoreKS(final String pass) {
  		this.password = new String(pass);
  	}
	
	@Override
	public char[] getPassword(X509Certificate arg0, String alias) {
		// TODO Auto-generated method stub
		return password.toCharArray();
	}

}
