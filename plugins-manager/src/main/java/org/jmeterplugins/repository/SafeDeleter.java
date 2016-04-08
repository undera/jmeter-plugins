package org.jmeterplugins.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class SafeDeleter {

    public static void main(String[] argsRaw) throws Throwable {
        ListIterator<String> args = Arrays.asList(argsRaw).listIterator();

        while (args.hasNext()) {
            String nextArg = args.next();
            if (nextArg.equalsIgnoreCase("--move-list")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing delete list file name");
                }

                File fCopy = new File(args.next());
                fCopy.deleteOnExit();
                moveFiles(fCopy);
            } else if (nextArg.equalsIgnoreCase("--restart-command")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing restart command file");
                }

                File file = new File(args.next());
                file.deleteOnExit();
                restartFromFile(file);
            } else {
                throw new IllegalArgumentException("Unknown option: " + nextArg);
            }
        }
    }

    private static void restartFromFile(File file) throws IOException {
        final ArrayList<String> command = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            command.add(line);
        }

        final ProcessBuilder builder = new ProcessBuilder(command);
        System.out.print("Starting: " + command);
        File cleanerLog = File.createTempFile("jpgc-restarter-", ".log");
        builder.redirectError(cleanerLog);
        builder.redirectOutput(cleanerLog);
        builder.start();
    }

    private static void moveFiles(File file) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length != 2) {
                System.err.println("Invalid line: " + line);
                continue;
            }

            System.out.println("Moving " + parts[0] + " to " + parts[1]);
            try {
                Files.move(new File(parts[0]).toPath(), new File(parts[1]).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
        System.out.println("Done moving files");
    }
}
