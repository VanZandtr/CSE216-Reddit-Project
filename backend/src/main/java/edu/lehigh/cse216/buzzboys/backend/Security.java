package edu.lehigh.cse216.buzzboys.backend;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;

public class Security {

    static int DEFAULT_SALT_SIZE = 16;
    static int DEFAULT_HASH_ITERATIONS = 1000;
    static int DEFAULT_KEY_LENGTH = 64 * 8;

    public static byte[] generateSalt() {
        return generateSalt(DEFAULT_SALT_SIZE);
    }

    public static byte[] generateSalt(int size) {
        byte[] ret = null;
        try { 
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            ret = new byte[size];
            sr.nextBytes(ret);
        }
        catch (NoSuchAlgorithmException e) {
            //TODO: log error in heroku
            System.out.println("Security.java: NoSuchAlgorithmException " + e.getMessage());
        }
        return ret;
    }

    public static byte[] hashPassword(String password, byte[] salt) {
        return hashPassword(password, salt, DEFAULT_HASH_ITERATIONS);
    }

    public static byte[] hashPassword(String password, byte[] salt, int iterations) {
        byte[] ret = null;
        try {
            char[] chars = password.toCharArray();
            PBEKeySpec pbe = new PBEKeySpec(chars, salt, iterations, DEFAULT_KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            ret = skf.generateSecret(pbe).getEncoded();
        }
        catch (NoSuchAlgorithmException e) {
            //TODO: log error in heroku
            System.out.println("Security.java: NoSuchAlgorithmException " + e.getMessage());
        }
        catch (InvalidKeySpecException e) {
            //TODO: log error in heroku
            System.out.println("Security.java: InvalidKeySpecException " + e.getMessage());
        }
        return ret;
    }
}