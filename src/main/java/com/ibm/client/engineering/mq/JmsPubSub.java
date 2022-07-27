package com.ibm.client.engineering.mq;
/*
* (c) Copyright IBM Corporation 2022
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * A minimal and simple application for Point-to-point messaging.
 *
 * Application makes use of fixed literals, any customizations will require
 * re-compilation of this source file. Application assumes that the named queue
 * is empty prior to a run.
 *
 * Notes:
 *
 * API type: JMS API (v2.0, simplified domain)
 *
 * Messaging domain: Point-to-point
 *
 * Provider type: IBM MQ
 *
 * Connection mode: Client connection
 *
 * JNDI in use: No
 *
 */
public class JmsPubSub {

    // Create variables for the connection to MQ
    static final String HOST = "mymq.containers.appdomain.cloud";
    static final int PORT = 443; // Listener port for your queue manager
    private static final String CHANNEL = "DEMO.SVRCONN"; // Channel name
    private static final String QMGR = "DEMO"; // Queue manager name
    static final String QUEUE_NAME = "TESTQUEUE"; // Queue that the application uses to put and get messages to and from
    static final String TOPIC_NAME = "CJT";
    static String PUBLICATION_NAME = "JmsPub"; //
    static String SUBSCRIPTION_NAME = "JmsSub";

    public static void startPub() {
        // Variables
        JMSContext context = null;
        Destination destination = null;
        JMSProducer producer = null;

        System.setProperty("javax.net.ssl.trustStore", "/Users/cjiang/Desktop/WorkSpace/cp4d/mq2keystore.jks");
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");/**/

        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
            cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JUST_A_NAME");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, false);
            cf.setStringProperty(WMQConstants.CLIENT_ID, PUBLICATION_NAME);
            // cf.setStringProperty(WMQConstants.USERID, APP_USER);
            // cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
            cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12ORHIGHER");

            // Create JMS objects
            context = cf.createContext();
            destination = context.createTopic("topic://" + TOPIC_NAME);

            long uniqueNumber = System.currentTimeMillis() % 1000;
            TextMessage message = context.createTextMessage("Your lucky number today is " + uniqueNumber);

            producer = context.createProducer();
            producer.send(destination, message);
            System.out.println("Sent pub message:\n" + message);

            System.out.println("PUB: SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PUB: FAILURE");
        }finally {
            if (context != null) {
                context.close();
            }
        }

    }

    public static void startSub() {
        // Variables
        JMSContext context = null;
        Destination destination = null;
        JMSConsumer consumer = null;

        System.setProperty("javax.net.ssl.trustStore", "/Users/cjiang/Desktop/WorkSpace/cp4d/mq2keystore.jks");
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");/**/

        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
            cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JUST_A_NAME");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, false);
            cf.setStringProperty(WMQConstants.CLIENT_ID, SUBSCRIPTION_NAME);
            // cf.setStringProperty(WMQConstants.USERID, APP_USER);
            // cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
            cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12ORHIGHER");

            // Create JMS objects
            context = cf.createContext();
            destination = context.createTopic("topic://" + TOPIC_NAME);

            consumer = context.createConsumer(destination); // autoclosable

            while (true) {
                try {
                    System.out.println("subscriber ready");
                    Message receivedMessage = consumer.receive();
                    System.out.println("\nReceived sub message:\n" + receivedMessage);
                } catch (JMSRuntimeException jmsex) {

                    jmsex.printStackTrace();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SUB: FAILURE");
        } finally {
            if (context != null) {
                context.close();
            }
            if (consumer != null) {
                consumer.close();
            }
        }

    }

}
