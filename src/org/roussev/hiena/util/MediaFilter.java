package org.roussev.hiena.util;

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

import java.util.Hashtable;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public final class MediaFilter extends FileFilter {

    private Hashtable filters = null;
    
    public MediaFilter() {
        this.filters = new Hashtable();
    }
    
    //---------------------------------------------
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        final String ext = Utils.getExtension(f);
        if (ext != null) {            
            if (
                ext.equals("mp3") 
            ||  ext.equals("mp2")
            ||  ext.equals("mp1")
            ||  ext.equals("ogg")
            ||  ext.equals("wav")
            ||  ext.equals("au")
            ||  ext.equals("snd")
            //||  ext.equals("aif")
            //||  ext.equals("mid")
            //||  ext.equals("midi")
            ) {
                    return true;
            } 
            else {
                return false;
            }
        }

        return false;
    }

    //---------------------------------------------
    public String getDescription() {
        return "Media files(*.mp3, *.ogg, *.wav, *.au, *.snd)";
        //return "*.m3u; *.wsz; *.mpg; *.snd; *.aifc; *.aif; *.wav; *.au; *.mp1; *.mp2; *.mp3; *.ogg";
        //return "*.mp3, *.au, *.wav";
    }
    
    //---------------------------------------------
}
