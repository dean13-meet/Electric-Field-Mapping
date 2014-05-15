package Main;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class fieldQueue {
	
	private final Field field;
	private final LinkedList<String> threadQueue;
	private final LinkedList<Boolean> booleanQueue;
	private boolean isInUse;
	private String currentlyUsing = "";
	private long lastLocked = System.currentTimeMillis();
	
	public fieldQueue(Field f){
		this.field = f;
		this.threadQueue = new LinkedList<String>();
		this.booleanQueue = new LinkedList<Boolean>();
		this.isInUse = false;
	}
	
	public void addToQueue(String s, boolean b){
		int size = threadQueue.size();
		threadQueue.add(size, s);
		booleanQueue.add(size, b);
	}
	public String getNext(){
		if(!isInUse){
			String next = this.threadQueue.pop();
			this.currentlyUsing = next;
			isInUse = this.booleanQueue.pop();
			lastLocked = System.currentTimeMillis();
			return next;
		}
		return null;
	}
	/**
	 * 
	 * @return How long access was held for
	 */
	public long releaseAccess(){
		this.isInUse = false;
		this.currentlyUsing = "";
		return System.currentTimeMillis() - this.lastLocked;
	}
	public boolean isInUse(){
		return this.isInUse;
	}
	public String getCurrentlyUsing(){
		return this.currentlyUsing;
	}
	public Field getField(){
		return this.field;
	}
	public String peekNext(){
		return this.threadQueue.peek();
	}

}
