package kg.apc.jmeter.charting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Stephane Hoblingre
 */
public class RowsCollector {

    private static HashMap<String, ArrayList<AbstractGraphRow>> models = null;
    private static RowsCollector instance = new RowsCollector();

    private RowsCollector()
    {
        models = new HashMap<String, ArrayList<AbstractGraphRow>>();
    }

    public static RowsCollector getInstance()
    {
        return instance;
    }

    public static void clear()
    {
        models.clear();
    }

    public synchronized void addRow(String vizualizerName, AbstractGraphRow row)
    {
        ArrayList<AbstractGraphRow> rows = models.get(vizualizerName);
        if(rows == null)
        {
            rows = new ArrayList<AbstractGraphRow>();
            models.put(vizualizerName, rows);
        }
        rows.add(row);
        System.out.println(vizualizerName + " has " + rows.size() + " row(s).");
    }

    public synchronized void clearRows(String vizualizerName)
    {
        models.remove(vizualizerName);
    }
}
