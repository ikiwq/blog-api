package com.ikiwq.blog.api.service;

import com.ikiwq.blog.api.config.property.AppProperties;
import com.ikiwq.blog.api.model.dto.response.FileUploadResponse;
import com.ikiwq.blog.api.model.exception.BlogException;
import com.ikiwq.blog.api.model.exception.BlogExceptionEnum;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    private final static String FILE_API_PATH = "/api/v1/files/";

    private final String domain;

    private final Path uploadLocation;


    public StorageService(AppProperties appProperties) {
        this.uploadLocation = Paths.get(appProperties.getUploadFolder());
        this.domain = appProperties.getDomain();
    }

    public FileUploadResponse store(MultipartFile file) {
        if(file.isEmpty()){
            throw new BlogException(BlogExceptionEnum.FILE_EMPTY);
        }

        if(file.getOriginalFilename() == null) {
            throw new BlogException(BlogExceptionEnum.FILE_INVALID_NAME);
        }

        Path filePath = Paths.get(file.getOriginalFilename());
        Path destination = this.uploadLocation.resolve(filePath).normalize().toAbsolutePath();

        if(!destination.getParent().equals(this.uploadLocation.toAbsolutePath())) {
            throw new BlogException(BlogExceptionEnum.FILE_INVALID_DIRECTORY);
        }

        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BlogException(BlogExceptionEnum.FILE_UPLOAD_FAILED);
        }

        String fileFullPath = domain.concat(FILE_API_PATH).concat(file.getOriginalFilename());
        return new FileUploadResponse(fileFullPath);
    }

    public Resource loadByName(String fileName) {
        try {
            Path file = uploadLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if(!resource.exists() || !resource.isReadable()) {
                throw new BlogException(BlogExceptionEnum.FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new BlogException(BlogExceptionEnum.FILE_NOT_FOUND);
        }
    }
}
