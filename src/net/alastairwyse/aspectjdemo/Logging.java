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

import net.alastairwyse.applicationlogging.*;

/**
 * Aspect which provides an example of logging for class TcpRemoteSender
 * @author Alastair Wyse
 */
privileged aspect Logging {

    ConsoleApplicationLogger logger;

    public Logging() {
        logger = new ConsoleApplicationLogger(LogLevel.Debug, '|', "  ");
    }

    /**
     * Defines a join point for when a message is sent.
     */
    private pointcut SendMessage(String message):
        call (void TcpRemoteSender.Send(String))
        && args(message);

    /**
     * Defines a join point for when the sender disconnects.
     */
    private pointcut Disconnected():
        call (void TcpRemoteSender.Disconnect());

    /**
     * Defines a join point for when the sender connects.
     */
    private pointcut Connected(TcpRemoteSender sender):
        call (void TcpRemoteSender.AttemptConnect())
        && target(sender);
        
    /**
     * Defines a join point for when the sender attempts to reconnect.
     */
    private pointcut Reconnect():
        call (void TcpRemoteSender.AttemptConnect()) && withincode (void TcpRemoteSender.HandleExceptionAndResend(Exception, String));

    /**
     * Implements logging after sending a message.
     */
    after(String message) returning: SendMessage(message) {
        logger.Log(thisJoinPoint.getTarget(), LogLevel.Information, "Message sent and acknowledged.");
        logger.Log(thisJoinPoint.getTarget(), LogLevel.Debug, "Sent message '" + message.toString() + "'.");
        }    

    /**
     * Implements logging after the sender disconnects.
     */
    after() returning: Disconnected() {
        logger.Log(thisJoinPoint.getTarget(), LogLevel.Information, "Disconnected.");
        }

    /**
     * Implements logging after the sender connects.
     */
    after(TcpRemoteSender sender) returning: Connected(sender) {
        logger.Log(thisJoinPoint.getTarget(), LogLevel.Information, "Connected to " + sender.ipAddress.toString() + ":" + sender.port + ".");
        }
        
    /**
     * Implements logging before the sender attempts to reconnect.
     */
    before(): Reconnect() {
        logger.Log(thisJoinPoint.getTarget(), LogLevel.Warning, "Attempting to reconnect to TCP socket.");
        }
}