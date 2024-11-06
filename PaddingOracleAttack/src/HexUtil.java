/**
 * Utility class for various operations on hexadecimal strings and byte arrays.
 * Provides methods for XOR operations, conversions between hexadecimal and ASCII,
 * and removing padding from a hex-encoded message.
 * 
 *  * <p><b>Author:</b> Bruno Ramirez Ledesma</p>
 */
public class HexUtil {

    /**
     * Default constructor for the HexUtil class.
     */
    public HexUtil() {

    }

    /**
     * Performs an XOR operation between two hexadecimal strings of the same length.
     * 
     * @param hex1 The first hexadecimal string.
     * @param hex2 The second hexadecimal string.
     * @return A hexadecimal string representing the XOR result of the two input strings.
     * @throws IllegalArgumentException if the two hexadecimal strings have different lengths.
     */
    public static String xorHex(String hex1, String hex2) {
        if (hex1.length() != hex2.length()) {
            throw new IllegalArgumentException("Both strings need the same length");
        }

        byte[] bytes1 = hexStringToByteArray(hex1);
        byte[] bytes2 = hexStringToByteArray(hex2);

        byte[] result = new byte[bytes1.length];
        for (int i = 0; i < bytes1.length; i++) {
            result[i] = (byte) (bytes1[i] ^ bytes2[i]);
        }

        return byteArrayToHexString(result);
    }

    /**
     * Converts a hexadecimal string to a byte array.
     * 
     * @param hex The hexadecimal string to convert.
     * @return A byte array corresponding to the given hexadecimal string.
     * @throws IllegalArgumentException if the hexadecimal string has an odd length or is invalid.
     */
    public static byte[] hexStringToByteArray(String hex) {
        // Remove the "0x" or "0X" prefix if present
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2);
        }

        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Needs to be even length");
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Converts a byte array to a hexadecimal string.
     * 
     * @param bytes The byte array to convert.
     * @return A string representing the hexadecimal value of the byte array, prefixed with "0x".
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder("0x"); // Add "0x" prefix
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Format in lowercase
        }
        return hexString.toString();
    }

    /**
     * Converts a hexadecimal string to its ASCII representation.
     * 
     * @param hex The hexadecimal string to convert.
     * @return A string representing the ASCII equivalent of the hexadecimal string.
     */
    public static String hexToAscii(String hex) {
        // Remove the "0x" prefix if present
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        StringBuilder ascii = new StringBuilder();

        // Process the hexadecimal string in steps of 2 characters (1 byte)
        for (int i = 0; i < hex.length(); i += 2) {
            // Take each pair of hexadecimal characters
            String part = hex.substring(i, i + 2);

            // Convert the hexadecimal pair to a decimal value (byte)
            int decimal = Integer.parseInt(part, 16);

            // Convert the decimal value to an ASCII character and append to result
            ascii.append((char) decimal);
        }

        return ascii.toString();
    }

    /**
     * Removes the padding from a plain message in hexadecimal form.
     * Assumes the padding follows the PKCS#7 scheme, where the value of the last byte
     * indicates the length of the padding.
     * 
     * @param plainMessage The plain message in hexadecimal format.
     * @return The message without padding, in hexadecimal format.
     */
    public static String removePadding(String plainMessage) {

        byte[] plainMessageByte = hexStringToByteArray(plainMessage);
        int paddingLength = plainMessageByte[plainMessageByte.length - 1]; // The last byte indicates padding length
        System.out.println(" (Padding Length = " + paddingLength + " byte)");
        if (paddingLength > 0 && paddingLength <= 8) {
            // Ensure no more bytes are removed than were added
            byte[] messageWithoutPadding = new byte[plainMessageByte.length - paddingLength];
            System.arraycopy(plainMessageByte, 0, messageWithoutPadding, 0, plainMessageByte.length - paddingLength);
            return byteArrayToHexString(messageWithoutPadding);
        } else {
            return plainMessage; // If no padding, return the original message
        }
    }
}
