package kg.apc.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import kg.apc.jmeter.JMeterPluginsUtils;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author apc
 */
public class HTTPRawSamplerGui
     extends AbstractSamplerGui
{
    private static final Logger log = LoggingManager.getLoggerForClass();
   //private JCheckBox isSuccessful;
   private JTextField hostName;
   private JTextField port;
   //private JTextField responseTime;
   private JTextArea requestData;

   /**
    *
    */
   public HTTPRawSamplerGui()
   {
      log.debug("Creating");
      init();
      initFields();
   }

   @Override
   public String getStaticLabel()
   {
      return JMeterPluginsUtils.prefixLabel("HTTP Raw Request");
   }

   @Override
   public void configure(TestElement element)
   {
      super.configure(element);

      hostName.setText(element.getPropertyAsString(HTTPRawSampler.HOSTNAME));
      port.setText(element.getPropertyAsString(HTTPRawSampler.PORT));
      requestData.setText(element.getPropertyAsString(HTTPRawSampler.BODY));
   }

   public TestElement createTestElement()
   {
      HTTPRawSampler sampler = new HTTPRawSampler();
      modifyTestElement(sampler);
      return sampler;
   }

   /**
    * Modifies a given TestElement to mirror the data in the gui components.
    *
    * @param sampler
    * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
    */
   public void modifyTestElement(TestElement sampler)
   {
      super.configureTestElement(sampler);

      if (sampler instanceof HTTPRawSampler)
      {
         HTTPRawSampler rawSampler = (HTTPRawSampler) sampler;
         rawSampler.setHostName(hostName.getText());
         rawSampler.setPort(port.getText());
         // first replace removes old \r\n
         // second eliminates orphan \r
         // third make all newlines - old and new  like \r\n
         String data=requestData.getText().replace("\r\n", "\n").replace("\r", "").replace("\n", "\r\n");
         rawSampler.setRawRequest(data);
      }
   }

   @Override
   public void clearGui()
   {
      super.clearGui();

      initFields();
   }

   public String getLabelResource()
   {
      return this.getClass().getSimpleName();
   }

   private void init()
   {
      setLayout(new BorderLayout(0, 5));
      setBorder(makeBorder());

      add(makeTitlePanel(), BorderLayout.NORTH);

      JPanel mainPanel = new JPanel(new GridBagLayout());

      GridBagConstraints labelConstraints = new GridBagConstraints();
      labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

      GridBagConstraints editConstraints = new GridBagConstraints();
      editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
      editConstraints.weightx = 1.0;
      editConstraints.fill = GridBagConstraints.HORIZONTAL;

      addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Hostname: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 1, hostName = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("TCP Port: ", JLabel.RIGHT));
      addToPanel(mainPanel, editConstraints, 1, 2, port = new JTextField());
      addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Request Data: ", JLabel.RIGHT));

      editConstraints.fill = GridBagConstraints.BOTH;
      addToPanel(mainPanel, editConstraints, 1, 4, requestData = new JTextArea());
      requestData.setRows(10);
      requestData.setBorder(new BevelBorder(BevelBorder.LOWERED));

      JPanel container = new JPanel(new BorderLayout());
      container.add(mainPanel, BorderLayout.NORTH);
      add(container, BorderLayout.CENTER);
   }

   private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component)
   {
      constraints.gridx = col;
      constraints.gridy = row;
      panel.add(component, constraints);
   }

    private void initFields() {
      hostName.setText("localhost");
      port.setText("80");
      requestData.setText("GET / HTTP/1.0\r\n"
            + "Host: localhost\r\n"
            + "Connection: close\r\n"
            + "\r\n");
    }
}
