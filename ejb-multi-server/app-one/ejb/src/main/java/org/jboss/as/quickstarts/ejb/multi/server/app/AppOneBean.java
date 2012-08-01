package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;


public @Stateless class AppOneBean implements AppOne {
    private static final Logger LOGGER = Logger.getLogger(AppOneBean.class);
    
    @Resource SessionContext context;
    
    @Override
    public String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }
    @Override
    public String invoke(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("["+caller.getName()+"] "+text);
        return "app1["+caller.getName()+"]@"+getJBossNodeName();
    }
    
    @Override
    @RolesAllowed({"AppOne","Intern"})
    public String invokeSecured(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("Secured invocation ["+caller.getName()+"] "+text);
        LOGGER.info("Is in Role AppOne="+context.isCallerInRole("AppOne")+" Intern="+context.isCallerInRole("Intern"));
        return "app1["+caller.getName()+"]@"+getJBossNodeName();
    }
}
