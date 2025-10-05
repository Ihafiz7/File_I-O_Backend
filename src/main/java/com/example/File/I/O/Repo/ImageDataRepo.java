package com.example.File.I.O.Repo;

import com.example.File.I.O.entity.FileData;
import com.example.File.I.O.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDataRepo extends JpaRepository<ImageData, Long> {

    Optional<FileData> findByName(String name);
}
