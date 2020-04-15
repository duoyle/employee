package pers.xxm.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by XuXuemin on 20/3/26
 */
public class AesUtil {

    private static final String seed = "dongle|dongle...";
    private static Charset charset = StandardCharsets.UTF_8;

    private static byte[] getSaltKey() {
        try {
            // 1. 构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            // 2. 根据"constant"常数，初始化密钥生成器
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); //生成一个128位的随机源
            random.setSeed(seed.getBytes(charset)); // 盐是随机的，但需每次都随机一样
            keygen.init(128, random); // 128位，16字节
            // 3. 产生原始对称密钥
            SecretKey key = keygen.generateKey();
            return key.getEncoded();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return seed.getBytes(charset); // 出问题后seed作为salt
    }

    /**
     * 对内容进行加密
     * @param source 要加密的明文
     * @return 加密后的密文
     */
    public static String encrypt(String source) {
        if (StringUtil.isEmpty(source)) {
            return source;
        }
        try {
            // salt可从getSalt获得（本质是每次一样的随机数），也可设置一个16字节数组常数。
            Key key = new SecretKeySpec(getSaltKey(), "AES"); // 加点盐，Salt
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 对应算法/模式/补码方式
            // CBC（向量模式）需要第三个参数指定向量，也就是偏移量；ECB是无向量模式
            // new IvParameterSpec("constant_vector".getBytes())指定向量
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(source.getBytes(charset));
            return Base64.encodeBase64String(encrypted); // 使用Base64又加了一次密
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 对密文进行解密
     * @param encrypted 要解密的密文
     * @return 解密后的明文
     */
    public static String decrypt(String encrypted) {
        if (StringUtil.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] aesEncrypted = Base64.decodeBase64(encrypted);
            Key key = new SecretKeySpec(getSaltKey(), "AES"); // 和加密同样的盐
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(aesEncrypted);
            return new String(decrypted, charset); // 使用Base64又加了一次密
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
