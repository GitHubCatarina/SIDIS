package com.example.serviceReaderQuery.readerManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.serviceReaderQuery.readerManagement.model.ReaderPhoto;

public interface ReaderPhotoRepository extends JpaRepository<ReaderPhoto, Long> {
}