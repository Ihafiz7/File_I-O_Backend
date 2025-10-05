package com.example.File.I.O.Repo;

import com.example.File.I.O.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDataRepo extends JpaRepository<FileData, Long> {
    @Query(value = "Select * from File_Data where name = :name", nativeQuery = true)
    FileData findAllSortedByNameUsingNative(String name);


    Optional<FileData> findByName(String name);
}
