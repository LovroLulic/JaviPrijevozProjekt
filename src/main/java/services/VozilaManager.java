package services;

import entities.*;
import java.util.*;
import java.util.stream.Collectors;

public class VozilaManager {
    private List<Vehicle> svaVozila = new ArrayList<>();
    private Map<String, Vehicle> vozilaPoRegistraciji = new HashMap<>();
    private static final String FILE_PATH="vozila.json";

    public void dodajPocetnaVozila(){
        dodajVozilo(new Tramvaj("ZG19055A", "Plava", 2019));
        dodajVozilo(new Bus("ZG23045B", "Plava", 2012));
        dodajVozilo(new Tramvaj("ZG01045C", "Bijela", 2023));
        dodajVozilo(new Bus("ZG01045D", "Bijela", 2023));
        dodajVozilo(new Bus("ZG01045E", "Zelena", 2023));
        dodajVozilo(new Tramvaj("ZG01045F", "Roza", 2023));
        dodajVozilo(new Bus("ZG01045G", "Crvena", 2023));
    }

    public void ucitajSvaVozila(){
        this.svaVozila=JsonService.ucitajIzJsona(FILE_PATH,Vehicle.class);
        svaVozila.forEach(v->vozilaPoRegistraciji.put(v.getRegistration().toUpperCase(),v));

    }

    public void osvjeziPodatke(List<Vehicle> noviPodaci) {
        this.svaVozila = noviPodaci;
        this.vozilaPoRegistraciji.clear();
        for (Vehicle v : svaVozila) {
            vozilaPoRegistraciji.put(v.getRegistration().toUpperCase(), v);
        }

        JsonService.spremiUJson("vozila.json", svaVozila);
    }

    public void dodajVozilo(Vehicle vozilo) {
        svaVozila.add(vozilo);
        vozilaPoRegistraciji.put(vozilo.getRegistration().toUpperCase(), vozilo);
        JsonService.spremiUJson(FILE_PATH,svaVozila);
    }

    public Optional<Vehicle> nadjiVozilo(String registracija) {
        return Optional.ofNullable(vozilaPoRegistraciji.get(registracija.toUpperCase()));
    }

    public boolean voziloPostoji(String registracija) {
        return vozilaPoRegistraciji.containsKey(registracija.toUpperCase());
    }

    public void ispisiSvaVozila() {
        System.out.println("=== SVA VOZILA ===");
        svaVozila.forEach(Vehicle::ispis);
    }

    public void ispisiElektricnaVozila() {
        System.out.println("=== ELEKTRIČNA VOZILA ===");
        svaVozila.stream()
                .filter(vozilo -> vozilo instanceof Elektricni)
                .filter(vozilo -> ((Elektricni) vozilo).jeElektricni())
                .forEach(Vehicle::ispis);
    }

    public Optional<Vehicle> getNajnovijeVozilo() {
        return svaVozila.stream()
                .max(Comparator.comparing(Vehicle::getYear));
    }

    public Optional<Vehicle> getNajstarijeVozilo() {
        return svaVozila.stream()
                .min(Comparator.comparing(Vehicle::getYear));
    }

    public List<Vehicle> getSvaVozila() {
        return svaVozila;
    }
}
