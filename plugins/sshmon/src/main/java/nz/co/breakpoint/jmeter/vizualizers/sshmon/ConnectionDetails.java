package nz.co.breakpoint.jmeter.vizualizers.sshmon;

import java.util.Objects;

/**
 * This class represents most details of an SSH connection.
 * It is used for 2 purposes:
 * - Create a connection based on these details,
 * - Identify a connection based on a subset of these details (host, port, username)
 *   in the context of connection pooling.
 */
public class ConnectionDetails {

	protected final String host;
	protected final int port;
	protected final String username;
	protected final String password;
	protected final byte[] privateKey;

	public ConnectionDetails(String host) {
		this("", host);
	}

	public ConnectionDetails(String host, int port) {
		this("", host, port);
	}

	public ConnectionDetails(String username, String host) {
		this(username, host, 22);
	}

	public ConnectionDetails(String username, String host, int port) {
		this(username, host, port, "");
	}

	public ConnectionDetails(String username, String host, String password) {
		this(username, host, 22, password);
	}

	public ConnectionDetails(String username, String host, int port, String password) {
		this(username, host, port, password, null);
	}

	public ConnectionDetails(String username, String host, int port, String password, byte[] privateKey) {
        this.username = username;
		this.host = host;
        this.port = port;
        this.password = password;
        this.privateKey = privateKey;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

    public byte[] getPrivateKey() {
        return privateKey;
    }
    
	public String getPassword() {
		return password;
	}
    
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof ConnectionDetails)) {
            return false;
        }
        ConnectionDetails o = (ConnectionDetails)other;
        // Exclude password from comparison:
        return username.equalsIgnoreCase(o.getUsername()) 
            && host.equalsIgnoreCase(o.getHost()) 
            && (port == o.getPort());
    }
    
	@Override
	public String toString() {
		return (username.isEmpty()? "" : username+"@")+host+":"+port;
	}
    
    @Override
    public int hashCode() {
        // Exclude password from code:
        return Objects.hash(username.toLowerCase(), host.toLowerCase(), port);
    }
}
