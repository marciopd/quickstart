package org.jboss.as.quickstarts.ejb.multi.server.app;

import javax.ejb.Remote;

@Remote
public interface AppOne {

    String invoke(String text);

}
