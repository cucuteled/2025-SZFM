package com.project.sfm2025;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

// ------------
// FIGYELEM! A CRYPTCODE NEM USER KÓD HANEM ADATBÁZIS KÓD AMI AZ EGYSZERŰSÉG KEDVÉÉRT A PROJECT ALATT: "admin" lesz mindenhol!
// ---------------

public class PasswordEncryptor {

    // JELSZÓ TITKOSÍTÁSA
    private static KeyPair generateKeyPair(String cryptcode) throws Exception {
        // Hash a cryptcode-ból (deterministic seed)
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] seed = sha256.digest(cryptcode.getBytes(StandardCharsets.UTF_8));

        // Deterministic SecureRandom
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048, secureRandom);
        return keyGen.generateKeyPair();
    }

    public static String encrypt_pass(String password, String cryptcode) {
        try {
            KeyPair keyPair = generateKeyPair(cryptcode);
            PublicKey publicKey = keyPair.getPublic();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JELSZÓ titkosítás feloldás
    public static String decrypt_pass(String encryptedPassword, String cryptcode) {
        try {
            KeyPair keyPair = generateKeyPair(cryptcode);
            PrivateKey privateKey = keyPair.getPrivate();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
