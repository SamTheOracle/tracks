package com.oracolo.findmycar.mqtt;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.mqtt.messages.VehicleMessage;

import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class VehiclePublisher {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	@Channel("vehicles")
	@Broadcast
	Emitter<VehicleMessage> vehicleMessageEmitter;

	public void sendMessage(VehicleMessage message) {
		logger.info("Sending vehicle message {}", message);
		vehicleMessageEmitter.send(Message.of(message));
	}



}
