package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;

@Clientside
public class WindowOpenedEvent extends PRET implements Cancelable {
	public String[] slots;
	public WindowOpenedEvent(String[]slots) {
		this.slots=slots;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
}
