package org.jmeterplugins.repository;

import kg.apc.cmdtools.AbstractCMDTool;

import java.io.PrintStream;
import java.util.ListIterator;

public class PluginManagerCMD extends AbstractCMDTool {

    @Override
    protected int processParams(ListIterator listIterator) throws UnsupportedOperationException, IllegalArgumentException {
        throw new RuntimeException();
        //return 0;
    }

    @Override
    protected void showHelp(PrintStream printStream) {
        printStream.println("Options for tool 'PluginManagerCMD': <command> <paramstr> "
                + " where <command> is one of: status, install, uninstall, upgrade-all");
    }
}
