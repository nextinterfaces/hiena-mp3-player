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
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;


/**
 * <pre>
 * <b>AtaProgressSlider</b> is a custom Slider with mouse, key and focus support.
 * </pre>
 *
 * @author
 *
 */



public final class XSlider extends java.awt.Panel /*javax.swing.JPanel*/ {
    
    
    //private final static    Color   FOCUS_COLOR = new Color(0x9999CC);
    private Color   progressColor = new Color(0xAEAEFF);
    private boolean   showProgress = true;
    private Rectangle progressRectangle = new Rectangle(0, 2);
    
    private     int             DOT_WIDTH  ;
    private     int             DOT_HEIGHT ;
    private     int             CANVAS_WIDTH ;
    private     int             CANVAS_HEIGHT ;
    
    //private int         percent = 0;
    
    private int         eX, eY;
    private int         eXprevios/*, eYprevios*/;
    private int         dotX = progressRectangle.x, dotY = progressRectangle.y;
    private boolean     mouseEnter = false;
    private boolean     mouseUp = true;
    private boolean     keyPressed = false;
    private boolean     draggable = false;
    
    //private     boolean         hasFocus;
    private boolean     focusable = true;
    //----------
    
    // Event Source Management
    private final Vector          actionListeners;
    private String          label;
    //-----------
    
    private Image       imgOff;
    private Image       dot;
    private Image       dot_;
    private Image       bgSlider;
    private Graphics    gOff;
    
    //-------------------------------------------------------
    public XSlider(String label, String[] url) {
        this.label = label;
        initComponents();
        // Initialize Event Source Management
        actionListeners = new Vector();
        
        if(url.length < 3) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 3 images are needed for XSlider." +
            "\n -- Usage:" +
            "\n -- \t String[] url = {" +
            "\n -- \t\t \"/img_arrow.gif\"" +
            "\n -- \t\t \"/img_arrow_OVER.gif\"" +  
            "\n -- \t\t \"/img_slider_BACKGROUND.gif\"" + 
            "\n -- \t }" +
            "\n ------------------------------"
            );
        }
        setImages( url[0], url[1], url[2]);
    }
    
    
    //-------------------------------------------------------
    public XSlider(String label, String[] url, Rectangle progressRectangle) {
        this( label, url);
        this.progressRectangle = progressRectangle;
        
        final double medium = progressRectangle.y + (double)progressRectangle.height/2.0;
        dotY = (int)(medium - (double)DOT_HEIGHT/2.0);
        dotX = progressRectangle.x;
    }
    
    
    //-------------------------------------------------------
    public XSlider(String label, URL[] url) {
        
        this.label = label;
        initComponents();
        // Initialize Event Source Management
        actionListeners = new Vector();
        
        if(url.length < 3) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 3 images are needed for XSlider." +
            "\n -- Usage:" +
            "\n -- \t URL[] url = {" +
            "\n -- \t\t url_img_arrow" +
            "\n -- \t\t url_img_arrow_OVER" +  
            "\n -- \t\t url_img_slider_BACKGROUND" + 
            "\n -- \t }" +
            "\n ------------------------------"
            );
        }
        setImages( url[0], url[1], url[2]);
    }
    
    //-------------------------------------------------------
    public XSlider(String label, URL[] url, Rectangle progressRectangle) {
        this( label, url);
        this.progressRectangle = progressRectangle;

        final double medium = progressRectangle.y + (double)progressRectangle.height/2.0;
        dotY = (int)(medium - (double)DOT_HEIGHT/2.0);
        dotX = progressRectangle.x;
    }
    
    
    
    
    //-----------------------------------------------
    private final void setImages(URL urlDot, URL urlDot_, URL urlBg) {
        
        final MediaTracker md = new MediaTracker(this);
        final Toolkit toolkit = getToolkit();
        dot    = toolkit.createImage( urlDot  );
        dot_    = toolkit.createImage( urlDot_ );
        bgSlider    = toolkit.createImage( urlBg );
        
        md.addImage(dot, 0);
        md.addImage(dot_, 1);
        md.addImage(bgSlider, 2);
        try {
            md.waitForAll();
            if (md.isErrorAny()) {  System.out.println("Error loading image ");            }
        } catch (Exception ex) { ex.printStackTrace(); }
        
        CANVAS_WIDTH  = bgSlider.getWidth(this);
        CANVAS_HEIGHT = bgSlider.getHeight(this);
        
        DOT_WIDTH  = dot.getWidth(this);
        DOT_HEIGHT = dot.getHeight(this);
        
        setSize( getPreferredSize());
    }
    
    
    //-----------------------------------------------
    private final void setImages(String urlDot, String urlDot_, String urlBg) {
        
        setImages(
        getClass().getResource(urlDot),
        getClass().getResource(urlDot_),
        getClass().getResource(urlBg)
        );
    }
    
    
    //-------------------------------------------------------
    private final void initComponents() {
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                doMouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                doMouseExited();
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                doMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                doMouseReleased();
            }
        });
        
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                doMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                doMouseDragged(evt);
            }
        });
        
        
        this.addFocusListener( new java.awt.event.FocusAdapter(){
            public void focusGained(java.awt.event.FocusEvent evt) {
                doFocusGained();
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doFocusLost();
            }
        });
        
        
        this.addKeyListener( new java.awt.event.KeyAdapter(){
            public void keyPressed(java.awt.event.KeyEvent evt) {
                doKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                doKeyReleased();
            }
        });
        
        
    }
    
    //-------------------------------------------------------
    public final void updateSlider(){
        if(mouseEnter){
            update( getGraphics() );
        }
    }
    
    //-------------------------------------------------------
    public final void update(Graphics g) {
        gOff = null;
        imgOff = null;
        
        // create the offscreen buffer and associated Graphics
        imgOff = createImage( this.CANVAS_WIDTH, this.CANVAS_HEIGHT );
        gOff = imgOff.getGraphics();
        
        // clear the exposed area
        gOff.setColor(getBackground());
        gOff.fillRect(0,0, this.CANVAS_WIDTH, this.CANVAS_HEIGHT);
        gOff.setColor(getForeground());
        
        // do normal redraw
        paint(gOff);
        
        // transfer offscreen to window
        g.drawImage(imgOff, 0, 0, this);
        imgOff.flush();
    }
    
    //-------------------------------------------------------
    public final void paint(Graphics g){
        
        g.drawImage( bgSlider, 0, 0, this);
        drawProgress(g);
        drawDot(g);
        
        // Focus Management
        /*if ( hasFocus && focusable ) {
            g.setColor(FOCUS_COLOR);
            g.drawRect( 0, 0, CANVAS_WIDTH - BORDER, CANVAS_HEIGHT - BORDER );
        }*/
        g.dispose();
    }
    
    
    //-------------------------------------------------------
    private final void drawProgress(Graphics g ){
        if(showProgress){
            g.setColor( progressColor );
            final int posX = (dotX - progressRectangle.x);
            g.fillRect( progressRectangle.x, progressRectangle.y, posX, progressRectangle.height );
        }
    }
    
    //-------------------------------------------------------
    private final void drawDot(Graphics g ){
        if( insideDot(eX,eY) || draggable ) {
            g.drawImage( dot_, dotX, dotY, this);
        }
        else {
            g.drawImage( dot, dotX, dotY, this);
        }
        
        
        if(!mouseEnter)
            g.drawImage( dot, dotX, dotY, this);
        
        if( keyPressed) {
            g.drawImage( dot_, dotX, dotY, this);
        }
    }
    
    //-------------------------------------------------------
    public final int getPercent(){

        final int slider_length = CANVAS_WIDTH - 2*progressRectangle.x - DOT_WIDTH;
        final int slider_pos = dotX - progressRectangle.x;
        int x =  (int)Math.round( slider_pos*100/slider_length);
        if(x >= 0 && x <= 100) {
            return x;
        }
        else {
            x = ( x < 0 )? 0 : x;
            x = ( x > 100 )? 100 : x;
            return x;
        }
        
    }
    
    //-------------------------------------------------------
    public final void setProgressColor(Color c) {
        this.progressColor = c;
    }
    
    //-------------------------------------------------------
    // Set the progress line to be visible
    public final void setShowProgress( boolean showProgress) {
        this.showProgress = showProgress;
    }
    
    //-------------------------------------------------------
    public final void setPercent(int percent){
        dotX = (percent*(CANVAS_WIDTH - progressRectangle.x ))/100 - DOT_WIDTH;
        repaint();
    }
    
    //-------------------------------------------------------
    final void doKeyPressed(java.awt.event.KeyEvent evt) {
        
        keyPressed = true;
        
        // Key Model Management
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_UP ) {
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
            if( dotX < CANVAS_WIDTH - DOT_WIDTH - progressRectangle.x  ){
                eX ++;
                dotX ++;
                repaint();
            }
        }
        else if (evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
            if( dotX > progressRectangle.x  ){
                eX --;
                dotX --;
                repaint();
            }
        }
    }
    
    //-----------------------------------
    final void doKeyReleased() {
        keyPressed = false;
        repaint();
    }
    
    //-------------------------------------------------------
    final void doFocusGained() {
        //hasFocus = true;
        keyPressed = true;
        repaint();
    }
    
    //-----------------------------------
    final void doFocusLost() {
        //hasFocus = false;
        keyPressed = false;
        repaint();
    }
    
    //-------------------------------------------------------
    final void doMouseEntered() {
        mouseEnter = true;
    }
    
    //-----------------------------------
    final void doMouseExited() {
        mouseEnter = false;
        keyPressed = false;
        repaint();
    }
    
    //-----------------------------------
    final void doMouseReleased() {
        mouseUp = true;
        setDraggable( eX, eY );
    }
    
    //-----------------------------------
    final void doMousePressed(java.awt.event.MouseEvent evt) {
        mouseUp = false;
        eX = evt.getX();
        eY = evt.getY();
        setDraggable( eX, eY );
        
        if( insideDraggCanvas(eX) ){
            dotX = eX - (int)Math.round(DOT_WIDTH/2);
            if( dotX > CANVAS_WIDTH - DOT_WIDTH - progressRectangle.x )
                dotX = CANVAS_WIDTH - DOT_WIDTH - progressRectangle.x ;
            else if(dotX < progressRectangle.x)
                dotX = progressRectangle.x ;
            
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
            
            repaint();
        }
    }
    
    //-----------------------------------
    final void doMouseMoved(java.awt.event.MouseEvent evt) {
        eX = evt.getX() ;
        eY = evt.getY() ;
        eXprevios = eX;
        
        setDraggable( eX, eY );
        
        repaint();
    }
    
    //-----------------------------------
    final void doMouseDragged(java.awt.event.MouseEvent evt) {
        eX = evt.getX();
        eY = evt.getY();
        
        int currentDotX = dotX + eX-eXprevios ;
        
        if( currentDotX >= progressRectangle.x  && currentDotX <= ( CANVAS_WIDTH - DOT_WIDTH - progressRectangle.x ) )
            dotX += eX-eXprevios ;
        else if ( currentDotX < progressRectangle.x  )
            dotX = progressRectangle.x ;
        else
            dotX = CANVAS_WIDTH - DOT_WIDTH - progressRectangle.x ;
        
        eXprevios = eX;
        
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
        
        repaint();
        
    }
    
    
    //-------------------------------------------------------
    private final void setDraggable(int x, int y){
        if( insideDot( x, y) && mouseEnter ){
            draggable = true;
        }
        else
            draggable = false;
    }
    
    //-------------------------------------------------------
    private final boolean insideDot(int x, int y){
        return ( x >= dotX  &&  x <= (dotX + DOT_WIDTH) &&
        y > dotY   &&  y <= (dotY + DOT_HEIGHT)
        );
    }
    //-------------------------------------------------------
    /*private final boolean insideCanvas(int x, int y){
        
        return (
        x >= 0  &&  x <= CANVAS_WIDTH   &&
        y >= 0  &&  y <= CANVAS_HEIGHT
        );
    }
    //-------------------------------------------------------
    private final boolean insideDraggCanvas(int x, int y){
        
        return (
        x >= progressRectangle.x   &&  x <= CANVAS_WIDTH - progressRectangle.x    &&
        y >= progressRectangle.x   &&  y <= CANVAS_HEIGHT - progressRectangle.x
        );
    }*/
    
    //-------------------------------------------------------
    private boolean insideDraggCanvas(int x){
        
        return (
        x >= progressRectangle.x   &&  x <= CANVAS_WIDTH - progressRectangle.x
        );
    }
    
    
    //-------------------------------------------------------
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
            final ActionListener li = (ActionListener)en.nextElement();
            li.actionPerformed(ev);
        }
    }
    
    //-------------------------------------------------------
    public final void setLabel(String l) {
        label = l;
        repaint();
    }
    
    public final String getLabel() {
        return label;
    }
    
    //--------------------------------------
    public final boolean isFocusable() {
        return focusable;
    }
    //-------------------------------------------------------
    public final void setFocusable( boolean focusable) {
        this.focusable = focusable;
        super.setFocusable(focusable);
    }
    
    
    //-------------------------------------------------------
    public final Dimension getPreferredSize() {
        return getMinimumSize();
    }
    
    public final Dimension getMinimumSize() {
        return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    }
    
    public final Dimension getMaximumSize() {
        return getMinimumSize();
    }
    
    //-------------------------------------------------------
}


