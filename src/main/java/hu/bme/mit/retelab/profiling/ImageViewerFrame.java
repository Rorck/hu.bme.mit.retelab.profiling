package hu.bme.mit.retelab.profiling;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ImageViewerFrame extends JFrame {
	JLabel label;
	JPanel panel;
	JFileChooser fileChooser;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu effectMenu;
	JMenuItem openFileMenuItem;
	JMenuItem applyEffectMenuItem;
	
	JScrollPane pictureScrollPane;
	
	ImageIcon imageIcon;
	ImageIcon displayedImageIcon;
	
	OilPaintFilter filter;
	
	public ImageViewerFrame() {
		filter = new OilPaintFilter(5);
		
		setSize(1024, 768);
		setResizable(false);
		
		label = new JLabel();
		add(label);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		effectMenu = new JMenu("Effects");
		menuBar.add(fileMenu);
		menuBar.add(effectMenu);
		
		openFileMenuItem = new JMenuItem("Open Image");
		applyEffectMenuItem = new JMenuItem("Apply Oil Painting Effect");
		
		effectMenu.add(applyEffectMenuItem);
		fileMenu.add(openFileMenuItem);
		
		openFileMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = fileChooser.showDialog(null, "Open Image");
				
				if (result == JFileChooser.APPROVE_OPTION) {
					String filePath = fileChooser.getSelectedFile().getPath();
					
					imageIcon = new ImageIcon(filePath);
					
					displayedImageIcon = scaleImage(imageIcon, label.getWidth(), label.getHeight());

				    label.setIcon(displayedImageIcon);

					pack();
				}
				
			}
		});
		
		applyEffectMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if (imageIcon != null) {
					BufferedImage bi = new BufferedImage(
							imageIcon.getIconWidth(),
							imageIcon.getIconHeight(),
						    BufferedImage.TYPE_3BYTE_BGR );
					Graphics g = bi.createGraphics();
					// paint the Icon to the BufferedImage.
					imageIcon.paintIcon(null, g, 0,0);
					g.dispose();
					
					try {
						BufferedImage result = filter.apply(bi);				
						imageIcon.setImage(result);
						displayedImageIcon = scaleImage(imageIcon, label.getWidth(), label.getHeight());
					    label.setIcon(displayedImageIcon);
						pack();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});

	}
	
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
}
