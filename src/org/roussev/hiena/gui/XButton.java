package org.roussev.hiena.gui;

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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;


/**
 * <pre>
 * <b>AtaButton</b> is a custom button with focus, mouse and key support.
 * </pre>
 *
 * @author	Atanas Roussev
 *
 */


public final class XButton extends Panel implements FocusListener, MouseListener, KeyListener {
    
    
    protected boolean         pressed;
    
    //private final static    Color   FOCUS_COLOR = new Color(0x9999CC);
    private String          label;
    
    // Event Source Management
    private final Vector          actionListeners;
    private boolean         mouseOver;
    //private boolean         hasFocus;
    private boolean         focusable = true;
    private boolean         _enabled;
    private boolean         doActionWhenPressed = false;
    
    private Image           imgOff;
    private Image           bg;
    private Image           btn;
    private Image           btn_over;
    private Image           btn_down;
    private Graphics        gOff;
    
    private int             width;
    private int             height;
    
    
    //--------------------------------------
    private XButton(String label) {
        
        this.label = label;
        pressed = false;
        _enabled = true;
        
        // Initialize Event Source Management
        actionListeners = new Vector();
        
        // Initialize Focus Management
        //hasFocus = false;
        this.addFocusListener(this);
        
        // Initialize Mouse Input Management
        mouseOver = false;
        this.addMouseListener(this);
        
        // Initialize Keyboard Input Management
        this.addKeyListener(this);
        
    }
    
    
    //--------------------------------------
    public XButton(String label, String[] url) {
        
        this(label);
        
        if(url.length < 4) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 4 images are needed for XButton." +
            "\n -- Usage:" +
            "\n -- \t String[] url = {" +
            "\n -- \t\t \"/img.gif\"" +
            "\n -- \t\t \"/img_OVER.gif\"" + 
            "\n -- \t\t \"/img_PRESSED.gif\"" +  
            "\n -- \t\t \"/img_BACKGROUND.gif\"" + 
            "\n -- \t }" +
            "\n ------------------------------"
            );
        }
        
        btn         = this.getToolkit().createImage(   this.getClass().getResource(url[0])  );
        btn_over    = this.getToolkit().createImage(   this.getClass().getResource(url[1]) );
        btn_down    = this.getToolkit().createImage(   this.getClass().getResource(url[2]) );
        bg          = this.getToolkit().createImage(   this.getClass().getResource(url[3]) );
        
        MediaTracker mt = new MediaTracker( this );
        mt.addImage(   btn, 0);
        mt.addImage(   btn_over, 1);
        mt.addImage(   btn_down, 2);
        mt.addImage(   bg, 3);
        try {
            mt.waitForAll();
            if (mt.isErrorAny()) {
                System.out.println("Error loading image ");
                repaint();
            }
        } catch (InterruptedException ie) {}
        
        width  = btn.getWidth(this);
        height = btn.getHeight(this);
        
        setSize( getPreferredSize());
    }
    
    
    //--------------------------------------
    public XButton(String label, URL[] url) {
        
        this(label);
        
        if(url.length < 4) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 4 images are needed for XButton." +
            "\n -- Usage:" +
            "\n -- \t URL[] url = {" +
            "\n -- \t\t url_img" +
            "\n -- \t\t url_img_OVER" + 
            "\n -- \t\t url_img_PRESSED" +  
            "\n -- \t\t url_img_BACKGROUND" + 
            "\n -- \t }" +
            "\n ------------------------------"
            );
        }
        
        btn         = this.getToolkit().createImage( url[0] );
        btn_over    = this.getToolkit().createImage( url[1] );
        btn_down    = this.getToolkit().createImage( url[2] );
        bg          = this.getToolkit().createImage( url[3] );
        
        MediaTracker mt = new MediaTracker( this );
        mt.addImage(   btn, 0);
        mt.addImage(   btn_over, 1);
        mt.addImage(   btn_down, 2);
        mt.addImage(   bg, 3); 
        try {
            mt.waitForAll();
            if (mt.isErrorAny()) {
                System.out.println("Error loading image ");
                repaint();
            }
        } catch (InterruptedException ie) {}
        
        width  = btn.getWidth(this);
        height = btn.getHeight(this);
        
        setSize( getPreferredSize());
    }
    
    
    //-------------------------------------------------------
    public final void update(Graphics g) {
        
        gOff = null;
        imgOff = null;
        
        // create the offscreen buffer and associated Graphics
        imgOff = createImage( width, height );
        gOff = imgOff.getGraphics();
        
        // clear the exposed area
        gOff.setColor(getBackground());
        gOff.fillRect(0,0, width, height);
        gOff.setColor(getForeground());
        
        // do normal redraw
        paint(gOff);
        
        // transfer offscreen to window
        g.drawImage(imgOff, 0, 0, this);
        imgOff.flush();
    }
    
    //-------------------------------------------------------
    public final void paint(Graphics g){
        draw(g);
    }
    
    
    //--------------------------------------
    public final void draw(Graphics g) {
        
        g.drawImage( bg, 0, 0, this);
        
        // Rollover
        if (mouseOver) {
            g.drawImage( btn_over, 0, 0, this);
        } else {
            g.drawImage( btn, 0, 0, this);
        }
        
        
        //g.setColor(FOCUS_COLOR);
        // MouseDown Management
        if (pressed) {
            g.drawImage( btn_down, 0, 0, this);
            //if(focusViewable)g.drawRect( 0, 0, width-2, height-2 );
        }
        
        
        if(!is_Enabled()){
            // Rollover
            if (mouseOver) {
                g.drawImage( btn_over, 0, 0, this);
            } else {
                g.drawImage( btn, 0, 0, this);
            }
        }
        
        // Focus Management
        //if (hasFocus && focusViewable) {
        //    g.drawRect( 0, 0, width-2, height-2 );
        //}
        
        g.dispose();
    }
    
    
    
    
    
    //--------------------------------------
    public final void setDoActionWhenPressed(boolean x) {
        this.doActionWhenPressed = x;
    }
    
    //--------------------------------------
    public final void setPressed(boolean pressed) {
        this.pressed = pressed;
        repaint();
    }
    public final boolean isPressed() {
        return pressed;
    }
    
    //--------------------------------------
    public final void set_Enabled(boolean _enabled) {
        this._enabled = _enabled;
    }
    public final boolean is_Enabled() {
        return _enabled;
    }
    
    
    
    
    
    //--------------------------------------
    public final void addActionListener(ActionListener li) {
        actionListeners.addElement(li);
    }
    
    //--------------------------------------
    public final void removeActionListener(ActionListener li) {
        actionListeners.removeElement(li);
    }
    
    //--------------------------------------
    protected final void fireActionPerformed(ActionEvent ev) {
        Enumeration en = actionListeners.elements();
        while ( en.hasMoreElements() ) {
            ActionListener li = (ActionListener)en.nextElement();
            li.actionPerformed(ev);
        }
    }
    
    
    
    //--------------------------------------
    public final boolean isFocusable() {
        return focusable;
    }
    public final void setFocusable(boolean focusable) {
        this.focusable = focusable;
        super.setFocusable(focusable);
    }
    
    
    
    //--------------------------------------
    public final Dimension getPreferredSize() {
        return getMinimumSize();
    }
    
    public final Dimension getMinimumSize() {
        return new Dimension(width, height);
    }
    
    public final Dimension getMaximumSize() {
        return getMinimumSize();
    }
    
    //--------------------------------------
    public final void setLabel(String l) {
        label = l;
        repaint();
    }
    
    //--------------------------------------
    public final String getLabel() {
        return label;
    }
    
    
    
    //--------------------------------------
    public final void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        requestFocus();
        pressed = true;
        repaint();
        if(doActionWhenPressed){
            if(is_Enabled()){
                fireActionPerformed( new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label) );
            }
        }
    }
    
    public final void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        pressed = false;
        repaint();
        if(!doActionWhenPressed){
            if(is_Enabled()){
                fireActionPerformed( new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label) );
            }
        }
    }
    
    public final void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        // When pressed and moved does not get the ActionEvent
        //if(is_Enabled()){
        //    fireActionPerformed( new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label) );
        //}
    }
    
    public final void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
        mouseOver = true;
        repaint();
    }
    
    public final void mouseExited(java.awt.event.MouseEvent mouseEvent) {
        mouseOver = false;
        repaint();
    }
    
    //--------------------------------------
    public final void focusGained(java.awt.event.FocusEvent focusEvent) {
        //hasFocus = true;
        pressed = true;
        repaint();
    }
    
    public final void focusLost(java.awt.event.FocusEvent focusEvent) {
        //hasFocus = false;
        pressed = false;
        repaint();
    }
    
    
    
    
    //--------------------------------------
    public final void keyReleased(java.awt.event.KeyEvent ev) {
        // Model Management
        if (ev.getKeyCode() == KeyEvent.VK_SPACE ) {
            if(is_Enabled()){
                fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
            }
            pressed = false;
            mouseOver = false;
            repaint();
        }
    }
    
    public final void keyPressed(java.awt.event.KeyEvent ev) {
        // Model Management
        if (ev.getKeyCode() == KeyEvent.VK_SPACE || ev.getKeyCode() == KeyEvent.VK_ENTER ) {
            pressed = true;
            mouseOver = true;
            repaint();
        }
    }
    
    public final void keyTyped(java.awt.event.KeyEvent keyEvent) {
    }
    
    //--------------------------------------
}















