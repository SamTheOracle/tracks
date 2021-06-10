package com.oracolo.findmycar.mqtt;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracolo.findmycar.mqtt.messages.PositionMessage;

import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class PositionPublisher {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Inject
	@Channel("positions")
	@Broadcast
	Emitter<PositionMessage> positionMessageEmitter;

	public void sendPosition(PositionMessage positionMessage) {
		logger.debug("Sending position message {}", positionMessage);
		positionMessageEmitter.send(Message.of(positionMessage));
	}
}
