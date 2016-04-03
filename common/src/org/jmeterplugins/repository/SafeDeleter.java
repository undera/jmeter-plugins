package org.jmeterplugins.repository;

import java.io.*;
import java.util.Arrays;
import java.util.ListIterator;

public class SafeDeleter {

    public static void main(String[] argsRaw) throws Throwable {
        ListIterator<String> args = Arrays.asList(argsRaw).listIterator();

        while (args.hasNext()) {
            String nextArg = (String) args.next();
            if (nextArg.equalsIgnoreCase("--delete-list")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing delete list file name");
                }

                String deleteList = (String) args.next();
                deleteFiles(new File(deleteList));
            } else {
                throw new IllegalArgumentException("Unknown option: " + nextArg);
            }
        }
    }

    private static void deleteFiles(File file) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            File f = new File(line);

            if (!f.exists()) {
                System.out.println("File not exists, won't try to delete: " + f.getAbsolutePath());
                continue;
            }

            // TODO: make sure it's jar?
            System.out.println("Trying to delete " + f.getAbsolutePath());
            int cnt = 1;
            while (!f.delete() && cnt++ < 60) {
                System.out.println("Did not delete #" + cnt + " " + f.getAbsolutePath());
                Thread.sleep(1000);
            }
        }
        System.out.println("Done deleting attempts");
        Thread.sleep(180000);
    }
}
