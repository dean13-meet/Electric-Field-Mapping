package Main;

import java.lang.reflect.Field;

public class fieldDealer {
	
	private final Field[] fields;
	private final fieldQueue[] queues;

	public fieldDealer(Field[] declaredFields) {
		this.fields = declaredFields;
		this.queues = new fieldQueue[fields.length];
		for (int i = 0; i < fields.length; i ++){
			this.queues[i] = new fieldQueue(fields[i]);
		}
		
	}
	
	
	public void getAccess(Thread t, Field f, boolean a){
		int index = getIndex(f, fields);
		queues[index].addToQueue(t.getName(), a);
		
		while(true){
			String next = queues[index].getNext();
			if(next!=null && next.equals(t.getName())){
				System.out.println("The Thread: " + t.getName() + " gained access to: " + f.getName());
				return;
			}
			System.out.println("The Thread: " + t.getName() + " is waiting to gain access to: " + f.getName() + " " + next);
		}
	}
	
	public void releaseAccess(Field f){
		int index = getIndex(f, fields);
		queues[index].releaseAccess();
	}
	
	public int getIndex(Object o, Object[] os){
		for(int i = 0; i < os.length ; i++){
			if(os[i].equals(o)) return i;
		}
		return -1;
	}
	
	
	

}
