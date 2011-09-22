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


import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.roussev.hiena.gui.*;
import org.roussev.hiena.*;

class TShedPanel extends TAbsPanel {

	//---------------------------------------------------------------------
	protected TShedPanel(PlaylistPropertyChangeListener propertyChangeListener) {
		//super(null,true); 
		this.propertyChangeListener = propertyChangeListener;       
		setLayout(null);
		setSize(getPreferredSize());

		drawControlers();
	}

	//------------------------------------------------------------------------
	private void drawControlers() {

		//--------------
		close = new XTransitionButton("close", new String[] { Constants.get("close"), Constants.get("close_"), Constants.get("close.down"), Constants.get("close")});
		close.setSize(close.getPreferredSize());
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_PL_CLOSE);
			}
		});
		
		shade = new XTransitionButton("shade", new String[] { Constants.get("shade.shade"), Constants.get("shade.shade_"), Constants.get("shade.shade.down"), Constants.get("shade.shade")});
		shade.setSize(shade.getPreferredSize());
		shade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_PL_SHADE);
			}
		});

		add(close);
		add(shade);

		close.setLocation(close_location[0], close_location[1]);
		shade.setLocation(shade_location[0], shade_location[1]);
	}


	//void 		setLocation(int x, int y);
	public void     	goNext(){};
	public void     	goPrev(){};
	public void     	eject(){};
	public Object   	getSource(){ return null; };
	public Data     	getData(){ return null; };

	//---------------------------------------------------------------------
	public final Dimension getPreferredSize() {
		return new Dimension(Constants.WIDTH, Constants.SHADE_HEIGHT);
	}

	//--------------------------------------------------------------------- 
	final PlaylistPropertyChangeListener propertyChangeListener;
	
	private XTransitionButton close;
	private XTransitionButton shade;


	//-------------------------------------------------------------------------
	private static final int[] close_location = { 341, 0 };
	private static final int[] shade_location = { 315, 0 };

	//-------------------------------------------------------------
}
