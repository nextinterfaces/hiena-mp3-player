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
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * FontBuilder is used to build gif image from graphical fonts. Former
 * javazoom.jlGui.skin.taftb.
 * 
 * @author E.B from JavaZOOM
 * 
 * Homepage : http://www.javazoom.net
 */
public class FontBuilder extends JPanel {

    private final Image theFonts;

    private final int imageW;

    private final int imageH;

    private final int fontWidth;

    private final int fontHeight;

    private final int Yspacing;

    private final Image theBanner;

    private final int pixels[];

    private final String theText;

    /**
     * Text banner building according to the alphabet index, font size and Y
     * spacing.
     */
    public FontBuilder(String alphaIndex, Image fontFile, int fontW, int fontH,
            int Yspc, String theTxt/* , Color BgValue */) {
        fontWidth = fontW;
        fontHeight = fontH;
        Yspacing = Yspc;
        theText = theTxt;
        theFonts = fontFile;
        imageW = theFonts.getWidth(this);
        imageH = theFonts.getHeight(this);
        /*-- We create the TextBanner by grabbing font letters in the image fonts --*/
        pixels = new int[theText.length() * fontW * fontH];
        int SpacePosition = 0;
        int offsetSp = 0;
        /*-- We search the space position in the Alphabet index --*/
        while ((offsetSp < alphaIndex.length())
                && (alphaIndex.charAt(offsetSp) != ' ')) {
            offsetSp++;
        }
        if (offsetSp < alphaIndex.length()) SpacePosition = offsetSp;
        for (int offsetT = 0; offsetT < theText.length(); offsetT++) {
            int xPos = 0;
            int yPos = 0;
            int reste = 0;
            int entie = 0;
            int offsetA = 0;
            int FontPerLine = (int) Math.rint((imageW / fontW));
            /*-- We search the letter's position in the Alphabet index --*/
            while ((offsetA < alphaIndex.length())
                    && (theText.charAt(offsetT) != alphaIndex.charAt(offsetA))) {
                offsetA++;
            }
            /*-- We deduce its image's position (Int forced) --*/
            if (offsetA < alphaIndex.length()) {
                reste = offsetA % FontPerLine;
                entie = (offsetA - reste);
                xPos = reste * fontW;
                yPos = ((entie / FontPerLine) * fontH)
                        + ((entie / FontPerLine) * Yspacing);
            } /*-- If the letter is not indexed the space (if available) is selected --*/
            else {
                reste = SpacePosition % FontPerLine;
                entie = (SpacePosition - reste);
                xPos = reste * fontW;
                yPos = ((entie / FontPerLine) * fontH)
                        + ((entie / FontPerLine) * Yspacing);
            }
            /*-- We grab the letter in the font image and put it in a pixel array --*/
            try {
                final PixelGrabber pg = new PixelGrabber(theFonts, xPos, yPos,
                        fontW, fontH, pixels, offsetT * fontW, theText.length()
                                * fontW);
                pg.grabPixels();
            } catch (InterruptedException e) {
            }
        }
        /*-- We create the final Image Banner throught an Image --*/
        theBanner = createImage(new MemoryImageSource(theText.length() * fontW,
                fontH, pixels, 0, theText.length() * fontW));
    }

    /**
     * Returns final banner as an image.
     */
    public final Image getBanner() {
        return theBanner;
    }

    /**
     * Returns final banner as cropped image.
     */
    public final Image getBanner(int x, int y, int sx, int sy) {
        final CropImageFilter cif = new CropImageFilter(x, y, sx, sy);
        final Image cropBanner = createImage(new FilteredImageSource(theBanner
                .getSource(), cif));
        return cropBanner;
    }

    /**
     * Returns final banner as a pixels array.
     */
    public final int[] getPixels() {
        return pixels;
    }

    /**
     * Returns banner's length.
     */
    public final int getPixelsW() {
        return theText.length() * fontWidth;
    }

    /**
     * Returns banner's height.
     */
    public final int getPixelsH() {
        return fontHeight;
    }

    //------------------------------------------------------
    /**
     * Returns final banner as a label.
     */
    public final JLabel getLabel() {
        JLabel label = new JLabel(new ImageIcon(getBanner()));
        label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        return label;
    }
    //------------------------------------------------------
}
