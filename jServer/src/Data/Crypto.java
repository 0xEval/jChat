/* ---------------------------------------------------------------------
 * Crypto.java
 *
 * Author:  0xEval
 * Project: jChat - TCP/UDP based multi-threaded chat application w/ GUI
 * Licence: GPLv3
 * GitHub:  https://github.com/0xEval/jChat
 *
 * Description:
 *
 *  Implementation of AES/GCM encryption functions.
 *  AES/GCM provides both data integrity on security with strong encryption mechanisms.
 *
 *  Galois/Counter Mode (GCM) is a mode of operation for symmetric key cryptographic
 *  block ciphers that has been widely adopted because of its efficiency and performance
 *
 *  NB:
 *      Usage of secure pseudo number generator (SecureRandom)
 *      Initialization vector should NEVER be re-used
 * --------------------------------------------------------------------- */

package Data;

import javax.crypto.Cipher;
import javax.crypto.AEADBadTagException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Crypto {

    private static final String ALGORITHM    = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT  = 128; // Length of authentication tag (integrity check)
    private static final int IV_LENGTH_BYTE  = 12;  // IV length as recommended by NIST
    private static final int KEY_LENGTH_BYTE = 16;  // Length of the symmetric key

    // Encrypt a given ByteArray and a symmetric key using AES/GCM.
    public static byte[] encrypt(byte[] key, byte[] rawData) throws Exception {
        SecureRandom secureRandom = new SecureRandom();

        byte[] iv = new byte[IV_LENGTH_BYTE];
        secureRandom.nextBytes(iv);

        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(
                Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_LENGTH_BIT, iv)
        );

        byte[] cipherText = cipher.doFinal(rawData);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();

        // Overwrite the content of the IV with zeroes.
        // NB: We cannot guarantee proper functioning b/c of Java's memory management.
        Arrays.fill(iv, (byte) 0);
        // Encoding in Base64 to provide human-readable cipher text
        // And plays nicer with String based messages.
        return Base64.getEncoder().encode(cipherMessage);
    }

    // Decrypt a given ByteArray and a symmetric key using AES/GCM.
    public static byte[] decrypt(byte[] key, byte[] encryptedData) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(encryptedData));

        int ivLength = byteBuffer.getInt();
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        byte[] encrypted = new byte[byteBuffer.remaining()];
        byteBuffer.get(encrypted);

        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(
                Cipher.DECRYPT_MODE,
                new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(TAG_LENGTH_BIT, iv)
        );

        byte[] decrypted = cipher.doFinal(encrypted);

        Arrays.fill(iv, (byte) 0);
        return decrypted;
    }

    // Generate 16-byte long symmetric key using secure pseudo RNG.
    public static byte[] generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[KEY_LENGTH_BYTE];
        secureRandom.nextBytes(key);
        return key;
    }

    public static void main(String[] args) throws Exception {
        byte[] key = generateKey();
        byte[] rawData = "“There's nothing wrong with having a tree as a friend.” - Bob Ross".getBytes();
        byte[] encryptedData = encrypt(key, rawData);
        byte[] decryptedData = decrypt(key, encryptedData);

        System.out.println("Testing Data Integrity");
        try {
            key = generateKey();
            decrypt(key, encryptedData);
            System.err.println("\tTest Failed: No exception thrown");
        } catch (AEADBadTagException tag) {
            System.out.println("\tTest Passed: Integrity error");
        }

        System.out.println("Testing Matching Data");
        System.out.println("\tInitial Input: "+new String(rawData));
        System.out.println("\tEncrypted: "+new String(encryptedData));
        System.out.println("\tDecrypted: "+new String(decryptedData));

        if (Arrays.equals(rawData, decryptedData)) {
            System.out.println("\tTest Passed: matching data");
        } else {
            System.out.println("\tTest Failed: result mismatch");
        }
    }
}

