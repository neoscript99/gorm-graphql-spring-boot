package neo.script.util

import com.google.common.hash.Hashing

import java.nio.charset.StandardCharsets

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
class EncoderUtil {
    /**
     * @param inputString
     * @return encoded string
     */
    @Deprecated
    static public String md5(String inputString) {
        return Hashing.md5().hashBytes(inputString.getBytes(StandardCharsets.UTF_8)).toString()
    }

    static public String sha256(String inputString) {
        return sha256(inputString.getBytes(StandardCharsets.UTF_8))
    }

    static public String sha256(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString()
    }
}
