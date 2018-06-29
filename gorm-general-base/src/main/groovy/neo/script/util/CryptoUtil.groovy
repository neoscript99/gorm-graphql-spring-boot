package neo.script.util

import cn.hutool.crypto.symmetric.SymmetricAlgorithm
import cn.hutool.crypto.symmetric.SymmetricCrypto
import groovy.transform.CompileStatic


@CompileStatic
class CryptoUtil {
    static SymmetricCryptoBase64 symmetricCrypto(String key) {
        //AES的key长度为16, 24, 32
        //EncoderUtil.sha256返回为64位，截取最后16位
        def symmetricCrypto = new SymmetricCrypto(
                SymmetricAlgorithm.AES,
                EncoderUtil.sha256(key).substring(48).getBytes());

        //base64加密后的字符串比较短
        new SymmetricCryptoBase64() {
            @Override
            String encrypt(String data) {
                symmetricCrypto.encryptBase64(data)
            }

            @Override
            String decrypt(String data) {
                symmetricCrypto.decryptStrFromBase64(data)
            }
        }
    }

    static interface SymmetricCryptoBase64 {
        String encrypt(String data)

        String decrypt(String data)
    }
}
