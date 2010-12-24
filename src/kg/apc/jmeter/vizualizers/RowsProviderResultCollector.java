package kg.apc.jmeter.vizualizers;

import java.io.Serializable;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.reporters.ResultCollector;



public class RowsProviderResultCollector extends ResultCollector implements Serializable {
    private ConcurrentSkipListMap<String, AbstractGraphRow> model;

    void setModel(ConcurrentSkipListMap<String, AbstractGraphRow> aModel)
    {
        model=aModel;
    }

    NavigableSet<String> getRowNames()
    {
         return model.keySet();
    }

}
