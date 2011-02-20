// TODO: have "check file consistency" button
package kg.apc.jmeter.modifiers;

import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author undera
 */
public class RawRequestSourcePreProcessorGui extends AbstractPreProcessorGui{

    @Override
    public String getStaticLabel() {
        return "Raw Request Source PreProcessor";
    }

    public String getLabelResource() {
       return  getClass().getCanonicalName();
    }

    public TestElement createTestElement() {
        RawRequestSourcePreProcessor preproc = new RawRequestSourcePreProcessor();
        modifyTestElement(preproc);
        return preproc;
    }

    public void modifyTestElement(TestElement te) {
        configureTestElement(te);
    }

}
