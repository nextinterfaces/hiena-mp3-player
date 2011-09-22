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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.roussev.hiena.*;
import org.roussev.hiena.util.*;
import org.roussev.hiena.gui.*;


final class URLWindow extends JWindow {
    
        
    private int                 XDrag = 0, YDrag = 0;
    private int                 OrigineX = 0, OrigineY = 0;
    private int                 XPressed = 0, YPressed = 0;
    private Dimension           screenSize;
    
    private final JPanel   panel;
    private JTextField urlField;
    private URL url = null;
    
    //private final static int[]    minimize_location           = {332, 4};
    //private final static int[]    close_location              = {345, 4};
    
    
    //---------------------------------------------------
    protected URLWindow(final Window owner) {
        super(owner);
        //topFrame.setResizable(false);
        this.setSize(Constants.URL_WIDTH, Constants.URL_HEIGHT );
        this.centerWindow();
        
        //--------------
        panel = new JPanel();
        initBG();
        
        //----------------
        this.getContentPane().setLayout(null);
        panel.setBounds(0,0, Constants.URL_WIDTH, Constants.URL_HEIGHT );
        this.getContentPane().add(panel);
        
        initMouseListeners();
        //topFrame.setLocation(-200,-200);
        //topFrame.setVisible(true);
        this.setVisible(true);
    }
    
    
    //---------------------------------------------------------------------
    private void initBG() {
        
        panel.setLayout(null);
        //-------------
        final ImageIcon  bg_1 = Utils.getIcon("url.1");
        final JLabel label_1 = new JLabel( bg_1 );
        label_1.setBounds(0,0, 360, 4);
        panel.add( label_1 , new Integer(Integer.MIN_VALUE) );
        
        //-------------
        final ImageIcon  bg_2 = Utils.getIcon("url.2");
        final JLabel label_2 = new JLabel( bg_2 );
        label_2.setBounds(0,4, 4, Constants.URL_HEIGHT-4);
        panel.add( label_2 , new Integer(Integer.MIN_VALUE) );
        
        //-------------
        final ImageIcon  bg_3 = Utils.getIcon("url.3");
        final JLabel label_3 = new JLabel( bg_3 );
        label_3.setBounds(0,Constants.URL_HEIGHT-4, 360, 4);
        panel.add( label_3 , new Integer(Integer.MIN_VALUE) );
        
        //-------------
        final ImageIcon  bg_4 = Utils.getIcon("url.4");
        final JLabel label_4 = new JLabel( bg_4 );
        label_4.setBounds(269,4, 91, 22);
        panel.add( label_4 , new Integer(Integer.MIN_VALUE) );
        
        //-------------
        urlField = new JTextField();
        urlField.setFont(new java.awt.Font("Dialog", 0, 12));
        urlField.setBackground( Utils.toColor("bg.white"));
        panel.add( urlField );
        urlField.setBounds( 4, 4, 265, 21 );
        
        
        //--------------
        XButton close = new XButton(
        "close",
        new String[]{
            Constants.get("close"),
            Constants.get("close_"),
            Constants.get("close.down"),
            Constants.get("btn.bg")
        }
        );
        close.setSize(close.getPreferredSize());
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                notifyPropertyChangeListeners("exit");
                dispose();
            }
        });
        
        
        //--------------
        XButton add = new XButton(
        "add",
        new String[]{
            Constants.get("url.add"),
            Constants.get("url.add_"),
            Constants.get("url.add.down"),
            Constants.get("btn.bg")
        }
        );
        add.setSize(add.getPreferredSize());
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                doAction();
            }
        });
        
        urlField.addKeyListener( new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doAction();
                }
            }
        });
        
        panel.add(add);
        panel.add(close);
        add.setLocation( 272, 5);
        close.setLocation( 347, 2);
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
    private final void centerWindow(){
        
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
        
        this.setLocation( OrigineX-50, OrigineY );
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
        OrigineX = - XPressed + OrigineX + DeltaX ;
        OrigineY = - YPressed + OrigineY + DeltaY ;
        
        
        // Keep player window in screen
        if (OrigineX < 0) OrigineX = 0;
        if (OrigineY < 0) OrigineY = 0;
        if (screenSize.width != -1) {
            if (OrigineX > screenSize.width-Constants.URL_WIDTH) OrigineX = screenSize.width-Constants.URL_WIDTH;
        }
        if (screenSize.height != -1) {
            if (OrigineY > screenSize.height-Constants.URL_HEIGHT-10) OrigineY = screenSize.height-Constants.URL_HEIGHT-10;
        }
        
        
        setLocation( OrigineX, OrigineY) ;
    }
    
    //--------------------------------------------
    final void doAction() {
        try {
            url = new URL( this.urlField.getText());
            notifyPropertyChangeListeners("add");
        } catch(java.net.MalformedURLException me) {
            /*
            final JOptionPane pane = new JOptionPane( Constants.get("err.url"), JOptionPane.INFORMATION_MESSAGE );
            final JDialog dialog = pane.createDialog(this, Constants.get("err.url"));
            dialog.show();
             */
            notifyPropertyChangeListeners("exit");
        }
        dispose();
    }
    
    
    //--------------------------------------------
    public final URL getURL() {
        return url;
    }
    
    
    /*-------------------------------------------------------------------*/
    /*---             notify PropertyChangeListeners                   --*/
    /*-------------------------------------------------------------------*/
    private final Vector listeners = new Vector();
    public synchronized final void addPropertyChangeListener(PropertyChangeListener l) {
        super.addPropertyChangeListener(l);
        //if( listeners != null ) {
        if (!listeners.contains(l)) {
            listeners.addElement(l);
            Utils.p( " --- URLWindow ADD " + listeners);
        }
    }
    
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (listeners.contains(l)) {
            listeners.removeElement(l);
            Utils.p( " --- URLWindow REMOVE " + listeners);
        }
    }
    
    final void notifyPropertyChangeListeners(String property) {
        /*final int size = listeners.size();
        PropertyChangeListener listener;
        for(int i=0; i < size ; ++i) {
            listener = (PropertyChangeListener)listeners.get(i);
            listener.propertyChange( new PropertyChangeEvent( this, property, null, null ));
        }*/
        
        //////--------------
        PropertyChangeEvent pce = new PropertyChangeEvent( this, property, null, null );
        Vector listenerCopy;
        synchronized (this) {
            listenerCopy = (Vector) listeners.clone();
        }
        
        for (int i = 0; i < listeners.size(); ++i) {
            PropertyChangeListener pcl = (PropertyChangeListener) listenerCopy.elementAt(i);
            pcl.propertyChange(pce);
        }
        
        //////--------------
    }
    
    
    //--------------------------------------------------------------
}
