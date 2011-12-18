package kg.apc.charting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListSet;
import org.apache.jorphan.gui.NumberRenderer;

/**
 *
 * @author Stéphane Hoblingre
 */
public class GraphModelToCsvExporter
{
    private AbstractMap<String, AbstractGraphRow> model = null;
    private File destFile = null;
    private String csvSeparator;
    private char decimalSeparator;
    private SimpleDateFormat dateFormatter = null;
    private String xAxisLabel;
    private NumberRenderer xAxisRenderer = null;
    private int hideNonRepValLimit = -1;

    public GraphModelToCsvExporter(
            AbstractMap<String, AbstractGraphRow> rows,
            File destFile,
            String csvSeparator,
            String xAxisLabel,
            NumberRenderer xAxisRenderer,
            int hideNonRepValLimit)
    {
        this.destFile = destFile;
        this.model = rows;
        this.csvSeparator = csvSeparator;
        this.decimalSeparator = new DecimalFormatSymbols().getDecimalSeparator();
        this.xAxisLabel = xAxisLabel;
        this.hideNonRepValLimit = hideNonRepValLimit;
        if(xAxisRenderer != null && xAxisRenderer instanceof DividerRenderer) {
           this.xAxisRenderer = new DividerRenderer(((DividerRenderer)xAxisRenderer).getFactor());
        } else if(xAxisRenderer != null && xAxisRenderer instanceof DateTimeRenderer) {
           dateFormatter = new SimpleDateFormat("HH:mm:ss" + decimalSeparator + "S");
        }
    }

    //used for Unit Tests only as of now
    public GraphModelToCsvExporter(
            AbstractMap<String, AbstractGraphRow> rows,
            File destFile,
            String csvSeparator,
            char decimalSeparator,
            NumberRenderer renderer,
            int hideNonRepValLimit)
    {
        this(rows, destFile, csvSeparator, "Elapsed time", renderer, hideNonRepValLimit);
        this.decimalSeparator = decimalSeparator;
        dateFormatter = new SimpleDateFormat("HH:mm:ss" + decimalSeparator + "S");
    }

    private String xValueFormatter(long xValue)
    {
        String ret;
        if(xAxisRenderer != null) {
            xAxisRenderer.setValue(xValue);
            ret = xAxisRenderer.getText();
        } else if (dateFormatter != null) {
            ret = dateFormatter.format(xValue);
        } else {
            ret = "" + xValue;
        }
        return ret;
    }

    public void writeCsvFile() throws IOException
    {
        //first, get all X values and rows names
        ConcurrentSkipListSet<Long> xValues = new ConcurrentSkipListSet<Long>();
        Iterator<Entry<String, AbstractGraphRow>> it = model.entrySet().iterator();
        ArrayList<String> rawsName = new ArrayList<String>();
        while(it.hasNext())
        {
            Entry<String, AbstractGraphRow> row = it.next();
            rawsName.add(row.getKey());
            Iterator<Entry<Long, AbstractGraphPanelChartElement>> itRow = row.getValue().iterator();
            while (itRow.hasNext())
            {
                Entry<Long, AbstractGraphPanelChartElement> element = itRow.next();
                    if(element.getValue().isPointRepresentative(hideNonRepValLimit)) {
                        xValues.add(element.getKey());
                    }
                }
            }

        //write file...
        //1st line
        BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
        writer.write(xAxisLabel);
   
        for(int i=0; i<rawsName.size(); i++)
        {
            writer.write(csvSeparator);
            writer.write(rawsName.get(i));
        }

        writer.newLine();
        writer.flush();

        //data lines

        Iterator<Long> itXValues = xValues.iterator();
        while(itXValues.hasNext())
        {
            long xValue = itXValues.next();
            writer.write(xValueFormatter(xValue));
         
            for(int i=0; i<rawsName.size(); i++)
            {
                writer.write(csvSeparator);
                AbstractGraphRow row = model.get(rawsName.get(i));
                AbstractGraphPanelChartElement value = row.getElement(xValue);
                if(value != null)
                {
                    writer.write("" + value.getValue());
                }
            }

            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
}
