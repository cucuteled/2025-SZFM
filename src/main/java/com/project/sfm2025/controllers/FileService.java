package com.project.sfm2025.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {

    private final Path uploadDir;

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
}