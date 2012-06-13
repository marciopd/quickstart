/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.ejb.asynchronous.client;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.ejb.ParallelAccess;
import org.jboss.as.quickstarts.ejb.asynchronous.AsynchronousAccess;

/**
 * A client to call the SingletonService via EJB remoting (AS7.1 / EAP6) to demonstrate the behaviour of asynchronous invocations.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class AsynchronousClient {
    private static final Logger LOGGER = Logger.getLogger(AsynchronousClient.class.getName());
    /**
     * Proxy of the SLSB with asynchronous methods
     */
    private final AsynchronousAccess accessBean;
    /**
     * Proxy of the SLSB that uses asynchronous bean calls inside to parallelize internal actions.
     */
    private final ParallelAccess parallelBean;

    /**
     * Constructor to prepare the client-context.<br/>
     * There must be a jboss-ejb-client.properties file in the classpath to specify the server connection(s).
     * 
     * @throws NamingException
     */
    private AsynchronousClient() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        String lookupName = "ejb:/jboss-as-ejb-asynchronous//AsynchronousAccessBean!" + AsynchronousAccess.class.getName();
        LOGGER.info("Lookup Bean >" + lookupName);
        accessBean = (AsynchronousAccess) context.lookup(lookupName);
        lookupName = "ejb:/jboss-as-ejb-asynchronous//ParallelAccessBean!" + ParallelAccess.class.getName();
        LOGGER.info("Lookup Bean >" + lookupName);
        parallelBean = (ParallelAccess) context.lookup(lookupName);
    }

    /**
     * Demonstrate a fire-and-forget call to an asynchronous bean.
     * 
     * @throws InterruptedException
     */
    private void fireAndForget() throws InterruptedException {
        long sleepMillis = 15000;
        accessBean.fireAndForget(sleepMillis);
        LOGGER.info("See server log around " + new Date(new Date().getTime() + sleepMillis) + " whether the call finished");
        // in AS7.1.1.Final there is a bug that an ERROR will be logged that the result can not be written
        // it will be solved in a later version
    }

    /**
     * Demonstrate how to call an asynchronous EJB and do some client stuff meanwhile. If the result is not present after the
     * timeout of get() the result will be ignored
     * 
     * @throws TimeoutException Will be thrown if you change the timing
     */
    private void getAResultAsync() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> myResult = accessBean.longerRunning(200); // Start a call with a short duration
        // simulate something
        Thread.sleep(400); // wait below 200ms will force a timeout during get
        // If you handle the TimeoutException you are able to ignore the result
        // WARNING: there will be an ERROR at server side that the result is not delivered
        LOGGER.info("Get the async result as expected => " + myResult.get(1, TimeUnit.MILLISECONDS));
    }

    /**
     * Demonstrate how to call an asynchronous EJB and do some client stuff meanwhile. The call will wait for the result.<br/>
     * Remember that the call of Future.get() will have a remote roundtrip to the server.
     */
    private void waitForAsyncResult() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> myResult = accessBean.longerRunning(1500); // Start a call with a short duration
        // you might do something here
        LOGGER.info("Get the async result as expected after wait => " + myResult.get());
    }

    /**
     * Invoke a synchronous EJB method that uses asynchronous calls internally to improve the duration.
     */
    private void callAnEJBwithAsyncAccess() {
        Collection<String> results = parallelBean.invokeAsyncParallel();
        LOGGER.info("Results of the parallel (server) processing : " + results);
    }

    /**
     * Demonstrate how a EJB can call different annotated asynchronous methods within the same application.
     */
    private void waitForAnotherAsyncResult2() throws InterruptedException, ExecutionException, TimeoutException {
        parallelBean.callInterfaceAnnotatedMethod();
    }

    /**
     * Demonstrate how to handle an Exception of an asynchronous call.
     */
    private void callAsyncWithFailure() throws InterruptedException {
        Future<String> x;
        try {
            x = accessBean.failure(); // this method will return successful, because the invocation will be successful!
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected failure during start asynchronous execution!", e);
        }
        try {
            x.get(); // this will never return successful
        } catch (ExecutionException e) {
            // TODO This is a bug in AS7.1.1.Final, in further versions the first cause is direct the IllegalAccessException
            // (JBoss AS 7.2.0.Alpha1-SNAPSHOT)
            if (e.getCause() instanceof IllegalAccessException) {
                LOGGER.info("Catch the expected Exception of the asynchronous execution!");
            } else {
                throw new RuntimeException("Unexpected ExecutionException during asynchronous call!", e);
            }
        }
    }

    /**
     * Invoke a synchronous EJB method that uses asynchronous calls internally to improve the duration.
     * @throws InterruptedException 
     * @throws ExecutionException 
     */
    private void checkAsyncCancel() throws InterruptedException, ExecutionException {
        Future<String> result = accessBean.cancelationAsync(20);
        Thread.sleep(200); // wait some time
        
        LOGGER.info("Request cancel");
        boolean cancel = result.cancel(true);
        if(!cancel) {
            LOGGER.info("Method can not be canceled!");
        }else{
            LOGGER.info("Cancel request successful");
            if(!result.isCancelled()) {
                LOGGER.warning("The result is not marked as canceled!");
            }
        }
        //result.get();
    }
    /**
     * Call all the different asynchronous methods.
     * 
     * @param args no arguments needed
     */
    public static void main(String[] args) throws Exception {
        AsynchronousClient client = new AsynchronousClient();
//
//        client.fireAndForget();
//        client.getAResultAsync();
//        client.waitForAsyncResult();
//
//        client.callAsyncWithFailure();
//
//        client.callAnEJBwithAsyncAccess();
//        client.waitForAnotherAsyncResult2();
        
        client.checkAsyncCancel();
    }

}
