package kg.apc.jmeter.charting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Stéphane Hoblingre
 */
public class GraphModelToCsvExporter
{

    private GraphPanelChart caller = null;
    private AbstractMap<String, AbstractGraphRow> model = null;
    private File destFile = null;
    private String csvSeparator;
    private char decimalSeparator;

    public GraphModelToCsvExporter(GraphPanelChart caller,
            AbstractMap<String, AbstractGraphRow> rows,
            File destFile,
            String csvSeparator)
    {
        this.caller = caller;
        this.destFile = destFile;
        this.model = rows;
        this.csvSeparator = csvSeparator;
        this.decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator();
    }

    private boolean isDateValue(long value)
    {
        return value > 1000000000000L;
    }

    public void writeCsvFile() throws IOException
    {
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss" + decimalSeparator + "S");

        BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));

        Iterator<Entry<String, AbstractGraphRow>> it = model.entrySet().iterator();

        writer.write("Serie" + csvSeparator + "X Axis" + csvSeparator + "Value");
        writer.newLine();
        writer.flush();

        while (it.hasNext())
        {
            Entry<String, AbstractGraphRow> row = it.next();
            String rowName = row.getKey();
            Iterator<Entry<Long, AbstractGraphPanelChartElement>> itRow = row.getValue().iterator();
            while (itRow.hasNext())
            {
                Entry<Long, AbstractGraphPanelChartElement> element = itRow.next();
                long xValue = element.getKey();
                if (isDateValue(xValue))
                {
                    writer.write(rowName + csvSeparator + formatter.format(xValue) + csvSeparator + element.getValue().getValue());
                } else
                {
                    writer.write(rowName + csvSeparator + xValue + csvSeparator + element.getValue().getValue());
                }

                writer.newLine();
                writer.flush();
            }
        }
        writer.close();
    }
}
