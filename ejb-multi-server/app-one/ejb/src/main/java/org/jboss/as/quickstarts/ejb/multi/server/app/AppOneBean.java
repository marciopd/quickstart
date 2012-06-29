package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import org.jboss.as.quickstarts.ejb.multi.server.app.AppOne;
import org.jboss.logging.Logger;

public @Stateless class AppOneBean implements AppOne {
    private static final Logger LOGGER = Logger.getLogger(AppOneBean.class);
    
    @Resource SessionContext context;
    
    @Override
    public String invoke(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("["+caller.getName()+"] "+text);
        return "app1["+caller.getName()+"]@"+System.getProperty("jboss.node.name");
    }
}
