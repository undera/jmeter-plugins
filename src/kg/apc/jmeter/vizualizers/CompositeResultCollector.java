package kg.apc.jmeter.vizualizers;

import java.io.Serializable;
import org.apache.jmeter.reporters.ResultCollector;

public class CompositeResultCollector extends ResultCollector implements Serializable, Cloneable {
    private CompositeModel compositeModel;

    public void setCompositeModel(CompositeModel model)
    {
        this.compositeModel=model;
    }
    
    public CompositeModel getCompositeModel()
    {
         return compositeModel;
    }
}
