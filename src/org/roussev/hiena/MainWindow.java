package org.roussev.hiena;

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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import org.roussev.hiena.player.*;
import org.roussev.hiena.util.*;

public final class MainWindow extends JWindow implements MainListener {

	private boolean isShade;
	private int plstick = Constants.STICK_UP;
	private Window childWindow = null;
	private JFrame topFrame = null;

	private int XDrag = 0, YDrag = 0;
	private int OrigineX = 0, OrigineY = 0;
	private int XPressed = 0, YPressed = 0;
	private Dimension screenSize;

	private final BasePlayer basePlayer;

	//-------------------------------------------------------
	public MainWindow(JFrame top) {
		super(top);
		top.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				destroy();
			}
		});

		this.setSize(Constants.WIDTH, Constants.HEIGHT);
		this.centerWindow();

		//--------------
		basePlayer = new BasePlayer(this);

		this.getContentPane().add(basePlayer);

		this.setVisible(true);
		initMouseListeners();

		this.topFrame = top;
		//topFrame.addWindowListener(this);
		topFrame.setLocation(-200, -200);
		topFrame.setSize(0, 0);
		topFrame.setResizable(false);
		topFrame.setVisible(true);
		topFrame.setIconImage(Toolkit.getDefaultToolkit().createImage(Utils.getResource("icon.small")));

		this.requestFocus();
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
		OrigineY = (screenSize.height - frameSize.height) / 2;

		this.setLocation(OrigineX, OrigineY);
	}

	//-------------------------------------------------------
	public final Window getOwnerWindow() {
		return (this);
	}
	public final boolean isApplet() {
		return (false);
	}
	public final void destroy() {
		basePlayer.destroy();
		topFrame.dispose();
		System.gc();
		System.exit(0);
	}
	public final void iconify() {
		topFrame.setState(JFrame.ICONIFIED);
	}
	public final void shade() {
		if (!isShade) {
			this.setSize(this.getWidth(), Constants.SHADE_HEIGHT);
			isShade = true;
		} else {
			this.setSize(this.getWidth(), Constants.HEIGHT);
			isShade = false;
		}

	}

	//-------------------------------------------------------
	public final void setChildWindow(Window child) {
		this.childWindow = child;
	}
	//-------------------------------------------------------
	public final void setPlstick(int x) {
		this.plstick = x;
	}

	//-------------------------------------------------------
	final void doMousePressed(MouseEvent evt) {
		XPressed = evt.getX();
		YPressed = evt.getY();
	}
	//-------------------------------------------------------
	final void doMouseDragged(MouseEvent evt) {

		int DeltaX = 0, DeltaY = 0;

		DeltaX = evt.getX() - XDrag;
		DeltaY = evt.getY() - YDrag;
		XDrag = evt.getX() - DeltaX;
		YDrag = evt.getY() - DeltaY;
		OrigineX = -XPressed + OrigineX + DeltaX;
		OrigineY = -YPressed + OrigineY + DeltaY;

		// Keep player window on screen
		if (OrigineX < - Constants.WIDTH + Constants.MIN_VISIBLE)
			OrigineX = 0;
		if (OrigineY < - Constants.HEIGHT + Constants.MIN_VISIBLE)
			OrigineY = 0;
		if (screenSize.width != -1) {
			if (OrigineX > screenSize.width - Constants.MIN_VISIBLE)
				OrigineX = screenSize.width - Constants.WIDTH;
		}
		if (screenSize.height != -1) {
			if (OrigineY > screenSize.height - Constants.TASKBAR_HEIGHT ) {
				OrigineY = screenSize.height - getHeight() - Constants.TASKBAR_HEIGHT;
			}
		}

		//++++++++++++++++++++++++++++++++++++++++
		//Outil.p( "1 OrigineY=" + OrigineY + " ][ childY=" + childY );
		if (plstick != Constants.STICK_UP && plstick != Constants.STICK_DOWN) {
			final int childX = childWindow.getX();
			final int childY = childWindow.getY();
			final int y_Stick_UP = Math.abs(Math.abs(childY - OrigineY) - getHeight());
			final int y_Stick_DOWN = Math.abs(childY + childWindow.getHeight() - OrigineY);
			final int x_Stick = Math.abs(OrigineX - childX);
			boolean booX = false;
			boolean booY = false;

			if (y_Stick_UP < 10) {
				if (childY - getHeight() > OrigineY) {
					OrigineY = childY - getHeight();
					booY = true;
				}
			} else if (y_Stick_DOWN < 10) {
				OrigineY = childY + childWindow.getHeight();
				booY = true;
			}

			//----
			if (x_Stick < 10) {
				OrigineX = childX;
				booX = true;
			}

			if (booX && booY) {
				if (childY > OrigineY) {
					this.setPlstick(Constants.STICK_DOWN);
				} else {
					this.setPlstick(Constants.STICK_UP);
				}
			} else {
				this.setPlstick(Constants.STICK_NOT);
			}

		}

		setLocation(OrigineX, OrigineY);
		//++++++++++++++++++++++++++++++++++++++++

		// Moves the playlist
		if (plstick == Constants.STICK_UP) {
			childWindow.setLocation(OrigineX, OrigineY - childWindow.getHeight());
		} else if (plstick == Constants.STICK_DOWN) {
			childWindow.setLocation(OrigineX, OrigineY + getHeight());
		}
		
		//System.out.println( "dfsdfg sdfgsdfg sdfg" );
	}

	//-------------------------------------------------------
	public final static void main(String[] args) {

		new MainWindow(new JFrame("hiëna.player"));
	}

	//-------------------------------------------------------
}
