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
import java.awt.Rectangle;
import java.awt.Color;
import java.net.URL;

import org.roussev.hiena.gui.*;
import org.roussev.hiena.*;
import org.roussev.hiena.util.*;



class AreaShadePanel extends AbsArea {
    
    //---------------------------------------------------------------------
    protected AreaShadePanel(boolean isApplet) {
        //super(null,true);        
        setLayout(null);
        setSize( getPreferredSize());
        
        this.isApplet = isApplet;
                  
        drawControlers();        
    }
      
         
    
    //------------------------------------------------------------------------
    private void drawControlers() {
        
        //----
        progressSlider = new XProgressSlider(
        "progress",
        new URL[]{
            Utils.getResource( "trans"),
            Utils.getResource( "trans"),
            Utils.getResource( "trans") // ?!
        },
        false,
        new Rectangle(0,6, -1, 1)
        );
        progressSlider.setProgressColor( Utils.toColor("color.border.line").darker() );
        
        //----
        gainSlider = new XSlider(
        "gain",
        new URL[]{
            Utils.getResource( "shade.slider.dot"),
            Utils.getResource( "shade.slider.dot_"),
            Utils.getResource( "shade.slider.gain")
        },
        new Rectangle(2,7, -1, 1)
        );
        gainSlider.setPercent(70);
        gainSlider.setProgressColor( new Color( 0xDEDEEB));        
        
        
        //----
        play = new XTransitionButton(
        "play",
        new String[]{
            Constants.get("shade.play"),
            Constants.get("shade.play_"),
            Constants.get("shade.play.down"),
            Constants.get("shade.play")
        }
        );
        play.setToggle(true);
        play.setReleasable(false);        
        
        //----
        pause = new XTransitionButton(
        "pause",
        new String[]{
            Constants.get("shade.pause"),
            Constants.get("shade.pause_"),
            Constants.get("shade.pause.down"),
            Constants.get("shade.pause")
        }
        );
        pause.setToggle(true);
        pause.setReleasable(false);
        
        
        //----
        stop = new XTransitionButton(
        "stop",
        new String[]{
            Constants.get("shade.stop"),
            Constants.get("shade.stop_"),
            Constants.get("shade.stop.down"),
            Constants.get("shade.stop")
        }
        );        
        
        //----
        prev = new XTransitionButton(
        "prev",
        new String[]{
            Constants.get("shade.prev"),
            Constants.get("shade.prev_"),
            Constants.get("shade.prev.down"),
            Constants.get("shade.prev")
        }
        );
        
        //----
        next = new XTransitionButton(
        "next",
        new String[]{
            Constants.get("shade.next"),
            Constants.get("shade.next_"),
            Constants.get("shade.next.down"),
            Constants.get("shade.next")
        }
        );
        
        //----
        pl = new XTransitionButton(
        "pl",
        new String[]{
            Constants.get("shade.pl"),
            Constants.get("shade.pl_"),
            Constants.get("shade.pl.down"),
            Constants.get("shade.pl")
        }
        );
        
        //----
        eject = new XTransitionButton(
        "eject",
        new String[]{
            Constants.get("shade.eject"),
            Constants.get("shade.eject_"),
            Constants.get("shade.eject.down"),
            Constants.get("shade.eject")
        }
        );
        
        
        //----
        shuffle = new XTransitionButton(
        "shuffle",
        new String[]{
            Constants.get("trans"),
            Constants.get("trans"),
            Constants.get("trans"),
            Constants.get("trans")
        }
        );
        shuffle.setToggle(true);
        
        
        //----
        repeat = new XTransitionButton(
        "repeat",
        new String[]{
            Constants.get("trans"),
            Constants.get("trans"),
            Constants.get("trans"),
            Constants.get("trans")
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
            Constants.get("shade.shade"),
            Constants.get("shade.shade_"),
            Constants.get("shade.shade.down"),
            Constants.get("shade.shade")
        }
        );
        shade.setSize(shade.getPreferredSize());
        
        //----------------
        //add(progressSlider);
        add(gainSlider);
        add(play);
        add(pause);
        add(stop);
        add(prev);
        add(next);
        add(pl);
        add(eject);
        //add(shuffle);
        //add(repeat);
        if( !isApplet) {
            add(close);
            add(minimize);
            add(shade);
        }
        
        //----------------
        play.setLocation(       play_location[0],   play_location[1]);
        pause.setLocation(      pause_location[0],  pause_location[1]);
        stop.setLocation(       stop_location[0],   stop_location[1]);
        prev.setLocation(       prev_location[0],   prev_location[1]);
        next.setLocation(       next_location[0],   next_location[1]);
        pl.setLocation(         pl_location[0],         pl_location[1]);
        eject.setLocation(      eject_location[0],      eject_location[1]);
        //shuffle.setLocation(    shuffle_location[0],    shuffle_location[1]);
        //repeat.setLocation(     repeat_location[0],     repeat_location[1]);
        //progressSlider.setLocation( progressSlider_location[0], progressSlider_location[1]);
        gainSlider.setLocation( gainSlider_location[0], gainSlider_location[1]);
        close.setLocation(      close_location[0],      close_location[1]);
        minimize.setLocation(   minimize_location[0],   minimize_location[1]);
        shade.setLocation(      shade_location[0],      shade_location[1]);
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
        return new Dimension(Constants.WIDTH, Constants.SHADE_HEIGHT);
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
    private static final int[]    prev_location               = {10, 0};
    private static final int[]    pause_location              = {30, 0};
    private static final int[]    play_location               = {50, 0};
    private static final int[]    stop_location               = {70, 0};
    private static final int[]    next_location               = {90, 0};
    
    //private static final int[]    shuffle_location            = {150, 0};
    //private static final int[]    repeat_location             = {160, 0};    
    private static final int[]    gainSlider_location         = {130, 0};
    
    private static final int[]    eject_location              = {200, 0};
    private static final int[]    pl_location                 = {240, 0};
    private static final int[]    close_location              = {341, 0};
    private static final int[]    shade_location              = {329,0};
    private static final int[]    minimize_location           = {315,0};
    
    //private static final int[]    progressSlider_location     = {144, 0};
    
    private final boolean isApplet;
    
    
    //-------------------------------------------------------------
}




