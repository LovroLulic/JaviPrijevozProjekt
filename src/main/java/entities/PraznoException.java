package entities;
/**
 * Baca se kada je korisnički unos prazan.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class PraznoException extends RuntimeException {
    public PraznoException(String message) {
        super(message);
    }
}
