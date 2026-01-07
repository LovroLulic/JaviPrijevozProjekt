package entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumerira tip vozila.
 */
public enum TipVozila {
    Bus,
    Tramvaj;

    @JsonCreator
    public static TipVozila fromString(String v) {
        if (v == null) return null;
        String norm = v.trim().toUpperCase();
        switch (norm) {
            case "BUS":
            case "AUTOBUS":
                return Bus;
            case "TRAMVAJ":
                return Tramvaj;
            default:
                throw new IllegalArgumentException("Nepoznat TipVozila: " + v);
        }
    }

    @JsonValue
    public String toJson() {
        return name(); // ili vratite lokalizirani naziv ako želite, ali onda prilagodite @JsonCreator
    }
}
