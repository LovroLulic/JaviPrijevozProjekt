package entities;

import java.util.Random;
/**
 * Predstavlja autobus u sustavu javnog prijevoza.
 * Nasljeđuje Vehicle klasu i implementira Elektricni sučelje.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class Bus extends Vehicle implements Elektricni, java.io.Serializable{
    private static final long serialVersionUID = 1L;


    private int pogon;

    public Bus(){
        super();
        this.setType( TipVozila.Bus);
    }


    /**
     * Konstruktor za stvaranje autobusa.
     *
     * @param registration Registracijska oznaka autobusa
     * @param color Boja autobusa
     * @param year Godina proizvodnje
     */
    public Bus(String registration, String color, Integer year) {
        super(registration, TipVozila.Bus, color, year);
        Random random=new Random();
        int rndm=random.nextInt(1,5);  //1-benzinski/dizel, 2-elektrican, 3-hibrid, 4-plinski
        this.pogon=rndm;
    }

    /**
     * Provjerava je li autobus električni.
     *
     * @return true ako je električni, false inače
     */
    @Override
    public boolean jeElektricni() {
        return pogon==2;
    }
    /**
     * Provjerava je li autobus hibridni.
     *
     * @return true ako je hibridni, false inače
     */
    @Override
    public boolean jeHibridni(){
        return pogon==3;
    }
    /**
     * Provjerava je li autobus plinski.
     *
     * @return true ako je plinski, false inače
     */
    @Override
    public boolean jePlinski(){
        return pogon==4;
    }
    /**
     * Provjerava je li tramvaj.
     *
     * @return true ako je tramvaj (odma elektricni), false inače
     */
    @Override
    public boolean jeTramvaj(){return false;}
    /**
     * Ispisuje detaljne informacije o autobusu.
     */
    @Override
    public void ispis(){
        System.out.println("===AUTOBUS===");
        System.out.println("Registracija: "+getRegistration());
        System.out.println("Boja: "+getColor());
        System.out.println("Godina: "+getYear());
        System.out.println("Pogon: "+pogon());
        System.out.println();
    }


}
