package studentmarksanalyser.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressDialog extends Frame {
	
	public static final Dimension FRAME_SIZE = new Dimension(300, 75);
	
	JPanel container = new JPanel();
	
	JProgressBar progressBar = new JProgressBar();

	public ProgressDialog(String title) {
		super(FRAME_SIZE, title, false, false, false);
		setContent(container);
		
		container.setLayout(new BorderLayout());
		container.add(progressBar, BorderLayout.CENTER);
	}
	
	public void setProgress(int progress)
	{
		progressBar.setValue(progress);
	}
	
	public int getProgress()
	{
		return progressBar.getValue();
	}
}
