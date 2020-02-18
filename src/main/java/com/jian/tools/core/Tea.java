package com.jian.tools.core;

/** */
/**
 * Tea算法 每次操作可以处理8个字节数据 KEY为16字节,应为包含4个int型数的int[]，一个int为4个字节
 * 加密解密轮数应为8的倍数，推荐加密轮数为64轮
 */
public class Tea {
	// 加密
	public static byte[] encrypt(byte[] content, int offset, int[] key, int times) {// times为加密轮数
		int[] tempInt = byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			sum += delta;
			y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0] = y;
		tempInt[1] = z;
		return intToByte(tempInt, 0);
	}

	// 解密
	public static byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times) {
		int[] tempInt = byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0xC6EF3720, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
			y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return intToByte(tempInt, 0);
	}

	// byte[]型数据转成int[]型数据
	private static int[] byteToInt(byte[] content, int offset) {

		int[] result = new int[content.length >> 2]; // 除以2的n次方 == 右移n位 即  content.length / 4 ==  content.length >> 2
		for (int i = 0, j = offset; j < content.length; i++, j += 4) {
			result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16
					| (int) content[j] << 24;
		}
		return result;

	}

	// int[]型数据转成byte[]型数据
	private static byte[] intToByte(int[] content, int offset) {
		byte[] result = new byte[content.length << 2]; // 乘以2的n次方 == 左移n位 即  content.length * 4 == content.length << 2
		for (int i = 0, j = offset; j < result.length; i++, j += 4) {
			result[j + 3] = (byte) (content[i] & 0xff);
			result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
			result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
			result[j] = (byte) ((content[i] >> 24) & 0xff);
		}
		return result;
	}

	// 若某字节被解释成负的则需将其转成无符号正数
	private static int transform(byte temp) {
		int tempInt = (int) temp;
		if (tempInt < 0) {
			tempInt += 256;
		}
		return tempInt;
	}
	
	// 通过TEA算法加密信息
	public static byte[] encryptByTea(String info, int[] KEY) {
		byte[] temp = info.getBytes();
		int n = 8 - temp.length % 8; // 若temp的位数不足8的倍数,需要填充的位数
		byte[] encryptStr = new byte[temp.length + n];
		encryptStr[0] = (byte) n;
		System.arraycopy(temp, 0, encryptStr, n, temp.length);
		byte[] result = new byte[encryptStr.length];
		for (int offset = 0; offset < result.length; offset += 8) {
			byte[] tempEncrpt = Tea.encrypt(encryptStr, offset, KEY, 32);
			System.arraycopy(tempEncrpt, 0, result, offset, 8);
		}
		return result;
	}
	
	// 通过TEA算法解密信息
	public static String decryptByTea(byte[] secretInfo, int[] KEY) {
		byte[] decryptStr = null;
		byte[] tempDecrypt = new byte[secretInfo.length];
		for (int offset = 0; offset < secretInfo.length; offset += 8) {
			decryptStr = Tea.decrypt(secretInfo, offset, KEY, 32);
			System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
		}
		int n = tempDecrypt[0];
		return new String(tempDecrypt, n, decryptStr.length - n);
	}

	public static void main(String[] args) {

		int[] KEY = new int[] { // 加密解密所用的KEY
				0x789f5645, 0xf68bd5a4, 0x81963ffa, 0x458fac58 };
		Tea tea = new Tea();

		byte[] info = new byte[] {

				1, 2, 3, 4, 5, 6, 7, 8 };
		System.out.print("原数据：");
		for (byte i : info)
			System.out.print(i + " ");
		System.out.println();

		byte[] secretInfo = encryptByTea(new String(info), KEY);
		System.out.print("加密后的数据：");
		for (byte i : secretInfo)
			System.out.print(i + " ");
		System.out.println();

		byte[] decryptInfo = decryptByTea(secretInfo, KEY).getBytes();
		System.out.print("解密后的数据：");
		for (byte i : decryptInfo)
			System.out.print(i + " ");
		
		/*String info = "www.blogjava.net/orangehf";
		System.out.println("原数据：" + info);


		byte[] encryptInfo = encryptByTea(info, KEY);
		System.out.print("加密后的数据：");
		for(byte i : encryptInfo)
		System.out.print(i + " ");
		System.out.println();

		String decryptInfo = decryptByTea(encryptInfo, KEY);
		System.out.print("解密后的数据：");
		System.out.println(decryptInfo);*/

		System.out.println();
		System.out.println(Integer.toBinaryString(22222));
		System.out.println(Float.floatToIntBits(12.3f));
	}
}