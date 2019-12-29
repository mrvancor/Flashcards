package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static flashcards.Game.logs;


public class Downloader {
    public static void download(String fileName) {
        File file = new File(fileName);

        try (Scanner fScan = new Scanner(file)) {
            int count = 0;

            while (fScan.hasNext()) {
                String term = fScan.nextLine();
                String def = fScan.nextLine();
                int mistakes = Integer.parseInt(fScan.nextLine());

                Game.flashcards.put(term, def);
                Game.cardsStats.put(term, mistakes);

                count++;
            }

            System.out.println(count + " cards have been loaded.\n");
            logs.add(count + " cards have been loaded.\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.\n");
            logs.add("File not found.\n");
        }
    }
}
