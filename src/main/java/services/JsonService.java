package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonService {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static <T> void spremiUJson(String putanja, List<T> lista) {
        try {
            mapper.writeValue(new File(putanja), lista);
        } catch (Exception e) {
            System.err.println("Greška pri spremanju u " + putanja + ": " + e.getMessage());
        }
    }

    public static <T> List<T> ucitajIzJsona(String putanja, Class<T> klasa) {
        try {
            File datoteka = new File(putanja);
            if (!datoteka.exists()) return new ArrayList<>();

            return mapper.readValue(datoteka,
                    mapper.getTypeFactory().constructCollectionType(List.class, klasa));
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju iz " + putanja + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}