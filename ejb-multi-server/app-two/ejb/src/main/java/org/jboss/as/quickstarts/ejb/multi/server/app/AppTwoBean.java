package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo;
import org.jboss.logging.Logger;

public @Stateless class AppTwoBean implements AppTwo {
    private static final Logger LOGGER = Logger.getLogger(AppTwoBean.class);
    
    @Resource SessionContext context;
    
    @Override
    public String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }

    @Override
    public String invoke(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("["+caller.getName()+"] "+text);
        return "app2["+caller.getName()+"]@"+getJBossNodeName();
    }
}
