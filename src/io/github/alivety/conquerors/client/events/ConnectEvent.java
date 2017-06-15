package io.github.alivety.conquerors.client.events;

import java.nio.channels.SocketChannel;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Clientside;

@Clientside
public class ConnectEvent extends Event {
	public SocketChannel ch;

	public ConnectEvent(final SocketChannel ch) {
		this.ch = ch;
	}
}