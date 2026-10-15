import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.print("$ ");
        Scanner scan = new Scanner(System.in);

        while (true) {

            String input = scan.nextLine();
            System.out.println(input + ": command not found");
        }
    }
}
