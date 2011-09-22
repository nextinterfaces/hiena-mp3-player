package org.roussev.hiena.player.playlist;

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

import java.util.List;
import java.util.Vector;

public final class Data implements java.io.Serializable {
    
    private final Vector  sources = new Vector();
    private final Vector  tag_infos = new Vector(){
        public synchronized final Object get(int i) {
			final StringBuffer sb = new StringBuffer();
            sb.append("  ");
            sb.append(i+1);
            sb.append(". ");
            sb.append( super.get(i));
            return sb.toString();
        }
    };
    private int selected = 0;
    
    //----------------------------------
    public Data() {
    }
    
    //----------------------------------
    public final void setSelected(int i) {
        selected = i;
    }
    //----------------------------------
    public final int getSelected() {
        //return (selected < 0) ? 0 : selected;
        return selected;
    }
    //----------------------------------
    public final String getSelectedInfo() {
        
        final String x = (String)tag_infos.get(selected);
        final int pos = x.indexOf('.');
        return x.substring(pos+2);
    }
    //----------------------------------
    public final int getSelectedForPlay() {
        return (selected < 0) ? 0 : selected;
    }
    //----------------------------------
    public final void goNext() {
        if(selected == sources.size()-1)
            selected = 0;
        else
            selected++;
    }
    
    //----------------------------------
    public final Object getSource() {
        return sources.get(getSelectedForPlay());
    }
    //----------------------------------
    public final void remove(int i) {
        sources.remove(i);
        tag_infos.remove(i);
    }
    //----------------------------------
    public final void clear() {
        selected = 0;
        sources.clear();
        tag_infos.clear();
    }
    //----------------------------------
    public final void addSource(Object o) {
        sources.add(o);
    }
    //----------------------------------
    public final void addInfo(Object o) {
        tag_infos.add(o);
    }
    //----------------------------------
    public final List getSources() {
        return sources;
    }
    //----------------------------------
    public final List getTag_infos() {
        return tag_infos;
    }
    //----------------------------------
    public final int getSize() {
        return tag_infos.size();
    }
    
    //----------------------------------
    public void dbg() {
        System.out.println( "selected = " + selected );
        System.out.println( "sources = " + sources );
        System.out.println( "tag_infos = " + tag_infos );
    }
    
    
    
    //----------------------------------
}
