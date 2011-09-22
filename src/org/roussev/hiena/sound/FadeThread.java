package org.roussev.hiena.sound;

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


public final class FadeThread implements Runnable {
    
    
    //-----------------------------------------------------------------------
    public FadeThread(FadeThreadListener listener) {
        this.listener = listener;
    }
    
    
    //----------------------------------------------------------------------
    public final void start(int f_status, double fadeLimit) {
        
        if ((m_status == STOPPED) || (m_status == READY)) {
            interval = ( f_status==FADE_IN )? INTERVAL_IN :INTERVAL_OUT ;
            //Outil.p( " ..] Start called [.. " );
            fadeStep = fadeLimit / ((double)interval/(double)STEP);
            this.f_status = f_status;
            this.fadeLimit = fadeLimit;
            fadeGain = 0.0;
            
            m_thread = new Thread(this, "FadeThread" );
            //Outil.p( " ..] Created new thread [.. " + m_thread.getName() );
            m_thread.start();
        }
    }
    
    //----------------------------------------------------------------------
    ////////////////////////////////////////////////////////////
    public final void stop() {
        
        if ( m_status == RUNNING ) {
            
            m_status = STOPPED;
            fadeGain = 0.0;
            cnt = 0;
            //Outil.p( " ..] Stop called [.. " + m_thread.getName() );
        }
    }
    
    
    
    
    //----------------------------------------------------------------------
    ////////////////////////////////////////////////////////////
    /**
     * Main loop.
     *
     * Player Status == STOPPED => End of Thread + Freeing Ressources.<br>
     * Player Status == RUNNING => Thread is Running <br>
     */
    int cnt;
    public final void run() {
        
        //Outil.p( " ..] Thread Running [.. " + m_thread.getName() );
        
        m_status = RUNNING;
        
        while ( m_status != STOPPED ) {
            
            if ( m_status == RUNNING ) {
                
                try{
                    cnt += STEP;
                    if( f_status == FADE_IN) {
                        fadeGain += fadeStep;
                    } else {                        
                        fadeGain = fadeLimit - fadeStep;
                        fadeLimit = fadeGain;
                    }
                    //Outil.p( " ..] cnt++ [.. " + cnt );
                    Thread.sleep( STEP );
                } catch ( InterruptedException x ) {}
                
                ///////////////////////
                if ( cnt <= interval ) {
                    listener.doFading( fadeGain );
                }
                else {
                    stop();
                }
                ///////////////////////
            }
        }
        
        
        //Outil.p( " ..]-- EOT-- " + m_thread.getName() + "  Stopped [.. " );
        m_status = READY;
        // End Of Thread
    }
    
    
    
    
    
    
    //----------------------------------------------------------------------
    private final static int		STEP = 10;
    private final static int		INTERVAL_IN = 2000;
    private final static int		INTERVAL_OUT = 1000;
    
    public final static int		FADE_IN = 0;
    public final static int		FADE_OUT = 1;
    
    private final static int		RUNNING = 0;
    private final static int		STOPPED = 1;
    private final static int		READY   = 2;
    private int                         m_status = READY;
    
    private final FadeThreadListener	listener;
    private Thread		m_thread = null;
    private double              fadeStep;
    private double              fadeGain;
    private double              fadeLimit;
    private int                 f_status;
    private int                 interval;
    
    
    
    
    
    //-----------------------------------------------------------------------
}


