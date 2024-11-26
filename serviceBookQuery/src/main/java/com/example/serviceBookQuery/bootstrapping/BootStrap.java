package com.example.serviceBookQuery.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.example.serviceBookQuery.authorManagement.model.Author;
import com.example.serviceBookQuery.bookManagement.model.BookAuthor;
import com.example.serviceBookQuery.authorManagement.repository.AuthorRepository;
import com.example.serviceBookQuery.bookManagement.model.Genre;
import com.example.serviceBookQuery.bookManagement.repositories.BookAuthorRepository;
import com.example.serviceBookQuery.bookManagement.model.Book;
import com.example.serviceBookQuery.bookManagement.repositories.BookRepository;
import com.example.serviceBookQuery.bookManagement.repositories.GenreRepository;

import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class BootStrap implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final GenreRepository genreRepository;

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

    @Override
    public void run(String... args) {
        if (bookRepository.count() == 0) {


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
        }
    }
}
