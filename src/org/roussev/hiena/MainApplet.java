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

import org.roussev.hiena.player.*;



public final class MainApplet extends javax.swing.JApplet implements MainListener{
    
    
    private BasePlayer basePlayer;
    JWindow ownerWindow;
    
    //------------------------------------------------------------------
    /**
     * Initializes the Applet.
     */
    public final void init() {
        
        JFrame topFrame = new JFrame();
        //topFrame.addWindowListener(this);
        topFrame.setLocation( -200, -200);
        topFrame.setSize(0,0);
        ownerWindow = new JWindow(topFrame);
        
        basePlayer = new BasePlayer(this);
        this.getContentPane().add( basePlayer );
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                //System.out.println( "mouseExited" + evt.getX() + " : " + evt.getY() );
            }
        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                //System.out.println( "mouseMoved" + evt.getX() + " : " + evt.getY() );
            }
        });
    }
    
    
    //------------------------------------------------------------------
    public final Window getOwnerWindow() {
        return ownerWindow;
    }
    public final boolean isApplet() {
        return (true);
    }
    public final void destroy() {
        basePlayer.destroy();
    }
    public final void iconify() {
    }
    public final void shade() {
    }
    
    
    //------------------------------------------------------------------
}