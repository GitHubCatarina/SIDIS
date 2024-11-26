package com.example.serviceReaderCom.readerManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.serviceReaderCom.readerManagement.model.ReaderPhoto;

public interface ReaderPhotoRepository extends JpaRepository<ReaderPhoto, Long> {
}