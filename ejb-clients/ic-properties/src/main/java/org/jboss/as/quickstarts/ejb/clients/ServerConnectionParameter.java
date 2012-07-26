package org.jboss.as.quickstarts.ejb.clients;

/**
 * Simple wrapper for necessary remote connection parameter of one JBoss server instance.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public final class ServerConnectionParameter {
    private final String host;
    private final int remotingPort;
    private final String username;
    private final String password;
    
    /**
     * 
     * @param host Name or IP address of the server instance
     * @param remotingPort Connection port for remoting, <code>null</code> default to 4447 
     * @param username Name of the user for security
     * @param password Password for the given user, mandatory if user is set
     */
    public ServerConnectionParameter(String host, Integer remotingPort, String username, String password) {
        if(username != null && password == null) {
            throw new IllegalArgumentException("Password is mandatory if user is given!");
        }
        this.host = host;
        this.remotingPort = remotingPort;
        this.username = username;
        this.password = username == null ? null : password;
    }

    public String getHost() {
        return host;
    }

    public int getRemotingPort() {
        return remotingPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
