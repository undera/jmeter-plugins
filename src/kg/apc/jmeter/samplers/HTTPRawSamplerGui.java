package kg.apc.jmeter.samplers;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;


/**
 *
 * @author undera
 */
public class HTTPRawSamplerGui extends AbstractSamplerGui
{

    @Override
    public TestElement createTestElement() {
        HTTPSamplerBase sampler = new HTTPRawSampler();
        modifyTestElement(sampler);
        return sampler;
    }
    
    public void modifyTestElement(TestElement sampler) {
        sampler.clear();
        //urlConfigGui.modifyTestElement(sampler);
        final HTTPRawSampler samplerBase = (HTTPRawSampler) sampler;
        //samplerBase.setMonitor(isMon.isSelected());
        //samplerBase.setMD5(useMD5.isSelected());
        //samplerBase.setEmbeddedUrlRE(embeddedRE.getText());
        this.configureTestElement(sampler);
    }

    @Override
    public void configure(TestElement element) {
        //super.configure(element);
        final HTTPRawSampler samplerBase = (HTTPRawSampler) element;
        //urlConfigGui.configure(element);
        //getImages.setSelected(samplerBase.isImageParser());
        //isMon.setSelected(samplerBase.isMonitor());
        //useMD5.setSelected(samplerBase.useMD5());
        //embeddedRE.setText(samplerBase.getEmbeddedUrlRE());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return "HTTP Raw Request";
    }
}
