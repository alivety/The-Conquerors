package io.github.alivety.conquerors.server.windows;

import java.util.ArrayList;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.window.Window;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;

public class WindowAlly extends Window {
	private final WindowRequestedEvent evt;
	private final Server server;
	
	public WindowAlly(final WindowRequestedEvent evt, final Server server) {
		this.evt = evt;
		this.server = server;
	}
	
	@Override
	protected Slot[] w_getSlots() {
		final PlayerObject[] ps = this.server.getOnlinePlayers();
		final ArrayList<Slot> list = new ArrayList<>();
		for (final PlayerObject po : ps) {
			if (this.server.getOnlinePlayers().length < 5) {
				if (this.evt.player.allyCount() == 2) {
					this.evt.player.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
					break;
				}
				if (po.allyCount() == 2)
					continue;
			} else {
				if (this.evt.player.allyCount() == 3) {
					this.evt.player.packet(9, Main.formatChatMessage("You cannot have more than 3 players in an alliance (2 for games with under 6 players"));
					break;
				}
				if (po.allyCount() == 3)
					continue;
			}
			if (po.username() == null)
				continue;
			if (po.isAlly(this.evt.player))
				continue;
			list.add(new Slot() {
				@Override
				public void click() {
					WindowAlly.this.evt.player.packet(9, Main.formatChatMessage("An ally request was sent to " + po.username()));
					po.packet(9, Main.formatChatMessage("You have an ally request from " + WindowAlly.this.evt.player.username()));
					WindowAlly.this.server.scheduleWindowOpen(po, new Window() {
						@Override
						public Slot[] w_getSlots() {
							return new Slot[] { new Slot() {
								@Override
								public void click() {
									if (WindowAlly.this.server.getOnlinePlayers().length < 5) {
										if (WindowAlly.this.evt.player.allyCount() == 2) {
											po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
											return;
										}
										if (po.allyCount() == 2) {
											po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
											return;
										}
									} else {
										if (WindowAlly.this.evt.player.allyCount() == 3) {
											po.packet(9, Main.formatChatMessage("You cannot have more than 3 players in an alliance (2 for games with under 6 players"));
											return;
										}
										if (po.allyCount() == 3) {
											po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
											return;
										}
									}
									WindowAlly.this.evt.player.packet(9, Main.formatChatMessage(po.username() + " accepted your ally request"));
									WindowAlly.this.evt.player.ally(po);
									po.ally(WindowAlly.this.evt.player);
								}
								
								@Override
								public String getValue() {
									return "Accept";
								}
							}, new Slot() {
								@Override
								public void click() {
									WindowAlly.this.evt.player.packet(9, Main.formatChatMessage(po.username() + " declined your ally request"));
								}
								
								@Override
								public String getValue() {
									return "Decline";
								}
							} };
						}
						
						@Override
						public String getTitle() {
							return "Ally Request";
						}
						
						@Override
						public String getDescription() {
							return "Ally request from " + WindowAlly.this.evt.player.username();
						}
					});
				}
				
				@Override
				public String getValue() {
					return po.username();
				}
			});
		}
		return list.toArray(new Slot[] {});
	}
	
	@Override
	public String getTitle() {
		return "Alliances";
	}
	
	@Override
	public String getDescription() {
		return "Gather your allies";
	}
}
