package kg.apc.jmeter.vizualizers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.charting.AbstractGraphRow;
import kg.apc.jmeter.graphs.CompositeNotifierInterface;

/**
 *
 * @author Stephane Hoblingre
 */
public class CompositeModel {

    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>> models = null;
    private Iterator<AbstractGraphRow> emptyIterator = null;
    private CompositeNotifierInterface notifier = null;

    public CompositeModel()
    {
        models = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>>();
        emptyIterator = new ConcurrentSkipListMap<String, AbstractGraphRow>().values().iterator();
    }

    //needed to refresh tree if row model changed
    public void setNotifier(CompositeNotifierInterface notifier)
    {
        this.notifier = notifier;
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
        notifier.refresh();
    }

    public void clearRows(String vizualizerName)
    {
        models.remove(vizualizerName);
        notifier.refresh();
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
