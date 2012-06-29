package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

public @Stateless
class MainAppBean implements MainApp {
    private static final Logger LOGGER = Logger.getLogger(MainAppBean.class);
    @Resource SessionContext context;
    private InitialContext iCtx;

    @PostConstruct
    public void init() {
        try {
            final Hashtable<String, String> p = new Hashtable<String, String>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            // create the InitialContext
            this.iCtx = new InitialContext(p);
        } catch (NamingException e) {
            throw new RuntimeException("Could not initialize context", e);
        }
    }

    @Override
    public String invokeAll(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("["+caller.getName()+"] "+text);
        final StringBuilder result = new StringBuilder("MainApp["+caller.getName()+"]@" + System.getProperty("jboss.node.name"));
        result.append("  >  [ "+ invokeAppOne(text));
        result.append(" ; "+invokeAppTwo(text));
        result.append(" ]");
        
        return result.toString();
    }

    private String invokeAppOne(String text) {
        try {
            final AppOne bean = (AppOne) iCtx.lookup("ejb:appone/ejb//AppOneBean!" + AppOne.class.getName());

            // invoke on the bean
            final String appOneResult = bean.invoke(text);

            LOGGER.info("AppOne return : " + appOneResult);
            return appOneResult;
        } catch (NamingException e) {
            throw new RuntimeException("Could not invoke appOne", e);
        }
    }


    private String invokeAppTwo(String text) {
        try {
            final AppTwo bean = (AppTwo) iCtx.lookup("ejb:apptwo/ejb//AppTwoBean!" + AppTwo.class.getName());

            // invoke on the bean
            final String appTwoResult = bean.invoke(text);

            LOGGER.info("AppTwo return : " + appTwoResult);
            return appTwoResult;
        } catch (NamingException e) {
            throw new RuntimeException("Could not invoke appTwo", e);
        }
    }
}
