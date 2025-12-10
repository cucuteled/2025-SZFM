package com.project.sfm2025.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {

    private final Path uploadDir;

    // MIVEL A JAVA NEM ÍRHATJA FUTÁS KÖZBEN A JAR-T ezért azon kivülre mellé egy uploads/ mappába kell szerzveni a képeket

    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Nem sikerült létrehozni az uploads könyvtárat", e);
        }
    }

    public void renameFile(String oldName, String newName) {
        Path oldPath = uploadDir.resolve(oldName + ".jpg");
        Path newPath = uploadDir.resolve(newName + ".jpg");

        if (Files.exists(oldPath)) {
            try {
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Átnevezve: " + newPath.toAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Nem sikerült átnevezni a fájlt", e);
            }
        } else {
            System.out.println("A fájl nem létezik: " + oldPath.toAbsolutePath());
        }
    }

    public String saveOriginalNameFile(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new IOException("Érvénytelen fájlnév");
        }

        Path targetPath = uploadDir.resolve(originalName);

        if (Files.exists(targetPath)) {
            throw new FileAlreadyExistsException("A fájl már létezik: " + originalName);
        }

        // Menti
        Files.copy(file.getInputStream(), targetPath);

        return originalName;
    }

    public void saveFileWithItemName(MultipartFile file, String itemName) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Üres fájl");

        // Kiterjesztés
        String extension = ".jpg";  // csak JPG-t engedünk
        Path targetPath = uploadDir.resolve(itemName + extension);

        if (Files.exists(targetPath)) {
            File epic = new File(targetPath.toUri());
            try {
                epic.delete();
            } catch (Exception e) {

            }
        }

        Files.copy(file.getInputStream(), targetPath);
    }

}