package entities;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
/**
 * Predstavlja rutu ili liniju u sustavu javnog prijevoza.
 * Sadrži informacije o vozilu, vremenu polaska, stanicama i cijenama.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class Route implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Vehicle vehicle;
    private LocalDate date;
    private String time;
    private String pocetnastanica;
    private String krajnastanica;
    private BigDecimal kilometers;
    private CijenaKarte cjenik;

    public Route() {
        super();
    }

    private Route(Vehicle vehicle, LocalDate date, String time, String pocetnastanica,String krajnastanica, BigDecimal kilometers, CijenaKarte cjenik) {
        this.vehicle = vehicle;
        this.date = date;
        this.time = time;
        this.pocetnastanica = pocetnastanica;
        this.krajnastanica = krajnastanica;
        this.kilometers = kilometers;
        this.cjenik=cjenik;
    }

    public static Builder builder(Vehicle vehicle, LocalDate date){
        return new Builder(vehicle,date);
    }

    /**
     * Builder klasa za stvaranje Route objekata s podrazumijevanim vrijednostima.
     */
    public static class Builder{
        private final Vehicle vehicle;
        private final LocalDate date;
        private String time="00:00";
        private String pocetnastanica="Nepoznata";
        private String krajnastanica="Nepoznata";
        private BigDecimal kilometers=BigDecimal.ZERO;
        private CijenaKarte cjenik;

        /**
         * Konstruktor za Builder klasu.
         * @param vehicle
         * @param date
         */
        private Builder(Vehicle vehicle, LocalDate date){
            this.vehicle=vehicle;
            this.date=date;
        }
        /**
         * Postavlja vrijeme polaska za rutu.
         *
         * @param time Vrijeme u formatu "HH:mm"
         * @return Builder objekt za ulančavanje
         */
        public Builder time(String time){
            this.time=time;
            return this;
        }

        /**
         * Postavlja pocetnu stanicu za rutu.
         * @param pocetnastanica
         * @return
         */
        public Builder pocetnastanica(String pocetnastanica){
            this.pocetnastanica=pocetnastanica;
            return this;
        }

        /**
         * Postavlja krajnju stanicu za rutu.
         * @param krajnastanica
         * @return
         */
        public Builder krajnastanica(String krajnastanica){
            this.krajnastanica=krajnastanica;
            return this;
        }

        /**
         * Postavlja udaljenost od pocetne stanice do krajnje stanice za rutu.
         * @param kilometers
         * @return
         */
        public Builder kilometers(BigDecimal kilometers){
            this.kilometers=kilometers;
            return this;
        }

        /**
         * Postavlja cijenu za rutu.
         * @param cjenik
         * @return
         */
        public Builder cjenik(CijenaKarte cjenik){
            this.cjenik=cjenik;
            return this;
        }

        /**
         * Stvara Route objekt.
         * @return
         */
        public Route build(){
            return new Route(vehicle,date,time,pocetnastanica,krajnastanica,kilometers,cjenik);
        }
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPocetnastanica() {return pocetnastanica;}

    public String getKrajnastanica() {return krajnastanica;}

    public BigDecimal getKilometers() { return kilometers; }

    public CijenaKarte getCjenik() { return cjenik; } // KLJUČNO: Dodano za ispravno spremanje
    public void setCjenik(CijenaKarte cjenik) { this.cjenik = cjenik; }
    /**
     * Ispisuje informacije o ruti.
     */
    public void ispis(){
        System.out.println("----------------------------------");
        System.out.println("Smjer " + pocetnastanica+" - "+krajnastanica);
        System.out.println("Datum: "+date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))+ " u "+time+" sati.");
        System.out.println("Udaljenost: "+kilometers+" km.");
        System.out.println();
        boolean nocna=CijenaKarte.jeNocna(time);
        if(nocna){
            System.out.println("⚠\uFE0F NOCNA VOZNJA (23:30 - 05:30)");
            System.out.println("Cijena: "+cjenik.nocnaCijena()+"€");
            System.out.println("(Studentski popust: "+cjenik.studentskaNocna()+"€, umirovljenicki popust: "+cjenik.umirovljenickaNocna()+"€)");
            System.out.println();
        }
        else{
            System.out.println("Cijena: "+cjenik.cijena()+"€");
            System.out.println("(Studentski popust: "+cjenik.studentskaCijena()+"€, umirovljenicki popust: "+cjenik.umirovljenickaCijena()+"€)");
            System.out.println();
        }
        vehicle.ispis();

        System.out.println("----------------------------------");
    }
}
