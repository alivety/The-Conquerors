package io.github.alivety.conquerors.server;

import java.nio.channels.SocketChannel;

import io.github.alivety.conquerors.common.PlayerObject;

public class OnlinePlayer extends PlayerObject {
	public OnlinePlayer(SocketChannel ch) {
		super(ch);
	}
}