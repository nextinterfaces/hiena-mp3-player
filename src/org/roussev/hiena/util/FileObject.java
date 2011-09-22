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

import java.io.*;

public final class FileObject {
    
    private static final String EXT = "jammmp";
    private String type = "";
    public static final String SETTING = "setting";
    public static final String PLAYLIST = "playlist";
    
    //----------------------------------------------------------------------
    public FileObject(String type, Object obj) {
        this.type = type;
        File f = new File( getPath());
        if(!f.exists()) {
            this.setObject(obj);
        }
    }
    
    //----------------------------------------------------------------------
    public final Object getObject(){
        
        Object obj = null;
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(  getPath() ));
            obj = ois.readObject();
        }
        catch (FileNotFoundException fnfe) {
            //System.out.println("Error: "+fnfe);
        }
        catch (IOException ioe) {
            //System.out.println("Error: "+ioe);
        }
        catch (ClassNotFoundException cnfe) {
            //System.out.println("Error: "+cnfe);
        }
        return obj;
    }
    
    
    //----------------------------------------------------------------------
    public final String getPath(){
        return  (System.getProperty("java.io.tmpdir") + File.separator +
        type + "." + EXT )
        ;
    }
    
    //----------------------------------------------------------------------
    public final void setObject(Object o){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream( getPath() ));
            oos.writeObject(o);
        }
        catch (IOException ioe) {
            System.out.println("Error: "+ioe);
        }
    }
    
    //-----------------------------------------------------------
    /*public static void main(String[] args) throws Exception {
        FileObject t = new FileObject(FileObject.SETTING, null);
         
        t.setObject(new Integer(123123));
        File f = new File( t.getPath());
        System.out.println( f.exists() );
        System.out.println( t.getObject() );
    }*/
    
    
    //-----------------------------------------------------------
}
