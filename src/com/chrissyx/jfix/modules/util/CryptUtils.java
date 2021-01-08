package com.chrissyx.jfix.modules.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Utils for encryption and decryption.
 *
 * @author Chrissyx
 */
public class CryptUtils
{
    /**
     * Hidden constructor to prevent instances of this class.
     */
    private CryptUtils()
    {
    }

    /**
     * Decodes an Base64 encoded string.
     *
     * @param string Base64 string to decode
     * @return Decoded string
     */
    public static String decode(final String string)
    {
        return new String(Base64.decodeBase64(string));
    }

    /**
     * Encodes a string with Base64.
     *
     * @param string String to encode
     * @return Base64 encoded string
     */
    public static String encode(final String string)
    {
        return Base64.encodeBase64String(string.getBytes());
    }
}
