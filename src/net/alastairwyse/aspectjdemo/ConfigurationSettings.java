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

/**
 * Container class holding configuration settings for the aspectjdemo program.
 * @author Alastair Wyse
 */
public class ConfigurationSettings {
    
    /**
     * Initialises a new instance of the ConfigurationSettings class.
     * @param metricLogFilePath          The full path to the configuration file for the application.
     * @param metricEventWriteFrequency  The frequency in which metric events should be written to the metric log file in milliseonds.
     */
    public ConfigurationSettings(String metricLogFilePath, int metricEventWriteFrequency) {
        MetricLogFilePath = metricLogFilePath;
        MetricEventWriteFrequency = metricEventWriteFrequency;
    }
    
    /**
     * The full path to the configuration file for the application.
     */
    public String MetricLogFilePath;
    
    /**
     * The frequency in which metric events should be written to the metric log file in milliseonds.
     */
    public int MetricEventWriteFrequency;
}
