package org.roussev.hiena.player;

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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.MediaTracker;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;

import org.roussev.hiena.gui.*;
import org.roussev.hiena.*;
import org.roussev.hiena.util.*;



class AreaPanel extends AbsArea {
    
    //---------------------------------------------------------------------
    protected AreaPanel(boolean isApplet) {
        //super(null,true);        
        setLayout(null);
        setSize( getPreferredSize());
        
        this.isApplet = isApplet;
        
        initFont();
        initBG();
        drawControlers();
    }
    
    
    
    //-----------------------------------------------------------------------
    private final void initFont(){
        info_width = 8;
        info_height = 10;
        info_space = 0;
        letter_index = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\"@    0123456789_.:()-'!_+\\/[]^&%,=$#    ?*";

        time_width = 10;
        time_height = 14;
        time_space = 0;
        time_index = "0123456789-: |";  
        kbps_index = "0123456789()_ ";
        
        kbps_width = 5;
        kbps_height = 7;
        kbps_space = 0; 
        
        
        infoImg = getToolkit().createImage( Utils.getResource("font.letters.big"));
        timeImg = getToolkit().createImage( Utils.getResource("font.numbers.big"));
        kbpsImg = getToolkit().createImage( Utils.getResource("font.numbers.kbps"));
        
        final MediaTracker mt = new MediaTracker(this);
        mt.addImage(infoImg, 0);
        mt.addImage(timeImg, 1);
        mt.addImage(kbpsImg, 2);
        try {
            mt.waitForAll();
        }   catch(Exception exc) { exc.printStackTrace(); }
        if(!mt.isErrorAny());
        
        //---------------
        info = (new FontBuilder(
                letter_index,
        infoImg,
        info_width,
        info_height,
        info_space, "                                  " )
        ).getLabel();        
        this.add(info);
        info.setLocation( info_location[0], info_location[1]);
        info.setSize( info.getPreferredSize() );
        
        //---------------
        time = (new FontBuilder(time_index, timeImg, time_width, time_height, time_space, "  |  " )).getLabel();
        this.add(time);
        time.setLocation( time_location[0], time_location[1]);
        time.setSize( time.getPreferredSize() );
        
        //---------------
        kbps = (new FontBuilder(kbps_index, kbpsImg, kbps_width, kbps_height, kbps_space, "     " )).getLabel();
        this.add(kbps);
        kbps.setLocation( kbps_location[0], kbps_location[1]);
        kbps.setSize( kbps.getPreferredSize() );
        kbps.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        
        //---------------
        khz = (new FontBuilder(kbps_index, kbpsImg, kbps_width, kbps_height, kbps_space, "    " )).getLabel();
        this.add(khz);
        khz.setLocation( khz_location[0], khz_location[1]);
        khz.setSize( khz.getPreferredSize() );
        khz.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    }
    
    
    //------------------------------------------------------------------------
    protected final void setInfo(String txt){
        info.setIcon(
        new ImageIcon(
        (new FontBuilder(letter_index, infoImg, info_width, info_height, info_space, txt.toUpperCase()  )).getBanner()
        )
        );
    }
    //------------------------------------------------------------------------
    protected final void setTime(String txt){
        time.setIcon(
        new ImageIcon(
        (new FontBuilder(time_index, timeImg, time_width, time_height, time_space, txt  )).getBanner()
        )
        );
    }
    
    //------------------------------------------------------------------------
    protected final void setKbps(String txt){        
        txt = ("".equals(txt))? "" : "("+txt.toUpperCase()+")" ;            
        kbps.setIcon(
        new ImageIcon(
        (new FontBuilder(kbps_index, kbpsImg, kbps_width, kbps_height, kbps_space, txt)).getBanner()
        )
        );
    }
    
    //------------------------------------------------------------------------
    protected final void setKhz(String txt){
        txt = ("".equals(txt))? "" : "("+txt.toUpperCase()+")" ;
        khz.setIcon(
        new ImageIcon(
        (new FontBuilder(kbps_index, kbpsImg, kbps_width, kbps_height, kbps_space, txt)).getBanner()
        )
        );
    }
    
    
    //------------------------------------------------------------------------
    private final void drawControlers() {
        
        //----
        progressSlider = new XProgressSlider(
        "progress",
        new URL[]{
            Utils.getResource( "slider.progress.dot"),
            Utils.getResource( "slider.progress.dot"),
            Utils.getResource( "slider.progress")
        },
        true,
        new Rectangle(6,3, -1, 3)
        );
        progressSlider.setProgressColor( new Color(0xDEEAF3) );
        
        
        //----
        gainSlider = new XSlider(
        "gain",
        new URL[]{
            Utils.getResource( "slider.gain.dot"),
            Utils.getResource( "slider.gain.dot_"),
            Utils.getResource( "slider.gain")
        }, 
        new Rectangle(2,5, -1, 1)
        );
        gainSlider.setPercent(70);
        gainSlider.setShowProgress(false);
        
        //----
        play = new XTransitionButton(
        "play",
        new String[]{
            Constants.get("play"),
            Constants.get("play_"),
            Constants.get("play.down"),
            Constants.get("play")
        }
        );
        play.setToggle(true);
        play.setReleasable(false);        
        
        //----
        pause = new XTransitionButton(
        "pause",
        new String[]{
            Constants.get("pause"),
            Constants.get("pause_"),
            Constants.get("pause.down"),
            Constants.get("pause")
        }
        );
        pause.setToggle(true);
        pause.setReleasable(false);
        
        
        //----
        stop = new XTransitionButton(
        "stop",
        new String[]{
            Constants.get("stop"),
            Constants.get("stop_"),
            Constants.get("stop.down"),
            Constants.get("stop")
        }
        );        
        
        //----
        prev = new XTransitionButton(
        "prev",
        new String[]{
            Constants.get("prev"),
            Constants.get("prev_"),
            Constants.get("prev.down"),
            Constants.get("prev")
        }
        );
        
        //----
        next = new XTransitionButton(
        "next",
        new String[]{
            Constants.get("next"),
            Constants.get("next_"),
            Constants.get("next.down"),
            Constants.get("next")
        }
        );
        
        //----
        pl = new XTransitionButton(
        "pl",
        new String[]{
            Constants.get("pl"),
            Constants.get("pl_"),
            Constants.get("pl.down"),
            Constants.get("pl")
        }
        );
        
        //----
        eject = new XTransitionButton(
        "eject",
        new String[]{
            Constants.get("eject"),
            Constants.get("eject_"),
            Constants.get("eject.down"),
            Constants.get("eject")
        }
        );
        
        
        //----
        shuffle = new XTransitionButton(
        "shuffle",
        new String[]{
            Constants.get("shuffle"),
            Constants.get("shuffle_"),
            Constants.get("shuffle.down"),
            Constants.get("shuffle")
        }
        );
        shuffle.setToggle(true);
        
        
        //----
        repeat = new XTransitionButton(
        "repeat",
        new String[]{
            Constants.get("repeat"),
            Constants.get("repeat_"),
            Constants.get("repeat.down"),
            Constants.get("repeat")
        }
        );
        repeat.setToggle(true);
        
        
        //--------------
        close = new XTransitionButton(
        "close",
        new String[]{
            Constants.get("close"),
            Constants.get("close_"),
            Constants.get("close.down"),
            Constants.get("close")
        }
        );
        close.setSize(close.getPreferredSize());
        
        minimize = new XTransitionButton(
        "minimize",
        new String[]{
            Constants.get("min"),
            Constants.get("min_"),
            Constants.get("min.down"),
            Constants.get("min")
        }
        );
        minimize.setSize(minimize.getPreferredSize());
        
        shade = new XTransitionButton(
        "shade",
        new String[]{
            Constants.get("shade"),
            Constants.get("shade_"),
            Constants.get("shade.down"),
            Constants.get("shade")
        }
        );
        shade.setSize(shade.getPreferredSize());
        
        //----------------
        
        
        //----
        add(progressSlider);
        add(gainSlider);
        add(play);
        add(pause);
        add(stop);
        add(prev);
        add(next);
        add(pl);
        add(eject);
        add(shuffle);
        add(repeat);
        if( !isApplet) {
            add(close);
            add(minimize);
            add(shade);
        }
        
        progressSlider.setLocation( progressSlider_location[0], progressSlider_location[1]);
        play.setLocation(       play_location[0],   play_location[1]);
        pause.setLocation(      pause_location[0],  pause_location[1]);
        stop.setLocation(       stop_location[0],   stop_location[1]);
        prev.setLocation(       prev_location[0],   prev_location[1]);
        next.setLocation(       next_location[0],   next_location[1]);
        pl.setLocation(         pl_location[0],         pl_location[1]);
        eject.setLocation(      eject_location[0],      eject_location[1]);
        shuffle.setLocation(    shuffle_location[0],    shuffle_location[1]);
        repeat.setLocation(     repeat_location[0],     repeat_location[1]);
        gainSlider.setLocation( gainSlider_location[0], gainSlider_location[1]);
        close.setLocation(      close_location[0],      close_location[1]);
        minimize.setLocation(   minimize_location[0],   minimize_location[1]);
        shade.setLocation(      shade_location[0],      shade_location[1]);
    }
    
    
    //---------------------------------------------------------------------
    private final void initBG() {
                 
        final Image mainImage = Utils.getIcon("panel.main").getImage();
        final int[]    $1$                = {0, 0, 103, 80};
        final int[]    $2$                = {103, 0, 143, 32};
        final int[]    $3$                = {246, 0, 30, 28};
        final int[]    $4$                = {276, 8, 24, 20};        
        final int[]    $5$                = {103, 72, 19, 8};
        final int[]    $6$                = {103, 48, 19, 6};        
        final int[]    $7$                = {103, 44, 143, 4};        
        final int[]    $8$                = {211, 44, 35, 10};        
        final int[]    $9$                = {229, 44, 17, 17};        
        final int[]    $10$               = {246, 48, 54, 13};        
        final int[]    $11$               = {283, 61, 17, 19};        
        final int[]    $12$               = {103, 77, 197, 3};        
        final int[]    $13$               = {211, 72, 18, 5};
        final int[]    $14$               = {287, 28, 13, 20};
          
        //-------------
        final JLabel label_1 = new JLabel( new CropImage( mainImage, $1$).getIcon() );
        label_1.setBounds($1$[0],$1$[1], $1$[2], $1$[3]);
        add( label_1 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_2 = new JLabel( new CropImage( mainImage, $2$).getIcon() );
        label_2.setBounds( $2$[0], $2$[1], $2$[2], $2$[3]);
        add( label_2 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_3 = new JLabel( new CropImage( mainImage, $3$).getIcon() );
        label_3.setBounds( $3$[0], $3$[1], $3$[2], $3$[3]);
        add( label_3 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_4 = new JLabel( new CropImage( mainImage, $4$).getIcon() );
        label_4.setBounds( $4$[0], $4$[1], $4$[2], $4$[3]);
        add( label_4 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_5 = new JLabel( new CropImage( mainImage, $5$).getIcon() );
        label_5.setBounds( $5$[0], $5$[1], $5$[2], $5$[3]);
        add( label_5 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_6 = new JLabel( new CropImage( mainImage, $6$).getIcon() );
        label_6.setBounds( $6$[0], $6$[1], $6$[2], $6$[3]);
        add( label_6 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_7 = new JLabel( new CropImage( mainImage, $7$).getIcon() );
        label_7.setBounds( $7$[0], $7$[1], $7$[2], $7$[3]);
        add( label_7 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_8 = new JLabel( new CropImage( mainImage, $8$).getIcon() );
        label_8.setBounds( $8$[0], $8$[1], $8$[2], $8$[3]);
        add( label_8 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_9 = new JLabel( new CropImage( mainImage, $9$).getIcon() );
        label_9.setBounds( $9$[0], $9$[1], $9$[2], $9$[3]);
        add( label_9 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_10 = new JLabel( new CropImage( mainImage, $10$).getIcon() );
        label_10.setBounds( $10$[0], $10$[1], $10$[2], $10$[3]);
        add( label_10 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_11 = new JLabel( new CropImage( mainImage, $11$).getIcon() );
        label_11.setBounds( $11$[0], $11$[1], $11$[2], $11$[3]);
        add( label_11 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_12 = new JLabel( new CropImage( mainImage, $12$).getIcon() );
        label_12.setBounds( $12$[0], $12$[1], $12$[2], $12$[3]);
        add( label_12 , new Integer(Integer.MIN_VALUE) );
        //----
        final JLabel label_13 = new JLabel( new CropImage( mainImage, $13$).getIcon() );
        label_13.setBounds( $13$[0], $13$[1], $13$[2], $13$[3]);
        add( label_13 , new Integer(Integer.MIN_VALUE) );
        
       
        //----
        final JLabel label_14 = new JLabel( new CropImage( mainImage, $14$).getIcon() );
        label_14.setBounds( $14$[0], $14$[1], $14$[2], $14$[3]);
        add( label_14 , new Integer(Integer.MIN_VALUE) );
        
         /* 
        final int[]    bg_1_location                = {0, 0};          
        final ImageIcon  bg_1 = Utils.getIcon("bg.1");
        final JLabel label_1 = new JLabel( bg_1 );
        label_1.setBounds(0,0, bg_1.getIconWidth(), bg_1.getIconHeight());
        add( label_1 , new Integer(Integer.MIN_VALUE) );  
        */
    }
    
    
    
    //--------------------------------------
    protected final XTransitionButton getPlay() {        return play;    }
    protected final XTransitionButton getPause() {       return pause;    }
    protected final XTransitionButton getStop() {        return stop;    }
    protected final XTransitionButton getPrev() {        return prev;    }
    protected final XTransitionButton getNext() {        return next;    }
    protected final XTransitionButton getPl() {          return pl;    }
    protected final XTransitionButton getEject() {       return eject;    }
    protected final XTransitionButton getShuffle() {     return shuffle;   }
    protected final XTransitionButton getRepeat() {      return repeat;   }
    protected final XTransitionButton getClose() {       return close;   }
    protected final XTransitionButton getMinimize() {    return minimize;   }
    protected final XTransitionButton getShade() {       return shade;   }
    
    protected final XProgressSlider getProgressSlider() {       return progressSlider;   }
    protected final XSlider getGainSlider() {       return gainSlider;    }
    //---------------------------------------------------------------------
    
    
    
    //---------------------------------------------------------------------
    public final Dimension getPreferredSize() {
        return new Dimension( Constants.WIDTH, Constants.HEIGHT);
    }
    
    
    
    
    //---------------------------------------------------------------------    
    private    XTransitionButton          play;
    private    XTransitionButton          pause;
    private    XTransitionButton          stop;
    private    XTransitionButton          prev;
    private    XTransitionButton          next;
    private    XTransitionButton          pl;
    private    XTransitionButton          eject;
    private    XTransitionButton          shuffle;
    private    XTransitionButton          repeat;
    private    XTransitionButton          close;
    private    XTransitionButton          minimize;
    private    XTransitionButton          shade;
    
    private    XProgressSlider  progressSlider;
    private    XSlider          gainSlider;
    
    //-------------------------------------------------------------------------
    private static int              info_width;
    private static int              info_height;
    private static int              info_space;
    private static String           letter_index;
    
    private static int              time_width;
    private static int              time_height;
    private static int              time_space;
    private static String           time_index;
    
    private static int              kbps_width;
    private static int              kbps_height;
    private static int              kbps_space;
    private static String           kbps_index;
        
    private         JLabel          info;
    private         JLabel          time;
    private         JLabel          kbps;
    private         JLabel          khz;
    private         Image           infoImg;
    private         Image           timeImg;
    private         Image           kbpsImg;
    
	private final boolean isApplet;
    
    private static final int[]    info_location       = { 15, 16};
    private static final int[]    kbps_location          = { 32, 34};
    private static final int[]    khz_location           = { 77, 34};
    private static final int[]    time_location          = { 15, 49};
    
    private static final int[]    play_location               = {152, 48};
    private static final int[]    pause_location              = {122, 48};
    private static final int[]    stop_location               = {182, 48};
    private static final int[]    prev_location               = {103, 54};
    private static final int[]    next_location               = {211, 54};
    private static final int[]    pl_location                 = {267, 28};
    private static final int[]    eject_location              = {246, 28};
    private static final int[]    shuffle_location            = {103, 32};
    private static final int[]    repeat_location             = {140, 32};
    private static final int[]    close_location              = {290, 0};
    private static final int[]    shade_location              = {283, 0};
    private static final int[]    minimize_location           = {276, 0};
    
    private static final int[]    progressSlider_location     = {176, 32};
    private static final int[]    gainSlider_location         = {229, 61};

    
    
    
    //-------------------------------------------------------------
}




