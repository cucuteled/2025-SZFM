package com.project.sfm2025;

import com.project.sfm2025.controllers.CouponController;
import com.project.sfm2025.controllers.DrinkController;
import com.project.sfm2025.controllers.FileController;
import com.project.sfm2025.controllers.FileService;
import com.project.sfm2025.entities.Coupon;
import com.project.sfm2025.entities.Drink;
import com.project.sfm2025.repositories.CodeCouponRepository;
import com.project.sfm2025.repositories.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

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

    @Test
    public void testGetMyCoupons_Unauthorized() {
        CouponController c = new CouponController(null, null);

        ResponseEntity<List<Coupon>> res = c.getMyCoupons(null);

        assertEquals(401, res.getStatusCodeValue());
    }
    @Test
    public void testAddCoupon_NotFound() {
        CodeCouponRepository repo = Mockito.mock(CodeCouponRepository.class);
        Mockito.when(repo.findByCode("AAA")).thenReturn(null);

        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.isAuthenticated()).thenReturn(true);

        CouponController c = new CouponController(null, repo);

        ResponseEntity<String> res = c.addCoupon("AAA", auth);

        assertEquals(401, res.getStatusCodeValue());
    }
    @Test
    public void testFileRename() {
        FileService fs = Mockito.mock(FileService.class);
        FileController fc = new FileController(fs);

        String msg = fc.renameFile("teszt.jpg", "teszt2.jpg");

        Mockito.verify(fs).renameFile("teszt.jpg", "teszt2.jpg");
        assertTrue(msg.contains("teszt.jpg"));
        assertTrue(msg.contains("teszt2.jpg"));
    }
    @Test
    public void testUpdateDrink_Found() {
        DrinkRepository repo = Mockito.mock(DrinkRepository.class);

        Drink original = new Drink();
        original.setName("Old");

        Drink updated = new Drink();
        updated.setName("New");

        Mockito.when(repo.findByName("Old")).thenReturn(Optional.of(original));
        Mockito.when(repo.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);

        DrinkController controller = new DrinkController(repo);
        ResponseEntity<Drink> res = controller.updateDrink("Old", updated);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals("New", res.getBody().getName());
    }

}
