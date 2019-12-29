package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static flashcards.Game.*;

public class Uploader {
    public static void upload(String fileName) {
        File file = new File(fileName);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (Map.Entry<String, String> entry : flashcards.entrySet()) {
                writer.println(entry.getKey());
                writer.println(entry.getValue());
                writer.println(cardsStats.getOrDefault(entry.getKey(), 0));
            }
            System.out.println(flashcards.size() + " cards have been saved.\n");
            logs.add(flashcards.size() + " cards have been saved.\n");
        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }
}
