package com.example.serviceReaderCom.readerManagement.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.example.serviceReaderCom.fileStorage.UploadFileResponse;
import com.example.serviceReaderCom.readerManagement.api.ReaderView;
import com.example.serviceReaderCom.readerManagement.model.Reader;
import com.example.serviceReaderCom.readerManagement.model.ReaderPhoto;

import java.util.List;
import java.util.Optional;

public interface ReaderService {
     Reader createReader(final EditReaderRequest resource, MultipartFile photo);
    Reader updateReader(final Long id, final EditReaderRequest resource, final long desiredVersion);
    Reader partialUpdateReader(final Long id, final EditReaderRequest resource, final long desiredVersion);
    UploadFileResponse doUploadFile(final String id, final MultipartFile file);
}
