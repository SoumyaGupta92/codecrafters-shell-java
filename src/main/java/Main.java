import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static Set<String> builtinCommands = new HashSet<>(Set.of("exit", "echo", "type", "pwd", "cd"));

    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String input = scan.nextLine();

            List<String> commands = List.of(input.split("\\s+"));

            switch (commands.get(0)) {
                case "type":
                    handleTypeCommand(commands.get(1));
                    break;
                case "exit":
                    System.exit(0);
                    break;
                case "echo":
                    System.out.println(input.substring(5).trim());
                    break;
                case "pwd":
                    System.out.println(System.getProperty("user.dir"));
                    break;
                case "cd":
                    handleCdCommand(commands);
                    break;
                default:
                    executeExternalFile(commands);
                    break;
            }
        }
    }

    // responsible for handling the "type" command
    private static void handleTypeCommand(String command) {
        if (builtinCommands.contains(command)) {
            System.out.println(command + " is a shell builtin");
            return;
        } else {
            File executable = findExecutable(command);

            if (executable != null) {
                System.out.println(
                        command + " is " + executable.getAbsolutePath());
            } else {
                System.out.println(command + ": not found");
            }
        }
    }

    // Returns the File representing the executable, or null if not found
    private static File findExecutable(String command) {

        String path = System.getenv("PATH");

        if (path == null) {
            return null;
        }

        String[] directories = path.split(File.pathSeparator, -1);

        for (String directory : directories) {

            if (directory.isEmpty()) {
                directory = ".";
            }

            File file = new File(directory, command);
            if (file.isFile() && file.canExecute()) {
                return file.getAbsoluteFile();
            }
        }

        return null;
    }

    // Executes the File if present
    private static void executeExternalFile(List<String> commands) {

        String command = commands.get(0);
        if (command.isEmpty()) {
            System.exit(0);
        }

        File executableFile = findExecutable(command);

        if (executableFile == null) {
            System.out.println(command + ": not found");
            return;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(commands);

            builder.inheritIO();
            Process process = builder.start();
            process.waitFor();

        } catch (IOException e) {

            System.out.println(command + ": execution failed: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void handleCdCommand(List<String> commands) {
        // if (commands.size() < 2) {
        // System.out.println("cd: missing argument");
        // return;
        // }

        // String directory = commands.get(1);
        // if (directory.equals("~")) {
        // directory = System.getProperty("user.home");
        // }

        // File dir = new File(directory);
        // if (!dir.exists()) {
        // System.out.println("cd: " + directory + ": No such file or directory");
        // return;
        // }

        // if (!dir.isDirectory()) {
        // System.out.println("cd: " + directory + ": Not a directory");
        // return;
        // }

        // System.setProperty("user.dir", dir.getAbsolutePath());
        if (commands.size() > 1) {
            String target = commands.get(1);

            if (target.equals("~")) {
                target = System.getProperty("user.home");
            }

            File directory = new File(target);

            if (!directory.isAbsolute()) {
                directory = new File(
                        System.getProperty("user.dir"), target);
            }

            if (directory.isDirectory()) {
                System.setProperty(
                        "user.dir",
                        directory.getAbsolutePath());
            } else {
                System.out.println(
                        "cd: " + target + ": No such file or directory");
            }
        }
    }

}