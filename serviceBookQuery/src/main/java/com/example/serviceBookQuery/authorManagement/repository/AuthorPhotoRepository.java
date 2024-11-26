package com.example.serviceBookQuery.authorManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.serviceBookQuery.authorManagement.model.AuthorPhoto;

public interface AuthorPhotoRepository extends JpaRepository<AuthorPhoto, Long> {}