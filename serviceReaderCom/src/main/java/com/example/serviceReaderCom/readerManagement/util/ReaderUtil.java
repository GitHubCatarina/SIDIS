package com.example.serviceReaderCom.readerManagement.util;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class ReaderUtil {

    public static final List<String> VALID_GENRES = Arrays.asList(
            "Self-Improvement", "Science Fiction", "Mystery", "Romance",
            "Fantasy", "Non-Fiction", "Thriller", "Biography", "History", "Children's"
    );

    public static boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }

        Period age = Period.between(dateOfBirth, LocalDate.now());

        return age.getYears() >= 12;
    }
    public static boolean isValidName(String name) {
        List<String> forbiddenNames = Arrays.asList("admin", "manager", "user", "reader", "lender", "librarian", "default", "borrower", "administrator");

        if (name == null || name.isEmpty()) {
            return false;
        }

        for (String forbiddenName : forbiddenNames) {
            if (name.equalsIgnoreCase(forbiddenName)) {
                return false;
            }
        }

        if (name.length() > 150) {
            return false;
        }

        return true;
    }

    public static boolean isValidReaderPhoto(MultipartFile photo) {
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
