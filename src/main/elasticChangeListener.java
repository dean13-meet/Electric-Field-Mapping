package main;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class elasticChangeListener implements ChangeListener {

	private final initialDisplay d;

	public elasticChangeListener(initialDisplay d){
		this.d = d;
	}

	public void stateChanged(ChangeEvent changeEvent) {
		Object source = changeEvent.getSource();
		JSlider theJSlider = (JSlider) source;
		int currentValue = theJSlider.getValue();
		d.elasticity = currentValue;
		d.elasticWallsButton.strs[0] = "Elasticity: " + d.elasticity + "%";
		d.elasticWallsButton.simulateClick();//Makes sure the button changes text size when the numbers are changing (if needed).
	}

}