package com.pi4j.component.dht;

import com.pi4j.io.gpio.*;
import com.pi4j.temperature.TemperatureScale;

/**
 * Created by azeez on 4/17/14.
 */
public class DHTSensor {

    private static final int MAX_TIMINGS = 85;

    private Pin dataPinNumber;
    private GpioPinDigitalOutput dataOutPin;
    private GpioPinDigitalInput dataInPin;
    private DHTSensorType type;
    private int count = 6;
    private long lastReadTime = 0;
    private boolean firstReading;
    private int[] data = new int[6];
    private GpioController gpio = GpioFactory.getInstance();

    public DHTSensor(Pin dataPinNumber, DHTSensorType type) {
        this.dataPinNumber = dataPinNumber; //TODO
        this.type = type;
        // set up the pins!
        dataOutPin = gpio.provisionDigitalOutputPin(dataPinNumber, "dhtDataOut", PinState.HIGH);
        lastReadTime = 0;
    }

    public float readTemperature(TemperatureScale scale) {
        float f;

        if (read()) {
            switch (type) {
                case DHT11:
                    f = data[2];
                    if(scale.equals(TemperatureScale.FARENHEIT)){
                        f = convertCtoF(f);
                    }
                    return f;
                case DHT22:
                case DHT21:
                    f = data[2] & 0x7F;
                    f *= 256;
                    f += data[3];
                    f /= 10;
                    //TODO: FIXME
//                    if (data[2] & 0x80)
//                        f *= -1;
                    if(scale.equals(TemperatureScale.FARENHEIT)){
                        f = convertCtoF(f);
                    }

                    return f;
            }
        }
        System.err.println("Read fail");
        return -1;
    }

    private float convertCtoF(float c) {
        return c * 9 / 5 + 32;
    }

    public float readHumidity() {
        float f;
        if (read()) {
            switch (type) {
                case DHT11:
                    f = data[0];
                    return f;
                case DHT22:
                case DHT21:
                    f = data[0];
                    f *= 256;
                    f += data[1];
                    f /= 10;
                    return f;
            }
        }
        System.err.println("Read fail");
        return -1;
    }


    private boolean read() {
        PinState lastState = PinState.HIGH;
        int counter = 0;
        int j = 0, i;
        long currentTime;

        // pull the pin high and wait 250 milliseconds
        dataOutPin.high();
        try {
            Thread.sleep(250);
        } catch (InterruptedException ignored) {
        }

        currentTime = System.currentTimeMillis();
        if (currentTime < lastReadTime) {
            // ie there was a rollover
            lastReadTime = 0;
        }
        if (!firstReading && ((currentTime - lastReadTime) < 2000)) {
            return true; // return last correct measurement
            //Thread.sleep(2000 - (currentTime - _lastreadtime));
        }
        firstReading = false;
  /*
    Serial.print("Currtime: "); Serial.print(currentTime);
    Serial.print(" Lasttime: "); Serial.print(_lastreadtime);
  */
        lastReadTime = System.currentTimeMillis();

        data[0] = data[1] = data[2] = data[3] = data[4] = 0;

        // now pull it low for ~20 milliseconds
        dataOutPin = gpio.provisionDigitalOutputPin(dataPinNumber, "dhtDataOut", PinState.HIGH);
        dataOutPin.low();
        try {
            Thread.sleep(20);
        } catch (InterruptedException ignored) {
        }
        dataOutPin.high();
        try {
            Thread.sleep(0, 40000); // 40ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dataInPin = gpio.provisionDigitalInputPin(dataPinNumber, "dhtDataIn");


        // read in timings
        for (i=0; i< MAX_TIMINGS; i++) {
            counter = 0;
            while (dataInPin.getState().equals(lastState)) {
                counter++;
                try {
                    Thread.sleep(0, 100);
                } catch (InterruptedException ignored) {
                }
                if (counter == 255) {
                    break;
                }
            }
            lastState = dataInPin.getState();

            if (counter == 255) break;

            // ignore first 3 transitions
            if ((i >= 4) && (i%2 == 0)) {
                // shove each bit into the storage bytes
                data[j/8] <<= 1;
                if (counter > count)
                    data[j/8] |= 1;
                j++;
            }

        }

  /*
  Serial.println(j, DEC);
  Serial.print(data[0], HEX); Serial.print(", ");
  Serial.print(data[1], HEX); Serial.print(", ");
  Serial.print(data[2], HEX); Serial.print(", ");
  Serial.print(data[3], HEX); Serial.print(", ");
  Serial.print(data[4], HEX); Serial.print(" =? ");
  Serial.println(data[0] + data[1] + data[2] + data[3], HEX);
  */

        // check we read 40 bits and that the checksum matches
        return (j >= 40) &&
                (data[4] == ((data[0] + data[1] + data[2] + data[3]) & 0xFF));


    }
}
