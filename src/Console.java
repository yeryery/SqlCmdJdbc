import java.util.Scanner;

public class Console implements View{
    @Override
    public void write(String input) {
        System.out.println(input);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
