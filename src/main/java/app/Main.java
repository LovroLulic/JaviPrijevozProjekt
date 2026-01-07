package app;

import entities.*;
import services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;

/**
 * Glavna klasa aplikacije za upravljanje sustavom javnog prijevoza.
 * Koordinira interakciju između korisnika, vozila, ruta i cjenika.
 * Pruža korisničko sučelje s mogućnostima unosa podataka i pretraživanja.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Glavna metoda koja pokreće aplikaciju.
     * Inicijalizira sve potrebne komponente, prikazuje korisničko sučelje
     * i upravlja glavnom navigacijom kroz program.
     *
     * @param args argumenti komandne linije (ne koriste se)
     */
    public static void main(String[] args) {
        log.info("Program pokrenut");
        Scanner scanner = new Scanner(System.in);

        // Setup
        VozilaManager vozila = new VozilaManager();
        RuteManager rute = new RuteManager(vozila);
        KorisnikManager korisnici = new KorisnikManager();
        CijenaKarte cjenik = new CijenaKarte(
                new BigDecimal("2.00"), new BigDecimal("1.00"), new BigDecimal("1.5"),
                new BigDecimal("4.5"), new BigDecimal("2.5"), new BigDecimal("3")
        );

        // Početni podaci
        vozila.ucitajSvaVozila();
        if (vozila.getSvaVozila().isEmpty()){
            log.info("JSON prazan, dodajem početna vozila...");
            vozila.dodajPocetnaVozila();
        }
        rute.ucitajRute();
        if(rute.getSveRute().isEmpty()){
            log.info("JSON prazan, dodajem pocetne rute...");
            rute.dodajPocetneRute(cjenik);
        }

        // Login
        User korisnik = korisnici.login(scanner);
        log.info("Dobrodošao {}!", korisnik.getName());

        // Glavna petlja
        int izbor;
        do {
            log.info("\n=== GLAVNI MENU ===");
            log.info("1) Unos podataka");
            log.info("2) Pretraživanje");
            log.info("3) Spremi Backup");
            log.info("4) Izvrsi Backup");
            log.info("5) Izlaz");
            log.info("Odabir: ");

            izbor = scanner.nextInt();
            scanner.nextLine();

            switch(izbor) {
                case 1 -> menuUnos(scanner, korisnik, vozila, rute, korisnici, cjenik);
                case 2 -> menuPretraga(scanner, vozila, rute);
                case 3 -> {
                    log.info("Spremanje sigurnosne kopije...");
                    BackupData trenutniPodaci = new BackupData(rute.getSveRute(), vozila.getSvaVozila());
                    SerializationService.kreirajBackup(trenutniPodaci);
                    log.info("Backup uspješno spremljen u backup.bin!");
                }
                case 4 -> {
                    log.info("Vraćanje podataka iz backupa...");
                    BackupData ucitaniBackup = SerializationService.ucitajBackup();

                    if (ucitaniBackup != null) {
                        // Ovdje "pregažujemo" trenutne liste s onima iz datoteke
                        vozila.osvjeziPodatke(ucitaniBackup.getVehicles());
                        rute.osvjeziPodatke(ucitaniBackup.getRoutes());
                        log.info("Podaci su uspješno vraćeni i pregaženi!");
                    } else {
                        log.error("Greška: Backup datoteka nije pronađena ili je neispravna.");
                    }
                }
                case 5 -> log.info("Hvala i doviđenja!");
                default -> log.info("Krivi odabir!");
            }
        } while(izbor != 5);

        scanner.close();
        log.info("Program završen");
    }









    /**
     * Prikazuje i upravlja podizbornikom za unos novih podataka.
     * Za pristup ovom izborniku potrebne su administratorske ovlasti.
     *
     * @param scanner Scanner objekt za unos korisničkih podataka
     * @param korisnik trenutno prijavljeni korisnik
     * @param vozila manager za upravljanje vozilima
     * @param rute manager za upravljanje rutama
     * @param korisnici manager za upravljanje korisnicima
     * @param cjenik cjenik karata za izračun cijena ruta
     */
    private static void menuUnos(Scanner scanner, User korisnik, VozilaManager vozila,
                                 RuteManager rute, KorisnikManager korisnici, CijenaKarte cjenik) {
        if(!korisnici.jeAdmin(korisnik, scanner)) return;

        int izbor;
        do {
            log.info("\n1) Novo vozilo");
            log.info("2) Nova ruta");
            log.info("3) Nazad");
            log.info("Odabir: ");
            izbor = scanner.nextInt();
            scanner.nextLine();

            if(izbor == 1) unosVozila(scanner, vozila);
            else if(izbor == 2) rute.unosNoveRute(scanner, cjenik);
        } while(izbor != 3);
    }

    /**
     * Omogućuje unos novog vozila u sustav.
     * Provjerava jedinstvenost registracije i validnost unesenih podataka.
     * Podržava dva tipa vozila: autobus (Bus) i tramvaj (Tramvaj).
     *
     * @param scanner Scanner objekt za unos podataka o vozilu
     * @param vozila manager u koji se dodaje novo vozilo
     */
    private static void unosVozila(Scanner scanner, VozilaManager vozila) {
        log.info("Registracija: ");
        String reg = scanner.nextLine().trim();
        if (reg.isEmpty()) {
            log.info("Registracija ne smije biti prazna!");
            return;
        }
        if(vozila.voziloPostoji(reg)) {
            log.info("Već postoji!");
            return;
        }

        String tip;
        while (true) {
            log.info("Tip (Bus/Tramvaj): ");
            tip = scanner.nextLine().toLowerCase();
            if (tip.equals("bus") || tip.equals("tramvaj")) {
                break;
            }
            log.info("Pogrešan tip! Unesite 'Bus' ili 'Tramvaj'");
        }

        log.info("Boja: ");
        String boja = scanner.nextLine().trim();
        if (boja.isEmpty()) {
            log.info("Boja ne smije biti prazna!");
            return;
        }

        int godina;
        while (true) {
            try {
                log.info("Godina: ");
                godina = scanner.nextInt();
                scanner.nextLine();
                if (godina < 1900 || godina > 2024) {
                    log.info("Godina mora biti između 1900 i 2024!");
                } else {
                    break;
                }
            } catch (InputMismatchException _) {
                log.info("Morate unijeti broj!");
                scanner.nextLine();
            }
        }

        Vehicle novo = tip.equals("bus") ?
                new Bus(reg, boja, godina) : new Tramvaj(reg, boja, godina);
        vozila.dodajVozilo(novo);
        log.info("Vozilo dodano!");
    }

    /**
     * Prikazuje i upravlja podizbornikom za pretraživanje podataka.
     * Nudi različite načine pretrage vozila i ruta.
     *
     * @param scanner Scanner objekt za unos kriterija pretrage
     * @param vozila manager za pretragu vozila
     * @param rute manager za pretragu ruta
     */
    private static void menuPretraga(Scanner scanner, VozilaManager vozila, RuteManager rute) {
        int izbor;
        do {
            log.info("\n=== PRETRAGA ===");
            log.info("1) Po registraciji");
            log.info("2) Po stanici");
            log.info("3) Sve rute");
            log.info("4) Sva vozila");
            log.info("5) Električna vozila");
            log.info("6) Dostupnost");
            log.info("7) Najkraća/najduža ruta");
            log.info("8) Najnovije/najstarije vozilo");
            log.info("9) Nazad");
            log.info("Odabir: ");
            izbor = scanner.nextInt();
            scanner.nextLine();

            switch(izbor) {
                case 1 -> pretragaRegistracija(scanner, rute);
                case 2 -> pretragaStanica(scanner, rute);
                case 3 -> rute.ispisiSveRute();
                case 4 -> vozila.ispisiSvaVozila();
                case 5 -> vozila.ispisiElektricnaVozila();
                case 6 -> rute.ispisiDostupnostVozila();
                case 7 -> menuKilometraza(rute);
                case 8 -> menuGodineProizvodnje(vozila);
                default -> log.info("Krivi odabir!");
            }
        } while(izbor != 9);
    }

    /**
     * Pretražuje rute po registracijskoj oznaci vozila.
     * Prikazuje sve rute na kojima se koristi vozilo s unesenom registracijom.
     *
     * @param scanner Scanner objekt za unos registracije
     * @param rute manager koji vrši pretragu ruta
     */
    private static void pretragaRegistracija(Scanner scanner, RuteManager rute) {
        log.info("Unesite registraciju: ");
        String reg = scanner.nextLine();
        List<Route> pronadjeno = rute.nadjiRutePoRegistraciji(reg);
        if (pronadjeno.isEmpty()) {
            log.info("Nema ruta za tu registraciju!");
        } else {
            pronadjeno.forEach(Route::ispis);
        }
    }

    /**
     * Pretražuje rute po nazivu stanice.
     * Prikazuje sve rute koje polaze ili dolaze na unesenu stanicu.
     *
     * @param scanner Scanner objekt za unos naziva stanice
     * @param rute manager koji vrši pretragu ruta
     */
    private static void pretragaStanica(Scanner scanner, RuteManager rute) {
        log.info("Unesite stanicu: ");
        String stanica = scanner.nextLine();
        List<Route> pronadjeno = rute.nadjiRutePoStanici(stanica);
        if (pronadjeno.isEmpty()) {
            log.info("Nema ruta za tu stanicu!");
        } else {
            pronadjeno.forEach(Route::ispis);
        }
    }

    /**
     * Prikazuje podizbornik za pretragu najkraće i najduže rute.
     * Omogućuje korisniku odabir želi li vidjeti najkraću ili najdužu rutu.
     *
     * @param rute manager koji sadrži podatke o rutama
     */
    private static void menuKilometraza(RuteManager rute) {
        log.info("\n1) Najkraća linija");
        log.info("2) Najduža linija");
        log.info("Odabir: ");

        Scanner temp = new Scanner(System.in);
        int izbor = temp.nextInt();

        if (izbor == 1) {
            rute.getNajkracaRuta().ifPresentOrElse(
                    ruta -> {
                        log.info("=== NAJKRAĆA LINIJA ===");
                        ruta.ispis();
                    },
                    () -> log.info("Nema linija!")
            );
        } else if (izbor == 2) {
            rute.getNajduzaRuta().ifPresentOrElse(
                    ruta -> {
                        log.info("=== NAJDUŽA LINIJA ===");
                        ruta.ispis();
                    },
                    () -> log.info("Nema linija!")
            );
        }
    }

    /**
     * Prikazuje podizbornik za pretragu najnovijeg i najstarijeg vozila.
     * Omogućuje korisniku odabir želi li vidjeti najnovije ili najstarije vozilo.
     *
     * @param vozila manager koji sadrži podatke o vozilima
     */
    private static void menuGodineProizvodnje(VozilaManager vozila) {
        log.info("\n1) Najnovije vozilo");
        log.info("2) Najstarije vozilo");
        log.info("Odabir: ");

        Scanner temp = new Scanner(System.in);
        int izbor = temp.nextInt();

        if (izbor == 1) {
            vozila.getNajnovijeVozilo().ifPresentOrElse(
                    vozilo -> {
                        log.info("=== NAJNOVIJE VOZILO ===");
                        vozilo.ispis();
                    },
                    () -> log.info("Nema vozila!")
            );
        } else if (izbor == 2) {
            vozila.getNajstarijeVozilo().ifPresentOrElse(
                    vozilo -> {
                        log.info("=== NAJSTARIJE VOZILO ===");
                        vozilo.ispis();
                    },
                    () -> log.info("Nema vozila!")
            );
        }
    }
}