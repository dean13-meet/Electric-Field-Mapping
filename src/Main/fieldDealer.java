package Main;

import java.lang.reflect.Field;
import java.util.ArrayList;

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

			if(queues[index].getCurrentlyUsing().equals(t.getName())) {return;}
			else{
				if(queues[index].getCurrentlyUsing().equals("")){
					if(queues[index].peekNext().equals(t.getName())){
						String next = queues[index].getNext();
						if(next!=null && next.equals(t.getName())){
							System.out.println("The Thread: " + t.getName() + " gained access to: " + f.getName());
							return;
						}
					}
				}

			}
			
			System.out.println("The Thread: " + t.getName() + " is waiting to gain access to: " + f.getName() + " " + queues[index].getCurrentlyUsing() + " "+ this.getOccupiedFieldsOfAThread(queues[index].getCurrentlyUsing()));
		}
	}

	public void releaseAccess(Field f){
		int index = getIndex(f, fields);
		System.out.println(Thread.currentThread().getName() + " Released access for " + 
		f.getName() + " which was held for: " + queues[index].releaseAccess());
		
	}

	public int getIndex(Object o, Object[] os){
		for(int i = 0; i < os.length ; i++){
			if(os[i].equals(o)) return i;
		}
		return -1;
	}

	public ArrayList<Field> getOccupiedFieldsOfAThread(String name){
		ArrayList<Field> retval = new ArrayList<Field>();
		for (fieldQueue q : this.queues){
			if(q.getCurrentlyUsing().equals(name)){
				retval.add(q.getField());
			}
		}
		return retval;
	}
	
	public void releaseAllLocks(String threadName){
		ArrayList<Field> locks = getOccupiedFieldsOfAThread(threadName);
		for (Field f : locks){
			for(Field ff : fields){
				if(f.equals(ff)){
					this.releaseAccess(f);
				}
			}
		}
	}




}
