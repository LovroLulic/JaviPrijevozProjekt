package services;

import entities.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class KorisnikManager {

    public User login(Scanner scanner) {
        System.out.println("=== PRIJAVA ===");

        System.out.print("Unesite ime i prezime: ");
        String ime = unosImePrezime(scanner);

        System.out.print("Unesite broj godina: ");
        int godine = unosGodine(scanner);

        System.out.print("Unesite username: ");
        String username = unosUsername(scanner);

        System.out.print("Unesite email: ");
        String email = unosEmail(scanner);

        return new User(ime, godine, username, email);
    }

    private String unosImePrezime(Scanner scanner) {
        while (true) {
            String ime = scanner.nextLine().trim();
            if (ime.isEmpty()) {
                System.out.print("Ime ne smije biti prazno. Pokušajte ponovo: ");
            } else if (ime.split(" ").length < 2) {
                System.out.print("Unesite i ime i prezime. Pokušajte ponovo: ");
            } else {
                return ime;
            }
        }
    }

    private int unosGodine(Scanner scanner) {
        while (true) {
            try {
                int godine = scanner.nextInt();
                scanner.nextLine();
                if (godine < 10 || godine > 100) {
                    System.out.print("Godine moraju biti između 10 i 100. Pokušajte ponovo: ");
                } else {
                    return godine;
                }
            } catch (InputMismatchException e) {
                System.out.print("Morate unijeti broj. Pokušajte ponovo: ");
                scanner.nextLine();
            }
        }
    }

    private String unosUsername(Scanner scanner) {
        while (true) {
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.print("Username ne smije biti prazan. Pokušajte ponovo: ");
            } else {
                return username;
            }
        }
    }

    private String unosEmail(Scanner scanner) {
        while (true) {
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.print("Email ne smije biti prazan. Pokušajte ponovo: ");
            } else if (!email.contains("@")) {
                System.out.print("Email mora sadržavati @. Pokušajte ponovo: ");
            } else {
                return email;
            }
        }
    }

    public boolean jeAdmin(User korisnik, Scanner scanner) {
        if (!korisnik.getNameID().equals("llulic")) {
            System.out.println("Nemate admin ovlasti!");
            return false;
        }

        System.out.print("Unesite admin lozinku: ");
        String lozinka = scanner.nextLine();

        if (!lozinka.equals("admin123")) {
            System.out.println("Kriva lozinka!");
            return false;
        }

        System.out.println("Admin prijava uspješna!");
        return true;
    }
}
