package kg.apc.jmeter.charting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    public void addRow(String vizualizerName, AbstractGraphRow row)
    {
        ArrayList<AbstractGraphRow> rows = models.get(vizualizerName);
        if(rows == null)
        {
            rows = new ArrayList<AbstractGraphRow>();
            models.put(vizualizerName, rows);
        }
        rows.add(row);
    }

    public void clearRows(String vizualizerName)
    {
        models.remove(vizualizerName);
    }

    public HashMap<String, ArrayList<AbstractGraphRow>> getMap()
    {
        return models;
    }

    public Iterator<String> getThreadSafeVizualizerNamesIterator()
    {
        Object clone = ((HashMap<String, ArrayList<AbstractGraphRow>>)models).clone();
        if(clone != null)
        {         
            return ((HashMap<String, ArrayList<AbstractGraphRow>>)clone).keySet().iterator();
        } else
        {
            return new HashMap<String, ArrayList<AbstractGraphRow>>().keySet().iterator();
        }
    }

    public Iterator<AbstractGraphRow> getThreadSafeRowsIterator(String vizualizerNames)
    {
        Object clone = ((ArrayList<AbstractGraphRow>)models.get(vizualizerNames)).clone();
        if(clone != null)
        {
            return ((ArrayList<AbstractGraphRow>)clone).iterator();
        } else
        {
            return new ArrayList<AbstractGraphRow>().iterator();
        }
    }
}
