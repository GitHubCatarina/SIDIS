package com.example.serviceBook.authorManagement.syncAuthor;

import com.example.serviceBook.authorManagement.model.Author;
import com.example.serviceBook.authorManagement.dto.AuthorDTO;
import com.example.serviceBook.authorManagement.model.AuthorPhoto;
import com.example.serviceBook.authorManagement.repository.AuthorPhotoRepository;
import com.example.serviceBook.authorManagement.repository.AuthorRepository;
import com.example.serviceBook.bookManagement.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class AuthorSyncService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorPhotoRepository authorPhotoRepository;
    public AuthorSyncService(AuthorRepository authorRepository, BookRepository bookRepository, AuthorPhotoRepository authorPhotoRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;

        this.authorPhotoRepository = authorPhotoRepository;
    }

    @Transactional
    public void syncData(AuthorSyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);

        Long authorId = syncRequest.getId();
        if (authorId == null) {
            throw new IllegalArgumentException("authorID não pode ser nulo");
        }

        Author existingAuthor = authorRepository.findById(authorId).orElse(null);


        if (existingAuthor != null) {
            updateAuthor(existingAuthor.getId(), syncRequest.getResource(), existingAuthor.getVersion());
            System.out.println("Recurso atualizado com sucesso.");
        } else {
            createAuthor(syncRequest.getResource());
            System.out.println("Recurso criado com sucesso.");
        }
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO, long desiredVersion) {
        // Busca o autor existente no repositório pelo ID
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found with id " + id));

        // Aplica as alterações usando versionamento otimista
        author.updateData(desiredVersion, authorDTO.getName(), authorDTO.getShortBio());
        author.setLents(authorDTO.getLents());

        // Caso haja uma foto associada, atualiza-a (se necessário)
        if (authorDTO.getAuthorPhotoId() != null) {
            AuthorPhoto photo = authorPhotoRepository.findById(authorDTO.getAuthorPhotoId())
                    .orElseThrow(() -> new NoSuchElementException("Author photo not found with id " + authorDTO.getAuthorPhotoId()));
            author.setAuthorPhoto(photo);
        } else {
            author.setAuthorPhoto(null); // Remove a foto caso o ID seja nulo
        }

        // Salva as alterações no banco de dados
        author = authorRepository.save(author);

        // Retorna o AuthorDTO atualizado
        return AuthorDTO.fromEntity(author);
    }


    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        // Mapeia o DTO para a entidade Author
        Author author = AuthorDTO.toEntity(authorDTO);

        // Salva o autor no repositório
        author = authorRepository.save(author);

        // Retorna o AuthorDTO criado a partir da entidade salva
        return AuthorDTO.fromEntity(author);
    }


}
