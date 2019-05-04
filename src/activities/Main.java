package activities;

class Main
{
    private static final String fileName = "data/activities.txt";

    public static void main(String[] args)
    {
        Controller controller = new Controller(fileName);
        ConsoleUI consoleUI = new ConsoleUI(controller);
        consoleUI.run();
    }
}
