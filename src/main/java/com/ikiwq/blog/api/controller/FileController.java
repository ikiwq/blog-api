package com.ikiwq.blog.api.controller;

import com.ikiwq.blog.api.model.dto.response.FileUploadResponse;
import com.ikiwq.blog.api.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final StorageService storageService;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> loadFile(@PathVariable String filename) {
        Resource res = storageService.loadByName(filename);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename\"" + res.getFilename() + "\""
                        )
                .body(res);
    }

    @PostMapping("/")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponse res = storageService.store(file);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
