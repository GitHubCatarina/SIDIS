package com.example.serviceBook.authorManagement.util;

import org.springframework.web.multipart.MultipartFile;

public class AuthorUtil {
    public static boolean isValidAuthorPhoto(MultipartFile photo) {
        if (photo.isEmpty()) {
            return false;
        }

        String contentType = photo.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/jpg") && !contentType.equals("image/png")) {
            return false;
        }

        long fileSizeKB = photo.getSize() / 1024; // Convert bytes to KB
        if (fileSizeKB > 20) {
            return false;
        }

        return true;
    }
}
