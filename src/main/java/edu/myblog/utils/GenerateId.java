package edu.myblog.utils;

import ch.qos.logback.classic.Logger;
import edu.myblog.controller.UploadController;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateId {
    private static Logger logger = (Logger) LoggerFactory.getLogger(GenerateId.class);
    public static String createOnlyIdByHex(){
        String id = "";
        try {
            // 生成一个随机数
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.valueOf(prng.nextInt()).toString();
            // 信息摘要 算法 把文本转换成可操作的二进制
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(randomNum.getBytes());
                id = hexEncode(result);
        } catch (NoSuchAlgorithmException e) {
            logger.error("生成随机数失败");
        }
    return id;
    }
// 哈希加密
    private static String hexEncode(byte[] input){
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < input.length; ++idx) {
            byte b = input[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }
}
