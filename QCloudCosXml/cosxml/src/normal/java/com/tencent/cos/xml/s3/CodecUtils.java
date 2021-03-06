/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.s3;

public enum CodecUtils {

    ;
    /**
     * Transforms the given string into the given destination byte array
     * truncating each character into a byte and skipping carriage returns and
     * line feeds if any.
     * <p>
     * dmurray: "It so happens that we're currently only calling this method
     * with src.length == dest.length, in which case it works, but we could
     * theoretically get away with passing a smaller dest if we knew ahead of
     * time that src contained some number of spaces. In that case it looks like
     * this implementation would truncate the result."
     * <p>
     * hchar:
     * "Yes, but the truncation is the intentional behavior of this internal
     * routine in that case."
     *
     * @param singleOctets
     *            non-null string containing only single octet characters
     * @param dest
     *            destination byte array
     *
     * @return the actual length of the destination byte array holding data
     * @throws IllegalArgumentException
     *             if the input string contains any multi-octet character
     */
    static int sanitize(final String singleOctets, byte[] dest) {
        final int capacity = dest.length;
        final char[] src = singleOctets.toCharArray();
        int limit=0;

        for (int i=0; i < capacity; i++) {
            final char c = src[i];

            if (c == '\r' || c == '\n' || c == ' ')
                continue;
            if (c > Byte.MAX_VALUE)
                throw new IllegalArgumentException("Invalid character found at position " + i + " for " + singleOctets);
            dest[limit++] = (byte)c;
        }
        return limit;
    }

    /**
     * Returns a byte array representing the given string,
     * truncating each character into a byte directly.
     *
     * @throws IllegalArgumentException if the input string contains any multi-octet character
     */
    public static byte[] toBytesDirect(final String singleOctets) {
        final char[] src = singleOctets.toCharArray();
        final byte[] dest = new byte[src.length];

        for (int i=0; i < dest.length; i++) {
            final char c = src[i];

            if (c > Byte.MAX_VALUE)
                throw new IllegalArgumentException("Invalid character found at position " + i + " for " + singleOctets);
            dest[i] = (byte)c;
        }
        return dest;
    }

    /**
     * Returns a string representing the given byte array,
     * treating each byte as a single octet character.
     */
    public static String toStringDirect(final byte[] bytes) {
        final char[] dest = new char[bytes.length];
        int i=0;

        for (byte b: bytes)
            dest[i++] = (char)b;

        return new String(dest);
    }

    /**
     * Sanity check the last decoded position is a possible value.
     *
     * @throws IllegalArgumentException if the given decoded position is
     * not a possible value produced via the respective encoding
     */
    static void sanityCheckLastPos(int pos, int mask) {
        if ((pos & mask) != 0) {
            throw new IllegalArgumentException
                    ("Invalid last non-pad character detected");
        }
    }

}
