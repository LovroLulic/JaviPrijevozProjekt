package entities;
/**
 * Baca se kada je korisnički unos pogrešnog formata.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class PogresanUnosException extends Exception {
    public PogresanUnosException(String message) {
        super(message);
    }
}
