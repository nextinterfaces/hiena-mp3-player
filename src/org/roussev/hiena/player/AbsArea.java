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

import org.roussev.hiena.gui.*;

abstract class AbsArea extends javax.swing.JComponent {
    
    //-----------------------------------------------
    protected abstract  XTransitionButton getPlay();
    protected abstract  XTransitionButton getPause();
    protected abstract  XTransitionButton getStop();
    protected abstract  XTransitionButton getPrev();
    protected abstract  XTransitionButton getNext();
    protected abstract  XTransitionButton getPl();
    protected abstract  XTransitionButton getEject();
    protected abstract  XTransitionButton getShuffle();
    protected abstract  XTransitionButton getRepeat();
    protected abstract  XTransitionButton getClose();
    protected abstract  XTransitionButton getMinimize();
    protected abstract  XTransitionButton getShade();
    
    //-----------------------------------------------
    protected XProgressSlider getProgressSlider() {       return null; }
    protected XSlider getGainSlider() {       return null; }
    
    
    //----------------------------------------------- 
    protected void setInfo(String txt){}
    protected void setTime(String txt){}
    protected void setKbps(String txt){}
    protected void setKhz(String txt){}
    
}
