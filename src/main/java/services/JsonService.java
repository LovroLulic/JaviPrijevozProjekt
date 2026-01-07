package services;

import jakarta.json.bind.*;
import java.io.*;
import java.util.*;

public class JsonService {

    public static <T> void spremiUJson(String putanja, List<T> lista) {
        JsonbConfig config = new JsonbConfig().withFormatting(true);

        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            try (FileWriter writer = new FileWriter(putanja)) {
                jsonb.toJson(lista, writer);
            }
        } catch (Exception e) {
            System.err.println("Greška pri spremanju u " + putanja + ": " + e.getMessage());
        }
    }

    public static <T> List<T> ucitajIzJsona(String putanja, Class<T> klasa) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            File datoteka = new File(putanja);
            if (!datoteka.exists()) {
                return new ArrayList<>();
            }

            return jsonb.fromJson(new FileInputStream(datoteka),
                    new ArrayList<T>(){}.getClass().getGenericSuperclass());
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju iz " + putanja + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
