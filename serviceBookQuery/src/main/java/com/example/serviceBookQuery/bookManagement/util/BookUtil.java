package com.example.serviceBookQuery.bookManagement.util;

import org.springframework.web.multipart.MultipartFile;

public class BookUtil {
    public static boolean isValidISBN(String isbn) {
        isbn = isbn.replaceAll("[\\s\\-]+", "");
        if (isbn.length() != 10 && isbn.length() != 13) {
            return false;
        }

        if (isbn.length() == 10) {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                char c = isbn.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
                sum += Character.getNumericValue(c) * (i + 1);
            }
            char lastChar = isbn.charAt(9);
            if (lastChar == 'X') {
                sum += 10 * 10;
            } else if (!Character.isDigit(lastChar)) {
                return false;
            } else {
                sum += Character.getNumericValue(lastChar) * 10;
            }
            return sum % 11 == 0;
        }

        if (isbn.length() == 13) {
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                char c = isbn.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
                sum += (i % 2 == 0) ? Character.getNumericValue(c) : Character.getNumericValue(c) * 3;
            }
            char lastChar = isbn.charAt(12);
            if (!Character.isDigit(lastChar)) {
                return false;
            }
            return (10 - (sum % 10)) % 10 == Character.getNumericValue(lastChar);
        }

        return true;
    }

    public static boolean isValidBookCover(MultipartFile photo) {
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
