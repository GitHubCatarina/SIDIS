package com.example.serviceReader.readerManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.serviceReader.readerManagement.model.ReaderPhoto;

public interface ReaderPhotoRepository extends JpaRepository<ReaderPhoto, Long> {
}