package io.github.alivety.conquerors.client;

import java.util.Stack;

import com.jme3.app.SimpleApplication;

public class GameApp extends SimpleApplication {
	private Stack<Runnable> tasks=new Stack<Runnable>();
	private Client client;
	public GameApp(Client client) {
		this.client=client;
	}
	
	@Override
	public void simpleInitApp() {
		
	}
	
	public void simpleUpdate(float tfs) {
		while (hasMoreTasks()) {
			getNextTask().run();
		}
	}
	
	
	protected synchronized void scheduleTask(Runnable r) {
		tasks.push(r);
	}
	
	private synchronized Runnable getNextTask() {
		return tasks.pop();
	}
	
	private synchronized boolean hasMoreTasks() {
		return !tasks.empty();
	}
}
