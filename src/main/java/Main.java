import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

            String[] commands = input.split(" ");

            if (commands[0].equals("type")) {
                handleTypeCommand(commands);
            } else if (commands[0].equals("exit")) {
                break;
            } else if (commands[0].equals("echo")) {
                System.out.println(input.substring(4).trim());
            } else {
                System.out.println(input + ": command not found");
            }

        }
    }

    private static void handleTypeCommand(String[] commands) {
        if (builtinCommands.contains(commands[1])) {
            System.out.println(commands[1] + " is a shell builtin");
            return;
        } else {
            String path = System.getenv("PATH");
            if (path == null || path.isEmpty()) {
                System.out.println(commands[1] + ": not found");
                return;
            }

            String[] directories = path.split(File.pathSeparator, -1);

            for (String directory : directories) {
                if (directory.isEmpty()) {
                    directory = ".";
                }
                File executable = new File(directory, commands[1]);

                if (executable.isFile() && executable.canExecute()) {
                    System.out.println(commands[1] + " is " + executable.getAbsolutePath());
                    return;
                }
            }
        }
        System.out.println(commands[1] + ": command not found");
    }
}
