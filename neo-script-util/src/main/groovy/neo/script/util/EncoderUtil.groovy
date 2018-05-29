package neo.script.util

import com.google.common.hash.Hashing

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
    static public String md5(String inputString) {
        return Hashing.md5().hashBytes(inputString.getBytes()).toString()
    }
}
