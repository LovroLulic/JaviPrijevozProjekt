package entities;
/**
 * Baca se kada je korisnički unos negativan broj.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class NegativniUnosException extends RuntimeException {
    public NegativniUnosException(String message) {
        super(message);
    }
}
