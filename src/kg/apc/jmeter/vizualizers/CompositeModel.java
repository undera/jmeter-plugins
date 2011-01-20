package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;

/**
 *
 * @author Stephane Hoblingre
 */
public class CompositeModel {

    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>> models = null;
    private Iterator emptyIterator = null;

    public CompositeModel()
    {
        models = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>>();
        emptyIterator = new ConcurrentSkipListMap<String, AbstractGraphRow>().values().iterator();
    }

    public void clear()
    {
        models.clear();
    }

    private synchronized ConcurrentSkipListMap<String, AbstractGraphRow> getRowsMap(String vizualizerName)
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> rows = models.get(vizualizerName);
        if(rows == null)
        {
            rows = new ConcurrentSkipListMap<String, AbstractGraphRow>();
            models.put(vizualizerName, rows);
        }
        return rows;
    }

    public void addRow(String vizualizerName, AbstractGraphRow row)
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> rows = models.get(vizualizerName);
        if(rows == null)
        {
            rows = getRowsMap(vizualizerName);
        }
        rows.put(row.getLabel(), row);
    }

    public void clearRows(String vizualizerName)
    {
        models.remove(vizualizerName);
    }

    public boolean containsVisualizer(String vizualizerName)
    {
        return models.containsKey(vizualizerName);
    }

    public Iterator<String> getVizualizerNamesIterator()
    {
        return models.keySet().iterator();
    }

    public Iterator<AbstractGraphRow> getRowsIterator(String vizualizerName)
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> rows = models.get(vizualizerName);
        if(rows != null)
        {
            return rows.values().iterator();
        } else
        {
            return emptyIterator;
        }
    }

    public AbstractGraphRow getRow(String testName, String rowName)
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> rows = models.get(testName);
        if(rows != null)
        {
            return models.get(testName).get(rowName);
        } else
        {
            return null;
        }
    }
}
