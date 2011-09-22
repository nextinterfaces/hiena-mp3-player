package org.roussev.hiena;

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

import java.util.ResourceBundle;


public final class Constants {
           
    public static final   int DIRECTORY_MAXDEPTH = 4;
    public static final   String[] EXTENTIONS = { "mp3", "mp2", "mp1", "ogg", "wav", "au", "snd" };
     
    public static final   int   STICK_NOT = 0;
    public static final   int   STICK_UP = 1;
    public static final   int   STICK_DOWN = 2;
    
    public static final   int   ALPHA = 100;
    public static final int     PL_WIDTH    = 300;
    public static final int     PL_HEIGHT   = 180;
    public static final int     URL_WIDTH    = 300;
    public static final int     URL_HEIGHT   = 29;
    public static final int     BORDER   = 10;
    public static final int     WIDTH    = 300;
    public static final int     HEIGHT   = 80;
    public static final int     SHADE_HEIGHT   = 21;
	public static final int     TASKBAR_HEIGHT   = 31;
	public static final int     MIN_VISIBLE   = 3;
    
    public static final   String             EVT_PLAY    = "EVT_PLAY";
    public static final   String             EVT_STOP    = "EVT_STOP";
    public static final   String             EVT_ICONIFY = "EVT_ICONIFY";
	public static final   String             EVT_CLOSE   = "EVT_CLOSE";
	public static final   String             EVT_PL_SHADE   = "EVT_PL_SHADE";
    public static final   String             EVT_PL_CLOSE   = "EVT_PL_CLOSE";
    
	public static final ResourceBundle bundle = ResourceBundle.getBundle("resources.hiena");
    
    //---------------------------------------
    public static String get(String key){
        return bundle.getString(key);
    }
    
    //----------------------------------------
}




