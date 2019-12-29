package flashcards;

public class Main {
    public static void main(String[] args) {
        Game.start();

        if (args.length > 0) {
            Game.useArgs(args);
        }

        Game.resume();
    }
}
