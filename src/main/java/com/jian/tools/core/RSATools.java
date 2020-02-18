package com.jian.tools.core;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSATools {

	private String key_algorithm = "RSA"; 
	private String charset = "utf-8"; 
	private Map<String, String> keyMap = new HashMap<>();
	private String publicKey ="publicKey";  
	private String privateKey ="privateKey";
	
	public RSATools(){
		
	}
	
	public RSATools(boolean genkey){
		if(genkey){
			generateKey(1024, null);
		}
	}

	//TODO -------------------------------------------------------------------------------------------------生成密钥
	
	/**
	 * 生成密钥
	 */
	public void generateKey(){
		generateKey(1024, null);
	}
	
	/**
	 * 生成密钥
	 * @param keysize
	 * @param key
	 */
	public void generateKey(int keysize, String key){
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(key_algorithm);
			SecureRandom random = new SecureRandom(); 
			if(key != null && !"".equals(key)){
				random.setSeed(key.getBytes(charset));
			}
			keygen.initialize(keysize, random);
			KeyPair kp = keygen.generateKeyPair();
			
			keyMap.put(publicKey, Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
			keyMap.put(privateKey, Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	//TODO -------------------------------------------------------------------------------------------------获取密钥
	
	/**
	 * 获取公钥
	 * @return
	 */
	public String getPublicKey(){
		return keyMap.get(publicKey);
	}

	/**
	 * 获取私钥
	 * @return
	 */
	public String getPrivateKey(){
		return keyMap.get(privateKey);
	}
	
	public RSAPublicKey getRSAPublicKey(String pubKeyStr){
		try {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(pubKeyStr));  
			KeyFactory factory = KeyFactory.getInstance(key_algorithm);
			RSAPublicKey pubKey = (RSAPublicKey) factory.generatePublic(spec);
			return pubKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RSAPrivateKey getRSAPrivateKey(String priKeyStr){
		try {
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKeyStr));
			KeyFactory factory = KeyFactory.getInstance(key_algorithm);
			RSAPrivateKey priKey = (RSAPrivateKey) factory.generatePrivate(spec);
			return priKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//TODO -------------------------------------------------------------------------------------------------加密
	/**
	 * 公钥加密.  BigInteger
	 * @param data
	 * @param pubKeyStr
	 * @return
	 */
	public byte[] encryptByPubKey(byte[] data, String pubKeyStr){
        // 获取公钥及参数e,n
        RSAPublicKey pbk = getRSAPublicKey(pubKeyStr);
        BigInteger e = pbk.getPublicExponent();
        BigInteger n = pbk.getModulus();
        // 获取明文m
        BigInteger m = new BigInteger(data);
        // 计算密文c
        BigInteger c = m.modPow(e, n);
        return c.toByteArray();
    }
	
	/**
	 * 私钥加密.  BigInteger
	 * @param data
	 * @param priKeyStr
	 * @return
	 */
	public byte[] encryptByPriKey(byte[] data, String priKeyStr){
        // 获取私钥及参数e,n
        RSAPrivateKey prk = getRSAPrivateKey(priKeyStr);
        BigInteger e = prk.getPrivateExponent();
        BigInteger n = prk.getModulus();
        // 获取明文m
        BigInteger m = new BigInteger(data);
        // 计算密文c
        BigInteger c = m.modPow(e, n);
        return c.toByteArray();
    }
	
	/**
	 * 公钥加密.  Cipher
	 * @param data
	 * @param pubKeyStr
	 * @return
	 */
	public byte[] encryptByPubKeyCipher(byte[] data, String pubKeyStr){
		try {
			Cipher cipher = Cipher.getInstance(key_algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey(pubKeyStr));
			
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * 私钥加密.  Cipher
	 * @param data
	 * @param priKeyStr
	 * @return
	 */
	public byte[] encryptByPriKeyCipher(byte[] data, String priKeyStr){
		try {
			Cipher cipher = Cipher.getInstance(key_algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, getRSAPrivateKey(priKeyStr));
			
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
	//TODO -------------------------------------------------------------------------------------------------解密
	/**
	 * 私钥解密.  BigInteger
	 * @param data
	 * @param priKeyStr
	 * @return
	 */
    public byte[] decryptByPriKey(byte[] data, String priKeyStr) {
        // 读取密文
        BigInteger c = new BigInteger(data);
        // 读取私钥
        RSAPrivateKey prk = getRSAPrivateKey(priKeyStr);
        BigInteger d = prk.getPrivateExponent();
        // 获取私钥参数及解密
        BigInteger n = prk.getModulus();
        BigInteger m = c.modPow(d, n);
        // 显示解密结果
        return m.toByteArray();
    }
	
	/**
	 * 公钥解密.  BigInteger
	 * @param data
	 * @param pubKeyStr
	 * @return
	 */
    public byte[] decryptByPubKey(byte[] data, String pubKeyStr) {
        // 读取密文
        BigInteger c = new BigInteger(data);
        // 读取公钥
        RSAPublicKey pbk = getRSAPublicKey(pubKeyStr);
        BigInteger d = pbk.getPublicExponent();
        // 获取公钥参数及解密
        BigInteger n = pbk.getModulus();
        BigInteger m = c.modPow(d, n);
        // 显示解密结果
        return m.toByteArray();
    }
    
    /**
     * 私钥解密.  Cipher
     * @param data
     * @param priKeyStr
     * @return
     */
    public byte[] decryptByPriKeyCipher(byte[] data, String priKeyStr) {
    	try {
    		Cipher cipher = Cipher.getInstance(key_algorithm);
    		cipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey(priKeyStr));
    		
    		return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
	/**
	 * 公钥解密.  Cipher
	 * @param data
	 * @param pubKeyStr
	 * @return
	 */
    public byte[] decryptByPubKeyCipher(byte[] data, String pubKeyStr) {
    	try {
    		Cipher cipher = Cipher.getInstance(key_algorithm);
    		cipher.init(Cipher.DECRYPT_MODE, getRSAPublicKey(pubKeyStr));
    		
    		return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static void main(String[] args) {
    	
    	String str = "sfad1234达到大赛的房间里对的二，多大dfadfs ";
    	RSATools rsa = new RSATools();
    	rsa.generateKey();
    	
    	String pubKey = rsa.getPublicKey();
    	String priKey = rsa.getPrivateKey();
		System.out.println("公钥：" + pubKey );
		System.out.println("私钥：" + priKey );
    	
		System.out.println("=========test1 公钥加密==============");
		long start1 = System.currentTimeMillis();
		String mstr1 = Base64.getEncoder().encodeToString(rsa.encryptByPubKey(str.getBytes(), pubKey));
		String astr1 = new String(rsa.decryptByPriKey(Base64.getDecoder().decode(mstr1), priKey));
		System.out.println("加密前：" + str );
		System.out.println("加密后：" + mstr1 );
		System.out.println("解密后：" + astr1 );
		System.out.println("耗时(ms)：" + (System.currentTimeMillis() - start1));
		System.out.println("=========end1==============");
		
		
		System.out.println("=========test2 私钥加密==============");
		long start2 = System.currentTimeMillis();
		String mstr2 = Base64.getEncoder().encodeToString(rsa.encryptByPriKey(str.getBytes(), priKey));
		String astr2 = new String(rsa.decryptByPubKey(Base64.getDecoder().decode(mstr2), pubKey));
		System.out.println("加密前：" + str );
		System.out.println("加密后：" + mstr2 );
		System.out.println("解密后：" + astr2 );
		System.out.println("耗时(ms)：" + (System.currentTimeMillis() - start2));
		System.out.println("=========end2==============");
		

		System.out.println("=========test3  公钥加密==============");
		long start3 = System.currentTimeMillis();
		String mstr3 = Base64.getEncoder().encodeToString(rsa.encryptByPubKeyCipher(str.getBytes(), pubKey));
		String astr3 = new String(rsa.decryptByPriKeyCipher(Base64.getDecoder().decode(mstr3), priKey));
		System.out.println("加密前：" + str );
		System.out.println("加密后：" + mstr3 );
		System.out.println("解密后：" + astr3 );
		System.out.println("耗时(ms)：" + (System.currentTimeMillis() - start3));
		System.out.println("=========end3==============");
		
		System.out.println("=========test4  私钥加密==============");
		long start4 = System.currentTimeMillis();
		String mstr4 = Base64.getEncoder().encodeToString(rsa.encryptByPriKeyCipher(str.getBytes(), priKey));
		String astr4 = new String(rsa.decryptByPubKeyCipher(Base64.getDecoder().decode(mstr4), pubKey));
		System.out.println("加密前：" + str );
		System.out.println("加密后：" + mstr4 );
		System.out.println("解密后：" + astr4 );
		System.out.println("耗时(ms)：" + (System.currentTimeMillis() - start4));
		System.out.println("=========end4==============");
	}
}
