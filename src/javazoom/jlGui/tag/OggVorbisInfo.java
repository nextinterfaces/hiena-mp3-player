package javazoom.jlGui.tag;

/**
 * OggVorbisInfo.
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
import com.jcraft.jorbis.*;

/**
 * This class gives information (audio format and comments) about Ogg Vorbis file.
 */
public class OggVorbisInfo implements TagInfo
{
  private int serial = 0;
  private int channels = 0;
  private int version = 0;
  private int rate = 0;
  private int minbitrate = 0;
  private int maxbitrate = 0;
  private int averagebitrate = 0;
  private int nominalbitrate = 0;
  private long totalms = 0;
  private String vendor = null;
  private String location = null;

  private long size = 0;
  private int track = -1;
  private String year = null;
  private String genre = null;
  private String title = null;
  private String artist = null;
  private String album = null;
  private Vector comments = null;

  private byte[] header;
  private byte[] packet;
  private long[] crc_lookup = new long[256];

  /**
   * Constructor.
   * @param input
   * @throws IOException
   * @throws JOrbisException
   */
  public OggVorbisInfo(String input) throws IOException, JOrbisException
  {
    VorbisFile vorbisfile = null;
    File file = new File(input);
    size = file.length();
    location = input;
    FileInputStream fis = new FileInputStream(input);
    checkAudioFormat(fis);
    fis.close();
    vorbisfile = new VorbisFile(input);
    this.loadInfo(vorbisfile);
  }

  /**
   * Check InputStream is Ogg Vorbis Stream.
   * @param s
   * @throws JOrbisException
   * @throws IOException
   */
  private void checkAudioFormat(InputStream s) throws JOrbisException, IOException
  {
    for (int i = 0; i < 256; i++)
    {
      crc_lookup[i] = _ogg_crc_entry(i);
      // read in the minimal packet header
    }
    byte[] head = new byte[27];
    int bytes = s.read(head);
    if (bytes < 27)
    {
      throw new JOrbisException("Not enough bytes in header");
    }
    if (!"OggS".equals(new String(head, 0, 4)))
    {
      throw new JOrbisException("Not a valid Ogg Vorbis file");
    }
    int headerbytes = (touint(head[26])) + 27;
    // get the rest of the header
    byte[] head_rest = new byte[touint(head[26])];
    bytes += s.read(head_rest);
    header = new byte[headerbytes];
    Arrays.fill(header, (byte) 0);
    // copy the whole header into header
    System.arraycopy(head, 0, header, 0, 27);
    System.arraycopy(head_rest, 0, header, 27, headerbytes - 27);
    if (bytes < headerbytes)
    {
      String error = "Error reading vorbis file: " + "Not enough bytes for header + seg table";
      throw new JOrbisException(error);
    }
    int bodybytes = 0;
    for (int i = 0; i < header[26]; i++)
    {
      bodybytes += touint(header[27 + i]);
    }
    packet = new byte[bodybytes];
    Arrays.fill(packet, (byte) 0);
    bytes += s.read(packet);
    if (bytes < headerbytes + bodybytes)
    {
      String error = "Error reading vorbis file: " + "Not enough bytes for header + body";
      throw new JOrbisException(error);
    }
    byte[] oldsum = new byte[4];
    System.arraycopy(header, 22, oldsum, 0, 4); // read existing checksum
    Arrays.fill(header, 22, 22 + 4, (byte) 0); // clear for calculation of checksum
    byte[] newsum = checksum();
    if (! (new String(oldsum)).equals(new String(newsum)))
    {
      // Checksum Failed.
    }
  }

  /**
   * Computes Ogg CRC.
   * @param index
   * @return
   */
  private long _ogg_crc_entry(long index)
  {
    long r;
    r = index << 24;
    for (int i = 0; i < 8; i++)
    {
      if ( (r & 0x80000000L) != 0)
      {
        r = (r << 1) ^ 0x04c11db7L;
      }
      else
      {
        r <<= 1;
      }
    }
    return (r & 0xffffffff);
  }

  /**
   * Computes Checksum.
   * @return
   */
  private byte[] checksum()
  {
    long crc_reg = 0;
    for (int i = 0; i < header.length; i++)
    {
      int tmp = (int) ( ( (crc_reg >>> 24) & 0xff) ^ touint(header[i]));
      crc_reg = (crc_reg << 8) ^ crc_lookup[tmp];
      crc_reg &= 0xffffffff;
    }
    for (int i = 0; i < packet.length; i++)
    {
      int tmp = (int) ( ( (crc_reg >>> 24) & 0xff) ^ touint(packet[i]));
      crc_reg = (crc_reg << 8) ^ crc_lookup[tmp];
      crc_reg &= 0xffffffff;
    }
    byte[] sum = new byte[4];
    sum[0] = (byte) (crc_reg & 0xffL);
    sum[1] = (byte) ( (crc_reg >>> 8) & 0xffL);
    sum[2] = (byte) ( (crc_reg >>> 16) & 0xffL);
    sum[3] = (byte) ( (crc_reg >>> 24) & 0xffL);
    return sum;
  }

  /**
   * Convert to uint.
   * @param n
   * @return
   */
  private int touint(byte n)
  {
    return (n & 0xff);
  }

  /**
   * Read OggVorbis information for Ogg Stream only.
   * @param vorbisfile
   */
  private void loadInfo(VorbisFile vorbisfile)
  {
    int links = vorbisfile.streams();
    serial = vorbisfile.serialnumber( -1);
    averagebitrate = vorbisfile.bitrate( -1);
    totalms = (long) Math.round(vorbisfile.time_total(-1));
    Comment[] commentsarray = vorbisfile.getComment();
    Info[] infos = vorbisfile.getInfo();
    comments = new Vector();
    for (int i = 0; i < links; i++)
    {
      Info info = infos[i];
      Comment comment = commentsarray[i];
      channels = info.channels;
      rate = info.rate;
      version = info.version;
      String infoStr = info.toString();
      infoStr = infoStr.substring(infoStr.indexOf("bitrate:") + 8, infoStr.length());
      StringTokenizer st = new StringTokenizer(infoStr, ",");
      if (st.hasMoreTokens())
      {
        minbitrate = Integer.parseInt(st.nextToken());
      }
      if (st.hasMoreTokens())
      {
        nominalbitrate = Integer.parseInt(st.nextToken());
      }
      if (st.hasMoreTokens())
      {
        maxbitrate = Integer.parseInt(st.nextToken());
      }
      vendor = comment.getVendor();
      for (int c = 0; c < comment.comments; c++)
      {
        String cmt = comment.getComment(c);
        int ind = cmt.indexOf("=");
        if (ind != -1)
        {
          String key = (cmt.substring(0,ind)).trim();
          String value = cmt.substring(ind+1,cmt.length());
          if (key.equalsIgnoreCase("artist")) artist = value;
          else if (key.equalsIgnoreCase("album")) album = value;
          else if (key.equalsIgnoreCase("title")) title = value;
          else if (key.equalsIgnoreCase("year")) year = value;
          else if (key.equalsIgnoreCase("genre")) genre = value;
          else if (key.equalsIgnoreCase("track")) track = Integer.parseInt(value);
          else
          {
            comments.add(cmt);
          }
        }
        else
        {
          comments.add(cmt);
        }
      }
    }
  }

  public int getSerial()
  {
    return serial;
  }

  public int getChannels()
  {
    return channels;
  }

  public int getVersion()
  {
    return version;
  }

  public int getMinBitrate()
  {
    return minbitrate;
  }

  public int getMaxBitrate()
  {
    return maxbitrate;
  }

  public int getAverageBitrate()
  {
    return averagebitrate;
  }

  public long getSize()
  {
    return size;
  }

  public String getVendor()
  {
    return vendor;
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
    return totalms;
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