package kg.apc.jmeter.threads;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kg.apc.jmeter.JMeterPluginsUtils;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.threads.AbstractThreadGroup;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.gui.AbstractThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;


public class PoissonThreadGroupGui extends AbstractThreadGroupGui  {
	
	private class FieldChangesListener implements DocumentListener {

        private final JTextField tf;

        public FieldChangesListener(JTextField field) {
            tf = field;
        }

        private void update() {
            refreshLabels();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (tf.hasFocus()) {
                update();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (tf.hasFocus()) {
                update();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (tf.hasFocus()) {
                update();
            }
        }
    }
	
    public static final String WIKIPAGE = "PoissonThreadGroup";
    private static final Logger log = LoggingManager.getLoggerForClass();
    
    private static final String THREAD_NAME = "Thread Field";
    private static final String RAMP_NAME = "Ramp Up Field";
    private static final String LAMBDA_NAME = "lambda Field";
    private static final String RANDOM_SEEED = "Random Field";    

    private JTextField rampInput;
    private JTextField lambdaInput;
    private JTextField randSeedInput;
    private LoopControlPanel loopPanel;
    private JPanel threadPanel;
    private JLabel threadInput;
    VerticalPanel intgrationPanel;
    VerticalPanel threadPropsPanel;
    
    public PoissonThreadGroupGui() {
        super();
        init();
        initGui();
    }

    private JPanel createControllerPanel() {
        loopPanel = new LoopControlPanel(false);
        LoopController looper = (LoopController) loopPanel.createTestElement();
        looper.setLoops(-1);
        looper.setContinueForever(true);
        loopPanel.configure(looper);
        return loopPanel;
    }
    
    void init() {
    	//log.info("Inside the init Function");
        JMeterPluginsUtils.addHelpLinkToPanel(this, WIKIPAGE);
    	
        // THREAD PROPERTIES
        threadPropsPanel = new VerticalPanel();
        threadPropsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("thread_properties"))); // $NON-NLS-1$

        // NUMBER OF THREADS
        threadPanel = new JPanel(new BorderLayout(5, 0));
        JLabel threadLabel = new JLabel(JMeterUtils.getResString("number_of_threads")); // $NON-NLS-1$
        threadPanel.add(threadLabel, BorderLayout.WEST);
        threadInput = new JLabel("");
        threadInput.setName(THREAD_NAME);
        threadLabel.setLabelFor(threadInput);
        threadPanel.add(threadInput, BorderLayout.CENTER);
        threadPropsPanel.add(threadPanel);

        // RAMP-UP
        JPanel rampPanel = new JPanel(new BorderLayout(5, 0));
        JLabel rampLabel = new JLabel(JMeterUtils.getResString("ramp_up")); // $NON-NLS-1$
        rampPanel.add(rampLabel, BorderLayout.WEST);
        rampInput = new JTextField(5);
        rampInput.setName(RAMP_NAME);
        rampLabel.setLabelFor(rampInput);
        rampPanel.add(rampInput, BorderLayout.CENTER);
        rampInput.getDocument().addDocumentListener(new FieldChangesListener(rampInput));
        threadPropsPanel.add(rampPanel);
        
        //Lambda value
        JPanel lambdaPanel = new JPanel(new BorderLayout(5, 0));
        JLabel lambdaLabel = new JLabel("Lambda");
        lambdaPanel.add(lambdaLabel, BorderLayout.WEST);
        lambdaInput = new JTextField(5);
        lambdaInput.setName(LAMBDA_NAME);
        lambdaLabel.setLabelFor(lambdaInput);
        lambdaPanel.add(lambdaInput, BorderLayout.CENTER);
        lambdaInput.getDocument().addDocumentListener(new FieldChangesListener(lambdaInput));
        threadPropsPanel.add(lambdaPanel);
        
        //Random value
        JPanel randPanel = new JPanel(new BorderLayout(5, 0));
        JLabel randLabel = new JLabel("Random Seed");
        randPanel.add(randLabel, BorderLayout.WEST);
        randSeedInput = new JTextField(5);
        randSeedInput.setName(RANDOM_SEEED);
        randLabel.setLabelFor(randSeedInput);
        randPanel.add(randSeedInput, BorderLayout.CENTER);
        randSeedInput.getDocument().addDocumentListener(new FieldChangesListener(randSeedInput));
        threadPropsPanel.add(randPanel);

        intgrationPanel= new VerticalPanel();
        intgrationPanel.add(threadPropsPanel);
        add(intgrationPanel, BorderLayout.CENTER);
        
        // this magic LoopPanel provides functionality for thread loops
        createControllerPanel();
    }
    
    private void initGui(){
        threadInput.setText("0"); // $NON-NLS-1$
        rampInput.setText("1"); // $NON-NLS-1$
        lambdaInput.setText("0");
        randSeedInput.setText("1");
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initGui();
    }
    
    @Override
    public String getStaticLabel() {
        return JMeterPluginsUtils.prefixLabel("Poisson Thread Group");
    }
    
	@Override
	public String getLabelResource() {
        return this.getClass().getSimpleName();
	}
	
    @Override
    public TestElement createTestElement() {
        PoissonThreadGroup tg = new PoissonThreadGroup();
        modifyTestElement(tg);
        tg.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
        return tg;
    }
    
    @Override
    public void configure(TestElement te) {
        super.configure(te);
        PoissonThreadGroup tg = (PoissonThreadGroup) te;
        
        rampInput.setText(tg.getRampUp());
        lambdaInput.setText(tg.getLambda());
        randSeedInput.setText(tg.getRandomSeed());
        
        refreshLabels();

        TestElement controller = (TestElement) tg.getProperty(AbstractThreadGroup.MAIN_CONTROLLER).getObjectValue();
        if (controller != null) {
            loopPanel.configure(controller);
        }
    }
    
	@Override
	public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        refreshLabels();
        if (te instanceof PoissonThreadGroup) {
        	PoissonThreadGroup tg = (PoissonThreadGroup) te;
            tg.setRampUp(rampInput.getText());
            tg.setLambda(lambdaInput.getText());
            tg.setRandomSeed(randSeedInput.getText());
            tg.setProperty(PoissonThreadGroup.NUM_THREADS, threadInput.getText());
            tg.setSamplerController((LoopController) loopPanel.createTestElement());
        }
	}
	
	private void refreshLabels()
	{
		try{
	        PoissonThreadGroup tg = new PoissonThreadGroup();
	        int NumofThreads = ScheduleThreads(tg);
	        threadInput.setText(String.valueOf(NumofThreads));
	        threadPanel.repaint();
		}
		catch ( Exception ex){
			log.error("refreshLabels: " + ex.getMessage());
		}
	}

	private int ScheduleThreads(PoissonThreadGroup tg)
	{	
		int numThreads = 0;
		try{
	        tg.setRampUp(rampInput.getText());
	        tg.setLambda(lambdaInput.getText());
	        tg.setRandomSeed(randSeedInput.getText());
	        
	        if (!lambdaInput.getText().isEmpty() && !(rampInput.getText().isEmpty()))
			{
		        final HashTree hashTree = new HashTree();
		        hashTree.add(new LoopController());
		        JMeterThread thread = new JMeterThread(hashTree, null, null);
		        ArrayList<Long> lambdaSchedule = tg.getLambdaThreadSchedules();
		        numThreads = lambdaSchedule.size();
				for (int n = 0; n < numThreads; n++) {
			    	thread.setThreadNum(n);
			        thread.setThreadName(Integer.toString(n));
			        tg.scheduleThread(thread, lambdaSchedule.get(n));
			    }
			}
	        return numThreads;
		}
		catch ( Exception ex){
			log.error("ScheduleThreads: " + ex.getMessage());
		}
		return numThreads;
	}
}
