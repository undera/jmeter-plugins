package com.blazemeter.jmeter.xmpp.actions;

import com.blazemeter.jmeter.xmpp.JMeterXMPPSampler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.filetransfer.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class SendFileXEP0096 extends AbstractXMPPAction implements FileTransferListener, ConnectionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public static final java.lang.String FILE_RECIPIENT = "file_recipient";
    public static final String FILE_PATH = "file_path";
    private static final long WAITING_CYCLES = 10;
    private JTextField recipient;
    private JTextField path;
    private FileTransferManager mgr;

    @Override
    public String getLabel() {
        return "Send File (XEP-0096)";
    }

    @Override
    public SampleResult perform(JMeterXMPPSampler sampler, SampleResult res) throws Exception {
        String recipient = sampler.getPropertyAsString(FILE_RECIPIENT);
        String filePath = sampler.getPropertyAsString(FILE_PATH);

        res.setSamplerData("Recipient: " + recipient + "\r\nFile: " + filePath + "\r\n");

        OutgoingFileTransfer transfer = mgr.createOutgoingFileTransfer(recipient);

        transfer.sendFile(new File(filePath), filePath);
        waitForTransfer(transfer, sampler.getXMPPConnection().getPacketReplyTimeout());
        res.setResponseData(("Bytes sent: " + transfer.getBytesSent()).getBytes());
        return res;
    }

    private void waitForTransfer(FileTransfer transfer, long timeout) throws SmackException, InterruptedException {
        double prevProgress = 0;
        long counter = 0;
        Thread.sleep(timeout / WAITING_CYCLES / 10); // optimistic
        while (!transfer.isDone()) {
            if (transfer.getStatus().equals(FileTransfer.Status.error)) {
                throw new SmackException(transfer.getError().toString(), transfer.getException());
            } else {
                log.debug("Status: " + transfer.getStatus() + " " + transfer.getProgress());
            }
            if (transfer.getProgress() <= prevProgress) {
                if (counter >= WAITING_CYCLES) {
                    throw new SmackException("File transfer timed out");
                }
                counter++;
                Thread.sleep(timeout / WAITING_CYCLES);
            } else {
                counter = 0;
                prevProgress = transfer.getProgress();
            }
        }

        if (transfer.getProgress() == 0) {
            throw new SmackException("No data transferred");
        }
    }

    @Override
    public void addUI(JComponent panel, GridBagConstraints labelConstraints, GridBagConstraints editConstraints) {
        addToPanel(panel, labelConstraints, 0, 0, new JLabel("Recipient: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 0, recipient = new JTextField(20));

        addToPanel(panel, labelConstraints, 0, 1, new JLabel("File to send: ", JLabel.RIGHT));
        addToPanel(panel, editConstraints, 1, 1, path = new JTextField(20));

        addToPanel(panel, labelConstraints, 1, 2, new JLabel("Note: you must use full JID as recipient, like user@server/resource)", JLabel.LEFT));
    }

    @Override
    public void clearGui() {
        recipient.setText("");
        path.setText("");
    }

    @Override
    public void setSamplerProperties(JMeterXMPPSampler sampler) {
        sampler.setProperty(FILE_RECIPIENT, recipient.getText());
        sampler.setProperty(FILE_PATH, path.getText());
    }

    @Override
    public void setGuiFieldsFromSampler(JMeterXMPPSampler sampler) {
        recipient.setText(sampler.getPropertyAsString(FILE_RECIPIENT));
        path.setText(sampler.getPropertyAsString(FILE_PATH));
    }

    @Override
    public void fileTransferRequest(FileTransferRequest request) {
        final IncomingFileTransfer transfer = request.accept();

        Thread transferThread = new Thread(new Runnable() {
            public void run() {
                try {
                    OutputStream os = new NullOutputStream();
                    InputStream is = transfer.recieveFile();
                    log.debug("Reading from stream: " + is.available());
                    IOUtils.copy(is, os);
                    log.debug("Left in stream: " + is.available());
                } catch (Exception e) {
                    log.error("Failed incoming file transfer", e);
                }
            }
        });
        transferThread.start();
    }

    @Override
    public void connected(XMPPConnection connection) {
        this.mgr = new FileTransferManager(connection);
        mgr.addFileTransferListener(this);
    }

    @Override
    public void authenticated(XMPPConnection connection) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
