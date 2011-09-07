package kg.apc.jmeter.vizualizers;

import java.util.ArrayList;
import java.util.Collection;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.testelement.TestElement;

/**
 * Class for backward compatibility
 * @author Stephane Hoblingre
 */
@Deprecated
public class TotalTransactionsPerSecondGui extends TransactionsPerSecondGui {


    public TotalTransactionsPerSecondGui() {
        isAggregate = true;
    }

    //do not insert this vizualiser in any JMeter menu
    private Collection<String> emptyCollection = new ArrayList<String>();
    @Override
    public Collection<String> getMenuCategories() {
        return emptyCollection;
    }

    @Override
    public TestElement createTestElement() {
        TestElement res = super.createTestElement();
        res.setComment(JMeterPluginsUtils.getWikiLinkText("TotalTransactionsPerSecond"));
        return res;
    }
}
