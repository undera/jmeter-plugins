/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kg.apc.jmeter.vizualizers;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;
import kg.apc.jmeter.charting.AbstractGraphRow;
import org.apache.jmeter.reporters.ResultCollector;

/**
 *
 * @author undera
 */
class RowsProviderResultCollector extends ResultCollector {
    private ConcurrentSkipListMap<String, AbstractGraphRow> rowsModel;

    public RowsProviderResultCollector(ConcurrentSkipListMap<String, AbstractGraphRow> model) {
        rowsModel=model;
    }

    public Collection<String> getRowNames()
    {
     return rowsModel.keySet();
    }
    
    public AbstractGraphRow getRowByName(String name)
    {
        return rowsModel.get(name);
    }
}
