package com.googlecode.jmeter.plugins.webdriver.sampler.gui;

import com.googlecode.jmeter.plugins.webdriver.sampler.WebDriverSampler;
import jsyntaxpane.DefaultSyntaxKit;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;

import javax.swing.*;
import java.awt.*;

public class WebDriverSamplerGui extends AbstractSamplerGui {

	private static final long serialVersionUID = -3484685528176139410L;

    private static final org.apache.log.Logger LOGGER = LoggingManager.getLoggerForClass();

    static {
        DefaultSyntaxKit.initKit();
    }

	JTextField parameters;

	JEditorPane script;

	public WebDriverSamplerGui() {
		createGui();
	}

    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Web Driver Sampler");
    }

    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        script.setText(element.getPropertyAsString(WebDriverSampler.SCRIPT));
        parameters.setText(element.getPropertyAsString(WebDriverSampler.PARAMETERS));
        super.configure(element);
    }

	@Override
	public TestElement createTestElement() {
        WebDriverSampler sampler = new WebDriverSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	@Override
	public void modifyTestElement(TestElement element) {
		element.clear();
		this.configureTestElement(element);
		element.setProperty(WebDriverSampler.SCRIPT, script.getText());
		element.setProperty(WebDriverSampler.PARAMETERS, parameters.getText());
	}

    @Override
    public void clearGui() {
        super.clearGui();

        parameters.setText(""); //$NON-NLS-1$
        script.setText(""); //$NON-NLS-1$
    }

	private void createGui() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());

		Box box = Box.createVerticalBox();
		box.add(makeTitlePanel());
		box.add(createParameterPanel());
		add(box, BorderLayout.NORTH);

		JPanel panel = createScriptPanel();
		add(panel, BorderLayout.CENTER);
		// Don't let the input field shrink too much
		add(Box.createVerticalStrut(panel.getPreferredSize().height),
				BorderLayout.WEST);
	}

	private JPanel createParameterPanel() {
		final JLabel label = new JLabel("Parameters (-> String Parameters and String[] args)");

		parameters = new JTextField(10);
		parameters.setName(WebDriverSampler.PARAMETERS);
		label.setLabelFor(parameters);

		final JPanel parameterPanel = new JPanel(new BorderLayout(5, 0));
		parameterPanel.add(label, BorderLayout.WEST);
		parameterPanel.add(parameters, BorderLayout.CENTER);
		return parameterPanel;
	}

	private JPanel createScriptPanel() {
		script = new JEditorPane();
        final JScrollPane scrollPane = new JScrollPane(script);
        script.setContentType("text/javascript");
        script.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        final JLabel label = new JLabel("Script (see below for variables that are defined)"); // $NON-NLS-1$
		label.setLabelFor(script);

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

		final JTextArea explain = new JTextArea("The following variables are defined for the script\\:\\nLabel, Parameters, args, log, Browser, SampleResult, OUT");
		explain.setLineWrap(true);
		explain.setEditable(false);
		explain.setBackground(this.getBackground());
		panel.add(explain, BorderLayout.SOUTH);

		return panel;
	}
}
