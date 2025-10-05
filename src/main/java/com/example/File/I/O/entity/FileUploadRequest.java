package com.example.File.I.O.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {

    @Schema(type = "string", format = "binary", description = "Image file to upload")
    private MultipartFile file;

    // Only needed for schema generation, not used in controller
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
