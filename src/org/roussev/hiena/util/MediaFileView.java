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

import java.io.File;
import javax.swing.filechooser.FileView;

public final class MediaFileView extends FileView {
    
    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        final String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals("mp3") ) {
                type = "MP3 Media File";
            } 
        }
        return type;
    }

    /*public Icon getIcon(File f) {
        //String ext = Outil.getExtension(f);
        //Icon icon = null;

        //if (f.isDirectory()) {
        //    icon = Outil.getIcon("icon.dir");
        //}
        
        if ( !f.isDirectory() && ext != null) {
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
                icon = Outil.getIcon("icon.small");
            }
        }
        return icon;
    }*/
}

