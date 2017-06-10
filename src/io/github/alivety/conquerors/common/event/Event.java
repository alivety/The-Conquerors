package io.github.alivety.conquerors.common.event;

import com.google.common.reflect.TypeToken;

public class Event {
	private boolean isCanceled = false;

	public boolean isCanceled() {
		return this.isCanceled;
	}

	public boolean isCancelable() {
		return TypeToken.of(this.getClass()).getTypes().interfaces().contains(TypeToken.of(Cancelable.class));
	}

	public void setCanceled(final boolean canceled) {
		if (!this.isCancelable())
			throw new UnsupportedOperationException("Cannot canel an event that does not implement Cancelable");
		this.isCanceled = canceled;
	}

	public void post() {
	}

	@Override
	public final String toString() {
		return this.getClass().getSimpleName() + "[canceled=" + this.isCanceled + "]";
	}
}