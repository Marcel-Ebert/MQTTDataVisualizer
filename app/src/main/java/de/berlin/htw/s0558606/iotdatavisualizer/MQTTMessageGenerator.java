package de.berlin.htw.s0558606.iotdatavisualizer;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Marcel Ebert S0558606 on 12.12.17.
 */

public class MQTTMessageGenerator implements MqttCallback {

    MqttClient client;
    Random random = new Random();

    public static void main(String[] args) {
        new MQTTMessageGenerator().setup();
    }

    public void setup() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "Testclient", persistence);
            client.connect();
            client.setCallback(this);
            client.subscribe("/iotdata");

            generateRandomNumbers();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomNumbers() throws MqttException {
        while (true){
            MqttMessage message = new MqttMessage();
            double number = random.nextDouble() * 20 - 10;
            message.setPayload(Double.toString(number).getBytes());
            client.publish("/iotdata", message);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
