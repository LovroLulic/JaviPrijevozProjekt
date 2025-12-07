package services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UnosPodataka {

    private LocalDate unesiDatum(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        while (true) {
            try {
                System.out.print("Unesite datum (dd.MM.yyyy): ");
                String datumText = scanner.nextLine();
                return LocalDate.parse(datumText, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Pogrešan format datuma! Koristite format dd.MM.yyyy");
            }
        }
    }
    public static String unesiText(Scanner scanner, String poruka) {
        System.out.print(poruka);
        return scanner.nextLine();
    }

    public static int unesiBroj(Scanner scanner, String poruka) {
        System.out.print(poruka);
        while (true) {
            try {
                int broj = scanner.nextInt();
                scanner.nextLine();
                return broj;
            } catch (Exception e) {
                System.out.print("Morate unijeti broj. Pokušajte ponovo: ");
                scanner.nextLine();
            }
        }
    }
}