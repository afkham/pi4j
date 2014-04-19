/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  DHTSensorExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.wiringpi.DHTSensor;
import com.pi4j.wiringpi.DHTSensorType;

/**
 * Created by azeez on 4/17/14.
 */
public class DHTSensorExample {
    public static void main(String[] args) {
        System.out.println("<--Pi4J--> DHT11 Sensor Example ... started.");

        DHTSensor dhtSensor = new DHTSensor(DHTSensorType.DHT11, 27);


        while (true) {
            System.out.println("-----------------------------");
            dhtSensor.read();
            System.out.println("temperature C= " + dhtSensor.getTemperature(false));
            System.out.println("temperature F= " + dhtSensor.getTemperature(true));
            System.out.println("humidity = " + dhtSensor.getHumidity());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
