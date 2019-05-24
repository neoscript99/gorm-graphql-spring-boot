package neo.script.gorm.general.util

import groovy.transform.CompileStatic
import neo.script.gorm.general.domain.sys.Token

@CompileStatic
class TokenHolder {

    /**
     * ThreadLocal to hold the Token for Threads to access.
     */
    private static final ThreadLocal<Token> threadLocal = new ThreadLocal<Token>();

    /**
     * Retrieve the assertion from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static Token getToken() {
        return threadLocal.get();
    }

    /**
     * Add the Token to the ThreadLocal.
     *
     * @param assertion the assertion to add.
     */
    public static void setToken(final Token token) {
        threadLocal.set(token);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
