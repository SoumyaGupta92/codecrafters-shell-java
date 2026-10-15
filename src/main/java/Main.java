import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String input = scan.nextLine();

            if (input.equals("exit")) {
                break;
            } else if (input.startsWith("echo")) {
                System.out.println(input.substring(4).trim());
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
