package org.jmeterplugins.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.ListIterator;

public class SafeDeleter {

    public static void main(String[] argsRaw) throws Throwable {
        ListIterator<String> args = Arrays.asList(argsRaw).listIterator();

        while (args.hasNext()) {
            String nextArg = args.next();
            if (nextArg.equalsIgnoreCase("--delete-list")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing delete list file name");
                }

                String deleteList = args.next();
                File dDel = new File(deleteList);
                deleteFiles(dDel);
                dDel.deleteOnExit();
            } else if (nextArg.equalsIgnoreCase("--copy-list")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing delete list file name");
                }

                String copyList = args.next();
                File fCopy = new File(copyList);
                copyFiles(fCopy);
                fCopy.deleteOnExit();
            } else {
                throw new IllegalArgumentException("Unknown option: " + nextArg);
            }
        }
    }

    private static void copyFiles(File file) throws IOException, InterruptedException {
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

    private static void deleteFiles(File file) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            File f = new File(line);

            if (!f.exists()) {
                System.err.println("File not exists, won't try to delete: " + f.getAbsolutePath());
                continue;
            }

            // TODO: make sure it's jar?
            System.out.println("Trying to delete " + f.getAbsolutePath());
            int cnt = 1;
            while (!f.delete() && cnt++ < 60) {
                System.err.println("Did not delete #" + cnt + " " + f.getAbsolutePath());
                Thread.sleep(1000);
            }
        }
        System.out.println("Done deleting attempts");
    }
}
