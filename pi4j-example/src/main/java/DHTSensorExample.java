import com.pi4j.component.dht.DHTSensor;
import com.pi4j.component.dht.DHTSensorType;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.temperature.TemperatureScale;

/**
 * Created by azeez on 4/17/14.
 */
public class DHTSensorExample {
    public static void main(String[] args) {
        System.out.println("<--Pi4J--> DHT11 Sensor Example ... started.");

        DHTSensor dhtSensor = new DHTSensor(RaspiPin.GPIO_01, DHTSensorType.DHT11);

        while (true) {
            float temperature = dhtSensor.readTemperature(TemperatureScale.CELSIUS);
            float humidity = dhtSensor.readHumidity();

            System.out.println("temperature = " + temperature);
            System.out.println("humidity = " + humidity);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
