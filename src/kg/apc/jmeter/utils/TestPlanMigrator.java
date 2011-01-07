package kg.apc.jmeter.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Tool to convert TestPlans if the plugin changed and cannot reload old versions.
 * @author Stephane Hoblingre
 */
public class TestPlanMigrator
{

    private boolean isPluginBlockProcessing = false;

    public TestPlanMigrator()
    {
    }

    private static void log(String message)
    {
        System.out.println(message);
    }

    private String getOutputFileName(String fileName)
    {
        StringBuilder builder = new StringBuilder(fileName);
        int index = fileName.lastIndexOf('.');
        builder.replace(index, index + 1, "-FIXED.");
        return builder.toString();
    }

    private boolean isPluginBlock(StringBuilder line)
    {
        return line.indexOf("ResultCollector guiclass=\"kg.apc.jmeter") >= 0;
    }

    private void replaceOnce(StringBuilder builder, String from, String to)
    {
        int index = builder.indexOf(from);
        if (index != -1)
        {
            builder.replace(index, index + from.length(), to);
        }
    }

    private void processLine030to040(StringBuilder builder)
    {
        if (isPluginBlock(builder))
        {
            isPluginBlockProcessing = true;
            replaceOnce(builder, "<ResultCollector", "<kg.apc.jmeter.vizualizers.RowsProviderResultCollector");
            replaceOnce(builder, "testclass=\"ResultCollector\"", "testclass=\"kg.apc.jmeter.vizualizers.RowsProviderResultCollector\"");
        } else
        {
            if (isPluginBlockProcessing)
            {
                if (builder.indexOf("</ResultCollector>") > 0)
                {
                    replaceOnce(builder, "</ResultCollector>", "</kg.apc.jmeter.vizualizers.RowsProviderResultCollector>");
                    isPluginBlockProcessing = false;
                }
            }
        }

        /*
        // TotalTransactionsPerSecondGui removal, replaced with TransactionsPerSecondGui
        if (builder.indexOf("kg.apc.jmeter.vizualizers.TotalTransactionsPerSecondGui") != -1)
        {
            replaceOnce(builder, "kg.apc.jmeter.vizualizers.TotalTransactionsPerSecondGui", "kg.apc.jmeter.vizualizers.TransactionsPerSecondGui");
        }

        // ServerPerfMonitoringGUI renamed to ServerPerfMonitoringGui
        if (builder.indexOf("kg.apc.jmeter.perfmon.ServerPerfMonitoringGUI") != -1)
        {
            replaceOnce(builder, "kg.apc.jmeter.perfmon.ServerPerfMonitoringGUI", "kg.apc.jmeter.perfmon.ServerPerfMonitoringGui");
        }
        */
    }

    public void processFile(String fileName) throws FileNotFoundException, IOException
    {
        isPluginBlockProcessing = false;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();

        if (!line.trim().startsWith("<?xml"))
        {
            log("ERROR: " + fileName + " is not an xml file!");
            return;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputFileName(fileName)));

        while (line != null)
        {
            StringBuilder builder = new StringBuilder(line);

            processLine030to040(builder);

            writer.write(builder.toString());
            writer.newLine();

            line = reader.readLine();
        }

        writer.flush();
        writer.close();

        log(fileName + " converted to " + getOutputFileName(fileName) + " successfuly.");
    }

    public void processDirectory(String dir)
    {
        File directory = new File(dir);
        if (!directory.isDirectory())
        {
            log("ERROR: " + dir + " is not a directory!");
            return;
        }
        File[] files = directory.listFiles(new JMXFileFilter());

        if (files.length == 0)
        {
            log("WARNING: " + dir + " does not contain jmx files!");
            return;
        }

        for (int i = 0; i < files.length; i++)
        {
            try
            {
                processFile(files[i].getAbsolutePath());
            } catch (FileNotFoundException ex)
            {
                log("ERROR while processing " + files[i].getAbsolutePath() + " : " + ex.getMessage());
            } catch (IOException ex)
            {
                log("ERROR while processing " + files[i].getAbsolutePath() + " : " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            if (args.length == 1)
            {
                if (!"-d".equalsIgnoreCase(args[0]))
                {
                    new TestPlanMigrator().processFile(args[0]);
                } else
                {
                    log("Please specify a Directory in the command line!");
                }

            } else if (args.length == 2)
            {
                if (!"-d".equalsIgnoreCase(args[0]))
                {
                    System.out.println("Only -d option is supported.");
                } else
                {
                    new TestPlanMigrator().processDirectory(args[1]);
                }
            } else
            {
                log("Please specify a TestPlan file or Directory in the command line!");
            }

        } catch (FileNotFoundException ex)
        {
            log("ERROR: " + ex.getMessage());
        } catch (IOException ex)
        {
            log("ERROR: " + ex.getMessage());
        }
    }

    private class JMXFileFilter implements FilenameFilter
    {

        @Override
        public boolean accept(File dir, String name)
        {
            return name.toLowerCase().endsWith(".jmx");
        }
    }
}
