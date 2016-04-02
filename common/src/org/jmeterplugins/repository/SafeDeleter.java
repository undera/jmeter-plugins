package org.jmeterplugins.repository;

import kg.apc.cmdtools.AbstractCMDTool;

import java.io.*;
import java.util.ListIterator;

public class SafeDeleter extends AbstractCMDTool {

    public SafeDeleter() {
    }

    @Override
    protected int processParams(ListIterator args) throws UnsupportedOperationException, IllegalArgumentException {
        while (args.hasNext()) {
            String nextArg = (String) args.next();
            if (nextArg.equalsIgnoreCase("--delete-list")) {
                if (!args.hasNext()) {
                    throw new IllegalArgumentException("Missing delete list file name");
                }

                String deleteList = (String) args.next();
                try {
                    deleteFiles(new File(deleteList));
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    return 1;
                }
            } else {
                throw new IllegalArgumentException("Unknown option: " + nextArg);
            }
        }

        return 0;
    }

    private void deleteFiles(File file) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            File f = new File(line);

            if (!f.exists()) {
                System.out.println("File not exists, won't try to delete: " + f.getAbsolutePath());
            }

            // TODO: make sure it's jar?
            System.out.println("Trying to delete " + f.getAbsolutePath());
            while (!f.delete()) {
                System.out.println("Did not delete " + f.getAbsolutePath());
                Thread.sleep(1000);
            }
        }
    }

    @Override
    protected void showHelp(PrintStream printStream) {
        // TODO
    }
}
