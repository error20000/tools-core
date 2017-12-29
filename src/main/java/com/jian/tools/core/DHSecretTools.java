package com.jian.tools.core;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DHSecretTools {

	private static String g = "235";
	private static String p = "58712468451231546546845131315464131313454131345468431";
	
	/**
	 * 获取密钥，随机数。
	 * @param g 公开的参数， 一个整数g，g是p的一个原根。
	 * @param p	公开的参数，一个素数p。
	 * @return map 失败 null，成功	secretKey： 密钥，random：随机数
	 */
	public static Map<String, String> getSecretKey(String g, String p) {
		if(Tools.isNullOrEmpty(g) || Tools.isNullOrEmpty(p)){
			return null;
		}
		BigInteger big_g = new BigInteger(g); // 公钥: 一个整数a,a是q的一个原根.
		BigInteger big_p = new BigInteger(p); // 公钥: 一个素数q

		String random = System.currentTimeMillis() + "" + new Random().nextInt(10000); //XB私有的随机数
		BigInteger big_a = new BigInteger(random);

		BigInteger k = big_g.modPow(big_a, big_p); // 密钥YA: YA=a^XA mod q, XB私有的随机数(XB<q)
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("secretKey", k.toString()); // 密钥
		map.put("random", random); // 密码
		return map;
	}
	
	/**
	 * 获取密钥，密码。
	 * @param g 公开的参数， 一个整数g，g是p的一个原根。
	 * @param p	公开的参数，一个素数p。
	 * @param k	密钥，YA=g^XA mod p。
	 * @return map 失败 null，成功	secretKey： 密钥，secret：密码
	 */
	public static Map<String, String> getSecret(String g, String p, String k) {
		if(Tools.isNullOrEmpty(g) || Tools.isNullOrEmpty(p) || Tools.isNullOrEmpty(k)){
			return null;
		}
		BigInteger big_g = new BigInteger(g); // 公钥: 一个整数a,a是q的一个原根.
		BigInteger big_p = new BigInteger(p); // 公钥: 一个素数q
		BigInteger big_k = new BigInteger(k); // 密钥YA: YA=a^XA mod q, XA私有的随机数(XA<q).  密码：K = (YB)^XA mod q

		String random = System.currentTimeMillis() + "" + new Random().nextInt(10000); //XB私有的随机数
		BigInteger big_a = new BigInteger(random);

		BigInteger sk = big_g.modPow(big_a, big_p); // 密钥YB: YB=a^XB mod q, XB私有的随机数(XB<q)
		BigInteger ss = big_k.modPow(big_a, big_p); // 密码: K = (YA)^XB mod q
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("secretKey", sk.toString()); // 密钥
		map.put("secret", ss.toString()); // 密码
		return map;
	}
	
	/**
	 * 获取密钥，密码。
	 * @param g 公开的参数， 一个整数g，g是p的一个原根。
	 * @param p	公开的参数，一个素数p。
	 * @param k	密钥。
	 * @param r	产出密钥的随机数。
	 * @return string 失败 null，成功	密码
	 */
	public static String getSecret(String g, String p, String k, String r) {
		if(Tools.isNullOrEmpty(g) || Tools.isNullOrEmpty(p) || Tools.isNullOrEmpty(k) || Tools.isNullOrEmpty(r)){
			return null;
		}
		BigInteger big_p = new BigInteger(p); // 公钥: 一个素数q
		BigInteger big_k = new BigInteger(k); // 密钥
		BigInteger big_r = new BigInteger(r); // 随机数

		BigInteger s = big_k.modPow(big_r, big_p); // 密码: K = (YA)^XB mod q
		
		return s.toString();
	}
	
	public static boolean isPrime(long n) {
	       
        if(n > 2 && (n & 1) == 0)
           return false;
        /* 运用试除法:
         * 1.只有奇数需要被测试
         * 2.测试范围从2与根号{n},反之亦然 */
        for(int i = 3; i * i <= n; i += 2)
            if (n % i == 0) 
                return false;
        return true;
    }

	public static void main(String[] args) {
		
		BigInteger big_g = new BigInteger(g); 
		BigInteger big_p = new BigInteger(p);
		
		String rm1 = System.currentTimeMillis() + "" + new Random().nextInt(10000); //XB私有的随机数
		BigInteger big_a = new BigInteger(rm1);
		BigInteger r = big_g.modPow(big_a, big_p);
		System.out.println(r.toString());
		

		String rm2 = System.currentTimeMillis() + "" + new Random().nextInt(10000); //XB私有的随机数
		BigInteger big_a2 = new BigInteger(rm2);
		BigInteger r2 = big_g.modPow(big_a2, big_p);
		System.out.println(r2.toString());
		
		BigInteger s1 = r2.modPow(big_a, big_p);
		System.out.println(s1.toString());
		
		BigInteger s2 = r.modPow(big_a2, big_p);
		System.out.println(s2.toString());
		
		
		Map<String, String> map1 = DHSecretTools.getSecretKey(g, p);
		Map<String, String> map2 = DHSecretTools.getSecret(g, p, map1.get("secretKey"));
		System.out.println(map1);
		System.out.println(map2);
		System.out.println(DHSecretTools.getSecret(g, p, map2.get("secretKey"), map1.get("random")));
        
		
	}
	
}
