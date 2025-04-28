package com.tencent.cos.xml.utils;

import java.io.IOException;
import java.io.InputStream;

public class CRC64Calculator {
    // ECMA-182标准参数
    private static final long POLY = 0xC96C5795D7870F42L;
    private static final long INIT = 0xFFFFFFFFFFFFFFFFL;
    private static final int GF2_DIM = 64; // GF(2)矩阵维度

    public static long getCRC64(InputStream inputStream, long skip, long size) throws IOException {
        // 确保精确跳过指定字节数
        long remaining = skip;
        while (remaining > 0) {
            long skipped = inputStream.skip(remaining);
            if (skipped <= 0) {
                throw new IOException("Failed to skip " + skip + " bytes");
            }
            remaining -= skipped;
        }

        CRC64 crc64 = new CRC64();
        byte[] buff = new byte[8 * 1024];
        long totalRead = 0;

        while (totalRead < size) {
            int needRead = (int) Math.min(size - totalRead, buff.length);
            int readLen = inputStream.read(buff, 0, needRead);
            if (readLen == -1) break;

            crc64.update(buff, 0, readLen);
            totalRead += readLen;
        }

        if (totalRead != size) {
            throw new IOException("Expected to read " + size + " bytes but got " + totalRead);
        }

        return crc64.getValue();
    }

    /**
     * 合并两个分段CRC64值
     * @param crc1 第一段CRC64值（对应0-start字节范围）
     * @param crc2 第二段CRC64值（对应start-end字节范围）
     * @param len2 第二段数据长度（end-start）
     * @return 合并后的CRC64值
     */
    public static long combine(long crc1, long crc2, long len2) {
//        crc1 = new BigInteger(Long.toUnsignedString(crc1,10)).longValue();
//        crc2 = new BigInteger(Long.toUnsignedString(crc2,10)).longValue();

        if (len2 == 0) return crc1;

        // 转换为中间计算状态（反转并处理初始值）
        crc1 = ~crc1 ^ INIT;
        crc2 = ~crc2;

        // 初始化GF(2)矩阵
        long[] mat = new long[GF2_DIM];
        long[] even = new long[GF2_DIM];
        long[] odd = new long[GF2_DIM];

        // 构建多项式矩阵
        mat[0] = POLY;
        long val = 1L;
        for (int i = 1; i < GF2_DIM; i++) {
            mat[i] = val;
            val <<= 1;
        }

        // 矩阵平方运算
        gf2MatrixSquare(even, mat);
        gf2MatrixSquare(odd, even);

        // 合并运算
        long len = len2;
        do {
            gf2MatrixSquare(even, odd);
            if ((len & 1) != 0) {
                crc1 = gf2MatrixTimes(even, crc1);
            }
            len >>>= 1;
            if (len == 0) break;

            gf2MatrixSquare(odd, even);
            if ((len & 1) != 0) {
                crc1 = gf2MatrixTimes(odd, crc1);
            }
            len >>>= 1;
        } while (len != 0);

        // 转换为标准CRC64输出格式
        return ~(crc1 ^ crc2);
    }

    // GF(2)矩阵乘法
    private static long gf2MatrixTimes(long[] mat, long vec) {
        long sum = 0;
        int index = 0;
        while (vec != 0) {
            if ((vec & 1) != 0) {
                sum ^= mat[index];
            }
            vec >>>= 1;
            index++;
        }
        return sum;
    }

    // GF(2)矩阵平方
    private static void gf2MatrixSquare(long[] square, long[] mat) {
        for (int n = 0; n < GF2_DIM; n++) {
            square[n] = gf2MatrixTimes(mat, mat[n]);
        }
    }
}