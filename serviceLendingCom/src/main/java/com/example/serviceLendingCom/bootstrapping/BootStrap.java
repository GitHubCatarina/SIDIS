package com.example.serviceLendingCom.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import com.example.serviceLendingCom.lendingManagementCom.repositories.LendingRepository;

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
         }
     }
}
