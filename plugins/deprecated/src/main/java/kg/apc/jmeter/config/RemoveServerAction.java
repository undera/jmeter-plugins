package kg.apc.jmeter.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveServerAction implements ActionListener {
    private final JMeterServerPanel owner;

    public RemoveServerAction(JMeterServerPanel jMeterServerPanel) {
        owner=jMeterServerPanel;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        owner.remove();
    }
}
