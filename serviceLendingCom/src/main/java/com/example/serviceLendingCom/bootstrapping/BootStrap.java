package com.example.serviceLendingCom.bootstrapping;

import com.example.serviceLendingCom.lendingManagementCom.model.*;
import com.example.serviceLendingCom.lendingManagementCom.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class BootStrap implements CommandLineRunner {
    private final LendingRepository lendingRepository;

    public static final Map<Month, String> funnyQuotes = new HashMap<>();

    static {
        funnyQuotes.put(Month.JANUARY, "New year, new you!");
        funnyQuotes.put(Month.FEBRUARY, "Love is in the air!");
        funnyQuotes.put(Month.MARCH, "Spring into action!");
        funnyQuotes.put(Month.APRIL, "April showers bring May flowers!");
        funnyQuotes.put(Month.MAY, "May the force be with you!");
        funnyQuotes.put(Month.JUNE, "Summer vibes!");
        funnyQuotes.put(Month.JULY, "Fireworks and freedom!");
        funnyQuotes.put(Month.AUGUST, "Endless summer!");
        funnyQuotes.put(Month.SEPTEMBER, "Back to school!");
        funnyQuotes.put(Month.OCTOBER, "Spooky season!");
        funnyQuotes.put(Month.NOVEMBER, "Thankful and grateful!");
        funnyQuotes.put(Month.DECEMBER, "Holly jolly holidays!");
    }

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final ReaderRepository readerRepository;

    //Lending Mock data
     @Override
     public void run(String... args) {
         if (lendingRepository.count() == 0) {

             Lending lending1 = new Lending("2024/1", 1L, 1L, "Orgulho e Preconceito", LocalDate.of(2024, 5, 3), LocalDate.of(2024, 5, 18), LocalDate.of(2024, 5, 10), true, 0.0f, "");
             Lending lending2 = new Lending("2024/2", 2L, 2L, "1984", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 10), false, 1.5f, "");
             Lending lending3 = new Lending("2024/3", 3L, 3L, "Moby Dick", LocalDate.of(2024, 7, 3), LocalDate.of(2024, 7, 18), LocalDate.of(2024, 7, 10), true, 0.0f, "");
             Lending lending4 = new Lending("2024/4", 4L, 4L, "O Senhor dos Anéis", LocalDate.of(2024, 8, 2), LocalDate.of(2024, 8, 16), LocalDate.of(2024, 8, 10), true, 0.0f, "");
             Lending lending5 = new Lending("2024/5", 5L, 5L, "Harry Potter e a Pedra Filosofal", LocalDate.of(2024, 9, 3), LocalDate.of(2024, 9, 17), LocalDate.of(2024, 9, 10), false, 2.0f, "");

             Lending lending6 = new Lending("2024/6", 6L, 6L, "Crime e Castigo", LocalDate.of(2024, 10, 4), LocalDate.of(2024, 10, 18), LocalDate.of(2024, 10, 11), true, 0.0f, "");
             Lending lending7 = new Lending("2024/7", 7L, 7L, "Dom Quixote", LocalDate.of(2024, 11, 5), LocalDate.of(2024, 11, 19), LocalDate.of(2024, 11, 12), false, 0.5f, "");
             Lending lending8 = new Lending("2024/8", 8L, 8L, "O Apanhador no Campo de Centeio", LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 17), LocalDate.of(2024, 12, 10), true, 0.0f, "");
             Lending lending9 = new Lending("2024/9", 9L, 9L, "As Aventuras de Huckleberry Finn", LocalDate.of(2024, 1, 6), LocalDate.of(2024, 1, 20), LocalDate.of(2024, 1, 10), true, 0.0f, "");
             Lending lending10 = new Lending("2024/10", 10L, 10L, "Guerra e Paz", LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 19), LocalDate.of(2024, 2, 12), false, 3.0f, "");

             Lending lending11 = new Lending("2024/11", 11L, 11L, "Jane Eyre", LocalDate.of(2024, 3, 7), LocalDate.of(2024, 3, 21), LocalDate.of(2024, 3, 14), true, 0.0f, "");
             Lending lending12 = new Lending("2024/12", 12L, 12L, "O Grande Gatsby", LocalDate.of(2024, 4, 8), LocalDate.of(2024, 4, 22), LocalDate.of(2024, 4, 15), true, 0.0f, "");
             Lending lending13 = new Lending("2024/13", 13L, 13L, "Anna Karenina", LocalDate.of(2024, 5, 10), LocalDate.of(2024, 5, 24), LocalDate.of(2024, 5, 17), true, 0.0f, "");
             Lending lending14 = new Lending("2024/14", 14L, 14L, "Drácula", LocalDate.of(2024, 6, 11), LocalDate.of(2024, 6, 25), LocalDate.of(2024, 6, 18), false, 1.0f, "");
             Lending lending15 = new Lending("2024/15", 15L, 15L, "Frankenstein", LocalDate.of(2024, 7, 12), LocalDate.of(2024, 7, 26), LocalDate.of(2024, 7, 19), true, 0.0f, "");

             Lending lending16 = new Lending("2024/16", 16L, 16L, "Odisseia", LocalDate.of(2024, 8, 13), LocalDate.of(2024, 8, 27), LocalDate.of(2024, 8, 20), true, 0.0f, "");
             Lending lending17 = new Lending("2024/17", 17L, 16L, "Odisseia", LocalDate.of(2024, 9, 14), LocalDate.of(2024, 9, 28), LocalDate.of(2024, 9, 21), true, 0.0f, "");
             Lending lending18 = new Lending("2024/18", 18L, 16L, "Odisseia", LocalDate.of(2024, 10, 15), LocalDate.of(2024, 10, 29), LocalDate.of(2024, 10, 22), true, 0.0f, "");
             Lending lending19 = new Lending("2024/19", 19L, 16L, "Odisseia", LocalDate.of(2024, 11, 16), LocalDate.of(2024, 11, 30), LocalDate.of(2024, 11, 23), false, 2.5f, "");
             Lending lending20 = new Lending("2024/20", 20L, 16L, "Odisseia", LocalDate.of(2024, 12, 17), LocalDate.of(2024, 12, 31), LocalDate.of(2024, 12, 24), true, 0.0f, "");

             Lending lending21 = new Lending("2024/21", 21L, 21L, "A Revolução dos Bichos", LocalDate.of(2024, 1, 18), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 25), true, 0.0f, "");
             Lending lending22 = new Lending("2024/22", 22L, 22L, "As Crónicas de Nárnia", LocalDate.of(2024, 3, 19), LocalDate.of(2024, 4, 2), LocalDate.of(2024, 3, 26), true, 0.0f, "");
             Lending lending23 = new Lending("2024/23", 23L, 23L, "Senhor das Moscas", LocalDate.of(2024, 4, 20), LocalDate.of(2024, 5, 4), LocalDate.of(2024, 4, 27), true, 0.0f, "");
             Lending lending24 = new Lending("2024/24", 24L, 24L, "Os Miseráveis", LocalDate.of(2024, 5, 21), LocalDate.of(2024, 6, 4), LocalDate.of(2024, 5, 28), false, 4.0f, "");
             Lending lending25 = new Lending("2024/25", 25L, 25L, "O Código Da Vinci", LocalDate.of(2024, 6, 22), LocalDate.of(2024, 7, 6), LocalDate.of(2024, 6, 29), true, 0.0f, "");

             Lending lending26 = new Lending("2024/26", 26L, 26L, "O Hobbit", LocalDate.of(2024, 7, 23), LocalDate.of(2024, 8, 6), LocalDate.of(2024, 7, 30), true, 0.0f, "");


             lendingRepository.saveAll(Arrays.asList(
                     lending1, lending2, lending3, lending4, lending5,
                     lending6, lending7, lending8, lending9, lending10,
                     lending11, lending12, lending13, lending14, lending15,
                     lending16, lending17, lending18, lending19, lending20,
                     lending21, lending22, lending23, lending24, lending25,
                     lending26
             ));



             // Genre Mock Data
             Genre genre1 = new Genre("Self-Improvement");
             Genre genre2 = new Genre("Science Fiction");
             Genre genre3 = new Genre("Mystery");
             Genre genre4 = new Genre("Romance");
             Genre genre5 = new Genre("Fantasy");
             Genre genre6 = new Genre("Non-Fiction");
             Genre genre7 = new Genre("Thriller");
             Genre genre8 = new Genre("Biography");
             Genre genre9 = new Genre("History");
             Genre genre10 = new Genre("Children's");

             genreRepository.saveAll(Arrays.asList(
                     genre1, genre2, genre3, genre4, genre5, genre6, genre7, genre8, genre9, genre10
             ));

             //Books Mock Data
             Book book1 = new Book("9781982137274", "The 7 Habits of Highly Effective People", genre1, null, "Powerful lessons in personal change.");
             Book book2 = new Book("9780735211292", "Atomic Habits", genre1, null, "Tiny Changes, Remarkable Results");
             Book book3 = new Book("9780671027032", "How to Win Friends and Influence People", genre1, null, "Timeless advice on building successful relationships.");
             Book book4 = new Book("9780345472328", "Mindset: The New Psychology of Success", genre2, null, "Discover the power of our mindset.");
             Book book5 = new Book("9781577314806", "The Power of Now", genre3, null, "A guide to spiritual enlightenment.");
             Book book6 = new Book("9781612681139", "Rich Dad Poor Dad", genre1, null, "What the Rich Teach Their Kids About Money That the Poor and Middle Class Do Not!");
             Book book7 = new Book("9780743273565", "The Great Gatsby", genre5, null, "A novel of the Jazz Age.");
             Book book8 = new Book("9780441172719", "Dune", genre4, null, "Science fiction novel about the son of a noble family.");
             Book book9 = new Book("9780316769488", "The Catcher in the Rye", genre6, null, "A story about teenage rebellion.");
             Book book10 = new Book("9780451524935", "1984", genre7, null, "A dystopian social science fiction novel.");
             Book book11 = new Book("9781400032716", "The Road", genre8, null, "A post-apocalyptic novel.");
             Book book12 = new Book("9780618640157", "The Hobbit", genre9, null, "A fantasy novel and children's book.");
             Book book13 = new Book("9780061120084", "To Kill a Mockingbird", genre6, null, "A novel about racial injustice.");
             Book book14 = new Book("9780439139595", "Harry Potter and the Goblet of Fire", genre9, null, "A young wizard's adventures.");
             Book book15 = new Book("9780307277671", "The Kite Runner", genre10, null, "A novel about the unlikely friendship between a wealthy boy and the son of his father's servant.");
             Book book16 = new Book("9780062316110", "Thinking, Fast and Slow", genre1, null, "A groundbreaking tour of the mind.");
             Book book17 = new Book("9780307949486", "The Immortal Life of Henrietta Lacks", genre2, null, "The story of a woman whose cells were used to create an immortal cell line.");
             Book book18 = new Book("9780385504201", "The Da Vinci Code", genre3, null, "A mystery thriller novel.");
             Book book19 = new Book("9780316015844", "Twilight", genre10, null, "A young adult vampire-romance novel.");
             Book book20 = new Book("9780140283334", "Siddhartha", genre3, null, "A novel about the spiritual journey of self-discovery.");

             bookRepository.saveAll(Arrays.asList(
                     book1, book2, book3, book4, book5, book6, book7, book8, book9, book10,
                     book11, book12, book13, book14, book15, book16, book17, book18, book19, book20
             ));

             //Authors Mock Data
             Author author1 = new Author("Stephen R. Covey", "An American educator, author, businessman, and keynote speaker.");
             Author author2 = new Author("James Clear", "An author and speaker focused on habits, decision-making, and continuous improvement.");
             Author author3 = new Author("Dale Carnegie", "An American writer and lecturer known for self-improvement courses.");
             Author author4 = new Author("Frank Herbert", "An American science fiction writer best known for the novel Dune.");
             Author author5 = new Author("Eckhart Tolle", "A spiritual teacher and author best known for his books The Power of Now and A New Earth.");
             Author author6 = new Author("Robert T. Kiyosaki", "An American businessman and author of Rich Dad Poor Dad.");
             Author author7 = new Author("J.D. Salinger", "An American writer known for his novel The Catcher in the Rye.");
             Author author8 = new Author("William Gibson", "An American-Canadian speculative fiction writer and essayist widely credited with pioneering the cyberpunk genre.");
             Author author9 = new Author("F. Scott Fitzgerald", "An American novelist and short story writer, widely regarded as one of the greatest writers of the 20th century.");
             Author author10 = new Author("J.K. Rowling", "A British author, best known for writing the Harry Potter fantasy series.");

             authorRepository.saveAll(Arrays.asList(
                     author1, author2, author3, author4, author5, author6, author7, author8, author9, author10
             ));

             //BookAuthor Mock Data
             BookAuthor bookAuthor1 = new BookAuthor(book1, author1);
             BookAuthor bookAuthor2 = new BookAuthor(book2, author2);
             BookAuthor bookAuthor3 = new BookAuthor(book3, author3);
             BookAuthor bookAuthor4 = new BookAuthor(book4, author4);
             BookAuthor bookAuthor5 = new BookAuthor(book5, author5);
             BookAuthor bookAuthor6 = new BookAuthor(book6, author6);
             BookAuthor bookAuthor7 = new BookAuthor(book7, author7);
             BookAuthor bookAuthor8 = new BookAuthor(book8, author8);
             BookAuthor bookAuthor9 = new BookAuthor(book9, author9);
             BookAuthor bookAuthor10 = new BookAuthor(book10, author10);
             BookAuthor bookAuthor11 = new BookAuthor(book11, author5);
             BookAuthor bookAuthor12 = new BookAuthor(book12, author8);
             BookAuthor bookAuthor13 = new BookAuthor(book13, author3);
             BookAuthor bookAuthor14 = new BookAuthor(book14, author3);
             BookAuthor bookAuthor15 = new BookAuthor(book15, author3);
             BookAuthor bookAuthor16 = new BookAuthor(book16, author10);
             BookAuthor bookAuthor17 = new BookAuthor(book17, author10);
             BookAuthor bookAuthor18 = new BookAuthor(book18, author10);
             BookAuthor bookAuthor19 = new BookAuthor(book19, author10);
             BookAuthor bookAuthor20 = new BookAuthor(book20, author10);
             BookAuthor bookAuthor21 = new BookAuthor(book2, author1);

             bookAuthorRepository.saveAll(Arrays.asList(
                     bookAuthor1, bookAuthor2, bookAuthor3, bookAuthor4, bookAuthor5, bookAuthor6, bookAuthor7, bookAuthor8, bookAuthor9, bookAuthor10,
                     bookAuthor11, bookAuthor12, bookAuthor13, bookAuthor14, bookAuthor15, bookAuthor16, bookAuthor17, bookAuthor18, bookAuthor19, bookAuthor20, bookAuthor21
             ));


             Reader reader1 = new Reader("2024/1", "Ricardo Costa", "ricardocosta@gmail.com", LocalDate.of(1998, 2, 27), 910787100, true, Arrays.asList("Science Fiction", "Fantasy"), funnyQuotes.getOrDefault(LocalDate.of(1998, 2, 27).getMonth(), "Every day is a great day!"));
             Reader reader2 = new Reader("2024/2", "Joao Fonsenca", "joao1236@isep.ipp.pt", LocalDate.of(2000, 9, 20), 987654321, true, Arrays.asList("Mystery", "Thriller"), funnyQuotes.getOrDefault(LocalDate.of(2000, 9, 20).getMonth(), "Every day is a great day!"));
             Reader reader3 = new Reader("2024/3", "Victor Barbosa", "victorbarbosa@gmail.com", LocalDate.of(2005, 1, 11), 933342550, true, null, funnyQuotes.getOrDefault(LocalDate.of(2005, 1, 11).getMonth(), "Every day is a great day!"));
             Reader reader4 = new Reader("2024/4", "Guilherme Gouveia", "guilhermeGouveia@outlook.com", LocalDate.of(1969, 3, 12), 917584125, true, Arrays.asList("Romance", "Non-Fiction"), funnyQuotes.getOrDefault(LocalDate.of(1969, 3, 12).getMonth(), "Every day is a great day!"));
             Reader reader5 = new Reader("2024/5", "Tiago Martins", "tiagomartins@gmail.com", LocalDate.of(1978, 3, 15), 916625978, true, Arrays.asList("Biography", "History"), funnyQuotes.getOrDefault(LocalDate.of(1978, 3, 15).getMonth(), "Every day is a great day!"));
             Reader reader6 = new Reader("2024/6", "Ana Silva", "ana.silva@gmail.com", LocalDate.of(1985, 5, 25), 912345678, true, Arrays.asList("Children's", "Self-Improvement"), funnyQuotes.getOrDefault(LocalDate.of(1985, 5, 25).getMonth(), "Every day is a great day!"));
             Reader reader7 = new Reader("2024/7", "Carlos Santos", "carlos.santos@hotmail.com", LocalDate.of(1992, 8, 10), 913456789, true,null, funnyQuotes.getOrDefault(LocalDate.of(1992, 8, 10).getMonth(), "Every day is a great day!"));
             Reader reader8 = new Reader("2024/8", "Mariana Pereira", "mariana.pereira@yahoo.com", LocalDate.of(1980, 11, 22), 914567890, true, Arrays.asList("Science Fiction", "Mystery"), funnyQuotes.getOrDefault(LocalDate.of(1980, 11, 22).getMonth(), "Every day is a great day!"));
             Reader reader9 = new Reader("2024/9", "José Ferreira", "jose.ferreira@gmail.com", LocalDate.of(1975, 6, 30), 915678901, true, null, funnyQuotes.getOrDefault(LocalDate.of(1975, 6, 30).getMonth(), "Every day is a great day!"));
             Reader reader10 = new Reader("2024/10", "Sara Gomes", "sara.gomes@outlook.com", LocalDate.of(1999, 7, 18), 916789012, true, Arrays.asList("Fantasy", "Romance"), funnyQuotes.getOrDefault(LocalDate.of(1999, 7, 18).getMonth(), "Every day is a great day!"));
             Reader reader11 = new Reader("2024/11", "Miguel Rocha", "miguel.rocha@gmail.com", LocalDate.of(2002, 2, 2), 917890123, true, null, funnyQuotes.getOrDefault(LocalDate.of(2002, 2, 2).getMonth(), "Every day is a great day!"));
             Reader reader12 = new Reader("2024/12", "Luisa Marques", "luisa.marques@isep.ipp.pt", LocalDate.of(1994, 12, 15), 918901234, true, Arrays.asList("Non-Fiction", "Thriller"), funnyQuotes.getOrDefault(LocalDate.of(1994, 12, 15).getMonth(), "Every day is a great day!"));
             Reader reader13 = new Reader("2024/13", "Rui Almeida", "rui.almeida@gmail.com", LocalDate.of(1988, 4, 27), 919012345, true, null, funnyQuotes.getOrDefault(LocalDate.of(1988, 4, 27).getMonth(), "Every day is a great day!"));
             Reader reader14 = new Reader("2024/14", "Beatriz Sousa", "beatriz.sousa@hotmail.com", LocalDate.of(1970, 9, 9), 910123456, true, Arrays.asList("Biography", "Self-Improvement"), funnyQuotes.getOrDefault(LocalDate.of(1970, 9, 9).getMonth(), "Every day is a great day!"));
             Reader reader15 = new Reader("2024/15", "Pedro Mendes", "pedro.mendes@yahoo.com", LocalDate.of(1991, 1, 19), 911234567, true, Arrays.asList("Children's", "Science Fiction"), funnyQuotes.getOrDefault(LocalDate.of(1991, 1, 19).getMonth(), "Every day is a great day!"));
             Reader reader16 = new Reader("2024/16", "Clara Oliveira", "clara.oliveira@gmail.com", LocalDate.of(1983, 8, 5), 912345678, true, null, funnyQuotes.getOrDefault(LocalDate.of(1983, 8, 5).getMonth(), "Every day is a great day!"));
             Reader reader17 = new Reader("2024/17", "André Ribeiro", "andre.ribeiro@outlook.com", LocalDate.of(1995, 3, 14), 913456789, true, Arrays.asList("Mystery", "Fantasy"), funnyQuotes.getOrDefault(LocalDate.of(1995, 3, 14).getMonth(), "Every day is a great day!"));
             Reader reader18 = new Reader("2024/18", "Teresa Lopes", "teresa.lopes@gmail.com", LocalDate.of(2001, 7, 29), 914567890, true, Arrays.asList("Romance", "Non-Fiction"), funnyQuotes.getOrDefault(LocalDate.of(2001, 7, 29).getMonth(), "Every day is a great day!"));
             Reader reader19 = new Reader("2024/19", "Hugo Cunha", "hugo.cunha@gmail.com", LocalDate.of(1972, 10, 21), 915678901, true, Arrays.asList("Thriller", "Biography"), funnyQuotes.getOrDefault(LocalDate.of(1972, 10, 21).getMonth(), "Every day is a great day!"));
             Reader reader20 = new Reader("2024/20", "Marta Faria", "marta.faria@yahoo.com", LocalDate.of(1986, 12, 3), 916789012, true, Arrays.asList("History", "Self-Improvement"), funnyQuotes.getOrDefault(LocalDate.of(1986, 12, 3).getMonth(), "Every day is a great day!"));
             readerRepository.saveAll(Arrays.asList(
                     reader1, reader2, reader3, reader4, reader5, reader6, reader7, reader8, reader9, reader10,
                     reader11, reader12, reader13, reader14, reader15, reader16, reader17, reader18, reader19, reader20
             ));

         }
     }
}
