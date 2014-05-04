package src.main;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JSlider;


public class elasticDisplay extends Display{
	private final initialDisplay d;
	private final JSlider slider;

	public elasticDisplay(int w, int h, JFrame f, Program program, initialDisplay d) {
		super(w, h, f, program);
		this.d = d;
		slider = new JSlider(JSlider.HORIZONTAL, 0, 100, d.elasticity);
		init();
	}

	@Override
	void init() {
		slider.addChangeListener(new elasticChangeListener(d));
		slider.setBounds(width/2-100, height/2-35, 200, 70);
		add(slider);
		slider.setVisible(true);

		Button startPause = d.getBallStart().getClone();
		startPause.setText("Click to Start/Pause Ball Pit");
		startPause.setBounds(width/2-200, height/2-25-50, 400, 50);

		add(startPause);


	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub

	}


}
