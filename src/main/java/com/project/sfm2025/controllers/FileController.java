package com.project.sfm2025.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/rename")
    public String renameFile(@RequestParam String oldName,
                             @RequestParam String newName) {
        fileService.renameFile(oldName, newName);
        return "Fájl átnevezve: " + oldName + " → " + newName;
    }
}
