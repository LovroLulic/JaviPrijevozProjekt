package entities;


/**
 * Fefinira metode za validaciju korisničkih podataka.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public sealed interface Provjera permits User {
    /**
     * Provjerava da li je korisnik unio dobro ime i prezime.
     * @param imeprezime
     * @return
     */
    boolean provjeriImePrezime(String imeprezime);

    /**
     * Provjerava da li je korisnik unio dobro godinu.
     * @param godina
     * @return
     */
    boolean provjeriGodine(int godina);

    /**
     * Provjerava da li je korisnik unio dobro email adresu.
     * @param mail
     * @return
     */
    boolean provjeriMail(String mail);
}
