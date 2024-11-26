package com.example.serviceBookCom.authorManagement.repository;

import com.example.serviceBookCom.authorManagement.model.AuthorPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorPhotoRepository extends JpaRepository<AuthorPhoto, Long> {}