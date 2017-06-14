package io.github.alivety.conquerors.client;

import java.util.Stack;

import com.google.common.base.Preconditions;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

import io.github.alivety.conquerors.common.Main;

public class GameApp extends SimpleApplication {
	private Stack<Runnable> tasks=new Stack<Runnable>();
	private Client client;
	public GameApp(Client client) {
		this.client=client;
	}
	
	@Override
	public void simpleInitApp() {
		KeyEvents.app=this;
		this.initKeyBindings();
	}
	
	private void initKeyBindings() {
		inputManager.clearMappings();
		
		addKeyMapping("Up",KeyInput.KEY_W);
		addKeyMapping("Left",KeyInput.KEY_A);
		addKeyMapping("Right",KeyInput.KEY_D);
		addKeyMapping("Down",KeyInput.KEY_S);
		
		addKeyMapping("Exit",KeyInput.KEY_ESCAPE);
		
		addKeyMapping("Clear",KeyInput.KEY_C);// TODO clear all selected units
		addKeyMapping("Chat",KeyInput.KEY_SLASH);// TODO open chat
		addKeyMapping("SelN",KeyInput.KEY_G);// TODO select nearby units
		addKeyMapping("Win",KeyInput.KEY_E);// TODO open window on selected unit
		
		inputManager.addListener(KeyEvents.MovementControl, "Up","Left","Right","Down");
		inputManager.addListener(KeyEvents.ExitControl, "Exit");
	}
	
	private void addKeyMapping(String name,int key) {
		inputManager.addMapping(name, new KeyTrigger(key));
	}
	
	public void simpleUpdate(float tpf) {
		while (hasMoreTasks()) {
			getNextTask().run();
		}
	}
	
	public Client getClient() {
		return client;
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
	
	public void handleError(String errMsg,Throwable t) {
		Main.handleError(new RuntimeException(errMsg,t));
	}
}
