package org.developer.wwb.core.utils;


import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * 
 * @author lihao
 */
public class EncryptUtil {
    private static final String RANDOM_STR_BUF = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ0123456789";

    private static final String RANDOM_NUMBER_BUF = "0123456789";

    private static final Pattern UUID_RP = Pattern.compile("[\\-]", Pattern.CASE_INSENSITIVE);
    

    /**
     * generate 32 characters by using MD5 algorithm
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String makeMD5(String value) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(value.getBytes("UTF8"));
        byte s[] = md5.digest();
        return CommonUtil.toHex(s);
    }

    /**
     * generate 40 characters by using SHA algorithm
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String makeSHA(String value) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(value.getBytes("UTF8"));
        byte s[] = sha.digest();
        return CommonUtil.toHex(s);
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return UUID_RP.matcher(uuid).replaceAll("");
    }

    /**
     * 
     * @Title: getRandomString 
     * @Description: 随机生成指定长度的字符串
     * @param length
     * @return String
     */
    public static String getRandomString(int length) {
        int number = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            number = ThreadLocalRandom.current().nextInt(RANDOM_STR_BUF.length());
            sb.append(RANDOM_STR_BUF.charAt(number));
        }
        return sb.toString();
    }

    /**
     *
     * @Title: getRandomString
     * @Description: 随机生成指定长度的字符串
     * @param length
     * @return String
     */
    public static String getRandomNumString(int length) {
        int number = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            number = ThreadLocalRandom.current().nextInt(RANDOM_NUMBER_BUF.length());
            sb.append(RANDOM_NUMBER_BUF.charAt(number));
        }
        return sb.toString();
    }


}
