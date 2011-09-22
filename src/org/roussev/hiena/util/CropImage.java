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

import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CropImage extends JPanel {

    private final int imageW;
    private final int imageH;
    private final Image image;

    public CropImage(Image imageSrc, int[] bounds ) {
        image = getCropImage(imageSrc, bounds);
        this.imageW = bounds[2] - bounds[0];
        this.imageH = bounds[3] - bounds[1];
    }
    
    public static final Image getCropImage(Image imageSrc, int[] bounds) {
        final CropImageFilter cif = new CropImageFilter(bounds[0],bounds[1], bounds[2], bounds[3]);
        return new JPanel().createImage(new FilteredImageSource(imageSrc
                .getSource(), cif));
    }
    
    public final int getImageW() {
        return imageW;
    }
    public final int getImageH() {
        return imageH;
    }

    public final Image getImage() {
        return image;
    }

    public final ImageIcon getIcon() {
        return new ImageIcon( getImage());
    }
    
    public final JLabel getLabel() {
        final JLabel label = new JLabel(new ImageIcon( getImage()));
        //label.setBounds(0,0, imageW, imageH);
        //label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        //label.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        return label;
    }
    
    
    //------------------------------------------------------
}
