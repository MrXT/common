package com.project.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.project.common.exception.ServerException;

/**
 * 编码解码工具（md5,base64,url）
 * @author XT
 * @version 2013-05
 */
public class EncodeDecodeUtils {

    private static transient Log logger = LogFactory.getLog(EncodeDecodeUtils.class);

    public static String SUN_PROVIDER = "SunJCE";

    public static String BOUNCY_CASTLE_PROVIDER = "BC";

    public static String TRIPPLEDES_ALGORITHM = "DESede";

    public static String DES_ALGORITHM = "DES";

    public static String NORMAL_TRANSFER_TYPE = "DES/CBC/PKCS5Padding";

    public static String TRIPLEDESPKCS7_TRANSFER_TYPE = "DESede/CBC/PKCS7Padding";

    public static String DEFAULT_PROVIDER = SUN_PROVIDER;

    public static String DEFAULT_DES_ALGORITHM = DES_ALGORITHM;

    public static String DEFAULT_TRANSFER_TYPE = NORMAL_TRANSFER_TYPE;

    public static String DEFAULT_IV = "0102030405060708";

    /**
     * 生成DES密钥("DES").
     * @param key_byte - seed key
     * @return
     * @throws Exception
     */
    public static SecretKey genDefaultDESKey(byte[] key_byte) throws Exception {
        return genDESKey(key_byte, null);
    }

    /**
     * 生成DES密钥.
     * @param key_byte - seed key
     * @param algorthm - 缺省为"DES"
     * @return
     * @throws Exception
     */
    public static SecretKey genDESKey(byte[] key_byte, String algorthm) throws Exception {
        if (StringUtils.isEmpty(algorthm))
            algorthm = DEFAULT_DES_ALGORITHM;
        if (algorthm.equalsIgnoreCase(DES_ALGORITHM) && key_byte.length != 16 || algorthm.equalsIgnoreCase(TRIPPLEDES_ALGORITHM)
            && key_byte.length != 48) {
            throw new IllegalArgumentException("key length error");
        }
        return new SecretKeySpec(key_byte, algorthm);
    }

    /**
     * 根据提供商、key、算法、传输模式得到cipher.
     * @param provider - 加解密类库提供商 缺省为 SunJCE
     * @param keyAlgorthm - key算法 缺省为 DES
     * @param key_byte - seed key 必填
     * @param transferType - 传输模式 缺省为 DES/CBC/PKCS5Padding
     * @param iv - 初始化向量 - 初始化向量
     * @param cryptMode - 加解密模式 必填
     * @return
     * @throws Exception
     */
    public static Cipher getDesCihper(String provider, String keyAlgorthm, byte[] key_byte, String transferType, byte iv[], int cryptMode)
        throws Exception {
        Cipher cipher = null;
        SecretKey key = null;
        if (key_byte == null || !(key_byte.length > 0) || cryptMode != 1 && cryptMode != 2)
            throw new IllegalArgumentException("key or cryptMode error");
        if (StringUtils.isNotEmpty(keyAlgorthm)) {
            key = genDESKey(key_byte, keyAlgorthm);
        } else {
            key = genDefaultDESKey(key_byte);
        }
        if (StringUtils.isNotEmpty(transferType)) {
            cipher = Cipher.getInstance(transferType, provider);
        } else {
            cipher = Cipher.getInstance(DEFAULT_TRANSFER_TYPE, provider);
        }
        if (iv != null && iv.length > 0) {
            cipher.init(cryptMode, key, new IvParameterSpec(iv));
        } else {
            cipher.init(cryptMode, key);
        }
        return cipher;
    }

    /**
     * 得到 SunJCE DES/CBC/PKCS5Padding 初始化向量为空 的cipher.
     * @param key_byte - seed key
     * @param cryptMode - 加解密模式
     * @return
     * @throws Exception
     */
    public static Cipher getDefaultDesCihper(byte[] key_byte, int cryptMode) throws Exception {
        return getDesCihper(DEFAULT_PROVIDER, null, key_byte, null, null, cryptMode);
    }

    /**
     * des encrypt(byte[]).
     * @param keyAlgorthm - key算法 缺省为 DES
     * @param key_byte - seed key 必填
     * @param transferType - 传输模式 缺省为 DES/CBC/PKCS5Padding
     * @param src - 所加密的内容 byte[]
     * @param iv - 初始化向量
     * @return
     * @throws Exception
     */
    public static byte[] desEncrypt(String keyAlgorthm, byte[] key_byte, String transferType, byte[] src, byte iv[]) throws Exception {
        Cipher cipher = getDesCihper(DEFAULT_PROVIDER, keyAlgorthm, key_byte, transferType, iv, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(src);
    }

    /**
     * des encrypt(String).
     * @param keyAlgorthm - key算法 缺省为 DES
     * @param key_byte - seed key 必填
     * @param transferType - 传输模式 缺省为 DES/CBC/PKCS5Padding
     * @param src - 所加密的内容 String
     * @param iv - 初始化向量
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String keyAlgorthm, byte[] key_byte, String transferType, String src, byte iv[]) throws Exception {
        return new String(desEncrypt(keyAlgorthm, key_byte, transferType, src.getBytes(), iv));
    }

    /**
     * des decrypt(byte[]).
     * @param keyAlgorthm - key算法 缺省为 DES
     * @param key_byte - seed key 必填
     * @param transferType - 传输模式 缺省为 DES/CBC/PKCS5Padding
     * @param src - 所解密的内容 byte[]
     * @param iv - 初始化向量
     * @return
     * @throws Exception
     */
    public static byte[] desDecrypt(String keyAlgorthm, byte[] key_byte, String transferType, byte[] src, byte iv[]) throws Exception {
        Cipher cipher = getDesCihper(DEFAULT_PROVIDER, keyAlgorthm, key_byte, transferType, iv, Cipher.DECRYPT_MODE);
        return cipher.doFinal(src);
    }

    /**
     * des decrypt(String).
     * @param keyAlgorthm - key算法 缺省为 DES
     * @param key_byte - seed key 必填
     * @param transferType - 传输模式 缺省为 DES/CBC/PKCS5Padding
     * @param src - 所解密的内容 String
     * @param iv - 初始化向量
     * @return
     * @throws Exception
     */
    public static String desDecrypt(String keyAlgorthm, byte[] key_byte, String transferType, String src, byte iv[]) throws Exception {
        return new String(desDecrypt(keyAlgorthm, key_byte, transferType, src.getBytes(), iv));
    }

    /**
     * MD5 摘要计算(byte[]).
     * @param src byte[]
     * @throws Exception
     * @return byte[] 16 bit digest
     */
    public static String md5Digest(byte[] src) {
        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ServerException(e);
            
        } // MD5 is 16 bit
                                                              // digest
        alg.update(src);
        byte b[] = alg.digest();

        int i;

        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * MD5 摘要计算(String).
     * @param src String
     * @throws Exception
     * @return String
     */
    public static String md5Digest(String src) throws Exception {
        return new String(md5Digest(src.getBytes()));
    }

    /**
     * SHA-1 摘要计算(byte[]).
     * @param src byte[]
     * @return Exception
     * @throws Exception
     */
    public static byte[] sha1Digest(byte[] src) throws Exception {
        MessageDigest alg = MessageDigest.getInstance("SHA-1");
        alg.update(src);
        return alg.digest();
    }

    /**
     * SHA-1 摘要计算(String).
     * @param src String
     * @return Exception
     * @throws Exception
     */
    public static String sha1Digest(String src) throws Exception {
        return new String(EncodeDecodeUtils.sha1Digest(src.getBytes()));
    }

    /**
     * BASE64 编码(String).
     * @param src String inputed string
     * @return String returned string
     */
    public static String base64Encode(String src) {
        return base64Encode(src.getBytes());
    }

    /**
     * BASE64 编码(byte[]).
     * @param src byte[] inputed string
     * @return String returned string
     */
    public static String base64Encode(byte[] src) {
        return new Base64().encodeAsString(src);
    }

    /**
     * BASE64 解码(String).
     * @param src String inputed string
     * @return String returned string
     */
    public static String base64Decode(String src) {
        Base64 decoder = new Base64();
        String str = null;
        try {
            str = new String(decoder.decode(src));
        } catch (Exception ex) {
            logger.error("decode password is failed", ex);
        }
        return str;
    }

    /**
     * BASE64 解码(byte[]).
     * @param src String inputed string
     * @return String returned string
     */
    public static byte[] base64DecodeToBytes(String src) {
        Base64 decoder = new Base64();
        try {
            return decoder.decode(src);
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw ex;
        }

    }

    /**
     * 对给定字符进行 URL 编码(GB2312).
     * @param src String
     * @return String
     */
    public static String urlEncode(String src) throws Exception {
        try {
            return URLEncoder.encode(src, "GB2312");
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw ex;
        }
    }

    /**
     * 对给定字符进行 URL 解码(GB2312).
     * @param src String
     * @return String
     */
    public static String urlDecode(String src) throws Exception {
        try {
            return URLDecoder.decode(src, "GB2312");
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw ex;
        }
    }

    /**
     * byte数组=>字符串(加解密时有些特殊情况下getByte不能得到正确结果时使用) 把byte数组转换成非乱码可见的字符串
     * 例如，代表byte数组{0x11,0x12,0x13}的字符串为"111213"
     * 只能与hex2byte()配套使用，不可与getByte()配套使用
     * @param src 所要转换的byte数组
     * @return
     */
    public static String byte2hex(byte[] src) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < src.length; n++) {
            stmp = (java.lang.Integer.toHexString(src[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * 字符串=>byte数组(加解密时有些特殊情况下new String不能得到正确结果时使用) 把代表byte数组的字符串转换成byte数组
     * 例如，字符串为"111213"转换为byte数组{0x11,0x12,0x13} 只能与byte2hex()配套使用，不能与new
     * String(..)配套使用
     * @param src 所要转换的字符串
     * @return
     * @throws IllegalArgumentException
     */
    public static byte[] hex2byte(String src) throws Exception {
        if (src.length() % 2 != 0) {
            throw new IllegalArgumentException("The input string lengh must be an even.");
        }
        char[] arr = src.toCharArray();
        byte[] b = new byte[src.length() / 2];
        for (int i = 0, j = 0, l = src.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    public static String encodeStringToUnicode(String str) {
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);

        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c > 255) {
                sb.append("\\u");
                j = (c >>> 8);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
                j = (c & 0xff);
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
            } else {
                sb.append(c);
            }

        }
        return (new String(sb));
    }

    public static String encodeStringToHtmlUnicode(String str) {

        if (str == null || str.length() < 1)
            return str;

        StringBuffer sb = new StringBuffer(20);
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c > 255) {
                sb.append("&#");
                sb.append(c & 0xffff);
                sb.append(";");
            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

}
