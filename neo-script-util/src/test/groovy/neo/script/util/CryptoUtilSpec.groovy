package neo.script.util

import cn.hutool.core.util.CharsetUtil
import cn.hutool.crypto.symmetric.SymmetricAlgorithm
import cn.hutool.crypto.symmetric.SymmetricCrypto
import spock.lang.Ignore
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

class CryptoUtilSpec extends Specification {
    def "SymmetricCrypto String"() {
        given:
        String content = String.format("自助餐zzcc1234-%s", LocalDateTime.now().toString())
        println(content: content, length: content.length())
        String key = "change-it";

        //构建
        def symmetricCrypto = CryptoUtil.symmetricCrypto(key);

        //加密为16进制，解密为原字符串
        String encryptHex = symmetricCrypto.encrypt(content);
        println(encryptHex: encryptHex, length: encryptHex.length())
        String decryptStr = symmetricCrypto.decrypt(encryptHex);
        expect:
        decryptStr == content;
    }

    @Ignore
    def "SymmetricCrypto Object"() {
        given:
        String key = "change-itaaaaaaaaaaaaaaa";
        def baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        Map map = [a: 'a', b: 'b', i: 33]
        oos.writeObject(map)

        //构建
        SymmetricCrypto symmetricCrypto = new SymmetricCrypto(
                SymmetricAlgorithm.AES,
                key.getBytes(CharsetUtil.CHARSET_UTF_8));

        //加密为16进制，解密为原字符串
        byte[] encrypt = symmetricCrypto.encrypt(baos.toByteArray());
        println(encrypt)
        byte[] decrypt = symmetricCrypto.decrypt(encrypt);
        def bais = new ByteArrayInputStream(decrypt)
        def oia = new ObjectInputStream(bais)
        def demap = oia.readObject()
        println(demap)
        expect:
        map == demap;
    }


    def "SymmetricCrypto json"() {
        given:
        def startLine = LocalDateTime.of(2018, 01, 01, 0, 0)
        def now = LocalDateTime.now();
        def duration = Duration.between(startLine, now);
        println([System.nanoTime(),
                 System.currentTimeMillis(),
                 System.currentTimeSeconds(),
                 now.toString(),
                 duration])
        Map map = [a: 12345678, b: duration.toMinutes()]
        String content = JsonUtil.toJson(map, false)
        println([content: content, length: content.length()])
        String key = "change-it";

        //构建
        def symmetricCrypto = CryptoUtil.symmetricCrypto(key);

        //加密为16进制，解密为原字符串
        String encryptHex = symmetricCrypto.encrypt(content);
        println([content: encryptHex, length: encryptHex.length()])
        String decryptStr = symmetricCrypto.decrypt(encryptHex);
        expect:
        decryptStr == content;
    }
}
