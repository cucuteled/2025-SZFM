package com.project.sfm2025;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTeszt {

    @Test
    public void testEncryptDecrypt() {
        String jelszo = "Olivár127Á@";
        String cryptcode = "admin";

        String jelszoCrypted = PasswordEncryptor.encrypt_pass(jelszo, cryptcode);
        String jelszoDecrypted = PasswordEncryptor.decrypt_pass(jelszoCrypted, cryptcode);

        // Ellenőrzések
        assertNotNull(jelszoCrypted, "A titkosított jelszó nem lehet null");
        assertNotEquals(jelszo, jelszoCrypted, "A titkosított jelszó ne legyen azonos a simával");
        assertEquals(jelszo, jelszoDecrypted, "A visszafejtett jelszó egyezzen az eredetivel");

        System.out.println("Jelszó: " + jelszo + "\n" + "Titkosítva: " + jelszoCrypted + "\n" + "Feloldva: " + jelszoDecrypted + "\n");
    }
}
