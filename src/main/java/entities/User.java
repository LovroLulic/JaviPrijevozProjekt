package entities;




/**
 * Predstavlja korisnika sustava.
 * Nasljeđuje Person klasu i implementira Provjera sučelje.
 *
 * @author Lovro Lulic
 * @version 1.0
 */

public final class User extends Person implements Provjera {
    private String nameID;
    private String email;

    /**
     * Konstruktor za stvaranje korisnika.
     *
     * @param name Ime i prezime korisnika
     * @param age Dob korisnika
     * @param nameID Korisničko ime
     * @param email Email adresa korisnika
     */
    public User(String name, int age, String nameID, String email) {
        super(name, age);
        this.nameID = nameID;
        this.email = email;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameID() {
        return nameID;
    }

    public String getEmail() {
        return email;
    }


    /**
     * Ispisuje informacije o korisniku.
     */
    @Override
    public void ispis(){
        System.out.println("Ime: " + getName() + ", Godine: " + getAge() +
                ", Username: "+nameID+", Email: "+email+".");
    }

    /**
     * Provjerava da li je korisnik unio dobro ime i prezime.
     * @param name
     * @return
     */
    @Override
    public boolean provjeriImePrezime(String name)  {
        if(name==null || name.trim().isEmpty()){
            return false;
        }

        return name.split(" ").length>=2;
    }

    /**
     * Provjerava da li je korisnik unio dobro godinu.
     * @param godina
     * @return
     */
    @Override
    public boolean provjeriGodine(int godina) {
        if(godina <100 && godina>10){
            return true;
        }
        return false;
    }

    /**
     * Provjerava da li je korisnik unio dobro email adresu.
     * @param email
     * @return
     */
    @Override
    public boolean provjeriMail(String email) {
        if(!email.contains("@")){
            return false;
        }
        if(email==null || email.trim().isEmpty()){
            return false;
        }
        return true;
    }
}
