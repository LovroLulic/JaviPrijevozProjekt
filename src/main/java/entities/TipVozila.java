package entities;

/**
 * Enumerira tip vozila.
 */
public enum TipVozila {
    BUS("Bus"),
    TRAMVAJ("Tramvaj");

    private final String naziv;

    TipVozila(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}
