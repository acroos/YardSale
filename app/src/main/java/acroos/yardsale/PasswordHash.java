package acroos.yardsale;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int SALT_NUM_BYTES = 24;
    public static final int HASH_NUM_BYTES = 24;
    public static final int ITERATIONS = 1000;
    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int HASH_INDEX = 2;

    public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    public static boolean validatePassword(String password, String correctHash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return validatePassword(password.toCharArray(), correctHash);
    }

    private static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[SALT_NUM_BYTES];
        sr.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt, ITERATIONS, HASH_NUM_BYTES);
        return ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static boolean validatePassword(char[] password, String correctHash) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] hashParts = correctHash.split(":");
        int iterations = Integer.parseInt(hashParts[ITERATION_INDEX]);
        byte[] salt = fromHex(hashParts[SALT_INDEX]);
        byte[] hash = fromHex(hashParts[HASH_INDEX]);

        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

        return slowEquals(hash, testHash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int hashNumBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, hashNumBytes * 8);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return keyFactory.generateSecret(keySpec).getEncoded();
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for(int i=0; i<binary.length; i++) {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i + 2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] bytes) {
        BigInteger bigInteger = new BigInteger(1, bytes);
        String hex = bigInteger.toString(16);
        int paddingLength = (bytes.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }
        return hex;
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i=0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
