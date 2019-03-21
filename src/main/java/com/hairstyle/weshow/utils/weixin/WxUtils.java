package com.hairstyle.weshow.utils.weixin;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * 微信工具类
 */
public class WxUtils {

    public static void main(String[] args) {
        String result = decryptData(
                "0MftPvgsLcurmhJIvZzSNYfuqsbzUlrHbbhMGsaMd32fZ+Ccq7tqg/ZQOvwHRDOQWEtdeJ4HWH7eDNLgmbvAb+M6T01TPwp662TRH1bedrOczkUjukda2wietJkK4K/GPvXX4kFwO1GCmilPK3RTfYWqjy1RqbalutJRRoIFsKNbZUhCK4xLbFrFTNsU53drZVOsxz9v1OwC6KzuuCeJWjsnTrhd9DLfwIwUgo+4tVYFDJbqpdFo4T3Ce1tOBKT+1wU2CHokDDqhoeEpVb6diZ+ohpJFIq+kwit+HuXqXVU+DHD50lULgCL7rjtBeyz6qx3WrOtIth9C28qgzVlNpx7umBi/BYkdzUbKSyVtJip7heO4WMiSl/We+sHMziEC89OviX7orKvzAKYK9cWe2V/4vkYfZu2PuColLbEGyxxRzcmrQwn9yZjE6MolyV5dhifSREvwyDjQdlsQiiPbPLzqcaEiB50zd4TLqjESn+QyNsrwF9y+woYS9KPnuY6f",
                "2gCRTCyEpjQTW5Fc89yg==",
                "NQdavcYTkWP5/0TjeIOEAQ=="
        );
        System.out.println("result = " + result);
    }

    public static String decryptData(String encryptDataB64, String sessionKeyB64, String ivB64) {
        return new String(
                decryptOfDiyIV(
                        Base64.decode(encryptDataB64),
                        Base64.decode(sessionKeyB64),
                        Base64.decode(ivB64)
                )
        );
    }

    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;

    private static void init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @param ivs           自定义对称解密算法初始向量 iv
     * @return 解密后的字节数组
     */
    private static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

}
