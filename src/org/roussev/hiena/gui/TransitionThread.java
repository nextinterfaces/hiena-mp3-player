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

final class TransitionThread implements Runnable {
    
    private final static int STEP = 30;
    private final TransitionListener listener;
    private Thread thread;
    
    //-----------------------------------------------------------
    public TransitionThread(TransitionListener listener) {
        this.listener = listener;
    }
    
    //-----------------------------------------------------------
    public boolean isInterrupted() {
        return (thread == null);
    }
        
    //-----------------------------------------------------------
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    } 
    
    
    //-----------------------------------------------------------
    public synchronized void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
        notifyAll();
    }
    
    //-----------------------------------------------------------
    public void run() {    	
    	
        Thread me = Thread.currentThread();        
        
        while (thread == me && !listener.isShowing() || listener.getSize().width == 0 ) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) { }
        }
        
        while (thread == me) {
            listener.repaint();
            try { 
                Thread.sleep(STEP);
            } catch (InterruptedException e) { }
        }
        thread = null;
    }
    
    //-----------------------------------------------------------
}
