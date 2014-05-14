package Main;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class fieldQueue {
	
	private final Field field;
	private final LinkedList<String> threadQueue;
	private final LinkedList<Boolean> booleanQueue;
	private boolean isInUse;
	
	public fieldQueue(Field f){
		this.field = f;
		this.threadQueue = new LinkedList<String>();
		this.booleanQueue = new LinkedList<Boolean>();
		this.isInUse = false;
	}
	
	public void addToQueue(String s, boolean b){
		threadQueue.add(s);
		booleanQueue.add(b);
	}
	public String getNext(){
		if(!isInUse) {
			if(this.threadQueue.size()>0){
			String next = this.threadQueue.pop();
			isInUse = this.booleanQueue.pop();
			return next;}
		}
		return null;
	}
	public void releaseAccess(){
		this.isInUse = false;
	}
	public boolean isInUse(){
		return this.isInUse;
	}

}
