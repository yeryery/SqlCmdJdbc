public class Main {

    public static void main(String[] args) {
        View view = new Console();
        JDBCManager manager = new JDBCManager();

        Controller controller = new Controller(view, manager);

        controller.run();
    }

}
