package javazoom.jlGui.tag;

/**
 * MpegInfo.
 * 
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

import java.io.*;
import java.util.*;
import helliker.id3.*;
import javazoom.jl.decoder.*;

/**
 * This class gives information (audio format and comments) about MPEG file.
 */
public class MpegInfo implements TagInfo
{
  private String channels = null;
  private String version = null;
  private int rate = 0;
  private String layer = null;
  private String emphasis = null;
  private int nominalbitrate = 0;
  private long total = 0;
  //private String vendor = null;
  private String location = null;
  private long size = 0;
  private boolean copyright = false;
  private boolean crc = false;
  private boolean original = false;
  //private boolean priv = false;
  private boolean vbr = false;
  private int track = -1;
  private String year = null;
  private String genre = null;
  private String title = null;
  private String artist = null;
  private String album = null;
  private Vector comments = null;

  /**
   * Constructor.
   * @param input
   * @throws IOException
   */
  public MpegInfo(String input) throws JavaLayerException, IOException, ID3Exception
  {
    MP3File mp3file = null;
    File file = new File(input);
    size = file.length();
    location = input;
    FileInputStream fis = new FileInputStream(input);
    checkAudioFormat(fis);
    fis.close();
    mp3file = new MP3File(input);
    this.loadInfo(mp3file);
  }

  /**
   * Check InputStream is Mpeg Stream.
   * @param s
   * @throws BitstreamException
   */
  private void checkAudioFormat(InputStream s) throws JavaLayerException
  {
    try
    {
      /*String[][] sm_aEncodings =
          {
          {
          "MpegEncoding.MPEG2L1", "MpegEncoding.MPEG2L2", "MpegEncoding.MPEG2L3"}
          ,
          {
          "MpegEncoding.MPEG1L1", "MpegEncoding.MPEG1L2", "MpegEncoding.MPEG1L3"}
          ,
          {
          "MpegEncoding.MPEG2DOT5L1", "MpegEncoding.MPEG2DOT5L2", "MpegEncoding.MPEG2DOT5L3"}
          ,

      };*/

      Bitstream m_bitstream = new Bitstream(s);
      Header m_header = m_bitstream.readFrame();
      // nVersion = 0 => MPEG2-LSF (Including MPEG2.5), nVersion = 1 => MPEG1
      int nVersion = m_header.version();
      int nLayer = m_header.layer();
      nLayer = m_header.layer();
      //int nSFIndex = m_header.sample_frequency();
      //int nMode = m_header.mode();
      int FrameLength = m_header.calculate_framesize();
      if (FrameLength < 0) throw new JavaLayerException("not a MPEG stream: invalid framelength");
      //int nFrequency = m_header.frequency();
      float FrameRate = (float) ( (1.0 / (m_header.ms_per_frame())) * 1000.0);
      if (FrameRate < 0) throw new JavaLayerException("not a MPEG stream: invalid framerate");
      int BitRate = Header.bitrates[nVersion][nLayer - 1][m_header.bitrate_index()];
      if (BitRate <= 0) throw new JavaLayerException("not a MPEG stream: invalid bitrate");
      int nHeader = m_header.getSyncHeader();
      //String encoding = sm_aEncodings[nVersion][nLayer - 1];
      int cVersion = (nHeader >> 19) & 0x3;
      if (cVersion == 1) throw new JavaLayerException("not a MPEG stream: wrong version");
      int cSFIndex = (nHeader >> 10) & 0x3;
      if (cSFIndex == 3) throw new JavaLayerException("not a MPEG stream: wrong sampling rate");
    } catch (Exception e)
     {
       throw new JavaLayerException(e.getMessage());
     }
  }

  /**
   * Load MP3 info.
   * @param mp3file
   * @throws ID3v2FormatException
   */
  private void loadInfo(MP3File mp3file) throws ID3v2FormatException
  {
    rate = mp3file.getSampleRate();
    nominalbitrate = mp3file.getBitRate()*1000;
    version = mp3file.getMPEGVersion();
    layer = mp3file.getMPEGLayer();
    copyright = mp3file.isMPEGCopyrighted();
    crc = mp3file.isMPEGProtected();
    original = mp3file.isMPEGOriginal();
    emphasis = mp3file.getMPEGEmphasis();
    channels = mp3file.getMPEGChannelMode();
    total = mp3file.getPlayingTime();
    vbr = mp3file.isVBR();
    album = mp3file.getAlbum();
    artist = mp3file.getArtist();
    title = mp3file.getTitle();
    genre = mp3file.getGenre();
    year = mp3file.getYear();
    track = mp3file.getTrack();
    comments = new Vector();
    comments.add(mp3file.getComment());
  }

  public boolean getVBR()
  {
    return vbr;
  }

  public String getChannels()
  {
    return channels;
  }

  public String getVersion()
  {
    return version;
  }

  public String getEmphasis()
  {
    return emphasis;
  }

  public boolean getCopyright()
  {
    return copyright;
  }

  public boolean getCRC()
  {
    return crc;
  }

  public boolean getOriginal()
  {
    return original;
  }

  public String getLayer()
  {
    return layer;
  }

  public long getSize()
  {
    return size;
  }

  public String getLocation()
  {
    return location;
  }

  /*-- TagInfo Implementation --*/

  public int getSamplingRate()
  {
    return rate;
  }

  public int getBitRate()
  {
    return nominalbitrate;
  }

  public long getPlayTime()
  {
    return total;
  }

  public String getTitle()
  {
    return title;
  }

  public String getArtist()
  {
    return artist;
  }

  public String getAlbum()
  {
    return album;
  }

  public int getTrack()
  {
    return track;
  }

  public String getGenre()
  {
    return genre;
  }

  public Vector getComment()
  {
    return comments;
  }

  public String getYear()
  {
    return year;
  }

}