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

import net.alastairwyse.methodinvocationremoting.*;

/**
 * Main entry point for the receiver part of the aspectjdemo program.
 * @author Alastair Wyse
 */
public class ReceiverProgram {

    public static void main(String[] args) throws Exception {
        try(TcpRemoteReceiver tcpRemoteReceiver = new TcpRemoteReceiver(10000, 20, 10000, 50, 1024)) {
            tcpRemoteReceiver.Connect();
            tcpRemoteReceiver.Disconnect();
            tcpRemoteReceiver.Connect();
            tcpRemoteReceiver.Receive();
            tcpRemoteReceiver.Receive();
            tcpRemoteReceiver.Disconnect();
        }
    }

}
