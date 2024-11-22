package com.example.serviceBookCom.bookManagementCom.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "BookGenreView")
public class BookGenreView {
    String name;
}
