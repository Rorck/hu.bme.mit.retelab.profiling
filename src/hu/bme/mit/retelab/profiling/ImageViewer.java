package hu.bme.mit.retelab.profiling;

import javax.swing.JFrame;

public class ImageViewer {

	public static void main(String[] args) {
		JFrame frame = new ImageViewerFrame();
		frame.setTitle("Profiling Laboratory");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
