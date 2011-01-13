package kg.apc.jmeter.charting;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.TestListener;

/**
 *
 * @author Stephane Hoblingre
 */
public class RowsCollector implements TestListener {

    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>> models = null;
    private static RowsCollector instance = new RowsCollector();
    private Iterator emptyIterator = null;

    private RowsCollector()
    {
        models = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, AbstractGraphRow>>();
        emptyIterator = new ConcurrentSkipListMap<String, AbstractGraphRow>().values().iterator();
        StandardJMeterEngine.register(this);
    }

    public static RowsCollector getInstance()
    {
        return instance;
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

    public Iterator<String> getVizualizerNamesIterator()
    {
        return models.keySet().iterator();
    }

    public Iterator<AbstractGraphRow> getRowsIterator(String vizualizerNames)
    {
        ConcurrentSkipListMap<String, AbstractGraphRow> rows = models.get(vizualizerNames);
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
        return models.get(testName).get(rowName);
    }

    @Override
    public void testStarted()
    {
        clear();
        //must re-register
        StandardJMeterEngine.register(this);
    }

    @Override
    public void testStarted(String string)
    {
        clear();
        //must re-register
        StandardJMeterEngine.register(this);
    }

    @Override
    public void testEnded()
    {
    }

    @Override
    public void testEnded(String string)
    {
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie)
    {
    }
}
