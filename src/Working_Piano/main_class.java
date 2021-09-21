package Working_Piano;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class main_class {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Piano");
		frame.setAlwaysOnTop(false);
		// setting cursor to the + cursor
		frame.setCursor(new Cursor(1));
		frame.setIconImage(new ImageIcon("src\\Working_Piano\\imageicon.png").getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(new Rectangle(850, 360));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		// Add closing window when application is closing
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(frame, "THANK YOU FOR USING PIANO.\nMADE BY NENAD GVOZDENAC, FTN 2021", "THANK YOU", JOptionPane.PLAIN_MESSAGE);
		    }
		});
		
		// using JLayeredPane due to needing layers on JPanel
		JLayeredPane panel = new JLayeredPane();
		panel.setBounds(frame.getBounds());
		panel.setBackground(Color.DARK_GRAY);
		panel.setLayout(null);
		panel.setOpaque(true);
		
		// Define the names of the tones
		
		String[] full_tones = {"C", "D", "E", "F", "G", "A", "H", "C2"};
		String[] half_tones = {"C#", "D#", "E#", "F#", "G#", "A#", "H#"};
		
		// Adding half tones
		for(Integer i = 0; i < 7; i++) {
			// if they are 2 or 6, continue because they are e# and h# which are, respectively, f and c2 
			if(i == 2 || i == 6) continue;
			JButton button = new_halftonebutton(i, half_tones[i]);
			
			// Add the button to the panel with depth of i
			panel.add(button, i);
		}
		
		// Adding full tones
		for(Integer i = 0; i < 8; i++) {
			JButton button = new_fulltonebutton(i, full_tones[i]);
			// add the button to panel at depth 0
			panel.add(button);
		}
		
		// for component c in components
		for(Component c : panel.getComponents()) {
			// if c is jbutton
			if(c instanceof JButton) {
				// add actionlistener to c
				((JButton) c).addActionListener(new ActionListener() {
					@Override
					// if anything has been done to the button (it can only be pressed so this solves that)
					public void actionPerformed(ActionEvent e) {
						try {
							// activate the function pressed_button
							pressed_button((JButton) c);
						} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
							System.out.print("Error");
							// otherwise print error if something went wrong
							e1.printStackTrace();
						}
					}
				});
				
				((JButton) c).setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
			}
		}
		
		// add that panel to frame
		frame.getContentPane().add(panel);
		// set the frame visible
		frame.setVisible(true);
	}
	
	// function to add fulltonebutton 
	public static JButton new_fulltonebutton(Integer i, String c) {
		
		// put title
		// add button with title
		JButton button = new JButton(c);
		
		// set font, bounds, location and background as needed
		button.setFont(new Font("Arial", Font.BOLD, 22));
		button.setBounds(new Rectangle(100, 250));
		button.setLocation(new Point(15 + (i * 100), 30));
		button.setBackground(Color.white);
		
		// return the button
		return button;
	}
	
	public static JButton new_halftonebutton(Integer i, String c) {
		// put title
		JButton button = new JButton(c);
		
		// set font, bounds, location, background and foreground as needed
		button.setFont(new Font("Arial", Font.BOLD, 20));
		button.setBounds(new Rectangle(62, 150));
		button.setLocation(new Point(83 + (i * 100), 30));
		button.setBackground(Color.BLACK);
		button.setForeground(Color.white);
		
		return button;
	}
	
	// declare a clip object
	public static Clip clip = null;
	
	// create a new function pressed_button which will be called whenever button gets pressed
	public static void pressed_button(JButton c) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		
		// declare string object for storing information which button has been pressed
		String text = null;
		
		// if text contains #, swap that for u (because # doesnt register in files)
		if(c.getText().contains("#"))	
			text = c.getText().replace('#', 'u');
		else
			text = c.getText();
		
		// create a file object pointing to .wav file that is used
		File f = new File("src\\Sound Files\\" + text + ".wav");
		
		// create a audioinputstream for manipulating music
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);
		
		// add that to clip
		clip = AudioSystem.getClip();
		
		// open the line
		clip.open(audioIn);
		
		// Lowering the volume to 0.3f (70% of the original volume)
	    FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float range = control.getMinimum();
	    float result = range * (0.3f);
	    control.setValue(result);
	        
	    clip.start();
	}
}
