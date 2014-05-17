package Main;
/**
 * @author Dean Leitersdorf, William Lee, Ophir Sneh
 */
//
//This
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
//update
public class Program {
	ArrayList<JFrame> frames = new ArrayList<JFrame>();
	ArrayList<String> framesId = new ArrayList<String>();
	private fieldDealer dealer;
	private Thread dealerThread;
	private final LinkedList<ArrayList<Object>> callsToDealer = new LinkedList<ArrayList<Object>>();
	private String finishedDealingFor = "";

	JFrame initialF = new JFrame();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();
	final int DISPLAY_WIDTH = (int) (width/1.2);
	final int DISPLAY_HEIGHT = (int) (height/1.2);
	final int xOffSet = 10;
	final int yOffSet = xOffSet;

	Program() { initialF.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
	initialF.setLayout(null);
	initialF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	initialF.setTitle("Particles in Electric Field Simulator");
	initialF.getContentPane().setBackground(new Color(96,96,96));

	Display initialD = createDisplay(xOffSet, yOffSet, DISPLAY_WIDTH - (2*xOffSet),
			DISPLAY_HEIGHT - (2*yOffSet), initialF, this);

	initialF.add(initialD);
	this.dealer = new fieldDealer(initialD.getClass().getDeclaredFields());
	initialF.setVisible(true);
	initialF.setResizable(false);
	System.out.println(this.DISPLAY_WIDTH + " " + this.DISPLAY_HEIGHT);

	startDealing();
	}

	private void startDealing() {
		Method[] mm = dealer.getClass().getDeclaredMethods();
		for(Method m : mm){
			System.out.println(m);
		}
		dealerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					maintainDealer();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


		});
		dealerThread.start();

	}
	public void maintainDealer() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException{
		while(true){
			Thread.sleep(1);//So that commands don't happen at the same exact time.
			if(!callsToDealer.isEmpty()){
				final ArrayList<Object> methodToRun = callsToDealer.pop();
				System.out.println("Processing call: " + methodToRun);
				final Object[] args = new Object[methodToRun.size()-2];
				Class<?>[] typesOfArgs = new Class<?>[methodToRun.size()-2];
				for(int i = 2; i < methodToRun.size(); i++){
					args[i-2] = methodToRun.get(i);
					typesOfArgs[i-2] = args[i-2].getClass();
				}
				
				final Method m = dealer.getClass().getMethod((String)methodToRun.get(1), typesOfArgs);
				Thread t = new Thread(new Runnable(){
					public void run(){
						try {
							m.invoke(dealer, args);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						finishedDealingFor = methodToRun.toString();
					}
				});
				
				t.start();
			}
		}
	}

	/**
	 * 
	 * @param objs - Object 0 is currentThread, Object 1 is the name of method to invoke.
	 * Object 0 must be there for verification that we are done for THE GIVEN thread.
	 */
	public void activateDealer(ArrayList<Object> objs){
		System.out.println("Activating dealer: " + objs);
		callsToDealer.add(objs);
		while(true){
			if(this.finishedDealingFor.equals(objs.toString())){
				this.finishedDealingFor = "";
				break;
			}
		}
	}

	public Display createDisplay(int x, int y, int w, int h, JFrame f, Program p) {
		//Not using x and y offsets for now.
		return new initialDisplay(w, h, f, p);
	}

	public void createJFrame(double percentageOfScreenWidth, double percentageOfScreenHeight,
			String title, Color background, Boolean resizable, final String id) {

		final JFrame retVal = new JFrame();

		retVal.setSize((int)(DISPLAY_WIDTH*percentageOfScreenWidth/100), (int)(DISPLAY_HEIGHT*percentageOfScreenHeight/100));
		retVal.setLayout(null);
		retVal.setTitle(title);
		retVal.getContentPane().setBackground(background);
		retVal.setVisible(true);
		retVal.setResizable(resizable);
		retVal.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				framesId.remove(id);
				frames.remove(retVal);
			}
		});
		framesId.add(id);
		frames.add(retVal);
	}

	public JFrame getJFrameById(String id) {
		int i = framesId.indexOf(id);

		if(i >= 0 ) {
			return frames.get(i);
		}
		return null;
	}

	public void closeAllFrames() {//Closes all frames except initialF
		for (JFrame f : frames) {
			f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		}
	}
}
