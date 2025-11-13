package entities;
/**
 * Baca se kada je datum pogrešnog formata.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public class PogresanDatumException extends Exception {
    public PogresanDatumException(String message) {
        super(message);
    }
}
