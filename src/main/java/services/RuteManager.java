package services;

import entities.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class RuteManager {
    private List<Route> sveRute = new ArrayList<>();
    private VozilaManager vozilaManager;
    public static final String FILE_PATH="rute.json";

    public RuteManager(VozilaManager vozilaManager) {
        this.vozilaManager = vozilaManager;
    }

    public void dodajPocetneRute(CijenaKarte cjenik) {
        List<Vehicle> vozila = vozilaManager.getSvaVozila();

        sveRute.add(Route.builder(vozila.get(0), LocalDate.of(2025, 12, 23))
                .time("19:04")
                .pocetnastanica("Velika Gorica")
                .krajnastanica("Aerodrom")
                .kilometers(new BigDecimal("6.24"))
                .cjenik(cjenik)
                .build());

        sveRute.add(Route.builder(vozila.get(2), LocalDate.of(2025, 9, 12))
                .time("18:45")
                .pocetnastanica("Glavni Kolodvor")
                .krajnastanica("Vrapce")
                .kilometers(new BigDecimal("8.5"))
                .cjenik(cjenik)
                .build());
        JsonService.spremiUJson(FILE_PATH, sveRute);
    }
    public void ucitajRute() {
        this.sveRute=JsonService.ucitajIzJsona(FILE_PATH,Route.class);
    }

    public void osvjeziPodatke(List<Route> noviPodaci) {
        this.sveRute = noviPodaci;

        JsonService.spremiUJson("rute.json", sveRute);
    }

    public void unosNoveRute(Scanner scanner, CijenaKarte cjenik) {
        System.out.println("=== UNOS NOVE RUTE ===");

        Vehicle vozilo = pronadiVozilo(scanner);
        if (vozilo == null) return;

        LocalDate datum = unesiDatum(scanner);
        String vrijeme = unesiVrijeme(scanner);
        String pocetna = unesiNePrazno(scanner, "Unesite početnu stanicu: ");
        String krajnja = unesiNePrazno(scanner, "Unesite krajnju stanicu: ");
        BigDecimal kilometri = unesiKilometrazu(scanner);

        Route novaRuta = Route.builder(vozilo, datum)
                .time(vrijeme)
                .pocetnastanica(pocetna)
                .krajnastanica(krajnja)
                .kilometers(kilometri)
                .cjenik(cjenik)
                .build();

        sveRute.add(novaRuta);
        JsonService.spremiUJson(FILE_PATH, sveRute);
        System.out.println("Ruta uspješno dodana!");
    }



    public List<Route> nadjiRutePoRegistraciji(String registracija) {
        return sveRute.stream()
                .filter(ruta -> ruta.getVehicle().getRegistration().equalsIgnoreCase(registracija))
                .toList();
    }

    public List<Route> nadjiRutePoStanici(String stanica) {
        return sveRute.stream()
                .filter(ruta -> stanica.equalsIgnoreCase(ruta.getPocetnastanica()) ||
                        stanica.equalsIgnoreCase(ruta.getKrajnastanica()))
                .toList();
    }

    public Optional<Route> getNajkracaRuta() {
        return sveRute.stream()
                .min(Comparator.comparing(Route::getKilometers));
    }

    public Optional<Route> getNajduzaRuta() {
        return sveRute.stream()
                .max(Comparator.comparing(Route::getKilometers));
    }

    public void ispisiSveRute() {
        System.out.println("=== SVE RUTE ===");
        sveRute.forEach(Route::ispis);
    }

    public void ispisiDostupnostVozila() {
        System.out.println("=== DOSTUPNOST VOZILA ===");

        Map<Boolean, List<Vehicle>> dostupnost = vozilaManager.getSvaVozila().stream()
                .collect(Collectors.partitioningBy(this::jeVoziloKoristeno));

        dostupnost.get(false).forEach(vozilo -> {
            vozilo.ispis();
            System.out.println("✅ DOSTUPAN\n");
        });

        dostupnost.get(true).forEach(vozilo -> {
            vozilo.ispis();
            System.out.println("❌ NEDOSTUPAN - koristi se na ruti\n");
        });
    }

    private boolean jeVoziloKoristeno(Vehicle vozilo) {
        return sveRute.stream()
                .anyMatch(ruta -> ruta.getVehicle().equals(vozilo));
    }

    private Vehicle pronadiVozilo(Scanner scanner) {
        return vozilaManager.nadjiVozilo(unesiText(scanner, "Unesite registraciju vozila: "))
                .orElseGet(() -> {
                    System.out.println("Vozilo ne postoji!");
                    return null;
                });
    }

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

    private String unesiVrijeme(Scanner scanner) {
        while (true) {
            System.out.print("Unesite vrijeme (HH:mm): ");
            String vrijeme = scanner.nextLine();
            if (vrijeme.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                return vrijeme;
            } else {
                System.out.println("Pogrešan format vremena! Koristite format HH:mm");
            }
        }
    }

    private BigDecimal unesiKilometrazu(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Unesite kilometražu: ");
                String kmText = scanner.nextLine();
                BigDecimal kilometri = new BigDecimal(kmText);
                if (kilometri.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Kilometraža ne smije biti negativna!");
                } else {
                    return kilometri;
                }
            } catch (NumberFormatException e) {
                System.out.println("Morate unijeti broj!");
            }
        }
    }

    private String unesiText(Scanner scanner, String poruka) {
        System.out.print(poruka);
        return scanner.nextLine();
    }

    private LocalDate parsirajDatum(String datumText) {
        try {
            String[] dijelovi = datumText.split("\\.");
            return LocalDate.of(
                    Integer.parseInt(dijelovi[2]),
                    Integer.parseInt(dijelovi[1]),
                    Integer.parseInt(dijelovi[0])
            );
        } catch (Exception _) {
            return LocalDate.now();
        }
    }

    private String unesiNePrazno(Scanner scanner, String poruka) {
        while (true) {
            System.out.print(poruka);
            String unos = scanner.nextLine().trim();
            if (unos.isEmpty()) {
                System.out.println("Polje ne smije biti prazno!");
            } else {
                return unos;
            }
        }
    }
    public List<Route> getSveRute() {
        return sveRute;
    }
}