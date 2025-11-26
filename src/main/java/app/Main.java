package app;
import entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

/**
 * Predstavlja početnu točku sustava za upravljanje javnim prijevozom.
 * Upravlja korisničkom autentifikacijom, navigacijom kroz izbornike i koordinacijom između vozila i ruta.
 *
 * @author Lovro Lulic
 * @version 1.0
 */

public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.trace("POCETAK - main metoda");
        log.info("Pokrenut program!");

        Scanner scanner = new Scanner(System.in);

        User korisnik = new User("", 0, "", "");
        List<Vehicle> vehicles = new ArrayList<>();
        Set<String> registracije = new HashSet<>();
        Map<String, Vehicle> mapaVozila = new HashMap<>();
        List<Route> routes = new ArrayList<>();

        // Inicijalizacija vozila
        vehicles.add(new Tramvaj("ZG19055A", "Plava", 2019));
        registracije.add("ZG19055A".toLowerCase());
        vehicles.add(new Bus("ZG23045B", "Plava", 2012));
        registracije.add("ZG23045B".toLowerCase());
        vehicles.add(new Tramvaj("ZG01045C", "Bijela", 2023));
        registracije.add("ZG01045C".toLowerCase());
        vehicles.add(new Bus("ZG01045D", "Bijela", 2023));
        registracije.add("ZG01045D".toLowerCase());
        vehicles.add(new Bus("ZG01045E", "Zelena", 2023));
        registracije.add("ZG01045E".toLowerCase());
        vehicles.add(new Tramvaj("ZG01045F", "Roza", 2023));
        registracije.add("ZG01045F".toLowerCase());
        vehicles.add(new Bus("ZG01045G", "Crvena", 2023));
        registracije.add("ZG01045G".toLowerCase());

        popuniMapuVozilima(mapaVozila, vehicles);

        CijenaKarte cjenik = new CijenaKarte(
                new BigDecimal("2.00"), new BigDecimal("1.00"), new BigDecimal("1.5"),
                new BigDecimal("4.5"), new BigDecimal("2.5"), new BigDecimal("3")
        );

        // Inicijalizacija ruta
        routes.add(Route.builder(vehicles.getFirst(), LocalDate.of(2025, 12, 23))
                .time("19:04")
                .pocetnastanica("Velika Gorica")
                .krajnastanica("Aerodrom")
                .kilometers(new BigDecimal("6.24"))
                .cjenik(cjenik)
                .build());

        routes.add(Route.builder(vehicles.get(2), LocalDate.of(2025, 9, 12))
                .time("18:45")
                .pocetnastanica("Glavni Kolodvor")
                .krajnastanica("Vrapce")
                .kilometers(new BigDecimal("8.5"))
                .cjenik(cjenik)
                .build());

        routes.add(Route.builder(vehicles.get(5), LocalDate.of(2026, 1, 30))
                .time("09:20")
                .pocetnastanica("Prisavlje")
                .krajnastanica("Mihaljevac")
                .kilometers(new BigDecimal("7.5"))
                .cjenik(cjenik)
                .build());

        // Login korisnika
        korisnik = login(scanner, korisnik);
        log.info("Korisnik {} ({}, {}) je prijavljen.", korisnik.getName(), korisnik.getNameID(), korisnik.getAge());
        log.info("\nDobrodosao {} ({}, {})", korisnik.getName(), korisnik.getNameID(), korisnik.getAge());

        // Glavni menu
        do {
            log.info("\n1) Unos vozila i linije");
            log.info("2) Pretrazivanje");
            log.info("3) Izlaz");

            int odabir = scanner.nextInt();
            scanner.nextLine();
            log.trace("Korisnicki odabir: {}", odabir);

            if (odabir == 1) {
                if (provjeraAdmin(korisnik, scanner)) continue;

                while (true) {
                    log.info("\n=== UNOS VOZILA I LINIJA ===");
                    log.info("Trenutno stanje vozila: {}", vehicles.size());
                    log.info("Trenutno stanje linija: {}", routes.size());
                    log.info("\n1) Unos novog vozila\n2) Unos novih linija\n3) Izlaz");

                    odabir = scanner.nextInt();
                    scanner.nextLine();

                    if (odabir == 1) {
                        procesDodavanjaVozila(vehicles, scanner, registracije);
                    } else if (odabir == 2) {
                        procesDodavanjaLinije(routes, scanner, vehicles, cjenik);
                    } else if (odabir == 3) {
                        break;
                    }
                }
            } else if (odabir == 2) {
                if (routes.isEmpty()) {
                    log.info("Nema vozila i linija.");
                    continue;
                }

                int odabir2=0;
                while (odabir2!=8) {
                    log.info("\nPretrazivanje po:");
                    log.info("1) Registracija");
                    log.info("2) Polaziste");
                    log.info("3) Kilometraza");
                    log.info("4) Prikaz linija");
                    log.info("5) Vozila");
                    log.info("6) Godine Proizvodnje");
                    log.info("7) Elektricna");
                    log.info("8) Izlaz");

                    odabir2 = scanner.nextInt();
                    scanner.nextLine();

                    switch (odabir2) {
                        case 1 -> pronadiRegistraciju(scanner, routes, mapaVozila);
                        case 2 -> pronadiStanice(scanner, routes);
                        case 3 -> pronadiKilometrazu(scanner, routes);
                        case 4 -> ispisiLinije(routes);
                        case 5 -> ispisiVozila(vehicles, routes);
                        case 6 -> pronadiGodinuProizvodnje(scanner, vehicles);
                        case 7 -> podjelaNaElektricna(vehicles);
                        case 8 -> odabir2=8;
                    }
                }
            } else if (odabir == 3) {
                log.info("Hvala na koristenju!");
                break;
            }
        } while (true);

        scanner.close();
        log.info("Program zatvoren.");
        log.trace("KRAJ - main metoda");
    }

    /**
     * Provjerava da li je vozilo trenutno korišteno na nekoj od ruta.
     */
    static int isVehicleUsed(Vehicle v, List<Route> routes) {
        if (v == null) return -1;

        for (int i = 0; i < routes.size(); i++) {
            Vehicle rvozilo = routes.get(i).getVehicle();
            if (rvozilo == v || (rvozilo != null && rvozilo.getRegistration().equals(v.getRegistration()))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ispisuje dostupnost svih vozila u sustavu.
     */
    static void dostupnostVozila(List<? extends Vehicle> vehicles, List<Route> routes) {
        Map<Boolean, List<Vehicle>> vozilaPoDostupnosti = vehicles.stream()
                .collect(Collectors.groupingBy(vehicle -> isVehicleUsed(vehicle, routes) != -1));

        Optional.ofNullable(vozilaPoDostupnosti.get(true)).ifPresent(nedostupna -> {
            for (Vehicle v : nedostupna) {
                v.ispis();
                int index = isVehicleUsed(v, routes);
                Route r = routes.get(index);
                log.info("⚠️ NEDOSTUPAN - Linija {} - {}\n", r.getPocetnastanica(), r.getKrajnastanica());
            }
        });

        Optional.ofNullable(vozilaPoDostupnosti.get(false)).ifPresent(dostupna -> {
            for (Vehicle v : dostupna) {
                v.ispis();
                log.info("✅ DOSTUPAN\n");
            }
        });
    }

    /**
     * Obavlja proces prijave korisnika u sustav.
     */
    private static User login(Scanner scanner, User korisnik) {
        log.info("Unesite ime i prezime: ");
        String name = null;
        while (name == null) {
            try {
                name = scanner.nextLine();
                if (name.isEmpty()) {
                    throw new PraznoException("Nedostaje ime i prezime.");
                } else if (!korisnik.provjeriImePrezime(name)) {
                    log.info("Morate unijeti i ime i prezime. Pokusajte ponovo: ");
                    name = null;
                }
            } catch (PraznoException _) {
                log.info("Pogresno uneseni ime i prezime. Pokusajte ponovo: ");
                name = null;
            }
        }

        log.info("Broj godina: ");
        int age;
        while (true) {
            try {
                age = unosGodina(scanner);
                if (korisnik.provjeriGodine(age)) break;
                else log.info("Pogresna godina (10-100). Pokusajte ponovo: ");
            } catch (PogresanUnosException | NegativniUnosException e) {
                log.info("{}, pokusajte ponovo", e.getMessage());
            }
        }

        log.info("Unesite username: ");
        String nameID = null;
        while (nameID == null) {
            try {
                nameID = scanner.nextLine();
                if (nameID.isEmpty()) throw new PraznoException("Nedostaje username.");
            } catch (PraznoException _) {
                log.info("Pogresno uneseni username. Pokusajte ponovo: ");
                nameID = null;
            }
        }

        log.info("Unesite email adresu: ");
        String email = null;
        while (email == null) {
            try {
                email = scanner.nextLine();
                if (email.isEmpty()) throw new PraznoException("Nedostaje email adresa.");
                else if (!korisnik.provjeriMail(email)) {
                    log.info("Pogresni format email adrese. Pokusajte ponovo: ");
                    email = null;
                }
            } catch (PraznoException e) {
                log.info("{}, Pokusajte ponovo: ", e.getMessage());
                email = null;
            }
        }

        return new User(name, age, nameID, email);
    }

    /**
     * Omogućuje unos broja godina s validacijom.
     */
    private static int unosGodina(Scanner scanner) throws PogresanUnosException {
        try {
            int age = scanner.nextInt();
            scanner.nextLine();
            if (age < 0) throw new NegativniUnosException("Godina ne smije biti negativna.");
            return age;
        } catch (InputMismatchException _) {
            throw new PogresanUnosException("Godina mora biti cijeli broj.");
        }
    }

    /**
     * Omogućuje unos datuma u formatu dd.MM.yyyy.
     */
    private static LocalDate unosDatum(Scanner scanner) throws PogresanDatumException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String datum = scanner.nextLine();
        try {
            return LocalDate.parse(datum, formatter);
        } catch (DateTimeParseException _) {
            throw new PogresanDatumException("Datum mora biti u formatu dd.MM.yyyy.");
        }
    }

    /**
     * Provjerava administratorske ovlasti korisnika.
     */
    private static boolean provjeraAdmin(User korisnik, Scanner scanner) {
        if (!korisnik.getNameID().equals("llulic")) {
            log.info("Greska! Korisnik {} nije autoriziran za ovu opciju.", korisnik.getNameID());
            return true;
        }

        log.info("Unesite lozinku za administratora ({}): ",korisnik.getNameID());
        String lozinka = scanner.nextLine();

        if (!"admin123".equals(lozinka)) {
            log.info("Greska! Lozinka nije ispravna.");
            return true;
        }

        log.info("Lozinka je ispravna.");
        return false;
    }

    /**
     * Upravlja procesom dodavanja novih linija u sustav.
     */
    private static void procesDodavanjaLinije(List<Route> routes, Scanner scanner, List<Vehicle> vehicles, CijenaKarte cjenik) {
        try {
            log.info("Koliko novih linija zelite dodati? ");
            int brojLinijaZaDodavanje = scanner.nextInt();
            scanner.nextLine();

            if (brojLinijaZaDodavanje < 0) {
                throw new NegativniUnosException("Broj linija ne smije biti negativan.");
            }

            dodavanjeLinija(brojLinijaZaDodavanje, scanner, vehicles, routes, cjenik);
        } catch (NegativniUnosException e) {
            log.info("{}, pokusajte ponovo: ",e.getMessage());
            scanner.nextLine();
        }
    }

    /**
     * Upravlja procesom dodavanja novih vozila u sustav.
     */
    private static void procesDodavanjaVozila(List<Vehicle> vehicles, Scanner scanner, Set<String> registracije) {
        try {
            log.info("Koliko novih vozila zelite dodati? ");
            int brojVozilaZaDodavanje = scanner.nextInt();
            scanner.nextLine();

            if (brojVozilaZaDodavanje < 0) {
                throw new NegativniUnosException("Broj vozila ne smije biti negativan.");
            }

            dodavanjeVozila(brojVozilaZaDodavanje, scanner, vehicles, registracije);
        } catch (NegativniUnosException e) {
            log.info("{}, pokusajte ponovo: ",e.getMessage());
            scanner.nextLine();
        }
    }

    /**
     * Obavlja unos podataka za nove linije.
     */
    private static void dodavanjeLinija(int brojLinijaZaDodavanje, Scanner scanner, List<Vehicle> vehicles,
                                        List<Route> routes, CijenaKarte cjenik) {
        for (int i = 0; i < brojLinijaZaDodavanje; i++) {
            log.info("Pocetna stanica: ");
            String pocetnastanica = unesiNePrazno(scanner, "pocetna stanica");

            log.info("Krajnja stanica: ");
            String krajnjastanica = unesiNePrazno(scanner, "krajnja stanica");

            log.info("Kilometraza: ");
            BigDecimal kilometers = unesiKilometrazu(scanner);

            log.info("Datum polaska (dd.MM.yyyy): ");
            LocalDate datum = unesiDatum(scanner);

            log.info("Vrijeme polaska: ");
            String vrijemepolaska = scanner.nextLine();

            Vehicle odabranovozilo = pronadiVoziloPoRegistraciji(scanner, vehicles);

            routes.add(Route.builder(odabranovozilo, datum)
                    .time(vrijemepolaska)
                    .pocetnastanica(pocetnastanica)
                    .krajnastanica(krajnjastanica)
                    .kilometers(kilometers)
                    .cjenik(cjenik)
                    .build());

            log.info("Linija uspjesno dodana!");
        }
    }

    /**
     * Obavlja unos podataka za nova vozila.
     */
    private static void dodavanjeVozila(int brojVozilaZaDodavanje, Scanner scanner,
                                        List<Vehicle> vehicles, Set<String> registracije) {
        for (int i = 0; i < brojVozilaZaDodavanje; i++) {
            log.info("Unesite registraciju: ");
            String registration = scanner.nextLine();

            if (registracije.contains(registration.toLowerCase())) {
                log.info("Greska! Vozilo sa registracijom {} vec postoji.", registration);
                i--;
                continue;
            }

            log.info("Unesite tip vozila (Bus/Tramvaj): ");
            String model = unesiTipVozila(scanner);

            log.info("Unesite boju: ");
            String color = scanner.nextLine();

            log.info("Unesite godinu proizvodnje: ");
            int year = unesiGodinu(scanner);
            scanner.nextLine();

            if (model.equals("Bus")) {
                vehicles.add(new Bus(registration, color, year));
            } else {
                vehicles.add(new Tramvaj(registration, color, year));
            }

            registracije.add(registration.toLowerCase());
            log.info("Uspjesno dodano vozilo.");
        }
    }

    // Pomoćne metode za unos podataka
    private static String unesiNePrazno(Scanner scanner, String naziv) {
        String unos;
        while (true) {
            unos = scanner.nextLine();
            if (!unos.isEmpty()) return unos;
            log.info("Nedostaje {}. Pokusajte ponovo: ",naziv);
        }
    }

    private static BigDecimal unesiKilometrazu(Scanner scanner) {
        while (true) {
            try {
                BigDecimal kilometers = new BigDecimal(scanner.nextLine());
                if (kilometers.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NegativniUnosException("Kilometraza ne smije biti negativna.");
                }
                return kilometers;
            } catch (NegativniUnosException e) {
                log.info("{}, pokusajte ponovo: ",e.getMessage());
            } catch (NumberFormatException _) {
                log.info("Kilometraza mora biti broj. Pokusajte ponovo: ");
            }
        }
    }

    private static LocalDate unesiDatum(Scanner scanner) {
        while (true) {
            try {
                return unosDatum(scanner);
            } catch (PogresanDatumException e) {
                log.info("{}, pokusajte ponovo: ",e.getMessage());
            }
        }
    }

    private static Vehicle pronadiVoziloPoRegistraciji(Scanner scanner, List<Vehicle> vehicles) {
        while (true) {
            log.info("Unesite registraciju vozila: ");
            String registracija = scanner.nextLine();

            Optional<Vehicle> vozilo = vehicles.stream()
                    .filter(v -> v.getRegistration().equalsIgnoreCase(registracija))
                    .findFirst();

            if (vozilo.isPresent())
            {
                return vozilo.get();
            }
            log.info("Greska! Vozilo sa registracijom {} ne postoji.", registracija);
        }
    }

    private static String unesiTipVozila(Scanner scanner) {
        String model;
        while (true) {
            model = scanner.nextLine();
            if (model.equals("Bus") || model.equals("Tramvaj")) return model;
            log.info("Greska! Pogresan tip vozila! Unesite tip vozila (Bus/Tramvaj): ");
        }
    }

    private static int unesiGodinu(Scanner scanner) {
        while (true) {
            try {
                return unosGodina(scanner);
            } catch (PogresanUnosException | NegativniUnosException e) {
                log.info("{}, pokusajte ponovo: ",e.getMessage());
            }
        }
    }

    /**
     * Ispisuje listu svih vozila s pripadajućom dostupnošću.
     */
    private static void ispisiVozila(List<Vehicle> vehicles, List<Route> routes) {
        log.info("=== VOZILA ===");
        dostupnostVozila(vehicles, routes);
    }

    /**
     * Ispisuje sve linije u sustavu.
     */
    private static void ispisiLinije(List<Route> routes) {
        log.info("=== LINIJE ===");
        routes.forEach(route -> {
            route.ispis();
            log.info("");
        });
    }

    /**
     * Pronalazi i ispisuje linije s najvećom i najmanjom kilometražom.
     */
    private static void pronadiKilometrazu(Scanner scanner, List<Route> routes) {
        int odabir;
        while (true) {
            log.info("1) Najkraca linija\n2) Najduza linija\n3) Izlaz");
            odabir = scanner.nextInt();
            scanner.nextLine();

            if (odabir == 1) {
                routes.stream()
                        .min(Comparator.comparing(Route::getKilometers))
                        .ifPresent(route -> {
                            log.info("Najkraca linija: ");
                            route.ispis();
                        });
            } else if (odabir == 2) {
                routes.stream()
                        .max(Comparator.comparing(Route::getKilometers))
                        .ifPresent(route -> {
                            log.info("Najduza linija: ");
                            route.ispis();
                        });
            } else if (odabir == 3) {
                break;
            }
        }
    }

    /**
     * Pronalazi i ispisuje sve linije koje kreću s određenog polazišta.
     */
    private static void pronadiStanice(Scanner scanner, List<Route> routes) {
        log.info("Unesite polaziste: ");
        String polaziste = scanner.nextLine();

        List<Route> pocetneStanice = routes.stream()
                .filter(route -> polaziste.equalsIgnoreCase(route.getPocetnastanica()))
                .toList();

        if (pocetneStanice.isEmpty()) {
            log.info("Ne postoji linija koja krece iz stanice {}.", polaziste);
        } else {
            pocetneStanice.forEach(Route::ispis);
        }
    }

    /**
     * Pronalazi i ispisuje sve linije koje koriste vozilo s određenom registracijom.
     */
    private static void pronadiRegistraciju(Scanner scanner, List<Route> routes, Map<String, Vehicle> mapaVozila) {
        log.info("Unesite registraciju: ");
        String registracijavozila = scanner.nextLine();

        Optional<Vehicle> voziloOpt = Optional.ofNullable(mapaVozila.get(registracijavozila.toUpperCase()));

        voziloOpt.ifPresentOrElse(vozilo -> {
            List<Route> ruteZaVozilo = routes.stream()
                    .filter(ruta -> registracijavozila.equalsIgnoreCase(ruta.getVehicle().getRegistration()))
                    .toList();

            if (ruteZaVozilo.isEmpty()) {
                log.info("Vozilo postoji, ali nema dodjeljenu liniju.");
                vozilo.ispis();
            } else {
                ruteZaVozilo.forEach(Route::ispis);
            }
        }, () -> log.info("Vozilo sa registracijom {} ne postoji.", registracijavozila));
    }

    /**
     * Pronalazi i ispisuje najnovije i najstarije vozilo u sustavu.
     */
    private static void pronadiGodinuProizvodnje(Scanner scanner, List<Vehicle> vehicles) {
        int odabir;
        while (true) {
            log.info("1) Najnovije vozilo\n2) Najstarije vozilo\n3) Izlaz");
            odabir = scanner.nextInt();
            scanner.nextLine();

            if (odabir == 1) {
                vehicles.stream()
                        .max(Comparator.comparing(Vehicle::getYear))
                        .ifPresent(vehicle -> {
                            log.info("=== Najnovije vozilo ===");
                            vehicle.ispis();
                        });
            } else if (odabir == 2) {
                vehicles.stream()
                        .min(Comparator.comparing(Vehicle::getYear))
                        .ifPresent(vehicle -> {
                            log.info("=== Najstarije vozilo ===");
                            vehicle.ispis();
                        });
            } else if (odabir == 3) {
                break;
            }
        }
    }

    /**
     * Popunjuje mapu vozila.
     */
    private static void popuniMapuVozilima(Map<String, Vehicle> mapaVozila, List<Vehicle> vehicles) {
        mapaVozila.clear();
        for (Vehicle vozilo : vehicles) {
            mapaVozila.put(vozilo.getRegistration(), vozilo);
        }
    }

    /**
     * Pretrazuje elektricna vozila.
     */
    private static void podjelaNaElektricna(List<Vehicle> vehicles) {
        List<Vehicle>elektricnavozila=vehicles.stream()
                        .filter(vozilo-> vozilo instanceof Elektricni elektricni && elektricni.jeElektricni())
                                .toList();

        log.info("=== ELEKTRICNA VOZILA ===");
        elektricnavozila.forEach(Vehicle::ispis);
    }
}