public class Controller {

    private View view;
    private Connect connect;

    public Controller(View view, JDBCManager manager) {
        this.view = view;
        this.connect = new Connect(view, manager);
    }

    public void run() {
        view.write("Hello, user!");
        view.write("Please, enter: " +
                        "connect|database|username|password");

        String input = view.read();
        connect.process(input);
        view.write("Success!");
    }
}
//connect|yeryery|postgres|nepomny
