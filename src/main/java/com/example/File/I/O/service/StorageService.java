package com.example.File.I.O.service;

import com.example.File.I.O.Repo.FileDataRepo;
import com.example.File.I.O.Repo.ImageDataRepo;
import com.example.File.I.O.dto.FileInfo;
import com.example.File.I.O.entity.FileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final FileDataRepo fileDataRepo;
    private final ImageDataRepo imageDataRepo;



    private final String FOLDER_PATH = "H:\\SpringProjects\\img/";

    public String UploadImgToSys(MultipartFile file) throws IOException{

        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")){
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = FOLDER_PATH + uniqueFileName;

        //not random file name can be duplicate
        //String filePath = FOLDER_PATH+file.getOriginalFilename();

        FileData fileData = FileData.builder()
                .name(uniqueFileName).
                type(file.getContentType()).
                filePath(filePath).build();
        FileData saved = fileDataRepo.save(fileData);

        file.transferTo(new File(filePath));
        return "File Saved Successfully: "+ saved.getFilePath();
    }

    public byte[] downloadImage(String fileName) throws IOException{
        FileData fileData = fileDataRepo.findAllSortedByNameUsingNative(fileName);
        if (fileData == null){
            throw  new RuntimeException("File not found: " + fileName);
        }
        return Files.readAllBytes(new File(fileData.getFilePath()).toPath());
    }

    public List<FileInfo> getAllImage(){
        List<FileInfo> fileInfos = new ArrayList<>();
        List<FileData> fileDataList = fileDataRepo.findAll();

        fileDataList.forEach(fileData -> {
            try {
                byte[] image = Files.readAllBytes(new File(fileData.getFilePath()).toPath());
                fileInfos.add(new FileInfo(fileData.getName(), fileData.getFilePath(), image));
            }catch (IOException e){
                throw new RuntimeException("Error reading: " + fileData.getName(),e);
            }
        });
        return fileInfos;
    }

    public void deleteByName(String name){
        FileData fileData = fileDataRepo.findByName(name).orElseThrow(() ->
                new RuntimeException("File not found: "+ name));
        File file = new File(fileData.getFilePath());
        if(file.exists() && !file.delete()){
            throw new RuntimeException("Failed to delete : "+ fileData.getFilePath());
        }
        fileDataRepo.delete(fileData);
    }
}
