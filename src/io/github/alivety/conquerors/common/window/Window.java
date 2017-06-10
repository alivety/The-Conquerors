package io.github.alivety.conquerors.common.window;

public abstract class Window implements IWindow {
	public void click(int slot) {
		this.getSlots()[slot].click();
	}
}
