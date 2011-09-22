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

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Color;

import org.roussev.hiena.*;

public final class Utils {
    
    //--------------------------------------------------------
    public static String filterUnix(String input) {
        
        //final char c = '\f';
        if (input == null) {
            return "";
        } else {
            final StringBuffer filtered = new StringBuffer(input.length());
            final int len = input.length();
            try {
                for (int i = 0; i < len; i++) {
                    char c1 = input.charAt(i);
                    /*if( '\u000C' '\003' '\f' '' \r\t\f\n */
                    if ('\0' == c1) {
                        ;
                    } else {
                        //System.out.println( new Character(c1).hashCode() );
                        filtered.append(c1);
                    }
                }
                //java.lang.Appendable -> 1.5 issue ?!
            } catch (Exception ignore) {}
            return filtered.toString();
        }
    }
    
    //----------------------------------------------------------
    public static boolean isAudioFile(String ff) {
        ff = ff.toLowerCase();
        for (int i = 0; i < Constants.EXTENTIONS.length; i++) {
            if (ff.endsWith(Constants.EXTENTIONS[i]))
                return true;
        }
        return false;
    }
    
    //----------------------------------------------------------
        /*
         * Get the extension of a file.
         */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
    
    //---------------------------------------------------------------
    private Utils() {
    }
    
    //-------------------------------------------------------------
    public static final URL getResource(String key) {
        return Utils.class.getResource(Constants.get(key));
    }
    
    //-------------------------------------------------------------
    public static final javax.swing.ImageIcon getIcon(String key) {
        //new ImageIcon( getClass().getResource( Constants.get("bg.8") ) );
        final java.net.URL imgURL = Utils.class.getResource(Constants.get(key));
        if (imgURL != null) {
            return new javax.swing.ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + Constants.get(key));
            return null;
        }
    }
    
    //-------------------------------------------------------------
    public static final javax.swing.ImageIcon getImageIcon(String path) {
        //new ImageIcon( getClass().getResource( Constants.get("bg.8") ) );
        final java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new javax.swing.ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    //--------------------------------------------------
    public static final String getName(Object source) {
        if (source instanceof File) {
            return ((File) source).getName();
        } else if (source instanceof URL) {
            return ((URL) source).toString();
        } else {
            return source.toString();
        }
    }
    
    //--------------------------------------------------
    public static final String replaceNull(Object param) {
        if (param == null) {
            return "";
        }
        return param.toString().trim();
    }
    
    //--------------------------------------------------
    public static void printThreadInfo() {
        Thread[] all = new Thread[Thread.activeCount()];
        Thread.enumerate(all);
        p(" -- activeCount = " + Thread.activeCount());
        for (int i = 0; i < all.length; i++) {
            if (all[i] != null) {
                if (!all[i].isDaemon()) {
                    Utils.p(
                    " ____________ "
                    + all[i].getName()
                    + " priority="
                    + all[i].getPriority()
                    + "   alive? "
                    + all[i].isAlive());
                    //print( " isDaemon " + all[i].isDaemon());
                    //print( " isInterrupted " + all[i].isInterrupted());
                }
            }
        }
    }
    
    //--------------------------------------------------
    public static final String getMinutes(double seconds) {
                /*
                int minutes = (int)( Math.floor( seconds / 60));
                String result = (minutes<10)?  ""+0+minutes  :  ""+minutes;
                return result;
                 */
        final int minutes = (int) (Math.floor(seconds / 60));
        final StringBuffer sb = new StringBuffer();
        if (minutes >= 10) {
            sb.append(minutes);
        } else {
            sb.append(0);
            sb.append(minutes);
        }
        return sb.toString();
    }
    
    //--------------------------------------------------
    public static final String getSeconds(double seconds) {
        //int sec = (int)( Math.ceil(seconds%60));
        //String result = (sec<10)?  ""+0+sec  : ""+sec;
        
        final int sec = (int) (Math.ceil(seconds % 60));
        final StringBuffer sb = new StringBuffer();
        if (sec >= 10) {
            sb.append(sec);
        } else {
            sb.append(0);
            sb.append(sec);
        }
        return sb.toString();
    }
    
    //-----------------------------------------------------
    public static final Setting getSetting() {
        FileObject fileObject = new FileObject(FileObject.SETTING, null);
        Object obj = fileObject.getObject();
        Setting setting = null;
        if (obj == null) {
            setting = new Setting();
        } else {
            setting = (Setting) obj;
        }
        return setting;
    }
    
    //-----------------------------------------------------
    public static final int search(int[] a, int val) {
        for (int i = a.length - 1; i >= 0; i--) {
            if (a[i] == val)
                return i;
        }
        return -1;
    }
    //-----------------------------------------------------
    public static final boolean isInArray(int[] a, int x) {
        
        return (search(a, x) >= 0);
    }
    
    //-----------------------------------------------------
    /**
     *  returns a value.
     */
    public static final Object getKeyByEntry(Hashtable t, Object value) {
        Object key;
        
        for (Enumeration e = t.keys(); e.hasMoreElements();) {
            key = e.nextElement();
            
            if (value.equals(t.get(key))) {
                
                return key;
            }
        }
        return null;
    }
    
    //-----------------------------------------------------
    public static final int toInt(Object obj) {
        try {
            return Integer.parseInt("" + obj);
        } catch (NumberFormatException ignore) {
        }
        return -1;
    }
    
    //-----------------------------------------------------
    public static final int toInt(String hexKey) {
        try {
            return Integer.decode(Constants.get(hexKey).trim()).intValue();
        } catch (NumberFormatException ignore) {
        }
        return 0;
    }
    
    //-----------------------------------------------------
    public static final Color toColor(String hexKey) {
        try {
            //return
            //new Color(Integer.decode( Constants.get(hexKey).trim() ).intValue());
            return Color.decode(Constants.get(hexKey).trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new Color(0xFFFFFF);
    }
    
    //-----------------------------------------------------
    public static final Color toColor(String hexKey, int alpha) {
        try {
            final Color c = Color.decode(Constants.get(hexKey).trim());
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new Color(0xFFFFFF);
    }
    
    //---------------------------------------------------------------------
    public static void p(String str) {
        System.out.println(str);
    }
    public static void p(byte x) {
        p("" + x);
    }
    public static void p(char x) {
        p("" + x);
    }
    public static void p(int x) {
        p("" + x);
    }
    public static void p(long x) {
        p("" + x);
    }
    public static void p(double x) {
        p("" + x);
    }
    public static void p(float x) {
        p("" + x);
    }
    public static void p(boolean x) {
        p("" + x);
    }
    public static void p(Object obj) {
        p("" + obj);
    }
    public static void p() {
        p("");
    }
    
    //---------------------------------------------------------------------
    public static void verifyImages() {
        ResourceBundle b = Constants.bundle;
        for (Enumeration e = b.getKeys(); e.hasMoreElements();) {
            String key = "" + e.nextElement();
            String val = Constants.get(key);
            javax.swing.ImageIcon icon = getImageIcon(val);
            if (icon == null) {
                System.out.println(key);
                //cnt++;
            }
        }
        //System.out.println( "cnt=" + cnt );
        
        //new ImageIcon( getClass().getResource( Constants.get("bg.8") ) );
        //final java.net.URL imgURL = Outil.class.getResource(path);
        
        //System.out.println( getImageIcon( "/resources/images/play.png" ) );
    }
    
    //---------------------------------------------------------------
    public static void main(String[] args)
    throws java.io.IOException, java.io.FileNotFoundException {
        
        verifyImages();
        
    }
    
    //---------------------------------------------------------------
}
