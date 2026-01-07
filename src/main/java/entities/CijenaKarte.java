package entities;

import java.math.BigDecimal;
/**
 * Predstavlja cjenik karata za različite tipove putnika i vremenske periode.
 * Sadrži cijene za redovne, studentske, umirovljeničke i noćne karte.
 *
 * @param cijena Redovna cijena karte
 * @param studentskaCijena Studentska cijena karte
 * @param umirovljenickaCijena Umirovljenička cijena karte
 * @param nocnaCijena Noćna redovna cijena karte
 * @param studentskaNocna Noćna studentska cijena karte
 * @param umirovljenickaNocna Noćna umirovljenička cijena karte
 * @author Lovro Lulic
 * @version 1.0
 */
public record CijenaKarte(
        BigDecimal cijena,
        BigDecimal studentskaCijena,
        BigDecimal umirovljenickaCijena,
        BigDecimal nocnaCijena,
        BigDecimal studentskaNocna,
        BigDecimal umirovljenickaNocna)
    implements java.io.Serializable

{
    private static final long serialVersionUID = 1L;
    /**
     * Provjerava je li vrijeme polaska unutar noćnog voznog reda (23:30 - 05:30).
     *
     * @param vrijeme Vrijeme u formatu "HH:mm"
     * @return true ako je noćna vožnja, false inače
     */
    public static boolean jeNocna(String vrijeme){
        String[] podijeljen=vrijeme.split(":");
        int sat=Integer.parseInt(podijeljen[0]);
        int minuta=Integer.parseInt(podijeljen[1]);

        return(sat==23 && minuta>=30) ||
                (sat>=0 && sat<5) ||
                (sat==5 && minuta>=30);
    }


}
