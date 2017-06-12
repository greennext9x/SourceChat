package org.awesomeapp.messenger.plugin;

/**
 * The password digest method used in IMPS login transaction.
 */
interface IPasswordDigest {
    /**
     * Gets an array of supported digest schema.
     *
     * @return an array of digest schema
     */
    String[] getSupportedDigestSchema();

    /**
     * Generates the digest bytes of the password.
     *
     * @param schema The digest schema to use.
     * @param nonce The nonce string returned by the server.
     * @param password The user password.
     * @return The digest bytes of the password.
     */
    String digest(String schema, String nonce, String password);
}
