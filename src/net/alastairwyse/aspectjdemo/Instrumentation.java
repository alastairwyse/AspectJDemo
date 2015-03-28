/*
 * Copyright 2015 Alastair Wyse (http://www.oraclepermissiongenerator.net/aspectjdemo/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.alastairwyse.aspectjdemo;

import java.lang.Thread.UncaughtExceptionHandler;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;

import net.alastairwyse.applicationmetrics.*;
import net.alastairwyse.methodinvocationremotingmetrics.*;

/**
 * Aspect which provides an example of instrumentation/metric logging for class TcpRemoteSender
 * @author Alastair Wyse
 */
aspect Instrumentation {
    
    private final String configurationFileName = "ApectJDemoConfiguration.xml";
    
    ConfigurationSettings configurationSettings;
    FileMetricLogger metricLogger;
    
    public Instrumentation() throws Exception {
        configurationSettings = ReadConfigurationSettings();
        metricLogger = new FileMetricLogger('|', configurationSettings.MetricLogFilePath, configurationSettings.MetricEventWriteFrequency, true, new MetricLoggerExceptionHandler());
        metricLogger.Start();
    }
    
    /**
     * Defines a join point for when a message is sent.
     */
    private pointcut SendMessage():
        call (void TcpRemoteSender.Send(String));
    
    /**
     * Defines a join point for when a TCP reconnect occurs.
     */
    private pointcut Reconnect():
        call (void TcpRemoteSender.AttemptConnect()) && withincode (void TcpRemoteSender.HandleExceptionAndResend(Exception, String));
    
    /**
     * Defines a join point for when the close() method is called.
     */
    private pointcut Close():
        call (void TcpRemoteSender.close());
    
    /**
     * Implements metric logging before sending a message.
     */
    before(): SendMessage() {
        metricLogger.Begin(new MessageSendTime());
        }
    
    /**
     * Implements metric logging after sending a message.
     */
    after() returning: SendMessage() {
        metricLogger.End(new MessageSendTime());
        metricLogger.Increment(new MessageSent());
        }

    /**
     * Implements metric logging after a reconnect occurs.
     */
    after() returning: Reconnect() {
        metricLogger.Increment(new TcpRemoteSenderReconnected());
        }
    
    /**
     * Cleans up aspect resources after the target object is closed
     */
    after() : Close() {
        try {
            metricLogger.Stop();
            metricLogger.close();
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
        }
    
    /**
     * Reads settings from the configuration file.
     * @return            The configuration settings.
     * @throws Exception  if an error occurred attempting to read the configuration file.
     */
    private ConfigurationSettings ReadConfigurationSettings() throws Exception {
        final String metricLogFilePathXmlPath = "/AspectJDemoConfiguration/MetricLogFilePath";
        final String metricEventWriteFrequencyXmlPath = "/AspectJDemoConfiguration/MetricEventWriteFrequency";
        
        // Setup xml parsing objects
        File configurationFile = new File(configurationFileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document configDocument;
        try {
            configDocument = documentBuilder.parse(configurationFile);
        }
        catch(IOException e) {
            throw new Exception("Failed to open configuration file '" + configurationFileName + "'.", e);
        }
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        // Read the metric log file path
        String metricLogFilePath;
        XPathExpression xPathExpression = xpath.compile(metricLogFilePathXmlPath);
        metricLogFilePath = (String)xPathExpression.evaluate(configDocument, XPathConstants.STRING);
        if(metricLogFilePath.isEmpty() == true) {
            throw new Exception("Failed to read metric log file path from configuration file path '" + metricLogFilePathXmlPath +"'.");
        }
        
        // Read the metric event write frequency
        String metricEventWriteFrequencyString; 
        int metricEventWriteFrequency;
        xPathExpression = xpath.compile(metricEventWriteFrequencyXmlPath);
        metricEventWriteFrequencyString  = (String)xPathExpression.evaluate(configDocument, XPathConstants.STRING);
        try {
            metricEventWriteFrequency = Integer.parseInt(metricEventWriteFrequencyString);
        }
        catch(Exception e) {
            throw new Exception("Failed to read metric event write frequency from configuration file path '" + metricEventWriteFrequencyXmlPath +"'.", e);
        }
        
        return new ConfigurationSettings(metricLogFilePath, 3000);
    }
    
    /**
     * Handles when an exception occurs in the metric logger class.
     */
    private class MetricLoggerExceptionHandler implements UncaughtExceptionHandler {
        
        @Override
        public void uncaughtException(Thread arg0, Throwable arg1) {
            arg1.printStackTrace(System.out);
        }
    }
}