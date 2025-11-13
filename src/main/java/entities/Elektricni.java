package entities;
/**
 * Definira metode za provjeru tipa pogona vozila.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public interface Elektricni {
    /**
     * Provjerava je li vozilo elektricno.
     * @return
     */
    boolean jeElektricni();

    /**
     * Provjerava je li vozilo hibridno.
     * @return
     */
    boolean jeHibridni();

    /**
     * Provjerava je li vozilo plinsko.
     * @return
     */
    boolean jePlinski();

    /**
     * Provjerava je li vozilo tramvaj.
     * @return
     */
    boolean jeTramvaj();

    /**
     * Ispisuje pogon vozila.
     * @return
     */
    default String pogon(){
        if(jeHibridni()) return "Hibrid";
        if(jePlinski()) return "Plinski";
        return jeElektricni()?"Elektrican":"Dizel";
    }
}
