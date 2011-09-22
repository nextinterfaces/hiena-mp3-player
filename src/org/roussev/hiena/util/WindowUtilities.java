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


import javax.swing.UIManager;
import javax.swing.UIDefaults;
import javax.swing.JDialog;
import java.awt.Color;
import java.util.*;

public final class WindowUtilities {
    
    
    
    static final String[] def_scroll_keys = {
        "ScrollBar.width",
        "ScrollBar.thumb",
        "ScrollBar.thumbShadow",
        "ScrollBar.thumbHighlight",
        "ScrollBar.background"
    };
    static final Object[] def_scroll_values = new Object[5];
    
    //--------------------------------
    public WindowUtilities() {
        for( int i=0; i<def_scroll_keys.length; i++){
            def_scroll_values[i] = UIManager.get(def_scroll_keys[i]);
        }
    }
    
    
    
    //--------------------------------
    public static final void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }
    }
    
    //--------------------------------
    public static final void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e) {
            System.out.println("Error setting Java LAF: " + e);
        }
        //String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        ////////String metal = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";
        //String metal = UIManager.getCrossPlatformLookAndFeelClassName();
        //String system = UIManager.getSystemLookAndFeelClassName();
        //-----------
        /*String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
        //String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        //String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel( metal );
        } catch (Exception e) { e.printStackTrace(); }
         */
    }
    
    //--------------------------------
    public static final void setMotifLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch(Exception e) {
            System.out.println("Error setting Motif LAF: " + e);
        }
    }
    
    
    
    
    //-------------------------------------------------------------------
    /*public static final void manageUI() {
     
        //-------- UI Management ---------
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        UIManager.put("OptionPane.font",            new Font("Dialog", 0, 11));
        UIManager.put("OptionPane.messageFont",     new Font("Dialog", 0, 11));
        UIManager.put("OptionPane.buttonFont",      new Font("Dialog", 0, 11));
     
        //listUIResources("OptionPane");
        /*
        OptionPane.warningDialog.titlePane.background
        OptionPane.errorDialog.titlePane.background
        OptionPane.buttonAreaBorder
        OptionPane.warningDialog.titlePane.foreground
        OptionPane.questionDialog.border.background
        OptionPane.errorDialog.titlePane.foreground
        OptionPane.questionDialog.titlePane.background
        OptionPane.warningDialog.titlePane.shadow
        OptionPane.messageForeground
        OptionPane.questionDialog.titlePane.foreground
        OptionPane.foreground
        OptionPane.border
        OptionPane.background
        OptionPane.errorDialog.border.background
        OptionPane.warningDialog.border.background
     
    }*/
    
    //-------------------------------------------------------------------
    public static final void manageScrollbarUI(boolean _default) {
        
        if( !_default) {
            UIManager.put("ScrollBar.width", new Integer(11));
            UIManager.put("ScrollBar.thumb", Utils.toColor("ScrollBar.thumb"));
            UIManager.put("ScrollBar.thumbShadow", Utils.toColor("ScrollBar.thumb").darker() );
            UIManager.put("ScrollBar.thumbHighlight", Utils.toColor("ScrollBar.thumb").brighter() );
            //UIManager.put("ScrollBar.track", Color.red /*Outil.toColor("ScrollBar.track")*/ );
            //UIManager.put("ScrollBar.foreground", 	Color.blue );
            UIManager.put("ScrollBar.background", 	Utils.toColor("ScrollBar.track"));
            //UIManager.put("ScrollBar.thumbDarkShadow", Color.green );
            
            //UIManager.put("ScrollBar.thumbDarkShadow",	Outil.toColor(BUNDLE,"frame.bg.color").darker().darker() );
            //UIManager.put("ScrollBar.foreground", 	new javax.swing.plaf.ColorUIResource(new java.awt.Color(255,0,0)));
            //UIManager.put("ScrollBar.background", 	Outil.toColor(BUNDLE,"frame.bg.color"));
            //UIManager.put("ScrollBar.track",		new javax.swing.plaf.ColorUIResource(new java.awt.Color(190,0,190)));
        }
        else {
            for( int i=0; i<def_scroll_keys.length; i++){
                UIManager.put(def_scroll_keys[i], def_scroll_values[i]);
            }
        }
    }
    
    
    //-------------------------------------------------------------------
    public static final void manageDialogUI() {
        
        //-------- UI Management ---------
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    /*     int COLOR = 0xC8C7D1;
        UIManager.put("OptionPane.border", new javax.swing.border.MatteBorder(new java.awt.Insets(0, 1, 0, 1), Outil.toColor("color.border.line")));
        //new javax.swing.border.CompoundBorder());
        UIManager.put("OptionPane.questionDialog.border.background",  Outil.toColor("color.border.line").darker().darker());
        UIManager.put("OptionPane.questionDialog.titlePane.background",  Outil.toColor("color.border.line"));
        UIManager.put("OptionPane.questionDialog.titlePane.foreground",  Outil.toColor("color.border.line").darker().darker());
        UIManager.put("OptionPane.questionDialog.titlePane.shadow",  Outil.toColor("color.border.line").darker().darker());
     */
    }
    
    //-------------------------------------------------------------------
    public static void listUIColorResources()    {
        List colorProperties = new ArrayList();
        UIDefaults uid  = UIManager.getDefaults();
        Enumeration e = uid.keys();
        while(e.hasMoreElements()) {
            Object key = e.nextElement();
            Object property = uid.get(key);
            if(property instanceof Color)
                colorProperties.add(key);
        }
        Collections.sort(colorProperties);
        Iterator it = colorProperties.iterator();
        while(it.hasNext())
            System.out.println("-SETTING- " + it.next());
    }
    
    
    //-------------------------------------------------------------------
    public static void listUIResources(String res) {
        UIDefaults uid = UIManager.getLookAndFeelDefaults();
        for (Enumeration e = uid.keys() ; e.hasMoreElements() ;) {
            String s = (String)e.nextElement();
            if(s.indexOf( res /*"ScrollBar"*/) != -1) {
                //System.out.println(s + ", " + uid.get(s));
                System.out.println(s + " <-> " + uid.get(s));
            }
        }
    }
    
    
    //--------------------------------
}
