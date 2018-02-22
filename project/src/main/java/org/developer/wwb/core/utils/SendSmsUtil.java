package org.developer.wwb.core.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@PropertySource("classpath:application.properties")
@Component
public class SendSmsUtil {
	//@Value("${smsSvcUrl}")
	protected String smsSvcUrl="http://115.29.47.151:8860";
	//@Value("${smsSvcCode}")
	protected String smsSvcCode="600031";
	//@Value("${smsSvcPassword}")
	protected String smsSvcPassword="R4ZHW53WD9";

	public int sendSms(String mobiles, String content) throws IOException {
		return sendSms(mobiles, content, "", 0);
	}

	public void sendSms(String mobiles, String content, long task_id)
			throws IOException {
		sendSms(mobiles, content, "", task_id);
	}

	public void sendSms(String mobiles, String content, String sp_code)
			throws IOException {
		sendSms(mobiles, content, sp_code, 0);
	}

	public int sendSms(String mobiles, String content, String sp_code,
			long task_id) throws IOException {
		String urlencContent = URLEncoder.encode(content,"utf-8");
		String sign = getMD5((urlencContent + smsSvcPassword));

		String postData = "content=" + urlencContent + "&destMobiles="
				+ mobiles + "&sign=" + sign + "&cust_code=" + smsSvcCode
				+ "&sp_code=" + sp_code + "&task_id=" + task_id;

		
		URL myurl = new URL(smsSvcUrl);
		URLConnection urlc = myurl.openConnection();
		urlc.setReadTimeout(1000 * 60);
		urlc.setConnectTimeout(1000) ;
		urlc.setDoOutput(true);
		urlc.setDoInput(true);
		urlc.setAllowUserInteraction(false);

		DataOutputStream server = new DataOutputStream(urlc.getOutputStream());
		System.out.println("发送数据=" + postData);
		int len = postData.getBytes("utf-8").length ;
		server.write(postData.getBytes("utf-8"),0,len);
		server.flush();
		server.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlc.getInputStream(), "utf-8"));
		String resXml = "", s = "";
		while ((s = in.readLine()) != null)
			resXml = resXml + s + "\r\n";
		in.close();
		System.out.println("接收数据=" + resXml);
		resXml=resXml.substring(resXml.lastIndexOf(":")+1).trim();
		System.out.println(resXml);
		if (resXml.equals("0")) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	public String getMD5(String source) {
		String md5Str = null;
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
		try {
			MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
			messageDigest.update( source.getBytes());
			byte tmp[] = messageDigest.digest();         // MD5 的计算结果是一个128位的长整数，用字节表示就是16个字节
			char str[] = new char[16 * 2];               // 每个字节用16进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
			int k = 0;                                   // 表示转换结果中对应的字符位置
			/** 从第一个字节开始，对 MD5 的每一个字节，转换成 16 进制字符的转换 */
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];                     // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];       // 取字节中低 4 位的数字转换
			}
			md5Str = new String(str);                         // 换后的结果转换为字符串
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	
	public static void main(String[] args) {
/*,18859231888,18659267733*/
		SendSmsUtil client = new SendSmsUtil();
		try {
			client.sendSms("13859920465", "验证码为  收到请回复 请在注册页面中输入已完成注册【姚明信息】");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
