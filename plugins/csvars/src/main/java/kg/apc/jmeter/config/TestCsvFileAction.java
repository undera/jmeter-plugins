package kg.apc.jmeter.config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class TestCsvFileAction implements ActionListener {

    private final VariablesFromCSVGui variablesCsvUi;

    public TestCsvFileAction(VariablesFromCSVGui variablesCsvUi) {
        this.variablesCsvUi = variablesCsvUi;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextArea infoArea = variablesCsvUi.getCheckInfoTextArea();
        infoArea.setText("");
        infoArea.setForeground(Color.black);

        VariablesFromCSV testElem = (VariablesFromCSV) variablesCsvUi.createTestElement();

        boolean noValues = true;
        StringBuilder msgVars = new StringBuilder();
        int count = 0;

        try {
            Map<String, String> vars = testElem.getArgumentsAsMap();
            for (Map.Entry<String, String> element : vars.entrySet()) {
                String var = element.getKey();
                String value = element.getValue();
                if (!"".equals(value)) {
                    noValues = false;
                }
                msgVars.append("${").append(var).append("} = ")
                        .append(value).append("\n");
                count++;
            }

            if (count == 0) {
                reportError("File parsed, but no variable found.");
            } else if (noValues) {
                reportOk("WARNING: File parsed, " + count + " variable" + (count > 1 ? "s" : "") + " found, but no variable have value!");
                reportOk(msgVars.toString());
            } else {
                reportOk("File successfuly parsed, " + count + " variable" + (count > 1 ? "s" : "") + " found:");
                reportOk(msgVars.toString());
            }
        } catch (Exception ex) {
            reportError("Error processing file: " + ex.toString());
        }
    }

    private void reportError(String msg) {
        JTextArea infoArea = variablesCsvUi.getCheckInfoTextArea();
        infoArea.setText(infoArea.getText() + "Problem detected: " + msg + "\n");
        infoArea.setForeground(Color.red);
    }

    private void reportOk(String string) {
        JTextArea infoArea = variablesCsvUi.getCheckInfoTextArea();
        infoArea.setText(infoArea.getText() + string + "\n");
    }
}
