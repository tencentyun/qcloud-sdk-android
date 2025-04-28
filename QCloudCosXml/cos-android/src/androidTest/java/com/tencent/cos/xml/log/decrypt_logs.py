#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import struct
from Crypto.Cipher import AES
from Crypto.Util.Padding import unpad

# 使用Java代码中指定的密钥和IV
KEY = bytes([
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
    0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
    0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
    0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
])
IV = bytes([
    0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
    0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F
])

def decrypt_log_file(encrypted_file_path, output_file_path):
    """
    解密加密的日志文件

    参数:
        encrypted_file_path: 加密的日志文件路径
        output_file_path: 解密后的输出文件路径
    """
    cipher = AES.new(KEY, AES.MODE_CBC, IV)

    print(f"IV: {IV}")

    with open(encrypted_file_path, 'rb') as fin, open(output_file_path, 'wb') as fout:
        while True:
            # 读取4字节的长度头(大端序)
            length_bytes = fin.read(4)
            if not length_bytes:
                break

            # 解析密文长度
            ciphertext_length = struct.unpack('>i', length_bytes)[0]

            # 读取密文数据
            ciphertext = fin.read(ciphertext_length)
            if len(ciphertext) != ciphertext_length:
                raise ValueError("文件损坏: 密文长度不匹配")

            try:
                # 解密并移除PKCS5/PKCS7填充
                plaintext = unpad(cipher.decrypt(ciphertext), AES.block_size)
                fout.write(plaintext)
            except ValueError as e:
                print(f"解密失败: {e}, 可能遇到损坏的日志块")
                continue

if __name__ == '__main__':
    # 示例用法
    input_file = '2025-04-10-12-04-21_encrypt.log'  # 加密日志文件路径
    output_file = 'decrypted.log' # 解密输出路径

    decrypt_log_file(input_file, output_file)
    print(f"解密完成，结果已保存到 {output_file}")
