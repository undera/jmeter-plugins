package kg.apc.jmeter.vizualizers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class for backward compatibility
 * @author Stephane Hoblingre
 */
public class TotalTransactionsPerSecondGui extends TransactionsPerSecondGui {
    
    private Collection<String> emptyCollection = new ArrayList<String>();

    public TotalTransactionsPerSecondGui()
    {
        isAggregate = true;
    }

    public Collection<String> getMenuCategories()
    {
        return emptyCollection;
    }
}
