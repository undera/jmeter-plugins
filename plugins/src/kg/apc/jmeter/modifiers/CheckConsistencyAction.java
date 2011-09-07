package kg.apc.jmeter.modifiers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.jmeter.RuntimeEOFException;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class CheckConsistencyAction implements ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();
    private final JTextField filename;
    private final JTextArea infoArea;

    public CheckConsistencyAction(JTextField field, JTextArea checkInfo) {
        filename = field;
        infoArea = checkInfo;
    }

    public void actionPerformed(ActionEvent e) {
        infoArea.setText("");
        infoArea.setForeground(Color.black);
        final String fname = filename.getText();

        File f = new File(fname);
        if (!f.exists()) {
            reportError("File '" + fname + "' was not found");
            return;
        }

        int cnt = 0;
        try {
            RawRequestSourcePreProcessor preproc = new RawRequestSourcePreProcessor();
            preproc.setFileName(fname);
            preproc.setVarName("test");
            preproc.setRewindOnEOF(false);
            while (true) {
                preproc.process();
                cnt++;
            }
        } catch (RuntimeEOFException ex) {
            reportOk("File seems to be OK.");
            infoArea.setForeground(Color.decode("0x00009900"));
        } catch (RuntimeException ex) {
            log.debug("Runtime Exception", ex);
            reportError(ex.toString()+" "+JMeterPluginsUtils.getStackTrace(ex));
        }
        reportOk("Parsed " + cnt + " requests in " + fname);
    }

    private void reportError(String msg) {
        infoArea.setText(infoArea.getText() + "File inconsistency: " + msg+"\n");
        infoArea.setForeground(Color.red);
    }

    private void reportOk(String string) {
        infoArea.setText(infoArea.getText() + string+ "\n" );
    }
}
