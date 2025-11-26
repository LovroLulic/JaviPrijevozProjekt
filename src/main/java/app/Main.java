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

    static Logger log= LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        log.trace("POCETAK - main metoda");
        log.info("Pokrenut program!");

        Scanner scanner=new Scanner(System.in);


        User korisnik = new User("",0,"","");
        List<Vehicle> vehicles=new ArrayList<>();
        Set<String> registracije=new HashSet<>();
        Map<String, Vehicle>mapaVozila=new HashMap<>();
        List<Route>routes=new ArrayList<>();

        vehicles.add(new Tramvaj("ZG19055A","Plava",2019));
        registracije.add("ZG19055A".toLowerCase());
        vehicles.add(new Bus("ZG23045B","Plava",2012));
        registracije.add("ZG23045B".toLowerCase());
        vehicles.add(new Tramvaj("ZG01045C","Bijela",2023));
        registracije.add("ZG01045C".toLowerCase());
        vehicles.add(new Bus("ZG01045D","Bijela",2023));
        registracije.add("ZG01045D".toLowerCase());
        vehicles.add(new Bus("ZG01045E","Zelena",2023));
        registracije.add("ZG01045E".toLowerCase());
        vehicles.add(new Tramvaj("ZG01045F","Roza",2023));
        registracije.add("ZG01045F".toLowerCase());
        vehicles.add(new Bus("ZG01045G","Crvena",2023));
        registracije.add("ZG01045G".toLowerCase());

        popuniMapuVozilima(mapaVozila, vehicles);

        int brojVozila=7;
        int brojVozilaKopijaPocetno=7;



        CijenaKarte cjenik=new CijenaKarte(new BigDecimal("2.00"),
                new BigDecimal("1.00"), new BigDecimal("1.5"),
                new BigDecimal("4.5"), new BigDecimal("2.5"), new BigDecimal("3"));


        routes.add(Route.builder(vehicles.getFirst(),LocalDate.of(2025,12,23))
                .time("19:04")
                .pocetnastanica("Velika Gorica")
                .krajnastanica("Aerodrom")
                .kilometers(new BigDecimal("6.24"))
                .cjenik(cjenik)
                .build());


        routes.add(Route.builder(vehicles.get(2),LocalDate.of(2025,9,12))
                .time("18:45")
                .pocetnastanica("Glavni Kolodvor")
                .krajnastanica("Vrapce")
                .kilometers(new BigDecimal("8.5"))
                .cjenik(cjenik)
                .build());


        routes.add(Route.builder(vehicles.get(5),LocalDate.of(2026,1,30))
                .time("09:20")
                .pocetnastanica("Prisavlje")
                .krajnastanica("Mihaljevac")
                .kilometers(new BigDecimal("7.5"))
                .cjenik(cjenik)
                .build());


        int brojRuta=3;
        String lozinka;


        //Korisnik

        korisnik = login(scanner, korisnik);
        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je prijavljen.");

        System.out.println();
        System.out.println("Dobrodosao "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+")");
        System.out.println();


        do{
            log.trace("ULAZ-Glavni Menu");
            System.out.println("1) Unos vozila i linije");
            System.out.println("2) Pretrazivanje");
            System.out.println("3) Izlaz");

            int odabir=scanner.nextInt();
            scanner.nextLine();
            log.trace("Korisnicki odabir: {}", odabir);

            if(odabir==1){
                log.trace("ULAZ-Unos vozila i linije");
                log.debug("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju Unos vozila i linije.");
                if (provjeraAdmin(korisnik, scanner)) continue;
                log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je prijavljen kao admin.");

                System.out.println("\n=== UNOS VOZILA I LINIJA ===");


                while(true) {
                    log.trace("ULAZ-Unos vozila i linija - ADMIN");
                    System.out.println("Trenutno stanje vozila: " + vehicles.size());
                    System.out.println("Trenutno stanje linija: " + routes.size());
                    System.out.printf("\n");
                    System.out.println("1)Unos novog vozila\n2)Unos novih linija\n3)Izlaz");

                    odabir=scanner.nextInt();
                    scanner.nextLine();
                    log.trace("Admin odabir: {}", odabir);

                    if (odabir == 1) {
                        log.trace("ULAZ-Dodavanje novog vozila");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju dodavanje novog vozila.");
                        brojVozila = procesDodavanjaVozila(brojVozila, vehicles, scanner, brojVozilaKopijaPocetno,registracije);
                        log.trace("POVRATAK-Dodavanje novog vozila - broj vozila: {}", brojVozila);
                    }
                    else if (odabir == 2) {
                        log.trace("ULAZ-Dodavanje novih linija");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju dodavanje novih linija.");
                        brojRuta = procesDodavanjaLinije(brojRuta, routes, scanner, brojVozila, vehicles,cjenik);
                        log.trace("POVRATAK-Dodavanje novih linija - broj ruta: {}", brojRuta);
                    }
                    else if(odabir==3){
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju izlaz.");
                        log.trace("IZLAZ-Unos vozila i linija");
                        break;
                    }
                }
            }

            else if(odabir==2){
                log.trace("ULAZ-Pretrazivanje");
                log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju Pretrazivanje.");
                if(brojRuta==0){
                    System.out.println("⚠\uFE0F Nema vozila i linija.");
                    continue;
                }
                while(true) {
                    System.out.println("Pretrazivanje po:\n1)Registracija\n2)Polaziste\n3)Kilometraza\n4)Prikaz linija\n5)Vozila\n6)Godine Proizvodnje\n7)Elektricna\n8)Izlaz");
                    int odabir2;
                    odabir2 = scanner.nextInt();
                    scanner.nextLine();
                    log.trace("Pretrazivanje odabir: {}", odabir2);

                    if (odabir2==1) {
                        log.trace("ULAZ-Pretrazivanje po registraciji");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju pretrazivanje po registraciji.");
                        pronadiRegistraciju(scanner, brojRuta, routes,registracije,mapaVozila);
                        log.trace("POVRATAK-Pretrazivanje po registraciji");
                    }

                    else if (odabir2==2) {
                        log.trace("ULAZ-Pretrazivanje po polazistu");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju pretrazivanje po polazistu.");
                        pronadiStanice(scanner, brojRuta, routes);
                        log.trace("POVRATAK-Pretrazivanje po polazistu");
                    }
                    else if (odabir2==3) {
                        log.trace("ULAZ-Pretrazivanje po kilometrazi");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju pretrazivanje po kilometrazi.");
                        pronadiKilometrazu(scanner, brojRuta, routes);
                        log.trace("POVRATAK-Pretrazivanje po kilometrazi");
                    }
                    else if(odabir2==4){
                        log.trace("ULAZ-Pretrazivanje po liniji");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju prikaz linije.");
                        ispisiLinije(brojRuta, routes);
                        log.trace("POVRATAK-Pretrazivanje po liniji");
                    }
                    else if(odabir2==5){
                        log.trace("ULAZ-Pretrazivanje po vozilu");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju prikaz vozila.");
                        ispisiVozila(vehicles, brojVozila, routes, brojRuta);
                        log.trace("POVRATAK-Pretrazivanje po vozilu");
                    }
                    else if(odabir2==6){
                        log.trace("ULAZ-Pretrazivanje po godinama proizvodnje");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju pretrazivanje po godinama proizvodnje.");
                        pronadiGodinuProizvodnje(scanner, vehicles, brojVozila);
                        log.trace("POVRATAK-Pretrazivanje po godinama proizvodnje");
                    }
                    else if(odabir2==7){
                        log.trace("Ulaz-Pretrazivanje elektricnih vozila");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju pretrazivanje elektricnih vozila");
                        podjelaNaElektricna(vehicles);
                    }
                    else if(odabir2==8){
                        log.trace("IZLAZ - Pretrazivanje");
                        log.info("Korisnik "+korisnik.getName()+" ("+korisnik.getNameID()+", "+korisnik.getAge()+") je odabrao opciju izlaz.");
                        break;
                    }
                }
            }

            else if(odabir==3){
                log.trace("IZLAZ - Glavni Menu");
                log.info("Korisnik je izasao iz programa");
                System.out.println("Hvala na koristenju!");
                break;
            }

        }while(true);

        scanner.close();
        log.info("Program zatvoren.");
        log.trace("KRAJ - main metoda");
    }


    /**
     * Provjerava da li je vozilo trenutno korišteno na nekoj od ruta.
     * Vraća index rute na kojoj je vozilo pronađeno ili -1 ako vozilo nije korišteno.
     * Provjera se vrši po referenci vozila i registracijskoj oznaci.
     *
     * @param v Vozilo za koje se provjerava dostupnost
     * @param routes Polje ruta koje se pretražuje
     * @param brojRuta Broj validnih ruta u polju
     * @return Index rute na kojoj je vozilo pronađeno ili -1 ako nije korišteno
     * @see Vehicle
     * @see Route
     */
    static int isVehicleUsed(Vehicle v, List<Route>routes, int brojRuta){
        log.trace("POCETAK - isVehicleUsed metoda");
        if(v==null){
            log.trace("Vehicle je null");
            return -1;
        }

        for(int i=0;i<brojRuta;i++){
            Vehicle rvozilo=routes.get(i).getVehicle();

            if(rvozilo==v) {
                log.trace("Pronadjeno vozilo na indexu: {}", i);
                return i;
            }

            if(rvozilo!=null && rvozilo.getRegistration().equals(v.getRegistration())){
                log.trace("Pronadjeno vozilo po registraciji na indexu: {}", i);
                return i;
            }
        }
        log.trace("Vozilo nije pronadjeno");
        log.trace("KRAJ - isVehicleUsed metoda");
        return -1;
    }

    /**
     * Ispisuje dostupnost svih vozila u sustavu.
     * Za svako vozilo provjerava je li korišteno na nekoj ruti i ispisuje
     * odgovarajuću poruku o dostupnosti zajedno s informacijama o vozilu.
     *
     * @param vehicles Polje vozila čija se dostupnost provjerava
     * @param brojVozila Broj validnih vozila u polju
     * @param routes Polje ruta za provjeru korištenja vozila
     * @param brojRuta Broj validnih ruta u polju
     * @see Vehicle
     * @see Route
     */
    static void dostupnostVozila(List<Vehicle>vehicles, int brojVozila, List<Route>routes, int brojRuta){
        log.trace("POCETAK - dostupnostVozila metoda");
        Map<Boolean, List<Vehicle>>vozilaPoDostupnosti=new HashMap<>();
        vozilaPoDostupnosti=vehicles.stream().collect(Collectors.groupingBy(vehicle -> isVehicleUsed(vehicle, routes, brojRuta) != -1));

        List<Vehicle>nedostupnaVozila=vozilaPoDostupnosti.get(true);
        List<Vehicle>dostupnaVozila=vozilaPoDostupnosti.get(false);






            if(nedostupnaVozila!=null && !nedostupnaVozila.isEmpty()){
                for(Vehicle v:nedostupnaVozila){
                    v.ispis();
                    int index=isVehicleUsed(v,routes,brojRuta);
                    Route r=routes.get(index);
                    log.trace("Vozilo {} nedostupno - koristi se na liniji {}", v.getRegistration(), r.getPocetnastanica() + " - " + r.getKrajnastanica());

                    System.out.println("⚠\uFE0F NEDOSTUPAN - Linija "+r.getPocetnastanica()+" - " +r.getKrajnastanica());
                    System.out.println();
                }
            }


            if(dostupnaVozila!=null && !dostupnaVozila.isEmpty()){
                for(Vehicle v:dostupnaVozila){

                    log.trace("Vozilo {} dostupno", v.getRegistration());
                    v.ispis();
                    System.out.println("✅ DOSTUPAN");
                    System.out.println();

                }
            }


//            if(indexLinije!=-1){
//                Route r=routes.get(indexLinije);
//                log.trace("Vozilo {} nedostupno - koristi se na liniji {}", vehicles.get(i).getRegistration(), r.getPocetnastanica() + " - " + r.getKrajnastanica());
//                vehicles.get(i).ispis();
//                System.out.println("⚠\uFE0F NEDOSTUPAN - Linija "+r.getPocetnastanica()+" - "
//                        +r.getKrajnastanica());
//                System.out.println();
//            }
//            else if(indexLinije==-1){
//                log.trace("Vozilo {} dostupno", vehicles.get(i).getRegistration());
//                vehicles.get(i).ispis();
//                System.out.println("✅ DOSTUPAN");
//                System.out.println();
//            }

        log.trace("KRAJ - dostupnostVozila metoda");
    }

    /**
     * Obavlja proces prijave korisnika u sustav.
     * Traži od korisnika unos svih potrebnih podataka: ime i prezime, godine,
     * korisničko ime i email adresu. Validira sve unose i vraća novi User objekt.
     *
     * @param scanner Scanner objekt za unos podataka
     * @param korisnik Privremeni User objekt za validaciju unosa
     * @return Novi User objekt s unesenim podacima
     * @throws PraznoException ako su polja prazna
     * @throws PogresanUnosException ako unos godina nije validan
     * @see User
     * @see PraznoException
     * @see PogresanUnosException
     */
    private static User login(Scanner scanner, User korisnik) {
        log.trace("POCETAK - login metoda");
        System.out.print("Unesite ime i prezime: ");
        String name = null;
        while (name == null) {
            try {
                name = scanner.nextLine();
                log.debug("Uneseno ime i prezime: " + name);
                if (name.isEmpty()) {
                    log.trace("Prazno ime i prezime - bacanje exceptiona");
                    throw new PraznoException("Nedostaje ime i prezime.");
                } else if (!korisnik.provjeriImePrezime(name)) {
                    System.out.print("Morate unijeti i ime i prezime (npr. 'Ivan Horvat'). Pokusajte ponovo: ");
                    name = null;
                    log.warn("Korisnik nije pravilno unio ime i prezime.");
                } else {
                    log.trace("Ime i prezime ispravno uneseno");
                }
            } catch (PraznoException e) {
                System.out.print("Pogresno uneseni ime i prezime. Pokusajte ponovo: ");
                log.warn("Greska u unosu ime i prezime: "+e);
                name = null;
            }
        }

        System.out.print("Broj godina: ");
        int age = 0;
        while (true) {
            try {
                age = unosGodina(scanner);
                if (korisnik.provjeriGodine(age)) {
                    log.trace("Godine ispravno unesene: {}", age);
                    break;
                } else {
                    System.out.print("Pogresna godina (10-100). Pokusajte ponovo: ");
                    log.warn("Unesena godina nije pravilna.");
                }
            } catch (PogresanUnosException e) {
                log.trace("PogresanUnosException u unosu godina");
                System.out.print(e.getMessage() + " Pokusajte ponovo: ");
                log.error("Greska u unosu godine: "+e);
                scanner.nextLine();
            } catch (NegativniUnosException e) {
                log.trace("NegativniUnosException u unosu godina");
                System.out.print(e.getMessage() + " Pokusajte ponovo: ");
                log.warn("Greska u unosu godine: "+e);
            }
        }

        System.out.print("Unesite username: ");
        String nameID = null;
        while (nameID == null) {
            try {
                nameID = scanner.nextLine();
                log.debug("Uneseno username: " + nameID);
                if (nameID.isEmpty()) {
                    log.trace("Prazan username - bacanje exceptiona");
                    throw new PraznoException("Nedostaje username.");
                } else {
                    log.trace("Username ispravno unesen");
                }
            } catch (PraznoException e) {
                System.out.print("Pogresno uneseni username. Pokusajte ponovo: ");
                log.warn("Greska u unosu username: "+e);
                nameID = null;
            }
        }

        System.out.print("Unesite email adresu: ");
        String email = null;
        while (email == null) {
            try{
                email=scanner.nextLine();
                log.debug("Unesena email adresa: " + email);
                if(email.isEmpty()){
                    log.trace("Prazan email - bacanje exceptiona");
                    throw new PraznoException("Nedostaje email adresa.");
                }
                else if(!korisnik.provjeriMail(email)){
                    System.out.print("Pogresni format email adrese. Pokusajte ponovo: ");
                    log.warn("Pogresna email adresa.");
                    email=null;
                } else {
                    log.trace("Email ispravno unesen");
                }
            }catch(PraznoException e){
                System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                log.warn("Greska u unosu email adrese: "+e);
                email = null;
            }

        }

        korisnik = new User(name, age, nameID, email);
        log.trace("KRAJ - login metoda - kreiran korisnik: {}", nameID);
        return korisnik;
    }

    /**
     * Omogućuje unos broja godina s validacijom.
     * Provjerava da li je unesen cijeli broj i da li nije negativan.
     *
     * @param scanner Scanner objekt za unos podataka
     * @return Uneseni broj godina
     * @throws PogresanUnosException ako unos nije cijeli broj
     * @throws NegativniUnosException ako je unesen negativan broj
     * @see PogresanUnosException
     * @see NegativniUnosException
     */
    private static int unosGodina(Scanner scanner) throws PogresanUnosException{
        log.trace("POCETAK - unosGodina metoda");
        int age=0;

        try {
            age = scanner.nextInt();
            scanner.nextLine();
            log.debug("Unesena godina: " + age);
            if(age<0){
                log.trace("Negativna godina - bacanje exceptiona");
                throw new NegativniUnosException("Godina ne smije biti negativna.");
            }
            log.trace("KRAJ - unosGodina metoda - uspjesno: {}", age);
            return age;

        } catch (InputMismatchException e) {
            log.trace("InputMismatchException u unosu godina");
            throw new PogresanUnosException("Godina mora biti cijeli broj. ");
        }
    }

    /**
     * Omogućuje unos datuma u formatu dd.MM.yyyy.
     * Parsira uneseni string u LocalDate objekt.
     *
     * @param scanner Scanner objekt za unos podataka
     * @return LocalDate objekt s unesenim datumom
     * @throws PogresanDatumException ako format datuma nije ispravan
     * @see LocalDate
     * @see PogresanDatumException
     */
    private static LocalDate unosDatum(Scanner scanner) throws PogresanDatumException {
        log.trace("POCETAK - unosDatum metoda");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String datum = scanner.nextLine();
        try {
            LocalDate result = LocalDate.parse(datum, formatter);
            log.trace("KRAJ - unosDatum metoda - uspjesno: {}", result);
            return result;
        } catch (DateTimeParseException ex) {
            log.trace("DateTimeParseException u unosu datuma: {}", datum);
            throw new PogresanDatumException("Datum mora biti u formatu dd.MM.yyyy. ");
        }
    }

    /**
     * Provjerava administratorske ovlasti korisnika.
     * Provjerava je li korisničko ime "llulic" i ispravnost administratorske lozinke.
     *
     * @param korisnik User objekt za provjeru identiteta
     * @param scanner Scanner objekt za unos lozinke
     * @return true ako korisnik nema administratorske ovlasti, false ako je provjera uspješna
     * @see User
     */
    private static boolean provjeraAdmin(User korisnik, Scanner scanner) {
        log.trace("POCETAK - provjeraAdmin metoda");
        String lozinka;
        if(!korisnik.getNameID().equals("llulic")){
            System.out.println("⚠\uFE0F Greska! Korisnik "+ korisnik.getNameID()+" nije autoriziran za ovu opciju.");
            log.warn("Korisnik "+ korisnik.getNameID()+" nije autoriziran za ovu opciju.");
            log.trace("KRAJ - provjeraAdmin metoda - nije admin");
            return true;
        }
        System.out.print("Unesite lozinku za administratora ("+ korisnik.getNameID()+"): ");
        lozinka= scanner.nextLine();
        log.debug("Unesena lozinka: " + lozinka);
        if(!"admin123".equals(lozinka)){
            System.out.println("⚠\uFE0F Greska! Lozinka nije ispravna.");
            log.warn("Korisnik "+ korisnik.getNameID()+" je unio krivi admin password.");
            log.trace("KRAJ - provjeraAdmin metoda - pogresna lozinka");
            return true;
        }
        System.out.println("✅ Lozinka je ispravna.");
        log.trace("KRAJ - provjeraAdmin metoda - uspjesna prijava");
        return false;
    }

    /**
     * Upravlja procesom dodavanja novih linija u sustav.
     * Provjerava dostupnost prostora i pokreće unos podataka za nove linije.
     *
     * @param brojRuta Trenutni broj ruta u sustavu
     * @param routes Polje ruta
     * @param scanner Scanner objekt za unos podataka
     * @param brojVozila Broj dostupnih vozila
     * @param vehicles Polje vozila
     * @param cjenik Cjenik karata za izračun cijena
     * @return Novi broj ruta nakon dodavanja
     * @throws NegativniUnosException ako je broj linija za dodavanje negativan
     * @see Route
     * @see Vehicle
     * @see CijenaKarte
     */
    private static int procesDodavanjaLinije(int brojRuta, List<Route>routes, Scanner scanner, int brojVozila, List<Vehicle>vehicles,CijenaKarte cjenik) {
        log.trace("POCETAK - procesDodavanjaLinije metoda");
        try {

                System.out.print("Koliko novih linija zelite dodati? ");
                int brojLinijaZaDodavanje = scanner.nextInt();
                scanner.nextLine();
                log.trace("Broj linija za dodavanje: {}", brojLinijaZaDodavanje);
                if(brojLinijaZaDodavanje<0){
                    log.trace("Negativan broj linija - bacanje exceptiona");
                    throw new NegativniUnosException("Broj linija ne smije biti negativan.");
                }

                log.trace("Poziv dodavanjeLinija");
                brojRuta = dodavanjeLinija(brojLinijaZaDodavanje, scanner, brojVozila, vehicles, routes, brojRuta, cjenik);
                System.out.println();

            log.trace("KRAJ - procesDodavanjaLinije metoda - uspjesno");
            return brojRuta;
        }catch(NegativniUnosException e){
            log.error("Greska u unosu broja linija: "+e);
            System.out.print(e.getMessage()+" Pokusajte ponovo: ");
            scanner.nextLine();
            log.trace("KRAJ - procesDodavanjaLinije metoda - exception");
            return procesDodavanjaLinije(brojRuta, routes, scanner, brojVozila, vehicles,cjenik);
        }
    }

    /**
     * Upravlja procesom dodavanja novih vozila u sustav.
     * Provjerava dostupnost prostora i pokreće unos podataka za nova vozila.
     *
     * @param brojVozila Trenutni broj vozila u sustavu
     * @param vehicles Polje vozila
     * @param scanner Scanner objekt za unos podataka
     * @param brojVozilaKopijaPocetno Početni broj vozila (za informaciju)
     * @return Novi broj vozila nakon dodavanja
     * @throws NegativniUnosException ako je broj vozila za dodavanje negativan
     * @see Vehicle
     */
    private static int procesDodavanjaVozila(int brojVozila, List<Vehicle>vehicles, Scanner scanner, int brojVozilaKopijaPocetno,Set<String>registracije) {
        log.trace("POCETAK - procesDodavanjaVozila metoda");
        try {

                System.out.print("Koliko novih vozila zelite dodati? ");
                int brojVozilaZaDodavanje = scanner.nextInt();
                log.debug("Uneseno broj vozila za dodavanje: "+brojVozilaZaDodavanje);

                scanner.nextLine();
                if(brojVozilaZaDodavanje<0){
                    log.trace("Negativan broj vozila - bacanje exceptiona");
                    throw new NegativniUnosException("Broj vozila ne smije biti negativan.");
                }


                log.trace("Poziv dodavanjeVozila");
                brojVozila = dodavanjeVozila(brojVozilaZaDodavanje, brojVozilaKopijaPocetno, scanner, vehicles, brojVozila,registracije);
                System.out.println();

            log.trace("KRAJ - procesDodavanjaVozila metoda - uspjesno");
            return brojVozila;
        }catch(NegativniUnosException e){
            log.error("Greska u unosu broja vozila: "+e);
            System.out.print(e.getMessage()+" Pokusajte ponovo: ");
            scanner.nextLine();
            log.trace("KRAJ - procesDodavanjaVozila metoda - exception");
            return procesDodavanjaVozila(brojVozila, vehicles, scanner, brojVozilaKopijaPocetno, registracije);
        }
    }

    /**
     * Obavlja unos podataka za nove linije.
     * Za svaku liniju traži unos početne i krajnje stanice, kilometraže,
     * datuma, vremena polaska i dodjeljuje vozilo.
     *
     * @param brojLinijaZaDodavanje Broj linija koje treba dodati
     * @param scanner Scanner objekt za unos podataka
     * @param brojVozila Broj dostupnih vozila
     * @param vehicles Polje vozila
     * @param routes Polje ruta
     * @param brojRuta Trenutni broj ruta
     * @param cjenik Cjenik karata
     * @return Novi broj ruta nakon dodavanja
     * @throws PraznoException ako su polja prazna
     * @throws NegativniUnosException ako je kilometraža negativna
     * @throws PogresanDatumException ako je format datuma neispravan
     * @see Route
     * @see Vehicle
     */
    private static int dodavanjeLinija(int brojLinijaZaDodavanje, Scanner scanner, int brojVozila, List<Vehicle>vehicles, List<Route>routes, int brojRuta, CijenaKarte cjenik) {
        log.trace("POCETAK - dodavanjeLinija metoda - broj linija: {}", brojLinijaZaDodavanje);
        for(int i = 0; i< brojLinijaZaDodavanje; i++){
            log.trace("Dodavanje {}. linije", i+1);
            System.out.print("Pocetna stanica: ");
            String pocetnastanica=null;
            while(pocetnastanica==null){
                try{
                    pocetnastanica=scanner.nextLine();
                    log.debug("Pocetna stanica: "+pocetnastanica);
                    if(pocetnastanica.isEmpty()){
                        log.trace("Prazna pocetna stanica - bacanje exceptiona");
                        throw new PraznoException("Nedostaje pocetna stanica.");
                    }
                }catch(PraznoException e){
                    log.warn("Greska u unosu pocetne stanice: "+e);
                    System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                    pocetnastanica=null;
                }
            }
            System.out.print("Krajnja stanica: ");
            String krajnjastanica=null;
            while(krajnjastanica==null){
                try{
                    krajnjastanica=scanner.nextLine();
                    log.debug("Krajnja stanica: "+krajnjastanica);
                    if(krajnjastanica.isEmpty()){
                        log.trace("Prazna krajnja stanica - bacanje exceptiona");
                        throw new PraznoException("Nedostaje krajnja stanica.");
                    }
                }catch(PraznoException e){
                    log.warn("Greska u unosu krajnje stanice: "+e);
                    System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                    krajnjastanica=null;
                }
            }

            System.out.print("Kilometraza: ");
            BigDecimal kilometers=null;
            while(kilometers==null){
                try{
                    kilometers=new BigDecimal(scanner.nextLine());
                    log.debug("Kilometraza: "+kilometers);
                    if(kilometers.compareTo(BigDecimal.ZERO)<0){
                        log.trace("Negativna kilometraza - bacanje exceptiona");
                        throw new NegativniUnosException("Kilometraza ne smije biti negativna.");
                    }
                }catch(NegativniUnosException e){
                    log.warn("Greska u unosu kilometraza: "+e);
                    System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                    kilometers=null;
                }catch(NumberFormatException e){
                    log.error("Greska u unosu kilometraza: "+e);
                    System.out.print("Kilometraza mora biti cijeli broj. Pokusajte ponovo: ");
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            System.out.print("Datum polaska (dd.MM.yyyy): ");
            LocalDate datum=null;
            while(datum==null) {
                try {
                    datum = unosDatum(scanner);
                } catch (PogresanDatumException e) {
                    log.error("Greska u unosu datuma: "+e);
                    System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                }
            }

            System.out.print("Vrijeme polaska: ");
            String vrijemepolaska= scanner.nextLine();
            log.trace("Vrijeme polaska: {}", vrijemepolaska);

            Vehicle odabranovozilo=null;

            while(odabranovozilo==null){
                System.out.print("Unesite registraciju vozila za taj smjer: ");
                String registracijazasmjer= scanner.nextLine();
                log.trace("Trazenje vozila po registraciji: {}", registracijazasmjer);
                for(int j = 0; j< brojVozila; j++){
                    if(vehicles.get(j).getRegistration().equalsIgnoreCase(registracijazasmjer)){
                        odabranovozilo= vehicles.get(j);
                        log.trace("Pronadjeno vozilo: {}", odabranovozilo.getRegistration());
                        break;
                    }
                }
                if(odabranovozilo==null){
                    log.info("Nepostojeca registracija: "+registracijazasmjer);
                    System.out.println("⚠\uFE0F Greska! Vozilo sa registracijom "+registracijazasmjer+" ne postoji.");
                    continue;
                }
            }

            log.trace("Kreiranje nove rute: {} - {}", pocetnastanica, krajnjastanica);
            routes.add(Route.builder(odabranovozilo,datum)
                    .time(vrijemepolaska)
                    .pocetnastanica(pocetnastanica)
                    .krajnastanica(krajnjastanica)
                    .kilometers(kilometers)
                    .cjenik(cjenik)
                    .build());
            brojRuta++;
            System.out.println("✅ Linija uspjesno dodana!");
            log.info("Uspjesno dodana linija.");
        }
        log.trace("KRAJ - dodavanjeLinija metoda - novi broj ruta: {}", brojRuta);
        return brojRuta;
    }

    /**
     * Obavlja unos podataka za nova vozila.
     * Za svako vozilo traži unos registracije, tipa, boje i godine proizvodnje.
     *
     * @param brojVozilaZaDodavanje Broj vozila koja treba dodati
     * @param brojVozilaKopijaPocetno Početni broj vozila
     * @param scanner Scanner objekt za unos podataka
     * @param vehicles Polje vozila
     * @param brojVozila Trenutni broj vozila
     * @return Novi broj vozila nakon dodavanja
     * @throws PraznoException ako su polja prazna
     * @throws NegativniUnosException ako je godina proizvodnje negativna
     * @throws PogresanUnosException ako godina nije cijeli broj
     * @see Vehicle
     * @see Bus
     * @see Tramvaj
     */
    private static int dodavanjeVozila(int brojVozilaZaDodavanje, int brojVozilaKopijaPocetno, Scanner scanner, List<Vehicle>vehicles, int brojVozila, Set<String>registracije) {
        log.trace("POCETAK - dodavanjeVozila metoda - broj vozila za dodavanje: {}", brojVozilaZaDodavanje);
        for(int i = 1; i< brojVozilaZaDodavanje +1; i++){
            log.trace("Dodavanje {}. vozila", brojVozila + 1);
            System.out.print("Unesite registraciju "+vehicles.size()+". vozila: ");
            String registration= scanner.nextLine();
            log.debug("Unesena registracija: "+registration);
            if(registracije.contains(registration.toLowerCase())){
                System.out.println("Greska! Vozilo sa registracijom "+registration+" vec postoji,");
                log.warn("Pokusaj unosa duplikata registracije: "+registration);
                i--;
                continue;
            }
            System.out.print("Unesite tip vozila (Bus/Tramvaj): ");
            String model= scanner.nextLine();
            log.debug("Uneseni tip vozila: "+model);
            while(!model.equals("Bus") && !model.equals("Tramvaj")){
                log.warn("Greska u unosu tipa vozila: "+model);
                System.out.print("⚠\uFE0F Greska! Pogresan tip vozila! Unesite tip vozila (Bus/Tramvaj): ");
                model= scanner.nextLine();
                log.debug("Uneseni tip vozila: "+model);
            }
            System.out.print("Unesite boju: ");
            String color= scanner.nextLine();
            log.debug("Unesena boja: "+color);
            System.out.print("Unesite godinu proizvodnje: ");
            int year=0;
            while(true){
                try{
                    try{
                        year= scanner.nextInt();
                        log.debug("Unesena godina: "+year);
                        if(year<0){
                            log.trace("Negativna godina proizvodnje - bacanje exceptiona");
                            throw new NegativniUnosException("Godina ne smije biti negativna.");
                        }
                        break;
                    }catch(InputMismatchException e){
                        log.trace("InputMismatchException u unosu godine proizvodnje");
                        throw new PogresanUnosException("Godina mora biti cijeli broj. ");
                    }

                }catch(NegativniUnosException e){
                    log.warn("Greska pri unosu godine: "+e);
                    System.out.print(e.getMessage()+" Pokusajte ponovo: ");
                }catch(PogresanUnosException e){
                    log.error("Greska pri unosu godine: "+e);
                    System.out.print("Godina mora biti cijeli broj. Pokusajte ponovo: ");
                    scanner.nextLine();
                }
            }
            scanner.nextLine();
            if(model.equals("Bus")){
                log.trace("Kreiranje novog busa: {}", registration);
                vehicles.add(new Bus(registration,color,year));
                registracije.add(registration.toLowerCase());
                brojVozila++;
            }
            else if(model.equals("Tramvaj")){
                log.trace("Kreiranje novog tramvaja: {}", registration);
                vehicles.add(new Tramvaj(registration,color,year));
                registracije.add(registration.toLowerCase());
                brojVozila++;
            }
        }
        System.out.println("✅ Uspjesno dodano vozilo.");
        log.info("Uspjesno dodano "+brojVozila+". vozilo.");
        log.trace("KRAJ - dodavanjeVozila metoda - novi broj vozila: {}", brojVozila);
        return brojVozila;
    }

    /**
     * Ispisuje listu svih vozila s pripadajućom dostupnošću.
     * Koristi dostupnostVozila metodu za prikaz stanja svakog vozila.
     *
     * @param vehicles Polje vozila
     * @param brojVozila Broj validnih vozila
     * @param routes Polje ruta za provjeru dostupnosti
     * @param brojRuta Broj validnih ruta
     * @see Vehicle
     * @see #dostupnostVozila(List, int, List, int)
     */
    private static void ispisiVozila(List<Vehicle>vehicles, int brojVozila, List<Route>routes, int brojRuta) {
        log.trace("POCETAK - ispisiVozila metoda");
        System.out.println("=== VOZILA ===");
        System.out.println();
        dostupnostVozila(vehicles, brojVozila, routes, brojRuta);
        log.trace("KRAJ - ispisiVozila metoda");
    }

    /**
     * Ispisuje sve linije u sustavu.
     * Za svaku liniju poziva njezin ispis metod.
     *
     * @param brojRuta Broj validnih ruta
     * @param routes Polje ruta
     * @see Route
     */
    private static void ispisiLinije(int brojRuta, List<Route>routes) {
        log.trace("POCETAK - ispisiLinije metoda");
        System.out.println("=== LINIJE ===");
        System.out.println();
        for(int i = 0; i< brojRuta; i++){
            log.trace("Ispis {}. linije", i+1);
            routes.get(i).ispis();
            System.out.println();
        }
        log.trace("KRAJ - ispisiLinije metoda");
    }

    /**
     * Pronalazi i ispisuje linije s najvećom i najmanjom kilometražom.
     * Omogućuje korisniku odabir između prikaza najkraće i najduže linije.
     *
     * @param scanner Scanner objekt za unos izbora
     * @param brojRuta Broj validnih ruta
     * @param routes Polje ruta
     * @see Route
     */
    private static void pronadiKilometrazu(Scanner scanner, int brojRuta, List<Route>routes) {
        log.trace("POCETAK - pronadiKilometrazu metoda");
        int odabir2;
        while(true) {
            System.out.println("1)Najkraca linija\n2)Najduza linija\n3)Izlaz");
            odabir2 = scanner.nextInt();
            log.debug("Uneseno odabir2: "+odabir2);
            routes.sort(Comparator.comparing((Route::getKilometers)).reversed());
//            routes.sort((r1,r2)->r1.getKilometers().compareTo(r2.getKilometers()));  lambda
            if (odabir2==1) {
                log.info("Unesena opcija: Najkraca Linija");
                System.out.println("Najkraca linija: ");
                routes.getLast().ispis();
                System.out.println();
            }

            else if (odabir2==2) {
                log.debug("Unesena odabir2: "+odabir2);
                log.info("Unesena opcija: Najduza Linija");
                System.out.println("Najduza linija: ");
                routes.getFirst().ispis();
                System.out.println();
            }

            else if(odabir2==3){
                log.debug("Unesena odabir2: "+odabir2);
                log.info("Korisnik odabrao opciju izlaz.");
                break;
            }
        }
        log.trace("KRAJ - pronadiKilometrazu metoda");
    }

    /**
     * Pronalazi i ispisuje sve linije koje kreću s određenog polazišta.
     * Pretraga nije osjetljiva na velika i mala slova.
     *
     * @param scanner Scanner objekt za unos polazišta
     * @param brojRuta Broj validnih ruta
     * @param routes Polje ruta
     * @see Route
     */
    private static void pronadiStanice(Scanner scanner, int brojRuta, List<Route>routes) {
        log.trace("POCETAK - pronadiStanice metoda");
        System.out.print("Unesite polaziste: ");
        String polaziste = scanner.nextLine();
        log.debug("Uneseno polaziste: "+polaziste);

        List<Route>pocetneStanice=routes.stream()
                .filter(route->polaziste.equalsIgnoreCase(route.getPocetnastanica()))
                .toList();

        if(pocetneStanice.isEmpty()){
            log.warn("Ne postoji linija koja kreće iz stanice "+polaziste);
            System.out.println("Ne postoji linija koja kreće iz stanice "+polaziste+".");
        }
        else{
            pocetneStanice.forEach(Route::ispis);
        }
        log.trace("KRAJ - pronadiStanice metoda");

    }

    /**
     * Pronalazi i ispisuje sve linije koje koriste vozilo s određenom registracijom.
     * Pretraga nije osjetljiva na velika i mala slova.
     *
     * @param scanner Scanner objekt za unos registracije
     * @param brojRuta Broj validnih ruta
     * @param routes Polje ruta
     * @see Route
     * @see Vehicle
     */
    private static void pronadiRegistraciju(Scanner scanner, int brojRuta, List<Route>routes,Set<String>registracije, Map<String,Vehicle>mapaVozila) {
        log.trace("POCETAK - pronadiRegistraciju metoda");
        System.out.print("Unesite registraciju: ");
        String registracijavozila = scanner.nextLine();
        log.debug("Unesena registracija: "+registracijavozila);
        Vehicle vozilo=mapaVozila.get(registracijavozila.toUpperCase());
        if(vozilo==null){
            System.out.println("Vozilo sa registracijom "+registracijavozila+" ne postoji.");
            return;
        }
        boolean postoji=false;
        for (Route ruta : routes) {
            if (registracijavozila.equalsIgnoreCase(ruta.getVehicle().getRegistration())) {
                ruta.ispis();
                postoji=true;
            }
        }
        if(!postoji){
            log.warn("Unesena registracija "+registracijavozila+" postoji, ali nema dodjeljenu liniju.");
            System.out.println("⚠\uFE0F Greska! Vozilo sa registracijom "+registracijavozila+" postoji, ali nema dodjeljenu liniju.");
            vozilo.ispis();
        } else {
            log.trace("Pronadjena registracija: {}", registracijavozila);
        }
        log.trace("KRAJ - pronadiRegistraciju metoda");
        return;
    }

    /**
     * Pronalazi i ispisuje najnovije i najstarije vozilo u sustavu.
     * Omogućuje korisniku odabir između prikaza najnovijeg i najstarijeg vozila.
     *
     * @param scanner Scanner objekt za unos izbora
     * @param vehicles Polje vozila
     * @param brojVozila Broj validnih vozila
     * @see Vehicle
     */
    private static void pronadiGodinuProizvodnje(Scanner scanner, List<Vehicle>vehicles, int brojVozila) {
        log.trace("POCETAK - pronadiGodinuProizvodnje metoda");
        int odabir;
        while(true) {
            System.out.println("1)Najnovije vozilo\n2)Najstarije vozilo\n3)Izlaz");
            odabir= scanner.nextInt();
            log.debug("Korisnik odabrao odabir: "+odabir);
            scanner.nextLine();
            vehicles.sort(Comparator.comparing(Vehicle::getYear).reversed());
            if (odabir == 1) {
                log.info("Unesena opcija: Najnovije vozilo");
                System.out.println("===Najnovije vozilo===\n");
                vehicles.getFirst().ispis();
                System.out.println("-----------------------------");
                System.out.println();
            }
            else if (odabir == 2) {
                log.info("Unesena opcija: Najstarije vozilo");
                System.out.println("===Najstarije vozilo===\n");
                vehicles.getLast().ispis();
                System.out.println("-----------------------------");
                System.out.println();
            }
            else if(odabir==3){
                log.info("Korisnik odabrao opciju izlaz.");
                break;
            }
        }
        log.trace("KRAJ - pronadiGodinuProizvodnje metoda");
    }

    /**
     * Popunjuje mapu vozila.
     * @param mapaVozila
     * @param vehicles
     */
    private static void popuniMapuVozilima(Map<String,Vehicle>mapaVozila, List<Vehicle>vehicles){
        mapaVozila.clear();
        for(Vehicle vozilo:vehicles){
            mapaVozila.put(vozilo.getRegistration(),vozilo);
        }
        log.info("Popunjena mapa vozila.");
    }

    /**
     * Pretrazuje elektricna vozila.
     * @param vehicles
     */
    private static void podjelaNaElektricna(List<Vehicle>vehicles){
        log.trace("POCETAK - podjelaNaElektricna metoda");
        Map<Boolean,List<Vehicle>> elektricnaVozila=vehicles.stream()
                .collect(Collectors.partitioningBy(vozilo ->
        vozilo instanceof Elektricni && ((Elektricni) vozilo).jeElektricni()));

        System.out.println("=== ELEKTRICNA VOZILA ===");
        elektricnaVozila.get(true).forEach(Vehicle::ispis);
    }
}