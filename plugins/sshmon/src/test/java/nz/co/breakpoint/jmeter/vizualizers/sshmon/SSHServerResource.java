package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.io.File;
import java.io.IOException;
import org.apache.sshd.server.auth.password.AcceptAllPasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.AcceptAllPublickeyAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.apache.sshd.server.SshServer;

/**
 * SSH server for testing purposes. Starts its own thread.
 */
public class SSHServerResource implements Runnable {

    private SshServer sshd = null;
    private int port;
    private boolean stop = false;

    public SSHServerResource(int port) {
        this.port = port;
    }
    
    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        if (sshd != null) return;
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(getPort());         
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("src/test/resources/hostkey.key")));
        sshd.setPasswordAuthenticator(AcceptAllPasswordAuthenticator.INSTANCE);
        sshd.setShellFactory(InteractiveProcessShellFactory.INSTANCE);
        sshd.setPublickeyAuthenticator(AcceptAllPublickeyAuthenticator.INSTANCE);
        sshd.setTcpipForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        try {
            System.out.println("sshd.start()");
            sshd.start();
            while(!stop) {
                Thread.sleep(100);
            }
            System.out.println("sshd.stop()");
            sshd.stop();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    protected void start() {
        new Thread(this).start();
    }
    
    // Stop SSHD thread and block until stopped (FIXME sometimes the socket is still in use)
    public void stop() {
        stop = true;
        try {
            while (sshd != null && !sshd.isClosed()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
