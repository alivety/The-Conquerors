package io.github.alivety.conquerors.server.windows;

import java.util.ArrayList;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.window.Window;
import io.github.alivety.conquerors.server.Server;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;

public class WindowAlly extends Window {
	private WindowRequestedEvent evt;
	private Server server;
	
	public WindowAlly(WindowRequestedEvent evt,Server server) {
		this.evt=evt;
		this.server=server;
	}

	@Override
	protected Slot[] w_getSlots() {
		PlayerObject[] ps=server.getOnlinePlayers();
		ArrayList<Slot> list=new ArrayList<>();
		for (final PlayerObject po:ps) {
			if (server.getOnlinePlayers().length<5) {
				if (evt.player.allyCount()==2) {
					evt.player.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
					break;
				}
				if (po.allyCount()==2) {
					continue;
				}
			} else {
				if (evt.player.allyCount()==3) {
					evt.player.packet(9, Main.formatChatMessage("You cannot have more than 3 players in an alliance (2 for games with under 6 players"));
					break;
				}
				if (po.allyCount()==3) {
					continue;
				}
			}
			if (po.username()==null)
				continue;
			if (po.isAlly(evt.player))
				continue;
			list.add(new Slot(){
				@Override
				public void click() {
					evt.player.packet(9, Main.formatChatMessage("An ally request was sent to "+po.username()));
					po.packet(9, Main.formatChatMessage("You have an ally request from "+evt.player.username()));
					server.scheduleWindowOpen(po, new Window(){
						@Override
						public Slot[] w_getSlots() {
							return new Slot[]{
									new Slot(){
										@Override
										public void click() {
											if (server.getOnlinePlayers().length<5) {
												if (evt.player.allyCount()==2) {
													po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
													return;
												}
												if (po.allyCount()==2) {
													po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
													return;
												}
											} else {
												if (evt.player.allyCount()==3) {
													po.packet(9, Main.formatChatMessage("You cannot have more than 3 players in an alliance (2 for games with under 6 players"));
													return;
												}
												if (po.allyCount()==3) {
													po.packet(9, Main.formatChatMessage("You cannot have more than 2 players in an alliance (3 for games with over 6 players"));
													return;
												}
											}
											evt.player.packet(9, Main.formatChatMessage(po.username()+" accepted your ally request"));
											evt.player.ally(po);
											po.ally(evt.player);
										}

										@Override
										public String getValue() {
											return "Accept";
										}},
									new Slot(){
										@Override
										public void click() {
											evt.player.packet(9, Main.formatChatMessage(po.username()+" declined your ally request"));
										}

										@Override
										public String getValue() {
											return "Decline";
										}}
							};
						}

						@Override
						public String getTitle() {
							return "Ally Request";
						}

						@Override
						public String getDescription() {
							return "Ally request from "+evt.player.username();
						}});
				}

				@Override
				public String getValue() {
					return po.username();
				}});
		}
		return list.toArray(new Slot[]{});
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
