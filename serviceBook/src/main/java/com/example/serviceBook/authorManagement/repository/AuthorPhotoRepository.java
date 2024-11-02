package com.example.serviceBook.authorManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.serviceBook.authorManagement.model.AuthorPhoto;

public interface AuthorPhotoRepository extends JpaRepository<AuthorPhoto, Long> {}