import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static Set<String> builtinCommands = new HashSet<>(Set.of("exit", "echo", "type"));

    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String input = scan.nextLine();

            if (input.startsWith("type")) {
                if (builtinCommands.contains(input.split("\\s+")[1])) {
                    System.out.println(input.split("\\s+")[1] + " is a shell builtin");
                } else {
                    System.out.println(input.split("\\s+")[1] + ": not found");
                }
            } else if (input.equals("exit")) {
                break;
            } else if (input.startsWith("echo")) {
                System.out.println(input.substring(4).trim());
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
