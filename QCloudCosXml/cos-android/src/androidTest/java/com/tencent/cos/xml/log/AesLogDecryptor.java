package com.tencent.cos.xml.log;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesLogDecryptor {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static void main(String[] args) {
//        if (args.length != 4) {
//            System.out.println("用法: java AesLogDecryptor <密钥> <IV> <加密文件路径> <解密输出路径>");
//            System.out.println("示例: java AesLogDecryptor 000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F 303132333435363738393A3B3C3D3E3F encrypted.log decrypted.log");
//            return;
//        }

        try {
            // 解析参数
//            byte[] key = hexStringToByteArray(args[0]);
//            byte[] iv = hexStringToByteArray(args[1]);
//            String encryptedFilePath = args[2];
//            String decryptedFilePath = args[3];

            byte[] key = new byte[] {
                    // 32个字节的16进制表示（示例值，实际应使用安全随机数生成）
                    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                    0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
                    0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
                    0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
            };
            byte[] iv ={
                    0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
                    0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
            };
            String encryptedFilePath = "/Users/jordanqin/QCloudAndroid/library/QCloudCosXml/cos-android/src/androidTest/java/com/tencent/cos/xml/log/2025-04-10-12-04-21_encrypt_ios.log";
            String decryptedFilePath = "/Users/jordanqin/QCloudAndroid/library/QCloudCosXml/cos-android/src/androidTest/java/com/tencent/cos/xml/log/2025-04-10-12-04-21_encrypt_ios.log.decrypted";

            // 解密文件
            decryptLogFile(new File(encryptedFilePath), new File(decryptedFilePath), key, iv);

            System.out.println("解密完成，结果已保存到: " + decryptedFilePath);
        } catch (Exception e) {
            System.err.println("解密过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解密加密的日志文件
     * @param encryptedFile 加密的日志文件
     * @param decryptedFile 解密后的输出文件
     * @param key AES密钥
     * @param iv 初始化向量
     * @throws Exception 解密过程中可能抛出的异常
     */
    public static void decryptLogFile(File encryptedFile, File decryptedFile, byte[] key, byte[] iv) throws Exception {
        System.err.print("iv: "+ Arrays.toString(iv));

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        try (DataInputStream dis = new DataInputStream(new FileInputStream(encryptedFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(decryptedFile))) {

            while (dis.available() > 0) {
                // 读取4字节的长度头(大端序)
                int length = dis.readInt();

                // 读取密文数据
                byte[] encrypted = new byte[length];
                dis.readFully(encrypted);

                // 解密数据
                String decrypted = new String(decryptSingle(encrypted, keySpec, ivSpec), StandardCharsets.UTF_8);

                // 写入解密后的内容到输出文件
                writer.write(decrypted);
            }
        }
    }

    /**
     * 解密单个数据块
     * @param ciphertext 密文数据
     * @param keySpec 密钥规范
     * @param ivSpec 初始化向量规范
     * @return 解密后的字节数组
     * @throws Exception 解密过程中可能抛出的异常
     */
    private static byte[] decryptSingle(byte[] ciphertext, SecretKeySpec keySpec, IvParameterSpec ivSpec) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(ciphertext);
    }

    /**
     * 将十六进制字符串转换为字节数组
     * @param s 十六进制字符串
     * @return 对应的字节数组
     */
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
