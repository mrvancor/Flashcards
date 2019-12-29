package flashcards;

import static flashcards.Game.logs;
import static flashcards.Game.scan;

public class Menu {

    public static void choose() {

        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            logs.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String choice = scan.nextLine();
            logs.add(choice);

            switch (choice) {
                case "add":
                    Game.add();
                    break;
                case "remove":
                    Game.remove();
                    break;
                case "import":
                    Game.load();
                    break;
                case "export":
                    Game.save();
                    break;
                case "ask":
                    Game.ask();
                    break;
                case "log":
                    Game.log();
                    break;
                case "hardest card":
                    Game.hardestCard();
                    break;
                case "reset stats":
                    Game.resetStats();
                    break;
                case "exit":
                    Game.finish();
                    return;
            }
        }
    }
}
