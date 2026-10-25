
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Set<String> builtinCommands = new HashSet<>(Set.of("exit", "echo", "type"));

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            System.out.flush();

            if (!scan.hasNextLine()) {
                break;
            }

            String input = scan.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0];

            switch (command) {
                case "exit":
                    scan.close();
                    return;

                case "echo":
                    System.out.println(
                            input.length() > 4
                                    ? input.substring(5)
                                    : "");
                    break;

                case "type":
                    if (parts.length < 2) {
                        System.out.println("type: missing operand");
                        break;
                    }

                    for (int i = 1; i < parts.length; i++) {
                        typeCommand(parts[i]);
                    }
                    break;

                default:
                    System.out.println(command + ": not found");
            }
        }

        scan.close();
    }

    private static void typeCommand(String command) {
        // 1. Check builtins first.
        if (builtinCommands.contains(command)) {
            System.out.println(command + " is a shell builtin");
            return;
        }

        // 2. Read PATH from the environment.
        String pathEnv = System.getenv("PATH");

        if (pathEnv != null) {
            // Use the platform-specific PATH separator.
            String[] directories = pathEnv.split(
                    java.util.regex.Pattern.quote(File.pathSeparator),
                    -1);

            // 3. Search directories in order.
            for (String directory : directories) {
                Path executablePath = directory.isEmpty()
                        ? Paths.get(command)
                        : Paths.get(directory, command);

                // 4. Skip missing files and non-executable files.
                if (Files.isRegularFile(executablePath)
                        && Files.isExecutable(executablePath)) {

                    System.out.println(
                            command + " is "
                                    + executablePath.toAbsolutePath().normalize());
                    return;
                }
            }
        }

        // 5. No matching builtin or executable found.
        System.out.println(command + ": not found");
    }
}
