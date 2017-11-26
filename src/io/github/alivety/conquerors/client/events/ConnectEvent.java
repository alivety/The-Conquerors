package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.packet.Clientside;

@Clientside
public class ConnectEvent extends Event {
	public PPLAdapter adapter;
	
	public ConnectEvent(final PPLAdapter adapter) {
		this.adapter = adapter;
	}
}