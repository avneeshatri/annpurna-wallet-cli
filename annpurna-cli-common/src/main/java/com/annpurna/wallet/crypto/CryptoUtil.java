package com.annpurna.wallet.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;



/*
openssl genrsa -out key.pem 2048
openssl req -new -key key.pem -out req.pem

openssl x509 -req -days 365 -in req.pem -signkey key.pem -sha256 -out pub.crt


openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in key.pem -out pkcs8.pem
openssl req -new -key pkcs8.pem -out req.pem
openssl x509 -req -days 365 -in req.pem -signkey pkcs8.pem -sha256 -out pub.crt
*/

public class CryptoUtil {

	public static byte[] signWithPrivatekey(byte[] data, String privateKeyFile) throws Exception {

		// Getting the privatekey from the key pair
		PrivateKey privKey = getPrivatekey(privateKeyFile);

		Signature sign = Signature.getInstance("SHA256withRSA");
		sign.initSign(privKey);
		sign.update(data);
		byte[] signature = sign.sign();
		return signature;

	}

	public static String getCertificateAsText(String resource) {
		InputStream in = null;
		try {
			if(!new File(resource).exists()) {
				throw new IllegalStateException("File doesnot exist:"+resource);
				
			}
			in = new FileInputStream(new File(resource));
			return new String(readAllBytes(in), StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			throw new IllegalStateException("Error while reading file",e);
		}
		
		finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new IllegalStateException("Error while closing file",e);}
			}
		}
	}
	
	public static byte[] loadPEM(String resource) throws IOException {
		String pem = getCertificateAsText(resource);
		Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
		String encoded = parse.matcher(pem).replaceFirst("$1");
		return Base64.getMimeDecoder().decode(encoded);
	}
	

	private static byte[] readAllBytes(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		for (int read = 0; read != -1; read = in.read(buf)) {
			baos.write(buf, 0, read);
		}
		return baos.toByteArray();
	}

	public static PrivateKey getPrivatekey(String keyfile) throws Exception {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return kf.generatePrivate(new PKCS8EncodedKeySpec(loadPEM(keyfile)));
	}

	public static PublicKey getPublicKeyFromCertFile(String keyfile) throws CertificateException, IOException {

		// before decoding we need to get rod off the prefix and suffix
		byte[] decoded = loadPEM(keyfile);
		return getPublicKeyFromCert(decoded);
	}

	public static PublicKey getPublicKeyFromCert(byte[] decoded) throws CertificateException, IOException {
		return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(decoded))
				.getPublicKey();
	}
	
	public static boolean verifySinature(byte[] signature, byte[] data, String pubKeyFile)
			throws InvalidKeyException, Exception {
		boolean verified = false;
		Signature pubSign = Signature.getInstance("SHA256withRSA");
		pubSign.initVerify(getPublicKeyFromCertFile(pubKeyFile));
		pubSign.update(data);

		// Verifying the signature
		verified = pubSign.verify(signature);
		return verified;
	}
	
	public static boolean verifySinature(byte[] signature, byte[] data, byte[] pubKey)
			throws InvalidKeyException, Exception {
		boolean verified = false;
		Signature pubSign = Signature.getInstance("SHA256withRSA");
		pubSign.initVerify(getPublicKeyFromCert(pubKey));
		pubSign.update(data);

		// Verifying the signature
		verified = pubSign.verify(signature);
		return verified;
	}

	public static String base64EndoedString(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	public static byte[] base64Decoded(String data) {
		return Base64.getDecoder().decode(data) ;
	}
}
