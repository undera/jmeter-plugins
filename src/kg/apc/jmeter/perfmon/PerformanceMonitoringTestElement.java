package kg.apc.jmeter.perfmon;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.util.JMeterUtils;

/**
 *
 * @author Stephane Hoblingre
 */
public class PerformanceMonitoringTestElement extends AbstractTestElement implements Serializable, Clearable, TestListener
{

    public static final String DATA_PROPERTY = "perfomdata";
    public static final String MONITORING_TYPE = "monitoringType";
    private MetricsProvider agentMetricsGetter = null;
    public AbstractPerformanceMonitoringGui gui = null;

    public PerformanceMonitoringTestElement()
    {
        super();
    }

    public static CollectionProperty tableModelToCollectionProperty(PowerTableModel model)
    {
        CollectionProperty rows = new CollectionProperty(PerformanceMonitoringTestElement.DATA_PROPERTY, new ArrayList<Object>());
        for (int col = 0; col < model.getColumnCount(); col++)
        {
            rows.addItem(model.getColumnData(model.getColumnName(col)));
        }
        return rows;
    }

    public JMeterProperty getData()
    {
        //log.info("getData");
        JMeterProperty prop = getProperty(DATA_PROPERTY);
        return prop;
    }

    public void setData(CollectionProperty rows)
    {
        //log.info("setData");
        setProperty(rows);
    }

    public int getType()
    {
        return super.getPropertyAsInt(MONITORING_TYPE, 0);
    }

    public void setType(int type)
    {
        super.setProperty(MONITORING_TYPE, type);
    }

    @Override
    public void clearData()
    {
    }

    public void register(AbstractPerformanceMonitoringGui gui)
    {
        this.gui = gui;
    }

    @Override
    public Object clone()
    {
        Object clone = super.clone();
        ((PerformanceMonitoringTestElement) clone).register(gui);
        return clone;
    }

    private AgentConnector[] getConnectors()
    {
        CollectionProperty servers = (CollectionProperty) getData();
        PropertyIterator iter = servers.iterator();

        CollectionProperty hosts = (CollectionProperty) iter.next();
        CollectionProperty ports = (CollectionProperty) iter.next();

        if (hosts.size() > 0)
        {
            PropertyIterator iterHosts = hosts.iterator();
            PropertyIterator iterPorts = ports.iterator();

            AgentConnector[] connectors = new AgentConnector[hosts.size()];
            int i = 0;

            while (iterHosts.hasNext() && iterPorts.hasNext())
            {
                StringProperty host = (StringProperty) iterHosts.next();
                StringProperty port = (StringProperty) iterPorts.next();

                connectors[i++] = new AgentConnector(host.getStringValue(), port.getIntValue());
            }
            return connectors;
        } else
        {
            return null;
        }
    }

    @Override
    public void testStarted()
    {
        AgentConnector[] connectors = getConnectors();
        if (connectors != null)
        {
            if (gui != null)
            {
                gui.setChartType(getType());
                gui.clearErrorMessage();
                agentMetricsGetter = new MetricsProvider(getType(), gui, connectors);
                agentMetricsGetter.testStarted();
            } else
            {
                agentMetricsGetter = new MetricsProvider(getType(), connectors);
                agentMetricsGetter.testStarted();
            }
        }
    }

    //enable perfmon in distributed test
    //-Jjppmfile must be setted for the slave who will collect the metrics
    @Override
    public void testStarted(String string)
    {
        if(JMeterUtils.getProperty("jppmfile") != null)
        {
            AgentConnector[] connectors = getConnectors();
            if (connectors != null)
            {
                agentMetricsGetter = new MetricsProvider(getType(), connectors);
                agentMetricsGetter.testStarted();
            }
        }
    }

    @Override
    public void testEnded()
    {
        if (agentMetricsGetter != null)
        {
            agentMetricsGetter.testEnded();
        }
    }

    //enable perfmon in distributed test
    //-Jjppmfile must be setted for the slave who will collect the metrics
    @Override
    public void testEnded(String string)
    {
        if(JMeterUtils.getProperty("jppmfile") != null)
        {
            testEnded();
        }
    }

    @Override
    public void testIterationStart(LoopIterationEvent lie)
    {
        //do nothing during test
    }
}
