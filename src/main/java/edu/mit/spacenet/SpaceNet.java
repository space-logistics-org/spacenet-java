/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.mit.spacenet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.SplashScreen;
import net.infonode.gui.laf.InfoNodeLookAndFeel;

/**
 * This class is used to launch the SpaceNet application.
 * 
 * @author Paul Grogan
 */
public class SpaceNet {
	
	/**
	 * Launches the SpaceNet application.
	 * 
	 * @param args the args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
					UIManager.put("TextArea.margin", new Insets(3,3,3,3));
					UIManager.put("Button.margin", new Insets(3,3,3,3));
					UIManager.put("ComboBox.background", Color.WHITE);
					UIManager.put("ProgressBar.background", Color.WHITE);
				} catch(Exception e) {
					e.printStackTrace();
				}
				// a customized swing worker is used to trigger the splash 
				// screen in a separate thread so the progress bar can spin 
				// while the components are loaded in the main frame
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					private SplashScreen splash;
					@Override
					protected Void doInBackground() {
						// create and display splash screen while application is 
						// loading
						splash = new SplashScreen();
						splash.setSize(500, 225);
						splash.setLocationRelativeTo(null);
						splash.setVisible(true);
						try {
							//TODO check if off screen (e.g. last used with different monitor settings
							Rectangle virtualBounds = new Rectangle();
							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
							GraphicsDevice[] gs = ge.getScreenDevices();
							for(int j=0; j<gs.length; j++) {
								GraphicsDevice gd = gs[j];
								GraphicsConfiguration[] gc = gd.getConfigurations();
								for(int i=0; i<gc.length; i++) {
									virtualBounds = virtualBounds.union(gc[i].getBounds());
								}
							}
							if(SpaceNetSettings.getInstance().getLastBounds()==null
									|| SpaceNetSettings.getInstance().getLastBounds().equals(new Rectangle())
									|| virtualBounds.intersection(SpaceNetSettings.getInstance().getLastBounds()).equals(new Rectangle())) {
								SpaceNetFrame.getInstance().setLocationRelativeTo(null);
								SpaceNetFrame.getInstance().setSize(new Dimension(1024,768));
							} else {
								SpaceNetFrame.getInstance().setBounds(virtualBounds.intersection(SpaceNetSettings.getInstance().getLastBounds()));
							}
							SpaceNetFrame.getInstance().setVisible(true);
						} catch(Exception ex) {
							// display error message if one occurs... since this
							// is inside a worker thread, the stack trace will
							// not be printed unless directed handled here
							JOptionPane.showMessageDialog(null, 
									"An error of type \"" + 
									ex.getClass().getSimpleName() + 
									"\" occurred while launching SpaceNet.\n", 
									"SpaceNet Error",
									JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
						// dispose of the (finished) splash screen
						splash.setVisible(false);
						splash.dispose();
						return null;
					}
				};
				
				// execute the worker
				worker.execute();
            }
        });
	}
}