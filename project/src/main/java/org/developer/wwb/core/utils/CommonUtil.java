package org.developer.wwb.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.developer.wwb.core.constants.AuthConstants;

import net.sf.json.JSONObject;

public class CommonUtil {
	private static final Logger log = Logger.getLogger(CommonUtil.class);
	public static final String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * close IO object don't throw exception
	 *
	 * @param obj
	 */
	public static void closeIO(Object obj) {
		if (obj == null)
			return;

		try {
			if (obj instanceof InputStream)
				((InputStream) obj).close();
			else if (obj instanceof OutputStream)
				((OutputStream) obj).close();
			else if (obj instanceof Reader)
				((Reader) obj).close();
			else if (obj instanceof Writer)
				((Writer) obj).close();
			else if (obj instanceof FileChannel)
				((FileChannel) obj).close();
			else if (obj instanceof Connection)
				((Connection) obj).close();
			else if (obj instanceof Statement)
				((Statement) obj).close();
			else if (obj instanceof PreparedStatement)
				((PreparedStatement) obj).close();
			else if (obj instanceof ResultSet)
				((ResultSet) obj).close();
		} catch (Exception exp) {
			log.warn("close io object meet error:" + exp.getMessage());
		}
	}

	/**
	 * transmit special file to target output stream
	 *
	 * @param filePath
	 * @param out
	 * @param gzip
	 *            indicate whether to compress file during transmit
	 * @throws Exception
	 */
	public static void transmitFile(String filePath, OutputStream out, boolean gzip) throws Exception {
		InputStream src = null;
		OutputStream dest = null;

		try {
			src = new BufferedInputStream(new FileInputStream(filePath));
			if (gzip)
				dest = new GZIPOutputStream(out, 1280 * 1024);
			else
				dest = out;

			byte[] buff = new byte[1024 * 1024];
			int len = 0;
			while ((len = src.read(buff)) > 0) {
				dest.write(buff, 0, len);
			}

			if (dest instanceof GZIPOutputStream)
				((GZIPOutputStream) dest).finish();
			dest.flush();
		} catch (Exception exp) {
			throw exp;
		} finally {
			closeIO(src);
			closeIO(dest);
		}
	}

	/**
	 * convert texture ip to byte array
	 *
	 * @param ip
	 * @return
	 */
	public static byte[] toIPBytes(String ip) {
		if (ip == null)
			return null;

		String[] tmp = ip.split("\\.");
		if (tmp.length != 4)
			return null;
		byte[] addr = new byte[4];
		for (int i = 0; i < tmp.length; i++)
			addr[i] = (byte) Short.parseShort(tmp[i]);
		return addr;
	}

	/**
	 * convert byte array to HEX string
	 *
	 * @param data
	 * @return
	 */
	public static String toHex(byte[] data) {
		return toHex(data, false);
	}

	/**
	 * Convert byte array to Hex code
	 * 
	 * @param data
	 * @param isFormat
	 *            format output string: 16 byte one line, one byte is separted
	 *            by space
	 * @return
	 */
	public static String toHex(byte[] data, boolean isFormat) {
		if (data == null || data.length == 0)
			return "";

		int tmp;
		int s1, s2;
		StringBuilder sb = new StringBuilder(data.length * 2 + (isFormat ? data.length + data.length / 16 : 0));
		for (int i = 0; i < data.length; i++) {
			tmp = data[i] & 0x000000FF;
			s1 = tmp / 16;
			s2 = tmp % 16;
			if (s1 < 10)
				sb.append((char) (s1 + 48));
			else if (s1 >= 10)
				sb.append((char) (s1 + 55));
			if (s2 < 10)
				sb.append((char) (s2 + 48));
			else if (s2 >= 10)
				sb.append((char) (s2 + 55));

			if (isFormat) {
				sb.append(" ");

				if ((i + 1) % 16 == 0)
					sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Convert Hex string to byte array hex string format is: 2 char represent
	 * one byte,can use space as separator for example, "12 1e 3d ee FF 09"
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] toBytes(String hex) {
		byte[] buff = new byte[hex.length() / 2];

		int s1, s2, count = 0;
		for (int i = 0; i < hex.length(); i++) {
			if (hex.charAt(i) >= '0' && hex.charAt(i) <= '9')
				s1 = hex.charAt(i) - 48;
			else if (hex.charAt(i) >= 'A' && hex.charAt(i) <= 'F')
				s1 = hex.charAt(i) - 55;
			else if (hex.charAt(i) >= 'a' && hex.charAt(i) <= 'f')
				s1 = hex.charAt(i) - 87;
			else
				continue;

			i++;
			if (hex.charAt(i) >= '0' && hex.charAt(i) <= '9')
				s2 = hex.charAt(i) - 48;
			else if (hex.charAt(i) >= 'A' && hex.charAt(i) <= 'F')
				s2 = hex.charAt(i) - 55;
			else if (hex.charAt(i) >= 'a' && hex.charAt(i) <= 'f')
				s2 = hex.charAt(i) - 87;
			else
				continue;

			buff[count] = ((byte) (s1 * 16 + s2));
			count++;
		}
		byte[] result = new byte[count];
		System.arraycopy(buff, 0, result, 0, result.length);
		return result;
	}

	public static long getLong(Object value, long def) {
		if (value == null)
			return def;
		else if (value instanceof Integer)
			return ((Integer) value).longValue();
		else if (value instanceof Long)
			return ((Long) value).longValue();
		if (value instanceof BigInteger)
			return ((BigInteger) value).longValue();
		else if (value instanceof BigDecimal)
			return ((BigDecimal) value).longValue();
		else
			return def;
	}

	public static double getDouble(Object value, double def) {
		if (value == null)
			return def;
		else if (value instanceof Double)
			return ((Double) value).doubleValue();
		else if (value instanceof Float)
			return ((Float) value).doubleValue();
		else if (value instanceof Integer)
			return ((Integer) value).doubleValue();
		else if (value instanceof Long)
			return ((Long) value).doubleValue();
		if (value instanceof BigInteger)
			return ((BigInteger) value).doubleValue();
		else if (value instanceof BigDecimal)
			return ((BigDecimal) value).doubleValue();
		else
			return def;
	}

	public static Map<String, String> standardCidrAndMask(String cidr) {
		String[] ipArr = cidr.split("/")[0].split("\\.");
		int num = Integer.parseInt(cidr.split("/")[1]);

		int a = num / 8;
		int b = num % 8;
		int[] maskArr = new int[4];
		int i = 0;
		if (a > 0) {
			for (i = 0; i < a; i++) {
				maskArr[i] = (int) (Math.pow(2.0, 8.0) - 1);
			}
			maskArr[i] = (int) (Math.pow(2.0, 8.0) - Math.pow(2.0, 8.0 - b));
		} else {
			maskArr[i] = (int) (Math.pow(2.0, 8.0) - Math.pow(2.0, 8.0 - b));
		}
		StringBuffer cidrBuffer = new StringBuffer();
		StringBuffer maskBuffer = new StringBuffer();
		for (int j = 0; j < ipArr.length; j++) {
			ipArr[j] = Objects.toString((Integer.parseInt(ipArr[j]) & maskArr[j]));
			System.out.println(ipArr[j]);
			cidrBuffer.append(ipArr[j] + ".");
			maskBuffer.append(maskArr[j] + ".");
		}
		cidr = cidrBuffer.replace(cidrBuffer.lastIndexOf("."), cidrBuffer.length(), "/" + num).toString();
		String mask = maskBuffer.substring(0, maskBuffer.length() - 1);
		Map<String, String> result = new HashMap<String, String>();
		result.put("cidr", cidr);
		result.put("mask", mask);

		return result;
	}

	public static String standardCidr(String cidr) {
		Map<String, String> cidrAndMask = standardCidrAndMask(cidr);
		return cidrAndMask.get("cidr");
	}

	public static String getMask(String cidr) {
		Map<String, String> cidrAndMask = standardCidrAndMask(cidr);
		return cidrAndMask.get("mask");
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	@SuppressWarnings("rawtypes")
	public static Object convertMap(Class type, Map map)
			throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

	/**
	 * 
	 * 判断字符串非空
	 * 
	 * @param str
	 * @return boolean
	 * @exception @since
	 *                1.0.0
	 */
	public static boolean isNotBlank(String str) {
		return !CommonUtil.isBlank(str);
	}

	/**
	 * 
	 * 判断字符串为 null 或为 “”
	 * 
	 * @param str
	 * @return boolean
	 * @exception @since
	 *                1.0.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 复制文件到指定目录
	 * 
	 * @param oldPath
	 *            原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            复制后路径 如：f:/test.txt
	 */
	@SuppressWarnings("resource")
	public static void copyFile(InputStream inStream, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行脚本命令
	 * 
	 * @param cmdArray
	 * @return
	 */
	public static String executeCommand(String... cmdArray) {
		Process process = null;
		ProcessBuilder pb = new ProcessBuilder(cmdArray);
		try {
			process = pb.start();
		} catch (IOException e) {
			log.error("ProcessBuilder error:\n" + e.getMessage());
		}
		InputStream input = process.getInputStream();
		BufferedReader buff = new BufferedReader(new InputStreamReader(input));
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = buff.readLine()) != null) {
				sb.append(line);
			}
			buff.close();  
			input.close();  
		} catch (IOException e) {
			log.error("Read error:\n" + e.getMessage());
		}
		return sb.toString();
	}
	
	/**
	 * 文件下载
	 * @param path
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static HttpServletResponse download(String path, HttpServletResponse response) throws IOException {
        try {
            File file = new File(path);
            String filename = file.getName();
            
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
        return response;
    }
	
	/**
	 * save uploaded file to a defined location on the server
	 * 
	 * @param uploadedInputStream
	 * @param serverLocation
	 * @throws YanException
	 */
	public static void saveFile(InputStream inputStream, String fileFullName) throws Exception {
		OutputStream outputStream = null;
		try {
			File file = new File(fileFullName);
			if (file.exists()) {
				file.delete();
			}
			int read = 0;
			byte[] bytes = new byte[1024];

			outputStream = new FileOutputStream(new File(fileFullName));
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			log.error("Failed to recieve file: " + fileFullName, e);
			throw new RuntimeException("Failed to recieve file");
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}
	}
	
	/**
	 * 修改 static final 修饰的常量值
	 * @param field
	 * @param newValue
	 * @throws Exception
	 */
	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}
	
	public static String readFile(String fileName) throws Exception {
		InputStream fip = CommonUtil.class.getClassLoader().getResourceAsStream(fileName);
		InputStreamReader isr = new InputStreamReader(fip, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);

		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append("\n");
		}
		reader.close();
		isr.close();
		fip.close();
		return builder.toString();
	}
	/**
	 * 解密token，分离出username和originToken
	 * @param token
	 * @return
	 * @throws YanException
	 */
	public static JSONObject decrypToken(String token) throws Exception {
		JSONObject json=new JSONObject();
		if (StringUtils.isBlank(token)) {
			log.error("Token is missing......");
			json.put("status", "fail");
			json.put("msg","Token is missing");
			return json;
		}
		try {
			DesUtils desUtils = new DesUtils(AuthConstants.DES_KEY);
			String tokens = new String(desUtils.decrypt(Base64.decodeBase64(token.getBytes())), "utf-8");
			json.put("status","ok");
			json.put("data", tokens);
			return json;
		} catch (Exception e) {
			log.error("Invalid token......", e);
			json.put("status","fail");
			json.put("msg","token error");
			return json;
		}
	}
	
}
