package com.example.File.I.O.controller;

import com.example.File.I.O.dto.FileInfo;
import com.example.File.I.O.dto.ResponseMessage;
import com.example.File.I.O.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class IoController {
    private final StorageService service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image file")
    @Schema()
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            String uploadImage = service.UploadImgToSys(file);
            message = "Uploaded Successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Failed Attempt: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/getFile/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException{
        byte[] imgData = service.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imgData);
    }

    @GetMapping("/allFiles")
    public ResponseEntity<?> getAllImage() throws IOException{
        List<FileInfo> fileInfoList = service.getAllImage();
        return ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
    }

    @DeleteMapping(value = "/name/{name}")
    public ResponseEntity<?> deleteFileByName(@PathVariable String name){
        try {
            service.deleteByName(name);
            return ResponseEntity.ok(new ResponseMessage("file deleted"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete File" + e.getMessage());
        }
    }

}
