package Main;

import java.lang.reflect.Field;
import java.util.Collection;

public class fieldDealer {


	private final Field[] fields;
	private final fieldQueue[] queues;
	private final ArrayList<threadHoldings> occupyingThreads = new ArrayList<threadHoldings>();


	public fieldDealer(Field[] declaredFields) {
		this.fields = declaredFields;
		this.queues = new fieldQueue[fields.length];
		for (int i = 0; i < fields.length; i ++){
			this.queues[i] = new fieldQueue(fields[i]);
		}

	}


	
	public void getAccess(String t, Field f, Boolean a){
		if(!occupyingThreads.contains(t)){
			occupyingThreads.add(new threadHoldings(t));
		}else{
			ArrayList<Field> holdings = occupyingThreads.get(occupyingThreads.indexOf(t)).getHoldings();
			if(holdings.size()!=0 && holdings.get(holdings.size()-1).getName().compareTo(f.getName()) <= 0){
				System.out.println("HOLDINGS: " + holdings.get(holdings.size()-1).getName() + " " + f.getName());
			}else if (holdings.size()!= 0){
				int size = holdings.size();
				ArrayList<Field> toRelease = new ArrayList<Field>();
				//Get loaction field f will fall in:
				int index = 0;
				for(index = 0; index < size; index++){
					if(holdings.get(index).getName().compareTo(f.getName()) > 0){
						break;
					}
				}

				for(int i = index - 1; i < size; i++){
					toRelease.add(holdings.get(i));
				}
				for(Field ff : toRelease){
					this.releaseAccess(ff);
				}
				this.getAccess(t, f, a);
				for(Field ff : toRelease){
					this.getAccess(t, ff, true);
				}

			}
		}
		int index = getIndex(f, fields);
		queues[index].addToQueue(t, a);

		while(true){

			if(queues[index].getCurrentlyUsing().equals(t)) {return;}
			else{
				if(queues[index].getCurrentlyUsing().equals("")){
					if(queues[index].peekNext().equals(t)){
						String next = queues[index].getNext();
						if(next!=null && next.equals(t)){
							System.out.println("The Thread: " + t + " gained access to: " + f.getName());
							occupyingThreads.get(occupyingThreads.indexOf(t)).add(f);
							System.out.println("\n After add holdings: " + occupyingThreads.get(occupyingThreads.indexOf(t)).getHoldings());
							return;
						}
					}
				}

			}

			//System.out.println("The Thread: " + t + " is waiting to gain access to: " + f.getName() + " " + queues[index].getCurrentlyUsing() + " "+ this.getOccupiedFieldsOfAThread(queues[index].getCurrentlyUsing()));
		}
	}

	public void releaseAccess(Field f){
		int index = getIndex(f, fields);
		threadHoldings thread = occupyingThreads.get(occupyingThreads.indexOf(queues[index].getCurrentlyUsing()));

		System.out.println("      \n     Currently have: " + thread.getHoldings() + " want to remove: " + f);
		thread.remove(f);
		if(thread.getHoldings().size()==0)occupyingThreads.remove(thread);
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

class threadHoldings{

	private final String threadName;
	private final ArrayList<Field> holdings = new ArrayList<Field>();

	public threadHoldings(String threadName){
		this.threadName = threadName;
	}
	public boolean equals(Object o){
		return (o instanceof threadHoldings && ((threadHoldings) o).getName().equals(threadName)) 
				||
				o.equals(threadName);//Get thread by name
	}

	public String getName(){
		return threadName;
	}
	public ArrayList<Field> getHoldings(){
		return holdings;
	}
	public void add(Field f){
		holdings.add(f);
	}
	public void remove(Field f){
		holdings.remove(f);
	}
	public String toString(){
		return threadName + "'s holdings: " + holdings;
	}
}

class ArrayList<E> extends java.util.ArrayList<E>{
	//Only reason for extending ArrayList, is to override indexOf and force it to use the
	//equals method in an appropriate way, so that it uses the equals method of threadHoldings
	//and not of the given object. (threadHoldings has a special equals method which needs to be
	//used when creating an arraylist of it).
	public ArrayList(){
		super();
	}
	public ArrayList(int i){
		super(i);
	}
	public ArrayList (Collection<? extends E> c){
		super(c);
	}
	public int indexOf(Object o){
		for(int i = 0 ; i < size(); i++){
			if(i!=size()){
				if(o==null ? 
						get(i)==null : 
							(o.equals(get(i))||
									(true&&get(i).equals(o)))){
					return i;
				}
			}
		}
		return -1;
	}
}
