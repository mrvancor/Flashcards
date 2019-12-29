package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Game {
    protected static Scanner scan;
    protected static List<String> logs;
    protected static Map<String, String> flashcards;
    protected static Map<String, Integer> cardsStats;
    private static List<String> hardestCards;
    private static String exFileName;

    public static void start() {
        scan = new Scanner(System.in);

        flashcards = new LinkedHashMap<>();

        cardsStats = new LinkedHashMap<>();
        hardestCards = new ArrayList<>();

        logs = new ArrayList<>();
    }

    protected static void useArgs(String[] args) {
        if (args.length == 4) {
            if (args[0].equals("-import")) {
                Downloader.download(args[1]);
                exFileName = args[3];
            } else {
                Downloader.download(args[3]);
                exFileName = args[1];
            }
        } else {
            if (args[0].equals("-import")) {
                Downloader.download(args[1]);
                exFileName = null;
            } else {
                exFileName = args[1];
            }
        }
    }

    public static void resume() {
        Menu.choose();
    }

    protected static void add() {
        System.out.println("The card:");
        logs.add("The card:");
        String term = scan.nextLine();
        logs.add(term);

        if (!isTermExists(term)) {
            System.out.println("The definition of the card:");
            logs.add("The definition of the card:");
            String def = scan.nextLine();
            logs.add(def);

            if (!isDefExists(def)) {
                flashcards.put(term, def);
                System.out.println("The pair (\"" + term + "\":\"" + def + "\") has been added.\n");
                logs.add("The pair (\"" + term + "\":\"" + def + "\") has been added.\n");
            }
        }
    }

    protected static void remove() {
        System.out.println("The card:");
        logs.add("The card:");
        String card = scan.nextLine();
        logs.add(card);

        if (isCardExists(card)) {
            flashcards.remove(card);
            cardsStats.remove(card);
            hardestCards.remove(card);

            System.out.println("The card has been removed.\n");
            logs.add("The card has been removed.\n");
        } else {
            System.out.println("Can't remove \"" + card + "\": there is no such card.\n");
            logs.add("Can't remove \"" + card + "\": there is no such card.\n");
        }
    }

    private static boolean isCardExists(String card) {
        return flashcards.containsKey(card);
    }

    private static boolean isTermExists(String term) {
        if (flashcards.containsKey(term)) {
            System.out.println("The card \"" + term + "\" already exists.\n");
            logs.add("The card \"" + term + "\" already exists.\n");
            return true;
        }

        return false;
    }

    private static boolean isDefExists(String def) {
        if (flashcards.containsValue(def)) {
            System.out.println("The definition \"" + def + "\" already exists.\n");
            logs.add("The definition \"" + def + "\" already exists.\n");
            return true;
        }

        return false;
    }

    protected static void load() {
        System.out.println("File name:");
        logs.add("File name:");
        String fileName = scan.nextLine();
        logs.add(fileName);
        File file = new File(fileName);

        try (Scanner fScan = new Scanner(file)) {
            int count = 0;

            while (fScan.hasNext()) {
                String term = fScan.nextLine();
                String def = fScan.nextLine();
                int mistakes = Integer.parseInt(fScan.nextLine());

                flashcards.put(term, def);
                cardsStats.put(term, mistakes);

                count++;
            }

            System.out.println(count + " cards have been loaded.\n");
            logs.add(count + " cards have been loaded.\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.\n");
            logs.add("File not found.\n");
        }
    }

    protected static void save() {
        System.out.println("File name:");
        logs.add("File name:");
        String fileName = scan.nextLine();
        logs.add(fileName);
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

    protected static void ask() {
        System.out.println("How many times to ask?");
        logs.add("How many times to ask?");
        int times = scan.nextInt();
        logs.add(times + "");
        scan.nextLine();

        printAns(times);
    }

    private static String getKeyByValue(String value) {
        for (Map.Entry<String, String> entry : flashcards.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void printAns(int times) {
        while (times > 0) {
            for (Map.Entry<String, String> entry : flashcards.entrySet()) {
                if (times < 1) {
                    return;
                }

                System.out.println("Print the definition of \"" + entry.getKey() + "\":");
                logs.add("Print the definition of \"" + entry.getKey() + "\":");
                String answer = scan.nextLine();
                logs.add(answer);

                if (entry.getValue().equals(answer)) {
                    System.out.println("Correct answer.\n");
                    logs.add("Correct answer.\n");
                } else {
                    if (flashcards.containsValue(answer)) {
                        System.out.println("Wrong answer. The correct one is \"" + entry.getValue() + "\"," +
                                " you've just written the definition of \"" + getKeyByValue(answer) + "\".\n");
                        logs.add("Wrong answer. The correct one is \"" + entry.getValue() + "\"," +
                                " you've just written the definition of \"" + getKeyByValue(answer) + "\".\n");
                    } else {
                        System.out.println("Wrong answer. The correct one is \"" + entry.getValue() + "\".\n");
                        logs.add("Wrong answer. The correct one is \"" + entry.getValue() + "\".\n");
                    }
                    cardsStats.put(entry.getKey(), cardsStats.getOrDefault(entry.getKey(), 0) + 1);
                }
                times--;
            }
        }
    }

    protected static void log() {
        System.out.println("File name:");
        logs.add("File name:");
        String fileName = scan.nextLine();
        logs.add(fileName);
        File file = new File(fileName);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String line : logs) {
                writer.println(line);
            }

            writer.println("The log has been saved.\n");
            System.out.println("The log has been saved.\n");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    protected static void hardestCard() {
        if (cardsStats.isEmpty()) {
            System.out.println("There are no cards with errors.\n");
            logs.add("There are no cards with errors.\n");
        } else {
            getHardestCards();

            if (hardestCards.size() == 1) {
                String card = hardestCards.get(0);
                System.out.println("The hardest card is \"" +
                        card + "\". You have " + cardsStats.get(card) +
                        " errors answering it.\n");
                logs.add("The hardest card is \"" +
                        card + "\". You have " + cardsStats.get(card) +
                        " errors answering it.\n");
            } else {
                System.out.print("The hardest cards are ");
                logs.add("The hardest cards are ");

                int size = hardestCards.size();

                for (int i = 0; i < size; i++) {
                    String card = hardestCards.get(i);

                    if (i == size - 1) {
                        System.out.print("\"" + card + "\". ");
                        logs.add("\"" + card + "\". ");
                    } else {
                        System.out.print("\"" + card + "\", ");
                        logs.add("\"" + card + "\", ");
                    }
                }

                System.out.println("You have " +
                        cardsStats.get(hardestCards.get(0)) +
                        " errors answering them.\n");
                logs.add("You have " +
                        cardsStats.get(hardestCards.get(0)) +
                        " errors answering them.\n");
            }
        }
    }

    private static void getHardestCards() {
        int hardestCardValue = getHardestCardValue();

        if (!hardestCards.isEmpty()) {
            hardestCards.clear();
        }

        for (Map.Entry<String, Integer> entry : cardsStats.entrySet()) {
            if (entry.getValue() == hardestCardValue) {
                hardestCards.add(entry.getKey());
            }
        }
    }

    private static int getHardestCardValue() {
        int max = 0;

        for (Map.Entry<String, Integer> entry : cardsStats.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }
        return max;
    }

    protected static void resetStats() {
        if (!cardsStats.isEmpty()) {
            cardsStats.clear();
            hardestCards.clear();
        }

        System.out.println("Card statistics has been reset.\n");
        logs.add("Card statistics has been reset.\n");
    }

    protected static void finish() {
        System.out.println("Bye bye!");

        if (exFileName != null && !exFileName.isEmpty()) {
            Uploader.upload(exFileName);
        }
    }
}
