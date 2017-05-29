package com.blazemeter.jmeter.control;

import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;

public class WeightedSwitchController extends GenericController implements Serializable {
    private static final Logger log = LoggingManager.getLoggerForClass();
    public static final String WEIGHTS = "Weights";
    private boolean chosen = false;
    protected long[] counts = null;
    protected long totalCount = 0;
    protected transient int currentCopy;

    public void setData(PowerTableModel model) {
        CollectionProperty prop = JMeterPluginsUtils.tableModelRowsToCollectionProperty(model, WEIGHTS);
        // log.warn("Set prop from model: " + prop);
        setProperty(prop);
    }

    public CollectionProperty getData() {
        JMeterProperty prop = getProperty(WEIGHTS);
        // log.info("Weights prop: " + prop);
        if (prop instanceof CollectionProperty) {
            return setEnabledSubGroups((CollectionProperty) prop);
        } else {
            log.warn("Returning empty collection");
            return new CollectionProperty();
        }
    }

    @Override
    public Sampler next() {
        if (chosen) {
            Sampler result = super.next();

            if (result == null || currentCopy != current ||
                    (super.getSubControllers().get(current) instanceof TransactionController)) {
                reset();
                for (TestElement element : super.getSubControllers()) {
                    if (element instanceof Controller) {
                        ((Controller) element).triggerEndOfLoop();
                    }
                }
                return null;
            }
            return result;
        } else {
            chosen = true;
            choose();
            return super.next();
        }
    }

    private void choose() {
        CollectionProperty data = removeDisableSubGroups(getData());

        if (counts == null) {
            log.debug("Creating array: " + data.size());
            counts = new long[data.size()];
        }

        double[] weights = getWeights(data);

        double maxDiff = Double.MIN_VALUE;
        int maxDiffIndex = Integer.MIN_VALUE;
        for (int n = 0; n < weights.length; n++) {
            double factWeight = totalCount > 0 ? ((double) counts[n] / totalCount) : 0;
            double diff = weights[n] - factWeight;
            if (diff > maxDiff) {
                maxDiff = diff;
                maxDiffIndex = n;
            }
        }

        if (maxDiffIndex == Integer.MIN_VALUE) {
            for (int n = 0; n < weights.length; n++) {
                double diff = weights[n];
                if (diff > maxDiff) {
                    maxDiff = diff;
                    maxDiffIndex = n;
                }
            }
        }

        totalCount++;
        counts[maxDiffIndex]++;
        current = currentCopy = maxDiffIndex;
    }

    private CollectionProperty removeDisableSubGroups(CollectionProperty data) {
        CollectionProperty result = new CollectionProperty();
        for (JMeterProperty property : data) {
            if (property instanceof CollectionProperty &&
                    ((CollectionProperty) property).size() == 3 &&
                    "true".equals(((CollectionProperty) property).get(2).getStringValue())) {
                result.addProperty(property);
            }
        }
        return result;
    }

    private CollectionProperty setEnabledSubGroups(CollectionProperty data) {
        for (JMeterProperty property : data) {
            if (property instanceof CollectionProperty) {

                CollectionProperty prop = (CollectionProperty) property;

                if (subControllersAndSamplers.size() > 0) {
                    boolean isFindSubChild = false;
                    for (TestElement child : subControllersAndSamplers) {
                        if (child.getName().equals(prop.get(0).getStringValue())) {
                            if (prop.size() == 2) {
                                prop.addItem(Boolean.toString(child.isEnabled()));
                            } else {
                                prop.set(2, Boolean.toString(child.isEnabled()));
                            }
                            isFindSubChild = true;
                            break;
                        }
                    }

                    if (!isFindSubChild) {
                        // means, that this element disable in test tree
                        if (prop.size() == 2) {
                            prop.addItem("false");
                        } else {
                            prop.set(2, "false");
                        }
                    }
                }

                if (prop.size() == 2) {
                    prop.addItem("true");
                }
            }
        }

        setProperty(data);
        return data;
    }

    private double[] getWeights(CollectionProperty data) {
        long sum = 0;
        double[] weights = new double[data.size()];
        for (int n = 0; n < data.size(); n++) {
            CollectionProperty row = (CollectionProperty) data.get(n);
            weights[n] = Long.parseLong(row.get(1).getStringValue());
            sum += weights[n];
        }

        for (int n = 0; n < weights.length; n++) {
            weights[n] /= sum;
        }
        //log.info("Weights: " + Arrays.toString(weights));
        return weights;
    }


    public void reset() {
        this.chosen = false;
        // reset child WSC
        for (TestElement controller : this.getSubControllers()) {
            if (controller instanceof WeightedSwitchController) {
                ((WeightedSwitchController) controller).reset();
            }
        }
    }
}
