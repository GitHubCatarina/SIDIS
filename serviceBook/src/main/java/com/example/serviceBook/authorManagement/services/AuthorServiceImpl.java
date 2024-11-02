package com.example.serviceBook.authorManagement.services;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.serviceBook.authorManagement.model.Author;
import com.example.serviceBook.authorManagement.model.AuthorPhoto;
import com.example.serviceBook.authorManagement.repository.AuthorPhotoRepository;
import com.example.serviceBook.authorManagement.repository.AuthorRepository;
import com.example.serviceBook.exceptions.NotFoundException;
import com.example.serviceBook.fileStorage.FileStorageService;
import com.example.serviceBook.fileStorage.UploadFileResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.serviceBook.authorManagement.util.AuthorUtil.isValidAuthorPhoto;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final EditAuthorMapper editAuthorMapper;
    private final AuthorPhotoRepository authorPhotoRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, EditAuthorMapper editAuthorMapper, AuthorPhotoRepository authorPhotoRepository, FileStorageService fileStorageService){
        this.authorRepository = authorRepository;
        this.editAuthorMapper = editAuthorMapper;
        this.authorPhotoRepository = authorPhotoRepository;
        this.fileStorageService = fileStorageService;
    }

    public int getTotalPages() {
        long totalAuthors = authorRepository.count();
        return (int) Math.ceil((double) totalAuthors / 5);
    }

    public Page<Author> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Optional<Author> getAuthorsById(final Long id) {
        return authorRepository.findAuthorById(id);
    }

    public AuthorPhoto getAuthorPhoto(final String authorId) {
        final var existingAuthor = authorRepository.findById(Long.parseLong(authorId)).orElseThrow(() -> new NotFoundException("[ERROR] Author not found"));

        if (existingAuthor.getAuthorPhoto() == null) {
            throw new IllegalArgumentException("[ERROR] Author Photo not found with ID: " + existingAuthor.getId());
        }

        return existingAuthor.getAuthorPhoto();
    }

    public List<Author> getAuthorsByName(final String name) {
        return authorRepository.findAll()
                .stream()
                .filter(author -> author
                        .getName()
                        .toLowerCase()
                        .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Author createAuthor(final EditAuthorRequest resource, MultipartFile authorPhoto) {
        validateCreateAuthorRequest(resource);

        Author author = editAuthorMapper.create(resource);

        authorRepository.save(author);

        if (authorPhoto != null) {
            doUploadFile(author.getId().toString(), authorPhoto);
        }

        return authorRepository.save(author);
    }
    @Transactional
    public Author updateAuthor(final Long id, final EditAuthorRequest resource, final long desiredVersion) {
        final var author = authorRepository.findById(id).orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));
        validateCreateAuthorRequest(resource);
        author.updateData(desiredVersion, resource.getName(), resource.getShortBio());
        return authorRepository.save(author);
    }

    public Author partialUpdateAuthor(final Long id, final EditAuthorRequest resource, final long desiredVersion) {
        final var author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[ERROR] Cannot update an object that does not yet exist"));

        author.applyPatch(desiredVersion, resource.getName(), resource.getShortBio());

        return authorRepository.save(author);
    }

    public UploadFileResponse doUploadFile(final String id, final MultipartFile file) {
        System.out.println(isValidAuthorPhoto(file));
        if (isValidAuthorPhoto(file)) {

            AuthorPhoto authorPhoto = new AuthorPhoto();
            try {
                authorPhoto.setImage(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            authorPhoto.setContentType(file.getContentType());
            authorPhotoRepository.save(authorPhoto);
            Author author = authorRepository.getById(Long.parseLong(id));
            author.setAuthorPhoto(authorPhoto);
            authorRepository.save(author);
        }

        final String fileName = fileStorageService.storeFile(id, file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(fileName)
                .toUriString();

        fileDownloadUri = fileDownloadUri.replace("/photos/", "/photo/");

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    public void validateCreateAuthorRequest(final EditAuthorRequest request) {
        if (StringUtils.isBlank(request.getName()) || StringUtils.isBlank(request.getShortBio())) {
            throw new IllegalArgumentException("[ERROR] Name and shortBio are mandatory.");
        }
    }
}