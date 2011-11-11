package kg.apc.jmeter.perfmon.client;

/**
 *
 * @author undera
 */
public interface AbstractTransport {

    public void disconnect();

    public void writeln(String string);

    public String readln();
}
