package entities;
/**
 * Predstavlja osobu u sustavu.
 * Služi kao osnovna klasa za sve tipove osoba.
 *
 * @author Lovro Lulic
 * @version 1.0
 */
public abstract class Person {
    private String name;
    private int age;

    /**
     * Konstruktor za stvaranje osobe.
     * @param name
     * @param age
     */
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    /**
     * Ispisuje informacije o osobi.
     */
    public abstract void ispis();

    //abstraktna klasa, treba  public abstract void display();
    //u ostalim klasama koje extendaju ovu klasu treba override i iskoristit display()
}
