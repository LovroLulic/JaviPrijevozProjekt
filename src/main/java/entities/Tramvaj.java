package entities;
/**
 * Predstavlja tramvaj u sustavu javnog prijevoza.
 * Nasljeđuje Vehicle klasu i implementira Elektricni sučelje.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class Tramvaj extends Vehicle implements Elektricni{
    /**
     * Konstruktor za stvaranje tramvaja.
     *
     * @param registration Registracijska oznaka tramvaja
     * @param color Boja tramvaja
     * @param year Godina proizvodnje
     */
    public Tramvaj(String registration, String color, Integer year) {
        super(registration, TipVozila.TRAMVAJ, color, year);
    }
    /**
     * Provjerava je li vozilo tramvaj.
     *
     * @return uvijek true jer je ovo tramvaj
     */
    @Override
    public boolean jeTramvaj(){return true;}
    /**
     * Provjerava je li vozilo Elektricno.
     *
     * @return uvijek true jer je ovo tramvaj
     */
    @Override
    public boolean jeElektricni(){return true;}
    /**
     * Provjerava je li vozilo Hibridno.
     *
     * @return uvijek false jer je ovo tramvaj
     */
    @Override
    public boolean jeHibridni(){return false;}
    /**
     * Provjerava je li vozilo Plinsko.
     *
     * @return uvijek false jer je ovo tramvaj
     */
    @Override
    public boolean jePlinski(){return false;}
    /**
     * Ispisuje detaljne informacije o tramvaju.
     */
    @Override
    public void ispis(){
        System.out.println("===TRAMVAJ===");
        System.out.println("Registracija: " + getRegistration());
        System.out.println("Boja: " + getColor());
        System.out.println("Godina: " + getYear());
        System.out.println("Pogon: "+pogon());
        System.out.println();
    }
}
