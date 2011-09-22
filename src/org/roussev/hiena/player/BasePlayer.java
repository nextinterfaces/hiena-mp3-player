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

import java.io.*;

import javax.sound.sampled.*;

import org.roussev.hiena.*;
import org.roussev.hiena.util.*;
import org.roussev.hiena.sound.*;


public final class BasePlayer extends BaseArea implements ABasicPlayerListener, FadeThreadListener {
     
    private     ABasicPlayer      theSoundPlayer = null;
    private     FadeThread        fadeThread = null;
    
    private     static final int                 maxGain	= 100;
    private     boolean             pausePressed = false;
    
    private     String  totalTime = "  :  ";
    private     int     bitRate = 0;
    private     long    fileSize;
    private     int     lengthInSeconds = 0;
    private     int     playedSeconds   = 0;
    private     boolean fade_out_started = false;
    
    
    //----------------------------------------------------------------
    public BasePlayer(MainListener mainListener) {
        super(mainListener);
        
        theSoundPlayer = new ABasicPlayer(this,-1);
        theSoundPlayer.setApplet(mainListener.isApplet());
        fadeThread = new FadeThread(this);
    }
    //-----------------------------------------------------------------
    public final void doFading(double step){
        if( step > 1) step = 1;
        theSoundPlayer.setGain( step );
    }
    
    
    //--------------------- RESUME -------------------------------------------
    private final synchronized void _play() {
        
        theSoundPlayer.stopPlayback();
        
        ///-------
        try {
            theSoundPlayer.setDataSource( playlist.getSource() );
        } 
        catch( UnsupportedAudioFileException e){
            setInfo("Audio format not supported");
            return;  
        } 
        catch( LineUnavailableException e){
            setInfo("File Reading Error");
            return;
        }
        catch( IOException e){
            setInfo("Stream Error");
            return;
        }
        
        // Status messages
        setKhz( "" );
        setKbps( "" );
        setInfo( "" );
        setTime("  |  ");
        getProgressSlider().setSliderPercent(0);
        getProgressSlider().setProgressPercent(0);
        //------------
        
        getPlay().setPressed(true);
        getPause().setPressed(false);
        try {
            theSoundPlayer.startPlayback();
            //gainActionEvent();
            fadeThread.start( FadeThread.FADE_IN, getGainSlider().getPercent()/100.0 );
        } catch (Exception e) {
            setInfo("Audio format not supported.");
            try {
                Thread.sleep(50);
                getPlay().setPressed(false);
            } catch(InterruptedException ignore) {}
        }
    }
    
    
    
    //--------------------- PLAY -------------------------------------------
    private final synchronized void _prepareInfo()  {
        
        bitRate  =  theSoundPlayer.getBitRate();
        
        if(theSoundPlayer.getAudioFileFormat() == null ) {
            getPlay().setPressed(false);
            return;
        }
        
        //---- size bytes
        fileSize = theSoundPlayer.getAudioFileFormat().getByteLength();
        
        //---- length seconds
        lengthInSeconds =  (int)theSoundPlayer.getTotalLengthInSeconds();
        
        //---- totalTime
        totalTime = Utils.getMinutes( (double)lengthInSeconds) + ":" + Utils.getSeconds((double)lengthInSeconds );
        
        final float SR = (theSoundPlayer.getAudioFormat()).getSampleRate();
        final int kh = Math.round( SR / 1000 );
        final int br = Math.round(  bitRate / 1000 );
        setKhz( "" + kh );
        setKbps( "" + br );
        setInfo( playlist.getData().getSelectedInfo() + " [" + totalTime + "]" );
    }
    
    
    //--------------------- RESUME -------------------------------------------
    private final synchronized void _resume() {
        
        getPause().setPressed(false);
        theSoundPlayer.resumePlayback();
        fadeThread.start( FadeThread.FADE_IN, getGainSlider().getPercent()/100.0 );
    }
    
    
    //--------------------- PAUSE -------------------------------------------
    private final synchronized void _pause() {
        
        getPlay().setPressed(false);
        theSoundPlayer.pausePlayback();
    }
    
    
    
    //--------------------- PLAY -------------------------------------------
    protected final synchronized void playActionEvent() {
        
        if(!pausePressed) {
            if( playlist.getData().getSize() > 0) {
                _play();
                _prepareInfo();
            } else {
                // playlist is empty. release play button.
                try {
                    Thread.sleep(50);
                    getPlay().setPressed(false);
                } catch(InterruptedException ignore) {}
            }
        }
        else {
            _resume();
        }
        
        pausePressed = false;
    }
    
    //--------------------- PAUSE -------------------------------------------
    protected final synchronized void pauseActionEvent() {
        
        if( theSoundPlayer.getStatus() == ABasicPlayer.PLAYING ) {
            _pause();
            pausePressed = true;
        }
        else {
            try {
                Thread.sleep(50);
                getPause().setPressed(false);
            } catch(InterruptedException ignore) {}
        }
    }
    
    //--------------------- STOP -------------------------------------------
    protected final synchronized void stopActionEvent() {
        
        //-----
        getPlay().setPressed(false);
        getPause().setPressed(false);
        
        //-----
        theSoundPlayer.stopPlayback();
        
        // Status messages
        setKhz( "" );
        setKbps( "" );
        setInfo( "" );
        setTime("  |  ");
        getProgressSlider().setSliderPercent(0);
        getProgressSlider().setProgressPercent(0);
    }
    
    
    //----------------------------------------------------------------------
    protected final void gainActionEvent() {
        final int x = getGainSlider().getPercent();
        if ( x == 0) {
            theSoundPlayer.setGain( 0 );
        } else {
            final double volume = (double)x / (double)maxGain ;
            theSoundPlayer.setGain( volume );
        }
    }
    
    /*protected final void panActionEvent() {
        int x = getPanSlider().getPercent();
        x -= 50;
        x *= 2;
        final float balance = (float)x / (float)100;
        theSoundPlayer.setPan( balance );
    }*/
    
    
    //------------------------------------------------------
    protected final synchronized void prevActionEvent() {
        
        playlist.goPrev();
        if( theSoundPlayer.getStatus() == org.roussev.hiena.sound.ABasicPlayer.PLAYING) {
            playActionEvent();
        }
        else if( theSoundPlayer.getStatus() == ABasicPlayer.PAUSED ) {
            this.pausePressed = false;
            playActionEvent();
        }
    }
    
    //------------------------------------------------------
    protected final synchronized void nextActionEvent() {
        
        playlist.goNext();
        if( theSoundPlayer.getStatus() == ABasicPlayer.PLAYING) {
            playActionEvent();
        }
        else if( theSoundPlayer.getStatus() == ABasicPlayer.PAUSED ) {
            this.pausePressed = false;
            playActionEvent();
        }
    }
    
    
    
    
    //-----------------------------------------------------
    public final void updateMediaCursor( int secondsAmount ) {
        
        if( secondsAmount > lengthInSeconds){
            secondsAmount = lengthInSeconds;
        }
        
        //---- set Slider --------
        if( playedSeconds != secondsAmount){
            playedSeconds = secondsAmount ;
            int percent = (int)Math.round( ((double)playedSeconds * 100.0 ) / (double)lengthInSeconds );
            if( percent > 100 )
                percent = 100;
            
            getProgressSlider().setProgressPercent(percent);
            getProgressSlider().setSliderPercent(percent);
             
            final String playedTime = Utils.getMinutes(playedSeconds) + ":" + Utils.getSeconds(playedSeconds);
            setTime( playedTime );
        }
        
        //----- fade out ----
        if( playedSeconds + 1 == lengthInSeconds){
            if( !fade_out_started ){
                fade_out_started = true;
                fadeThread.start( FadeThread.FADE_OUT, getGainSlider().getPercent()/100.0 );
            }
        }
    }
    
    //-----------------------------------------------------
    /**
     * End Of Media reached
     */
    public final void updateMediaState(String state) {
        if( lengthInSeconds == playedSeconds ){
            playlist.goNext();
            playActionEvent();
        }
        else if(playedSeconds < lengthInSeconds) {
            //Outil.p( "::::: EOM >> unexpected END <<<< ");
        } else {
            Utils.p( ">>> updateMediaState unkown state ?! -> " + state);
        }
        fade_out_started = false;
    }
    
    
    
    
    
    //----------------------------------------------------------------------
    public final void destroy() {
        stopActionEvent();
        new FileObject(FileObject.PLAYLIST, null).setObject( playlist.getData());
    }
    
    
    //----------------------------------------------------------------------
}



