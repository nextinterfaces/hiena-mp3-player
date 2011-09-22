package org.roussev.hiena.player.playlist;

/*
 *  Copyright 2009 Hiena Mp3 Player http://code.google.com/p/hiena-mp3-player/
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import java.util.Vector;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.Toolkit;
import javax.swing.JWindow;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//import org.roussev.media.util.*;

import org.roussev.hiena.*;

public final class TWindow extends JWindow implements PlaylistPropertyChangeListener, Playlist {

	private int XDrag = 0, YDrag = 0;
	private int OrigineX = 0, OrigineY = 0;
	private int XPressed = 0, YPressed = 0;
	private Dimension screenSize;

	private final Window owner;
	private TAbsPanel tPanel;

	//---------------------------------------------------
	public TWindow(Window owner) {
		super(owner);
		this.owner = owner;
		if (owner instanceof MainWindow) {
			((MainWindow)owner).setChildWindow(this);
		}
		this.setSize(Constants.PL_WIDTH, Constants.PL_HEIGHT);
		this.centerWindow();

		getContentPane().setLayout(null);

		tPanel = new TPanel(this);
		tPanel.setLocation(0, 0);
		getContentPane().add(tPanel);

		initMouseListeners();
	}

	//-------------------------------------------------------
	private final void initMouseListeners() {

		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				doMousePressed(evt);
			}
		});

		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				doMouseDragged(evt);
			}
		});
	}
	//-------------------------------------------------------
	private final void centerWindow() {

		//Center the window
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		OrigineX = (screenSize.width - frameSize.width) / 2;
		OrigineY = (screenSize.height - owner.getHeight()) / 2 - frameSize.height;

		this.setLocation(OrigineX, OrigineY);
	}

	//--------------------------------------
	final void doMousePressed(MouseEvent evt) {
		XPressed = evt.getX();
		YPressed = evt.getY();
	}

	//--------------------------------------------------------------
	final void doMouseDragged(MouseEvent evt) {

		int DeltaX = 0, DeltaY = 0;

		DeltaX = evt.getX() - XDrag;
		DeltaY = evt.getY() - YDrag;
		XDrag = evt.getX() - DeltaX;
		YDrag = evt.getY() - DeltaY;
		OrigineX = -XPressed + OrigineX + DeltaX;
		OrigineY = -YPressed + OrigineY + DeltaY;

		// Keep player window on screen
		if (OrigineX < - Constants.PL_WIDTH + Constants.MIN_VISIBLE)
			OrigineX = 0;
		if (OrigineY <  - Constants.PL_HEIGHT + Constants.MIN_VISIBLE)
			OrigineY = 0;
		if (screenSize.width != -1) {
			if (OrigineX > screenSize.width - Constants.MIN_VISIBLE)
				OrigineX = screenSize.width - Constants.PL_WIDTH;
		}
		if (screenSize.height != -1) {
			if (OrigineY > screenSize.height /*- getHeight()*/ - Constants.TASKBAR_HEIGHT ) {
				OrigineY = screenSize.height - getHeight() - Constants.TASKBAR_HEIGHT;
				//Outil.p( "DeltaY="+DeltaY + "  YDrag="+YDrag + "   OrigineY="+OrigineY);
			}
		}
 

		final int ownX = owner.getX();
		final int ownY = owner.getY();
		final int y_Stick_UP = Math.abs(Math.abs(ownY - OrigineY) - getHeight());
		final int y_Stick_DOWN = Math.abs(ownY + owner.getHeight() - OrigineY);
		final int x_Stick = Math.abs(OrigineX - ownX);
		boolean booX = false;
		boolean booY = false;

		if (y_Stick_UP < 10) {
			if (ownY - getHeight() > OrigineY) {
				OrigineY = ownY - getHeight();
				booY = true;
			}
		} else if (y_Stick_DOWN < 10) {
			OrigineY = ownY + owner.getHeight();
			booY = true;
		}

		//----
		if (x_Stick < 10) {
			OrigineX = ownX;
			booX = true;
		}

		setLocation(OrigineX, OrigineY);

		if (owner instanceof MainWindow) {
			if (booX && booY) {
				if (OrigineY > ownY) {
					((MainWindow)owner).setPlstick(Constants.STICK_DOWN);
				} else {
					((MainWindow)owner).setPlstick(Constants.STICK_UP);
				}
			} else {
				((MainWindow)owner).setPlstick(Constants.STICK_NOT);
			}
		}

	}

	//--------------------------------------------------------------
	public final void shade() {
		if (tPanel instanceof TPanel) {
			final TAbsPanel tmp = new TShedPanel(this);
			this.remove(tPanel);
			tPanel = tmp;
			this.setSize(Constants.WIDTH, Constants.SHADE_HEIGHT);
		}
		else {
			final TAbsPanel tmp = new TPanel(this);
			this.remove(tPanel);
			tPanel = tmp;
			this.setSize(Constants.WIDTH, Constants.PL_HEIGHT);
		}
		//---
		if ( this.owner.getY() > OrigineY) {
			setLocation(owner.getX(), owner.getY()-getHeight());
		}
		else {
			setLocation(owner.getX(), owner.getY() + owner.getHeight());
		}
		this.getContentPane().add(tPanel);
		tPanel.repaint();

	}

	//--------------------------------------------------------------
	public final void setLocation(int x, int y) {
		OrigineX = x;
		OrigineY = y;
		super.setLocation(x, y);
	}

	/*-------------------------------------------------------------------*/
	/*---             notify PropertyChangeListeners                   --*/
	/*-------------------------------------------------------------------*/
	private final Vector listeners = new Vector();

	public synchronized final void addPropertyChangeListener(PropertyChangeListener l) {
		//super.addPropertyChangeListener(l);
		//if( listeners != null ) {
		if (!listeners.contains(l)) {
			listeners.addElement(l);
		}
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		if (listeners.contains(l)) {
			listeners.removeElement(l);
		}
	}

	public final void firePropertyChangeEvent(String property) {
		/*final int size = listeners.size();
		PropertyChangeListener listener;
		for(int i=0; i < size ; ++i) {
		    listener = (PropertyChangeListener)listeners.get(i);
		    listener.propertyChange( new PropertyChangeEvent( this, property, null, null ) );
		    //listener.propertyChange(null);
		}*/

		//////--------------
		final Vector listenerCopy;
		synchronized (this) {
			listenerCopy = (Vector)listeners.clone();
		}

		for (int i = 0; i < listeners.size(); ++i) {
			final PropertyChangeListener pcl = (PropertyChangeListener)listenerCopy.elementAt(i);
			pcl.propertyChange(new PropertyChangeEvent(this, property, null, null));
		}
		//////--------------

	}

	//--------------------------------------------------------------
	public void goNext() {
		tPanel.goNext();
	}
	public void goPrev() {
		tPanel.goPrev();
	}
	public void eject() {
		tPanel.eject();
	}
	public Object getSource() {
		return tPanel.getSource();
	}
	public Data getData() {
		return tPanel.getData();
	}

	//--------------------------------------------------------------
}
