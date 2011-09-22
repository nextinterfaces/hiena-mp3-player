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

import java.util.Enumeration;
import java.util.Vector;
import java.awt.MediaTracker;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.AlphaComposite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.net.URL;
//--


public final class XTransitionButton extends javax.swing.JPanel implements FocusListener, MouseListener, TransitionListener, KeyListener {
    
    private static final int UP = 0;
    private static final int DOWN = 1;
    int position = UP;
    
    private boolean focusable = true;
    boolean releasable = true;
    private boolean _enabled = true;
    boolean toggle;
    int toggleActions = 0;
    private boolean firstInit = true;
    boolean         mouseOver;
    boolean         pressed;
    boolean         enableFireActionPerformed = true;
    
    // Event Source Management
    private final Vector          actionListeners;
    
    final String       label;
    //--
    private Image           bg;
    private Image           btn;
    //--
    private int             width;
    private int             height;
    
    private ImageData       iData;
    private ImagePressData        iDataPress;
    final TransitionThread   animator;
    final TransitionThread   animatorPress;
    
    //--------------------------------------
    private XTransitionButton(String label) {
        //
        setDoubleBuffered(true);
        this.label = label;
        
        // Initialize Event Source Management
        actionListeners = new Vector();
        
        // Initialize Mouse Input Management
        this.addMouseListener(this);
        
        // Initialize Focus Management
        this.addFocusListener(this);
        
        // Initialize Keyboard Input Management
        this.addKeyListener(this);
        
        animator = new TransitionThread(this);
        animatorPress = new TransitionThread(this);
    }
    
    //-----------------------------------------------
    public XTransitionButton(String label, String[] url) {
        
        this(label);
        if(url.length < 4) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 4 images are needed for XTransitionButton." +
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
        setImages(url[0], url[1], url[2], url[3]);
    }
    
    //-----------------------------------------------
    public XTransitionButton(String label, URL[] url) {
        
        this(label);
        if(url.length < 4) {
            throw new ArrayIndexOutOfBoundsException(
            "\n ------------------------------" +
            "\n -- Error loading images. " +
            "\n -- 4 images are needed for XTransitionButton." +
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
        setImages(url[0], url[1], url[2], url[3]);
    }
    
    
    
    //-----------------------------------------------
    private final void setImages(URL url, URL url_over, URL url_down, URL url_bg) {
        //--- url = "resources/name.gif" ---
        //final URLClassLoader urlLoader = (URLClassLoader)getClass().getClassLoader();
        //final Image img = toolkit.createImage(urlLoader.findResource(url));
        
        final MediaTracker md = new MediaTracker(this);
        final Toolkit toolkit = getToolkit();
        btn         = toolkit.createImage( url);
        final Image btn_over    = toolkit.createImage( url_over );
        final Image btn_down    = toolkit.createImage( url_down );
        bg          = toolkit.createImage( url_bg );
        
        md.addImage(btn, 0);
        md.addImage(btn_over, 1);
        md.addImage(btn_down, 2);
        md.addImage(bg, 3);
        try {
            md.waitForAll();
            if (md.isErrorAny()) {  System.out.println("Error loading image ");            }
        } catch (Exception ex) { ex.printStackTrace(); }
        
        width  = btn.getWidth(this);
        height = btn.getHeight(this);
        setSize( getPreferredSize());
        
        iData = new ImageData( btn_over);
        iDataPress = new ImagePressData( btn_down);
    }
    
    
    
    //-----------------------------------------------
    private final void setImages(String url, String url_over, String url_down, String url_bg) {
        setImages(
        getClass().getResource(url),
        getClass().getResource(url_over),
        getClass().getResource(url_down),
        getClass().getResource(url_bg)
        );
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
    protected final void fireActionPerformed(ActionEvent evt) {
        final Enumeration e = actionListeners.elements();
        while ( e.hasMoreElements() ) {
            final ActionListener li = (ActionListener)e.nextElement();
            li.actionPerformed(evt);
        }
    }
    //-----------------------------------------------
    public final Dimension getMinimumSize(){
        return getPreferredSize();
    }
    public final Dimension getMaximumSize(){
        return getPreferredSize();
    }
    public final Dimension getPreferredSize(){
        return new Dimension(btn.getWidth(this), btn.getHeight(this));
    }
    //--------------------------------------
    public final boolean isFocusable() {
        return focusable;
    }
    
    public final void setFocusable(boolean focusable) {
        this.focusable = focusable;
        super.setFocusable(focusable);
    }
    
    
    
    
    
    
    //-----------------------------------------------------------
    public final Graphics2D createGraphics2D(/*int width,  int height,*/ Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        //g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,  RenderingHints.VALUE_RENDER_SPEED);
        //g2.clearRect(0, 0, width, height);
        
        return g2;
    }
    
    //-----------------------------------------------------------
    private final void drawAlpha(ImageData id, Graphics2D g2) {
        final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, id.alpha);
        g2.setComposite(ac);
        //g2.setPaint( id.paint);
        //g2.translate( id.x, id.y);
        g2.drawImage( id.image, 0, 0, this);
        //g2.translate( -id.x, -id.y);
    }
    
    //-----------------------------------------------------------
    private final void drawAlphaPress(ImagePressData id, Graphics2D g2) {
        final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, id.alpha);
        g2.setComposite(ac);
        //g2.setPaint( id.paint);
        //g2.translate( id.x, id.y);
        g2.drawImage( id.image, 0, 0, this);
        //g2.translate( -id.x, -id.y);
    }
    
    //-----------------------------------------------------------
    public final void paint(Graphics g) {
        
        //final Dimension d = getSize();
        
        if (firstInit) {
            repaint();
            firstInit = false;
        }
        
        if ( !animatorPress.isInterrupted()) {
            iDataPress.step();
        }
        else if ( !animator.isInterrupted()) {
            iData.step();
        }
        
        final Graphics2D g2 = createGraphics2D(/*d.width, d.height,*/ g);
        g2.drawImage( bg, 0, 0, this);
        g2.drawImage( btn, 0, 0, this);
        drawAlpha(iData, g2);
        drawAlphaPress(iDataPress, g2);
        g2.dispose();
    }
    
    
    
    //--------------------------------------
    public final void set_Enabled(boolean _enabled) {
        this._enabled = _enabled;
    }
    public final boolean is_Enabled() {
        return _enabled;
    }
    public final void setReleasable(boolean x) {
        this.releasable = x;
        if(!releasable) {
            setToggle(true);
        }
    }
    public final void setToggle(boolean x) {
        this.toggle = x;
    }
    public final void setPressed(boolean boo) {
        if(!boo) {
            if( toggle && pressed) {
                animatorPress.start();
                animator.start();
            }
        }
        else {
            if( !pressed) {
                pressAlphaDirection = ON;
                position = UP;
                pressed = true;
                enableFireActionPerformed = false;
                animatorPress.start();
            }
        }
    }
    
    
    //-----------------------------------------------------------
    public final void mouseClicked(java.awt.event.MouseEvent e) {
    }
    public final void mouseEntered(java.awt.event.MouseEvent e) {
        mouseOver = true;
        if(!pressed) {
            animator.start();
        }
    }
    public final void mouseExited(java.awt.event.MouseEvent e) {
        mouseOver = false;
        if(!pressed) {
            animator.start();
        }
    }
    //---
    public final void mousePressed(java.awt.event.MouseEvent e) {
        requestFocus();
        if(_enabled) {
            if(releasable) {
                pressAlphaDirection = ON;
                position = UP;
                pressed = true;
                enableFireActionPerformed = true;
                animatorPress.start();
            }
            else if(position == UP ) {
                pressed = true;
                enableFireActionPerformed = true;
                animatorPress.start();
            }
        }
    }
    public final void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    //--------------------------------------
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        mouseEntered(null);
    }
    
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        mouseExited(null);
    }
    
    //--------------------------------------
    
    public void keyPressed(java.awt.event.KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_SPACE || ev.getKeyCode() == KeyEvent.VK_ENTER ) {
            mousePressed(null);
        }
    }
    
    public void keyReleased(java.awt.event.KeyEvent ev) {
    }
    public void keyTyped(java.awt.event.KeyEvent keyEvent) {
    }
    
    //--------------------------------------
    
    
    
    //==================================================================
    int     pressAlphaDirection = ON;
    private static final int ON = 0;
    private static final int OFF = 1;
    
    private final class ImagePressData {
        
        private static final double STEP_DOWN = 0.15;
        private static final double STEP_UP = 0.1;
        
        private static final float ALPHA_MIN = 0.0f;
        private static final float ALPHA_MAX = 1.0f;
        final   Image image;
        float   alpha = ALPHA_MIN;
        
        //---------------------------------
        public ImagePressData(Image image) {
            this.image = image;
        }
        
        //---------------------------------
        public final void reset() {
            alpha = ALPHA_MIN;
            pressAlphaDirection = ON;
        }
        
        //---------------------------------
        public final void step() {
            
            //----------- in -- [0.0 -> 1.0]
            if (pressAlphaDirection == ON) {
                if ((alpha += STEP_DOWN) >= ALPHA_MAX ) {
                    pressAlphaDirection = OFF;
                    alpha = ALPHA_MAX;
                }
                if(alpha == ALPHA_MAX && pressed) {
                    if(!releasable) {
                        animatorPress.stop();
                        position = DOWN;
                    } else if(toggle) {
                        if( toggleActions%2==0 ) {
                            animatorPress.stop();
                            position = DOWN;
                        }
                        else {
                            setPressed(false);
                        }
                        toggleActions++;
                    }
                    if(enableFireActionPerformed) {
                        fireActionPerformed( new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label) );
                    }
                } else {
                    animatorPress.start();
                }
                
                if(!pressed) {
                    pressAlphaDirection = OFF;
                    pressed = false;
                }
            }
            //----------- out -- [1.0 -> 0.0]
            else if (pressAlphaDirection == OFF) {
                if ((alpha -= STEP_UP) <= ALPHA_MIN) {
                    pressAlphaDirection = ON;
                    alpha = ALPHA_MIN;
                }
                
                if(alpha == ALPHA_MIN && !pressed) {
                    animatorPress.stop();
                    position = UP;
                } else {
                    animatorPress.start();
                }
                
                if(pressed) {
                    pressAlphaDirection = ON;
                    pressed = false;
                }
            }
        } //-- end of step
        
    }//----- end of ImagePressData
    
    
    
    
    
    
    
    //==================================================================
    private final class ImageData {
        
        private static final double STEP = 0.1;
        
        private static final float ALPHA_MIN = 0.0f;
        private static final float ALPHA_MAX = 1.0f;
        final   Image image;
        float   alpha = ALPHA_MIN;
        private int     alphaDirection = ON;
        
        //---------------------------------
        public ImageData(Image image) {
            this.image = image;
        }
        
        //---------------------------------
        public final void reset() {
            alpha = ALPHA_MIN;
            alphaDirection = ON;
        }
        
        
        //---------------------------------
        public final void step() {
            //----------- in -- [0.0 -> 1.0]
            if (alphaDirection == ON) {
                if ((alpha += STEP) >= ALPHA_MAX ) {
                    alphaDirection = OFF;
                    alpha = ALPHA_MAX;
                }
                if(alpha == ALPHA_MAX && mouseOver) {
                    animator.stop();
                } else {
                    animator.start();
                }
                
                if(!mouseOver) {
                    alphaDirection = OFF;
                }
            }
            //----------- out -- [1.0 -> 0.0]
            else if (alphaDirection == OFF) {
                if ((alpha -= STEP) <= ALPHA_MIN) {
                    alphaDirection = ON;
                    alpha = ALPHA_MIN;
                }
                
                if(alpha == ALPHA_MIN && !mouseOver) {
                    animator.stop();
                } else {
                    animator.start();
                }
                
                if(mouseOver) {
                    alphaDirection = ON;
                }
            }
        } //-- end of step
        
    }//----- end of ImageData
    
    
    
    
    //---------------------------------------------------------
}

