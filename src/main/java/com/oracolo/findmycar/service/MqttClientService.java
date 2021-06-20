package com.oracolo.findmycar.service;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.mqtt.messages.PositionMessage;
import com.oracolo.findmycar.mqtt.messages.VehicleMessage;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class MqttClientService implements IMqttActionListener, MqttCallbackExtended {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private static final String VEHICLE_TOPIC = "vehicles";
	private static final String POSITION_TOPIC = "positions";

	@ConfigProperty(name = "mqtt.port")
	Integer mqttPort;

	@ConfigProperty(name = "mqtt.host")
	String mqttHost;

	@ConfigProperty(name = "mqtt.protocol")
	String mqttProtocol;

	@ConfigProperty(name = "mqtt.client.id")
	String mqttClientId;

	private  MqttAsyncClient mqttClient;

	void init(@Observes StartupEvent event) throws MqttException {
		String serverURI = mqttProtocol + "://" + mqttHost + ":" + mqttPort;
		logger.info("Connecting to broker {}.", serverURI);

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setAutomaticReconnect(true);
		connOpts.setKeepAliveInterval(10);
		connOpts.setUserName(mqttClientId);
		mqttClient = new MqttAsyncClient(serverURI, mqttClientId, new MemoryPersistence());
		mqttClient.setCallback(this);
		mqttClient.connect(connOpts);
	}

	@Override
	public void onSuccess(IMqttToken asyncActionToken) {
		logger.trace("Action success {}.", asyncActionToken);
	}

	@Override
	public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
		logger.error("Action failed.", exception);

	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		logger.info("{}onnected to {}.", reconnect ? "Ric" : "C", serverURI);
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.error("Lost connection.", cause);

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.trace("Received message {} from {}.", new String(message.getPayload()), topic);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.trace("Delivery complete {}.", token);

	}

	public void sendVehicleMessage(VehicleMessage vehicleMessage){
		sendMessage(VEHICLE_TOPIC,convertMessageToBytes(vehicleMessage),false);
	}
	public void sendPositionMessage(PositionMessage positionMessage){
		sendMessage(POSITION_TOPIC,convertMessageToBytes(positionMessage),false);
	}
	private void sendMessage(String topic, byte[] message, boolean retained) {
		try {
			mqttClient.publish(topic, message, 0, retained);
		} catch (MqttException e) {
			logger.error("Could not send message.", e);
		}
	}

	private static byte[] convertMessageToBytes(Object payload){
		return JsonObject.mapFrom(payload).encode().getBytes(StandardCharsets.UTF_8);
	}
}
