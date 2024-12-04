package com.example.serviceReaderCom.readerManagement.services;

import com.example.serviceReaderCom.readerManagement.api.LendingReaderView;
import com.example.serviceReaderCom.readerManagement.api.ReaderView;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.serviceReaderCom.client.LendingServiceClient;
import com.example.serviceReaderCom.exceptions.NotFoundException;
import com.example.serviceReaderCom.fileStorage.FileStorageService;
import com.example.serviceReaderCom.fileStorage.UploadFileResponse;
import com.example.serviceReaderCom.readerManagement.api.*;
import com.example.serviceReaderCom.readerManagement.model.Reader;
import com.example.serviceReaderCom.readerManagement.model.ReaderPhoto;
import com.example.serviceReaderCom.readerManagement.repositories.ReaderPhotoRepository;
import com.example.serviceReaderCom.readerManagement.repositories.ReaderRepository;
import com.example.serviceReaderCom.readerManagement.util.ReaderUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.serviceReaderCom.readerManagement.util.ReaderUtil.isValidReaderPhoto;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final RestTemplate restTemplate;
    private final ReaderRepository readerRepository;
    private final EditReaderMapper editReaderMapper;
    private final FileStorageService fileStorageService;
    private final ReaderPhotoRepository readerPhotoRepository;
    private final LendingServiceClient lendingServiceClient;
    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository, EditReaderMapper editReaderMapper, ReaderPhotoRepository readerPhotoRepository, FileStorageService fileStorageService, RestTemplate restTemplate, LendingServiceClient lendingServiceClient) {
        this.readerRepository = readerRepository;
        this.editReaderMapper = editReaderMapper;
        this.fileStorageService = fileStorageService;
        this.readerPhotoRepository = readerPhotoRepository;
        this.restTemplate = restTemplate;
        this.lendingServiceClient = lendingServiceClient;
    }

    public Page<Reader> getReadersByName(final String name, Pageable pageable) {
        List<Reader> filteredReaders =  readerRepository.findAll()
                .stream()
                .filter(reader -> reader.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return toPage(filteredReaders, pageable);
    }

    public Page<Reader> getReaders(Pageable pageable) {
        List<Reader> readers = readerRepository.findAll(pageable).getContent();
        readers.forEach(this::updateAge);
        return new PageImpl<>(readers, pageable, readerRepository.count());    }

    public Iterable<Reader> getAllReaders() {
        List<Reader> readers = readerRepository.findAll();
        readers.forEach(this::updateAge);
        return readers;
    }



    public Optional<Reader> getReaderByIdWithQuote(Long id) {
        Optional<Reader> readerOpt = readerRepository.findReaderById(id);
        readerOpt.ifPresent(this::updateAge);
       if (readerOpt.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Cannot find Reader");
        }
        return readerOpt;
    }

    public Reader createReader(final EditReaderRequest resource, MultipartFile photo) {
        validateCreateReaderRequest(resource);
        Reader reader = editReaderMapper.create(resource);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(reader.getDateOfBirth(), currentDate);
        Integer age = period.getYears();
        reader.setAge(age);
        String readerCode = java.time.Year.now().getValue() + "/" + (readerRepository.findMaxReaderId() + 1);
        reader.setReaderCode(readerCode);
        reader.setInterests(resource.getInterests());

        readerRepository.save(reader);

        if (photo != null) {
            doUploadFile(reader.getId().toString(), photo);
        }

        return readerRepository.save(reader);
    }

    @Transactional
    public Reader updateReader(final Long id, final EditReaderRequest resource, final long desiredVersion) {
        final var reader = readerRepository.findById(id).orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));
        reader.updateData(desiredVersion, resource.getName(), resource.getEmail(), resource.getDateOfBirth(), resource.getPhoneNumber(), resource.getGDBRConsent(), resource.getInterests());
        return readerRepository.save(reader);
    }

    public Reader partialUpdateReader(final Long id, final EditReaderRequest resource, final long desiredVersion) {
        final var reader = readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));
        reader.applyPatch(desiredVersion, resource.getName(), resource.getEmail(), resource.getDateOfBirth(), resource.getPhoneNumber(), resource.getGDBRConsent(), resource.getInterests());
        return readerRepository.save(reader);
    }

    private void validateCreateReaderRequest(final EditReaderRequest request) {

        if (!request.getGDBRConsent()) {
            throw new IllegalArgumentException("[ERROR] GDBR consent must be true to register a new reader");
        }

        Optional<Reader> existingReader = readerRepository.findByEmail(request.getEmail());
        if (existingReader.isPresent()) {
            throw new IllegalArgumentException("[ERROR] Reader with email " + request.getEmail() + " already exists");
        }

        if (!ReaderUtil.isValidDateOfBirth(request.getDateOfBirth())) {
            throw new IllegalArgumentException("[ERROR] Invalid date of birth. Reader must be at least 12 years old.");
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().toString().length() != 9) {
            throw new IllegalArgumentException("[ERROR] Phone number must be exactly 9 digits long.");
        }

        if (!ReaderUtil.isValidName(request.getName())) {
            throw new IllegalArgumentException("[ERROR] Invalid reader name.");
        }
        validateGenres(request.getInterests());

    }

    private void validateGenres(List<String> interests) {
        if (interests != null && !interests.isEmpty()) {
            List<String> invalidGenres = interests.stream()
                    .filter(genre -> !ReaderUtil.VALID_GENRES.contains(genre))
                    .collect(Collectors.toList());
            if (!invalidGenres.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] Invalid genres: " + String.join(", ", invalidGenres));
            }
        }
    }

    public UploadFileResponse doUploadFile(final String id, final MultipartFile file) {
        if (isValidReaderPhoto(file)) {
            ReaderPhoto photo = new ReaderPhoto();
            try {
                photo.setImage(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            photo.setContentType(file.getContentType());
            readerPhotoRepository.save(photo);
            Reader reader = readerRepository.getById(Long.parseLong(id));
            reader.setReaderPhoto(photo);
            readerRepository.save(reader);
        }

        final String fileName = fileStorageService.storeFile(id, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
                .toUriString();

        fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    private Page<Reader> toPage(List<Reader> readers, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), readers.size());
        List<Reader> sublist = readers.subList(start, end);
        sublist.forEach(this::updateAge);
        return new PageImpl<>(sublist, pageable, readers.size());
    }

    private void updateAge(Reader reader) {
        if (reader.getDateOfBirth() != null) {
            reader.setAge(Period.between(reader.getDateOfBirth(), LocalDate.now()).getYears());
        }
    }


    public List<String> getUserRolesFromAuthService(String username) {
        String authServiceUrl = "http://localhost:8081/api/auth/roles/" + username;

        ResponseEntity<String[]> response = restTemplate.getForEntity(authServiceUrl, String[].class);
        return Arrays.asList(response.getBody());
    }


    private Optional<Reader> getReaderById(Long readerId) {
        return readerRepository.findReaderById(readerId);
    }

    public boolean readerExists(String readerCode) {
        return readerRepository.findByReaderCode(readerCode).isPresent();
    }



}
