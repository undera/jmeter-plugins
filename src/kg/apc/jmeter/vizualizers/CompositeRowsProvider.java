
package kg.apc.jmeter.vizualizers;

import java.util.Collection;
import kg.apc.jmeter.charting.AbstractGraphRow;


public interface CompositeRowsProvider {
    public Collection<String> getRowNames();
    public AbstractGraphRow getRowByName(String name);
}
