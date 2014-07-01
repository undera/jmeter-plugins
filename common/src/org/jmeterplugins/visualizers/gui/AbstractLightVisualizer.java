/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.jmeterplugins.visualizers.gui;

import java.awt.Container;

import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.visualizers.Visualizer;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;

/**
 * 
 * @author Félix Henry
 * @author Vincent Daburon
 */

/**
 * 
 * This class provides the same behavior as AbstractVisualizer without FilePanel
 */
public abstract class AbstractLightVisualizer extends AbstractListenerGui
		implements Visualizer, UnsharedComponent {
	private static final long serialVersionUID = 240L;

	protected ResultCollector collector = new ResultCollector();

	protected boolean isStats = false;

	public AbstractLightVisualizer() {
		super();
	}

	@Override
	public boolean isStats() {
		return isStats;
	}

	protected ResultCollector getModel() {
		return collector;
	}

	/* Implements JMeterGUIComponent.createTestElement() */
	@Override
	public TestElement createTestElement() {
		if (collector == null) {
			collector = new ResultCollector();
		}
		modifyTestElement(collector);
		return (TestElement) collector.clone();
	}

	/* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
	@Override
	public void modifyTestElement(TestElement c) {
		configureTestElement((AbstractListenerElement) c);
	}

	/* Overrides AbstractJMeterGuiComponent.configure(TestElement) */
	@Override
	public void configure(TestElement el) {
		super.configure(el);
		ResultCollector rc = (ResultCollector) el;
		if (collector == null) {
			collector = new ResultCollector();
		}
		collector.setSaveConfig((SampleSaveConfiguration) rc.getSaveConfig()
				.clone());
	}

	protected void configureTestElement(AbstractListenerElement mc) {
		super.configureTestElement(mc);
		mc.setListener(this);
	}

	@Override
	protected Container makeTitlePanel() {
		Container panel = super.makeTitlePanel();
		return panel;
	}

	protected void setModel(ResultCollector collector) {
		this.collector = collector;
	}

	@Override
	public void clearGui() {
		super.clearGui();
	}
}