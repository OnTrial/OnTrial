package org.developer.wwb.core.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

public class DesUtils {

	private Cipher encryptCipher;
	private Cipher decryptCipher;
	private final String ALGORITHM_DES = "DES";

	/**
	 * constructor
	 * 
	 * @param keyStr
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	public DesUtils(String keyStr) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Key key = getKey(keyStr);
		// 返回实现指定转换的 Cipher 对象
		this.encryptCipher = Cipher.getInstance(ALGORITHM_DES);
		this.decryptCipher = Cipher.getInstance(ALGORITHM_DES);
		// 用密钥初始化 Cipher
		this.encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		this.decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	/**
	 * 构造密钥
	 * 
	 * @param keyStr
	 * @return
	 */
	private Key getKey(String keyStr) {
		byte[] keyBytes = keyStr.getBytes();
		byte[] bytes = new byte[8];
		for (int i = 0; (i < keyBytes.length) && (i < bytes.length); i++) {
			bytes[i] = keyBytes[i];
		}
		// 根据给定的字节数组构造一个密钥
		Key key = new SecretKeySpec(bytes, ALGORITHM_DES);
		return key;
	}

	/**
	 * 加密数据
	 * 
	 * @param bytes
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] encrypt(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
		// 按单部分操作加密数据
		return this.encryptCipher.doFinal(bytes);
	}

	/**
	 * 加密数据
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String str) throws Exception {
		return byteArrToHexStr(encrypt(str.getBytes("utf-8")));
	}

	/**
	 * 解密数据
	 * 
	 * @param bytes
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] decrypt(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
		// 按单部分操作解密数据
		return decryptCipher.doFinal(bytes);
	}

	/**
	 * 解密数据
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String str) throws Exception {
		return new String(decrypt(hexStrToByteArr(str)));
	}

	/**
	 * transform byte array into hexadecimal string
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private static String byteArrToHexStr(byte[] bytes) throws Exception {
		if (bytes == null || bytes.length == 0) {
			throw new Exception("The byte array is empty.");
		}
		int len = bytes.length;
		StringBuffer sb = new StringBuffer(len * 2);
		for (int i = 0; i < len; i++) {
			int tmp = bytes[i];
			while (tmp < 0) {
				tmp += 256;
			}
			if (tmp < 16) {
				sb.append("0");
			}
			// 转为16进制字符串
			sb.append(Integer.toString(tmp, 16));
		}
		return sb.toString();
	}

	/**
	 * transform hexadecimal string into byte array
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private static byte[] hexStrToByteArr(String hexStr) throws Exception {
		if (StringUtils.isEmpty(hexStr)) {
			throw new Exception("The hexadecimal string is empty.");
		}
		byte[] bytes = hexStr.getBytes("utf-8");
		int len = bytes.length;
		byte[] result = new byte[len / 2];
		for (int i = 0; i < len; i = i + 2) {
			String tmp = new String(bytes, i, 2);
			result[i / 2] = (byte) Integer.parseInt(tmp, 16);
		}
		return result;
	}
	
	public static void main(String[] args) {
//		DesUtils des;
//		try {
//			des = new DesUtils(AuthConstants.DES_KEY);
////			//加密
////			System.out.println(des.encrypt("你好达到了"));
//			//解密
//			byte[] ss = des.decrypt(Base64.decodeBase64("X5TkReb4RGIqIiU_VzTTbA43Zdgq3Ukk6tvx64L4buVINFSrEmYOr62Ft7pDqBCC"));
//			System.out.println(new String(ss));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	   
	}
}
