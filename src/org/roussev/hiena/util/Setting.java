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


public final class Setting implements java.io.Serializable{
    
    //-------------------------------------------
    public Setting() {
    }
    
    //-------------------------------------------
    private String mp3Folder = "/";
    public void setMp3Folder(String x) {
        mp3Folder = x;
    }
    public String getMp3Folder() {
        return mp3Folder;
    }
    
    
    
    //-------------------------------------------
}