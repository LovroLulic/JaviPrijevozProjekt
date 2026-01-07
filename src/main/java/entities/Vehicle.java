package entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "vehicle_class"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Bus.class, name = "Bus"),
        @JsonSubTypes.Type(value = Tramvaj.class, name = "Tramvaj")
})
/**
 * Predstavlja vozilo u sustavu javnog prijevoza.
 * Služi kao osnovna klasa za sve tipove vozila.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public abstract class Vehicle {
    private String registration;
    private TipVozila type;
    private String color;
    private Integer year;


    public Vehicle() {}
    /**
     * Konstruktor za stvaranje vozila.
     *
     * @param registration Registracijska oznaka vozila
     * @param type Tip vozila (Bus/Tramvaj)
     * @param color Boja vozila
     * @param year Godina proizvodnje
     */
    public Vehicle(String registration, TipVozila type, String color, Integer year) {
        this.registration = registration;
        this.type = type;
        this.color = color;
        this.year = year;
    }

    public String getRegistration() {
        return registration;
    }

    @JsonIgnore
    public String getType() {
        return type.getNaziv();
    }

    public String getColor() {
        return color;
    }

    public Integer getYear() {
        return year;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setType(TipVozila type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    /**
     * Ispisuje informacije o vozilu.
     */
    public abstract void ispis();
}
