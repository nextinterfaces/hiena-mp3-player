package org.roussev.hiena.sound;

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

import  java.io.IOException;
import  java.io.File;
import  java.io.InputStream;
import  java.io.BufferedInputStream;
import  java.io.FileInputStream;
import  java.util.Map;
import  java.util.StringTokenizer;
import	java.net.URL;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.DataLine;
import	javazoom.spi.mpeg.sampled.file.MpegAudioFileFormat;
import	javazoom.spi.mpeg.sampled.file.MpegAudioFormat;

public class ABasicPlayer implements Runnable {
    
    private static final int		EXTERNAL_BUFFER_SIZE = 4096 * 4;
    
    private Thread			m_thread = null;
    private Object			m_dataSource;
    private AudioInputStream		m_audioInputStream;
    private AudioFileFormat		m_audioFileFormat;
    private SourceDataLine		m_line;
    private FloatControl			m_gainControl;
    private FloatControl			m_panControl;
    
    //---------
    public static final int		PLAYING = 0;
    public static final int		PAUSED = 1;
    public static final int		STOPPED = 2;
    public static final int		READY = 3;
    private int				m_status = READY;
    private ABasicPlayerListener	m_bpl = null;
    private boolean isApplet;
    
    public int debugLevel = 0;
    
    
    
    //---------------------------------------------
    public ABasicPlayer() {
        m_dataSource = null;
        m_audioInputStream = null;
        m_audioFileFormat = null;
        m_line = null;
        m_gainControl = null;
        m_panControl = null;
    }
    
    
    
    //---------------------------------------------
    public ABasicPlayer(int debugLevel) {
        this();
        this.debugLevel = debugLevel;
    }
    
    //---------------------------------------------
    public ABasicPlayer(ABasicPlayerListener bpl) {
        this();
        m_bpl = bpl;
    }
    
    //---------------------------------------------
    public ABasicPlayer(ABasicPlayerListener bpl, int debugLevel) {
        this(debugLevel);
        m_bpl = bpl;
    }
    
    //---------------------------------------------
    public final int getStatus() {
        return 	m_status;
    }    
    //---------------------------------------------
    public final void setApplet(boolean isApplet) {
        this.isApplet = isApplet;
    }
    //---------------------------------------------
    public final String getStatusInfo() {
        if(m_status == PAUSED) {
            return 	"PAUSED";
        } else if(m_status == PLAYING) {
            return 	"PLAYING";
        } else if(m_status == STOPPED) {
            return 	"STOPPED";
        }
        return 	"NONE";
    }
    
    //---------------------------------------------
    public final void setDataSource(Object source) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (source != null) {
            m_dataSource = source;
            initAudioInputStream();
        }
    }
    
    //---------------------------------------------
    private final void initAudioInputStream() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (m_dataSource instanceof URL) {
            initAudioInputStream((URL) m_dataSource);
        }
        else if (m_dataSource instanceof File) {
            initAudioInputStream((File) m_dataSource);
        }
    }
    
    
    
    
    //---------------------------------------------
    /**
     * Inits Audio ressources from file.
     */
    private final void initAudioInputStream(File file) throws	UnsupportedAudioFileException, IOException {
        if(isApplet) {
            // Applet UGLY workaround.
            m_audioInputStream = AppletMpegSPIWorkaround.getAudioInputStream(file);
            m_audioFileFormat = AppletMpegSPIWorkaround.getAudioFileFormat(file);
        } else {
            m_audioInputStream = AudioSystem.getAudioInputStream(file);
            m_audioFileFormat = AudioSystem.getAudioFileFormat(file);
        }
    }
    
    
    
    
    //---------------------------------------------
    /**
     * Inits Audio ressources from URL.
     */
    private final void initAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        if(isApplet) {
            // Applet UGLY workaround.
            m_audioInputStream = AppletMpegSPIWorkaround.getAudioInputStream(url);
            m_audioFileFormat = AppletMpegSPIWorkaround.getAudioFileFormat(url);
        } else {
            m_audioInputStream = AudioSystem.getAudioInputStream(url);
            m_audioFileFormat = AudioSystem.getAudioFileFormat(url);
        }
    }
    
    
    
    
    //---------------------------------------------
    /**
     * Inits Audio ressources from AudioSystem.<br>
     * DateSource must be present.
     */
    protected final void initLine() throws LineUnavailableException {
        if (m_line == null) {
            createLine();
            trace(1,"---", "Create Line OK ");
            openLine();
        }
        else {
            final AudioFormat	lineAudioFormat = m_line.getFormat();
            final AudioFormat	audioInputStreamFormat = (m_audioInputStream == null)? null : m_audioInputStream.getFormat();
            if (!lineAudioFormat.equals(audioInputStreamFormat)) {
                m_line.close();
                openLine();
            }
        }
    }
    
    
    
    //---------------------------------------------
    /**
     * Inits a DateLine.<br>
     *
     * We check if the line supports Volume and Pan controls.
     *
     * From the AudioInputStream, i.e. from the sound file, we
     * fetch information about the format of the audio data. These
     * information include the sampling frequency, the number of
     * channels and the size of the samples. There information
     * are needed to ask JavaSound for a suitable output line
     * for this audio file.
     * Furthermore, we have to give JavaSound a hint about how
     * big the internal buffer for the line should be. Here,
     * we say AudioSystem.NOT_SPECIFIED, signaling that we don't
     * care about the exact size. JavaSound will use some default
     * value for the buffer size.
     */
    private final void createLine() throws LineUnavailableException {
        if (m_line == null) {
            final AudioFormat	sourceFormat = m_audioInputStream.getFormat();
            //trace(1,"---", "Source format : ", sourceFormat.toString());
            
            final AudioFormat	targetFormat = new AudioFormat( 
            AudioFormat.Encoding.PCM_SIGNED,
            sourceFormat.getSampleRate(),
            16,
            sourceFormat.getChannels(),
            sourceFormat.getChannels() * 2,
            sourceFormat.getSampleRate(),
            false
            );            
            //trace(1,"---", "Target format: " + targetFormat);
            
            if(isApplet) {
                // Applet UGLY workaround.
                m_audioInputStream = AppletMpegSPIWorkaround.getAudioInputStream(targetFormat,m_audioInputStream);
            } else {
                m_audioInputStream = AudioSystem.getAudioInputStream(targetFormat, m_audioInputStream);
            }
            
            final AudioFormat audioFormat = m_audioInputStream.getFormat();
            trace(1,"---", "Create Line : ", audioFormat.toString());
            DataLine.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            m_line = (SourceDataLine) AudioSystem.getLine(info);
            
            /*-- Display supported controls jdk_1.5. dont supports them ?! --* /
            Outil.p(" :::::::::::: Controls : " );
            Control[] c = m_line.getControls();
            for (int p=0; p < c.length; p++) {
                Outil.p( "Controls : " + c[p].toString() );
                trace(2,"---", "Controls : " + c[p].toString() );
            }*/
            
            /*-- Is Gain Control supported ? --*/
            if (m_line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                m_gainControl = (FloatControl) m_line.getControl(FloatControl.Type.MASTER_GAIN);
                trace(1,"---", "Master Gain Control : ["+m_gainControl.getMinimum()+","+m_gainControl.getMaximum()+"]",""+m_gainControl.getPrecision());
            }
            
            /*-- Is Pan control supported ? --*/
            if (m_line.isControlSupported(FloatControl.Type.PAN)) {
                m_panControl = (FloatControl) m_line.getControl(FloatControl.Type.PAN);
                trace(1,"---", "Pan Control : ["+ m_panControl.getMinimum()+","+m_panControl.getMaximum()+"]",""+m_panControl.getPrecision());
            }
        }
    }
    
    
    
    
    
    //---------------------------------------------
    /**
     * Opens the line.
     */
    private final void openLine() throws LineUnavailableException {
        if (m_line != null) {
            final AudioFormat	audioFormat = m_audioInputStream.getFormat();
            trace(1,"---", "AudioFormat : "+audioFormat);
            m_line.open(audioFormat, m_line.getBufferSize());
        }
    }
    
    
    
    //---------------------------------------------
    /**
     * Stops the playback.<br>
     *
     * Player Status = STOPPED.<br>
     * Thread should free Audio ressources.
     */
    public final void stopPlayback() {
        if ( (m_status == PLAYING) || (m_status == PAUSED) ) {
            if (m_line != null) {
                //doFade_DOWN(STOPPED);
                m_line.flush();
                m_line.stop();
            }
            m_status = STOPPED;
            trace(1,"---", "Stop called");
        }
    }
    
    
    
    
    //---------------------------------------------
    /**
     * Pauses the playback.<br>
     *
     * Player Status = PAUSED.
     */
    public final void pausePlayback() {
        if (m_line != null) {
            if (m_status == PLAYING) {
                doStop(PAUSED);
                trace(1,"===================", "Pause called");
            }
        }
    }
    
    
    //---------------------------------------------    
    private final void doStop(int status) {
        m_line.flush();
        m_line.stop();
        m_status = status;
    }
    
    
    
    //---------------------------------------------
    /**
     * Resumes the playback.<br>
     *
     * Player Status = PLAYING.
     */
    public final void resumePlayback() {
        if (m_line != null) {
            if (m_status == PAUSED) {
                m_line.start();
                m_status = PLAYING;
                //trace(1,"=====================", "Resume called");
            }
        }
    }
    
    
    
    
    //---------------------------------------------
    /**
     * Starts playback.
     */
    public final String startPlayback() throws  javax.sound.sampled.LineUnavailableException {
        
        if ((m_status == STOPPED) || (m_status == READY)) {
            trace(1,"---", "Start called");
            if (!(m_thread == null || !m_thread.isAlive())) {
                trace(1,"---", "WARNING: old thread still running!!");
                int cnt = 0;
                while (m_status != READY) {
                    try {
                        if (m_thread != null) {
                            cnt++;
                            Thread.sleep(1000);
                            if (cnt > 2) m_thread.interrupt();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        trace(1,"---", "Waiting Error : "+e.getMessage());
                    }
                    trace(1,"---", "Waiting ... "+cnt);
                }
            }
            
            initLine();            
            
            trace(1,"---", "Creating new thread");
            
            m_thread = new Thread(this, "BasicPlayer_Thread" );
            m_thread.start();
            
            if (m_line != null) {
                m_line.start();
            }
        }
        return null;
    }
    
       
    
    //---------------------------------------------
    /**
     * Main loop.
     *
     * Player Status == STOPPED => End of Thread + Freeing Audio Ressources.<br>
     * Player Status == PLAYING => Audio stream data sent to Audio line.<br>
     * Player Status == PAUSED => Waiting for another status.
     */
    public final void run() {
        trace(1,"---", "Thread Running");
        //if (m_audioInputStream.markSupported()) m_audioInputStream.mark(m_audioFileFormat.getByteLength());
        //else trace(1,"---", "Mark not supported");
        int	nBytesRead = 1;
        m_status = PLAYING;
        int nBytesCursor = 0;
        final byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
        final float nFrameSize = (float) m_line.getFormat().getFrameSize();
        final float nFrameRate =  m_line.getFormat().getFrameRate();
        final float bytesPerSecond = nFrameSize*nFrameRate;
        //final int secondsTotal = (int) Math.round(getTotalLengthInSeconds());
        //secondsTotal =  Math.round((float)m_audioFileFormat.getByteLength()/bytesPerSecond);
        
        //------
        while ( (nBytesRead != -1) && (m_status != STOPPED) ) {
            
            if (m_status == PLAYING) {
                try {
                    nBytesRead = m_audioInputStream.read(abData, 0, abData.length);
                }
                catch (IOException e) {
                    trace(1,"---", "InputStream error : ("+nBytesRead+")",e.getMessage());
                    e.printStackTrace();
                    m_status = STOPPED;
                } catch (ArrayIndexOutOfBoundsException e) {
                    trace(1,"---", "Internal error : ",e.getMessage());
                    e.printStackTrace();
                    m_status = STOPPED;
                    // -- ?! --
                    /*java.lang.ArrayIndexOutOfBoundsException: 580
                     at javazoom.jl.decoder.LayerIIIDecoder.huffman_decode(Unknown Source)
                     at javazoom.jl.decoder.LayerIIIDecoder.decode(Unknown Source)
                     at javazoom.jl.decoder.LayerIIIDecoder.decodeFrame(Unknown Source)
                     at javazoom.jl.decoder.Decoder.decodeFrame(Unknown Source)
                     at javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream.execute(Unknown Source)
                     at org.tritonus.share.TCircularBuffer.read(TCircularBuffer.java:134)
                     at org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream.read(TAsynchronousFilteredAudioInputStream.java:189)
                     */
                }
                
                if (nBytesRead >= 0) {
                    final int nBytesWritten = m_line.write(abData, 0, nBytesRead);
                    nBytesCursor = nBytesCursor + nBytesWritten;
                    if (m_bpl != null) {
                        final int secondsAmount = (int) Math.round((float)nBytesCursor/bytesPerSecond);
                        m_bpl.updateMediaCursor( secondsAmount );
                    }
                }
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                    trace(1,"---", "Thread cannot sleep : ",e.getMessage());
                }
            }
        } // end of while
        //------
        
        if (m_line != null) {
            try {
                m_line.drain();
                m_line.stop();
                m_line.close();
            } catch (Exception e) {
                e.printStackTrace();
                trace(1,"---", "Cannot Free Audio ressources",e.getMessage());
            }
            finally {
                m_line = null;
            }
        }
        
        trace(1,"---", "Thread Stopped");
        m_status = READY;
        // End Of Media
        if (m_bpl != null)  m_bpl.updateMediaState("EOM");
    }
    
    
    
    
    
    /*----------------------------------------------*/
    /*--               Gain Control               --*/
    /*----------------------------------------------*/
    
    /**
     * Returns true if Gain control is supported.
     */
    public final boolean hasGainControl() {
        return m_gainControl != null;
    }
    
    
    /**
     * Sets Gain value.
     * Linear scale 0.0  <-->  1.0
     * Threshold Coef. : 1/2 to avoid saturation.
     */
    public final void setGain(double param) {
        if (hasGainControl()) {
            final double minGainDB = getMinimum();
            final double ampGainDB = ((10.0f/20.0f)*getMaximum()) - getMinimum();
            final double cste = Math.log(10.0)/20;
            final double valueDB = minGainDB + (1/cste)*Math.log(1+(Math.exp(cste*ampGainDB)-1)*param);
            m_gainControl.setValue((float)valueDB);
            //trace(1,"---", "Gain : "+valueDB);
        }
    }
    
    
    /**
     * Returns Gain value.
     */
    public final float getGain() {
        if (hasGainControl()) {
            return m_gainControl.getValue();
        }
        else {
            return 0.0F;
        }
    }
    
    /**
     * Gets max Gain value.
     */
    public final float getMaximum() {
        if (hasGainControl()) {
            return m_gainControl.getMaximum();
        }
        else {
            return 0.0F;
        }
    }
    
    
    /**
     * Gets min Gain value.
     */
    public final float getMinimum() {
        if (hasGainControl()) {
            return m_gainControl.getMinimum();
        }
        else {
            return 0.0F;
        }
    }
    
    
    
    /*----------------------------------------------*/
    /*--               Pan Control                --*/
    /*----------------------------------------------*/
    /**
     * Returns true if Pan control is supported.
     */
    public final boolean hasPanControl() {
        return m_panControl != null;
    }
    
    //-------------------------
    /**
     * Returns Pan precision.
     */
    public final float getPrecision() {
        if (hasPanControl()) {
            return m_panControl.getPrecision();
        }
        else {
            return 0.0F;
        }
    }
    
    
    //--------------------------
    /**
     * Returns Pan value.
     */
    public final float getPan() {
        if (hasPanControl()) {
            return m_panControl.getValue();
        }
        else {
            return 0.0F;
        }
    }
    
    
    //------------------------------
    /**
     * Sets Pan value.
     * Linear scale : -1.0 <--> +1.0
     */
    public final void setPan(float fPan) {
        if (hasPanControl()) {
            m_panControl.setValue(fPan);
            //trace(1,"---", "Pan : "+fPan);
        }
    }
    
    
    
    
    
    
    /*----------------------------------------------*/
    /*--               Audio Format               --*/
    /*----------------------------------------------*/
    
    //-------------------------------------------------------
    public final AudioFormat getAudioFormat() {
        if (m_audioFileFormat != null) {
            return m_audioFileFormat.getFormat();
        }
        else {
            return null;
        }
    }
    
    
    
    //-------------------------------------------------------
    public final AudioFileFormat getAudioFileFormat() {
        if (m_audioFileFormat != null) {
            return m_audioFileFormat;
        }
        else {
            return null;
        }
    }
    
    
    
    //---------------------------------------------
    public final double getTotalLengthInSeconds() {
        double lengthInSecond = 0.0;
        if ( getAudioFileFormat() != null) {
            final int FL = (getAudioFileFormat()).getFrameLength();
            final int FS = (getAudioFormat()).getFrameSize();
            final float SR = (getAudioFormat()).getSampleRate();
            //final float FR = (getAudioFormat()).getFrameRate();
            final int TL = (getAudioFileFormat()).getByteLength();
            final String type = (getAudioFileFormat()).getType().toString();
            //final String encoding = (getAudioFormat()).getEncoding().toString();
            //-----
            if ( FL != -1 && type.startsWith("MP3") ) {
                
                final MpegAudioFileFormat fileFormat = ((MpegAudioFileFormat)getAudioFileFormat());
                final Map map = fileFormat.properties();
                final long l = ((Long)map.get("duration")).longValue();
                lengthInSecond = l/1000000;
            }
            //-----
            else if ( FL != -1 && type.startsWith("VORBIS") ) {
                // No accurate formula :-(
                // Vorbis SPi does not have yet VorbisAudioFileFormat.
                // Assumes that type includes xBitRate string.
                // Old dirty solution with SPI.
                try {
                    final StringTokenizer st = new StringTokenizer(type,"x");
                    st.nextToken();
                    st.nextToken();
                    final String totalMSStr = st.nextToken();
                    lengthInSecond = Math.round((Integer.parseInt(totalMSStr))/1000);
                }
                catch(java.util.NoSuchElementException e) {     e.printStackTrace();   }
            }
            //-----
            else {
                final int br = getBitRate();
                if (br > 0) lengthInSecond = TL*8/br;
                else lengthInSecond = TL/(FS*SR);
                
            }
            //trace(2,"---","Type="+type+" Encoding="+encoding+" FL="+FL+" FS="+FS+" SR="+SR+" FR="+FR+" TL="+TL," lenghtInSecond="+lenghtInSecond);
        }
        
        if (lengthInSecond < 0.0) lengthInSecond = 0.0;
        
        return lengthInSecond;
    }
    
    
    
    //---------------------------------------------
    public final Map getMpegFormatProperties() {
        
        if ( getAudioFileFormat() != null && getAudioFormat() != null ) {
            final String type = (getAudioFileFormat()).getType().toString();
            
            if ( (type != null) && ((type.startsWith("MP3")) ) ) {
                final MpegAudioFormat audioFormat = ((MpegAudioFormat)getAudioFormat());
                return audioFormat.properties();
            }
        }
        return null;
    }
    
    //---------------------------------------------
    public final Map getMpegFileFormatProperties() {
        
        if ( getAudioFileFormat() != null) {
            final String type = (getAudioFileFormat()).getType().toString();
            
            if ( (type != null) && ((type.startsWith("MP3")) ) ) {
                final MpegAudioFileFormat audiofileFormat = ((MpegAudioFileFormat)getAudioFileFormat());
                return audiofileFormat.properties();
            }
        }
        return null;
    }
    
    //---------------------------------------------
    public final int getBitRate() {
        int bitRate = 0;
        if ( getAudioFileFormat() != null) {
            //final int FL = (getAudioFileFormat()).getFrameLength();
            final int FS = (getAudioFormat()).getFrameSize();
            //final float SR = (getAudioFormat()).getSampleRate();
            final float FR = (getAudioFormat()).getFrameRate();
            //final int TL = (getAudioFileFormat()).getByteLength();
            final String type = (getAudioFileFormat()).getType().toString();
            //final String encoding = (getAudioFormat()).getEncoding().toString();
            
            if ( (type != null) && ((type.startsWith("MP3")) ) ) {
                
                final MpegAudioFormat audioFormat = ((MpegAudioFormat)getAudioFormat());
                final Map map = audioFormat.properties();
                bitRate = ((Integer)map.get("bitrate")).intValue();
            }
            else if ( type != null && type.startsWith("VORBIS") ) {
                // Dirty solution :-(.
                try {
                    StringTokenizer st = new StringTokenizer(type,"x");
                    if (st.hasMoreTokens()) {
                        st.nextToken();
                        String bitRateStr = st.nextToken();
                        bitRate = Math.round((Integer.parseInt(bitRateStr)));
                    }
                } catch(Exception ignore) { System.out.println(ignore); }
            }
            else {
                bitRate = Math.round(FS*FR*8);
            }
            //trace(2,"---","Type="+type+" Encoding="+encoding+" FL="+FL+" FS="+FS+" SR="+SR+" FR="+FR+" TL="+TL," bitRate="+bitRate);
        }
        
        // N/A so computes bitRate for output.
        if ((bitRate <= 0) && (m_line != null)) {
            bitRate = Math.round(((m_line.getFormat()).getFrameSize())*((m_line.getFormat()).getFrameRate())*8);
        }
        
        return bitRate;
    }
    
    
    
    //---------------------------------------------
    protected final InputStream openInput(File file) throws IOException {
        return new BufferedInputStream( new FileInputStream(file));
    }
    
    
    
    /*----------------------------------------------*/
    /*--                   Misc                   --*/
    /*----------------------------------------------*/
    
    /**
     * Sends traces to Debug.
     */
    private final void trace(int level, String msg1, String msg2) {
        if( debugLevel >= level )
            System.out.println( msg1 + " : " + msg2 );
    }
    
    private final void trace(int level, String msg1, String msg2, String msg3) {
        if( debugLevel >= level )
            System.out.println( msg1 + " : " + msg2 + " : " + msg3);
    }
    
    
    //---------------------------------------------
    public final void dbg() {
        
        System.out.println( "[ AudioFileFormat = " + getAudioFileFormat() );
        System.out.println( "[ AudioFormat = " + getAudioFormat() );
        System.out.println( "[ BitRate = " + getBitRate() );
        System.out.println( "[ Gain = " + getGain() );
        System.out.println( "[ Maximum = " + getMaximum() );
        System.out.println( "[ Minimum = " + getMinimum() );
        System.out.println( "[ Pan = " + getPan() );
        System.out.println( "[ Precision = " + getPrecision() );
        System.out.println( "[ Status = " + getStatus() );
    }
    
    
    
    //---------------------------------------------
}




