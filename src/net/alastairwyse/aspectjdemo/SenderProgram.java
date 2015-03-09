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

import java.net.InetAddress;

import net.alastairwyse.applicationlogging.*;

/**
 * Main entry point for the sender part of the aspectjdemo program.
 * @author Alastair Wyse
 */
public class SenderProgram {

    public static void main(String[] args) throws Exception {
        // If a logger is not passed to the constructor of a TcpRemoteSender object, the object will log to the console by default.
        //   To simplify this example, and reduce the information logged to the console, the below ConsoleApplicationLogger application logger is configured 
        //   to only log critical events, and passed to the TcpRemoteSender constructor.  To re-enable the logging, set the LogLevel to Debug.
        ConsoleApplicationLogger consoleLogger = new ConsoleApplicationLogger(LogLevel.Critical, '|', "  ");
        
        try(TcpRemoteSender tcpRemoteSender = new TcpRemoteSender(InetAddress.getLocalHost(), 10000, 20, 10000, 30000, 50, consoleLogger)) {
            tcpRemoteSender.Connect();
            tcpRemoteSender.Send("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit... (message 1)");
            tcpRemoteSender.Send("Lorem ipsum dolor sit amet, mel mazim viderer at, per quis saepe dissentias ad, case scaevola... (message 2)");
            tcpRemoteSender.Disconnect();
        }
    }
}