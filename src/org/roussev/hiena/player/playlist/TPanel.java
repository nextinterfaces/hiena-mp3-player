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

import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;

import javazoom.jlGui.tag.*;
import com.jcraft.jorbis.JOrbisException;
import helliker.id3.ID3Exception;
import javazoom.jl.decoder.JavaLayerException;

import org.roussev.hiena.*;
import org.roussev.hiena.gui.*;
import org.roussev.hiena.util.*;


final class TPanel extends TAbsPanel implements PlaylistController {

	//-----------------------------------------
	//private Window owner;
	//private boolean isUrlFrameOpen;
	private XTransitionButton add;
	private XTransitionButton del;
	JList m_list;
	DefaultListModel m_listModel;
	Data data;
	final PlaylistPropertyChangeListener propertyChangeListener;
	
	private static final Rectangle scrollPane_bounds = new Rectangle(6, 11, 292, 150);
	private static final int[] add_location = {21, 162};
	private static final int[] del_location = {42, 162};
	private static final int[] shade_location = {282, 170};
	private static final int[] close_location = {290, 170};

	//-----------------------------------------
	protected TPanel(PlaylistPropertyChangeListener pcl) {
		this.propertyChangeListener = pcl;
		initComponents();
		initBG();
		setSize(getPreferredSize());
		load_PL_init();
	}
	
	//------------------------------------------
	private final void load_PL_init() {
		final Data plSer = (Data)new FileObject(FileObject.PLAYLIST, new Data()).getObject();
		if (plSer != null) {
			this.data = plSer;
			load_PL();
		} else {
			data = new Data();
		}
		//pl.dbg();
		this.m_list.setSelectedIndex(data.getSelected());
	}
	//------------------------------------------
	final void load_PL() {
		final java.util.List infos = data.getTag_infos();
		final int size = infos.size();
		for (int i = 0; i < size; i++) {
			m_listModel.addElement(infos.get(i));
		}
	}

	//------------------------------------------
	private final void initComponents() {

		//--------------
		final XTransitionButton close = new XTransitionButton("close", new String[] { Constants.get("pl.close"), Constants.get("pl.close_"), Constants.get("pl.close.down"), Constants.get("close")});
		close.setSize(close.getPreferredSize());
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_PL_CLOSE);
			}
		});

		//--------------
		final XTransitionButton shade = new XTransitionButton("shade", new String[] { Constants.get("pl.shade"), Constants.get("pl.shade_"), Constants.get("pl.shade.down"), Constants.get("shade")});
		shade.setSize(shade.getPreferredSize());
		//TODO: Build shade modes functionality
		/*shade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_PL_SHADE);
			}
		});*/

		add = new XTransitionButton("add", new String[] { Constants.get("add"), Constants.get("add_"), Constants.get("add.down"), Constants.get("add")});
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addEvent();
			}
		});

		del = new XTransitionButton("del", new String[] { Constants.get("del"), Constants.get("del_"), Constants.get("del.down"), Constants.get("del")});
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delEvent();
			}
		});

		m_listModel = new DefaultListModel();
		m_list = new javax.swing.JList(m_listModel);
		m_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION /*SINGLE_INTERVAL_SELECTION*/
		);
		m_list.setSelectedIndex(0);
		m_list.setForeground(Utils.toColor("pl.list.bg").darker().darker());
		m_list.setSelectionForeground(Utils.toColor("pl.list.bg").darker().darker().darker());
		m_list.setFont(new Font("Dialog", Font.PLAIN, 10));
		m_list.setCellRenderer(new UserlistCellRenderer());

		m_list.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					play();
				}
			}
		});
		m_list.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					play();
				} else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
					deleteSelection();
				}
			}
		});

		final JScrollPane scrollPane = new javax.swing.JScrollPane();

		scrollPane.setViewportView(m_list);
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(0, 1, 0, 1), Utils.toColor("pl.list.select.bg", 0)));

		//+++++++++++++ Transparency  ++++++++++++++
		this.setOpaque(false);
		m_list.setOpaque(false);
		m_list.setSelectionBackground(Utils.toColor("pl.list.select.bg", Constants.ALPHA));
		m_list.setBackground(Utils.toColor("pl.list.bg", Constants.ALPHA));
		m_list.setForeground(Utils.toColor("pl.list.select.bg"));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		//+++++++++++++++++++++++++++

		this.setLayout(null);
		this.add(scrollPane);
		this.add(add);
		this.add(del);
		this.add(close);
		this.add(shade);
		
		scrollPane.setBounds( scrollPane_bounds);
		add.setLocation( add_location[0], add_location[1]);
		del.setLocation( del_location[0], del_location[1]);
		shade.setLocation( shade_location[0], shade_location[1]);
		close.setLocation(  close_location[0], close_location[1]);
	}

	//---------------------------------------------------------------------
	private void initBG() {
		//-------------
		final ImageIcon bg_1 = Utils.getIcon("panel.pl");
		final JLabel label_1 = new JLabel(bg_1);
		label_1.setBounds(0, 0, bg_1.getIconWidth(), bg_1.getIconHeight());
		add(label_1, new Integer(Integer.MIN_VALUE));
	}

	//----------------------------------------------------------
	final void addEvent() {
		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem item1 = new JMenuItem("Add Files");
		final JMenuItem item2 = new JMenuItem("Add Folder");
		//final JMenuItem item3 = new JMenuItem("Add URL");
		//popup.setOpaque(true);
		item1.setOpaque(false);
		item2.setOpaque(false);
		//item3.setOpaque(false);
		popup.setBackground(Utils.toColor("ScrollBar.track"));

		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFilesActionEvent(true, false);
			}
		});

		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFilesActionEvent(false, false);
			}
		});

		/*item3.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        addURLActionEvent();
		    }
		});*/

		//java.awt.Font font = new java.awt.Font("Dialog", 0, 10);
		item1.setFont(new java.awt.Font("Dialog", 0, 10));
		item2.setFont(new java.awt.Font("Dialog", 0, 10));
		//item3.setFont( new java.awt.Font("Dialog", java.awt.Font.ITALIC, 10) );

		popup.add(item1);
		popup.add(item2);
		//popup.add(item3);
		popup.setInvoker(this);

		// Don't go out of screen
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int h = popup.getHeight();
		// h = 0 !!! Dont know why.
		h = (h == 0) ? 43 : h;
		final int taskbar = 30;
		final int x = this.getLocationOnScreen().x + (int)add.getLocation().getX();
		int y = this.getLocationOnScreen().y + (int)add.getLocation().getY() /*+ this.scrollPane.getHeight()*/
		+add.getPreferredSize().height;
		y = (y + h >= screenSize.getHeight() - taskbar) ? y - h - add.getPreferredSize().height - 1 : y;
		//int y = this.getLocationOnScreen().y + (int)add.getLocation().getY() + this.scrollPane.getHeight() + add.getPreferredSize().height;
		//y = (y + h >= screenSize.getHeight()-taskbar)?  y-h-add.getPreferredSize().height-1 : y;

		popup.setLocation(x, y);
		popup.setVisible(true);
	}

	//----------------------------------------------------------
	final void addFilesActionEvent(boolean isFile, boolean clearList) {

		WindowUtilities.manageScrollbarUI(true);
		WindowUtilities.setNativeLookAndFeel();

		final JFileChooser chooser = new JFileChooser(Utils.getSetting().getMp3Folder());
		chooser.addChoosableFileFilter(new MediaFilter());
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileView(new MediaFileView());

		File[] files = null;
		if (isFile) {
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setMultiSelectionEnabled(true);
		} else {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled(false);
		}

		final int returnVal = chooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			//----
			if (clearList) {
				m_listModel.removeAllElements();
				data.clear();
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_STOP);
			}
			//----
			final Setting setting = Utils.getSetting();
			setting.setMp3Folder(chooser.getSelectedFile().getParent());
			new FileObject(FileObject.SETTING, null).setObject(setting);
			//----
			if (isFile) {
				files = chooser.getSelectedFiles();
			} else {
				files = new File[] { chooser.getSelectedFile()};
			}
			//-------
			new AddFilesThread(isFile, files).start();
		} // end APPROVE_OPTION

		WindowUtilities.manageScrollbarUI(false);
		WindowUtilities.setJavaLookAndFeel();

	}

	//----------------------------------------------------------
	final void parseFiles(boolean isFile, File[] files) {

		if (isFile) {
			final int len = files.length;
			for (int i = 0; i < len; i++) {
				if (!files[i].isDirectory()) {
					parseInfo(files[i]);
				}
			}
			//if(m_listModel.size() == len )
			//    m_list.setSelectedIndex(0);
		} else {
			add_files_recursive(files[0], 0);
			//if(m_listModel.size() == children.length )
			//    m_list.setSelectedIndex(0);
		}
	}

	//----------------------------------------------------------
	private final void add_files_recursive(File rootDir, int depth) {
		if (rootDir == null || depth > Constants.DIRECTORY_MAXDEPTH)
			return;
		final String[] lists = rootDir.list();
		if (lists == null)
			return;

		final int len = lists.length;
		for (int i = 0; i < len; i++) {
			final File ff = new File(rootDir + File.separator + lists[i]);
			if (ff.isDirectory()) {
				add_files_recursive(ff, depth + 1);
			} else {
				if (Utils.isAudioFile(lists[i])) {
					parseInfo(ff);
				}
			}
		}
	}

	//----------------------------------------------------------
	private final void parseInfo(File file) {

		TagInfo tagInfo = null;

		// Mpegformat.
		try {
			tagInfo = new MpegInfo(file.getPath());
		} catch (ID3Exception e) {
			tagInfo = null;
		} catch (IOException e) {
			tagInfo = null;
		} catch (JavaLayerException e) {
			// Not Mpeg Format
			tagInfo = null;
		}

		// Ogg Vorbis format.
		if (tagInfo == null) {
			try {
				tagInfo = new OggVorbisInfo(file.getPath());
			} catch (JOrbisException ex) {
				// Not Ogg Vorbis Format
				tagInfo = null;
			} catch (IOException ex) {
				tagInfo = null;
			}
		}

		if (tagInfo != null) {

			final String artist = tagInfo.getArtist();
			final String title = tagInfo.getTitle();

			if (artist != null && title != null && !"".equals(artist) && !"".equals(title)) {
				data.addSource(file);
				data.addInfo(Utils.filterUnix(artist) + " - " + Utils.filterUnix(title));
			} else {
				data.addSource(file);
				data.addInfo(file.getName());
			}
		} else {
			data.addSource(file);
			data.addInfo(file.getName());
		}
	}

	//----------------------------------------------------------
	/*private final void addURLActionEvent() {
	    if(!isUrlFrameOpen) {
	        isUrlFrameOpen = true;
	        final URLWindow urlWindow = new URLWindow(owner);
	        urlWindow.addPropertyChangeListener( new PropertyChangeListener() {
	            public void propertyChange(final PropertyChangeEvent evt) {
	                if( "add".equals(evt.getPropertyName())) {
	                    isUrlFrameOpen = false;
	                    data.addSource( urlWindow.getURL());
	                    data.addInfo( urlWindow.getURL());
	                    m_listModel.addElement( data.getTag_infos().get(data.getTag_infos().size()-1) );
	                }
	                if("exit".equals(evt.getPropertyName())) {
	                    isUrlFrameOpen = false;
	                }
	            }
	        });
	    }
	}*/

	//----------------------------------------------------------
	final void delEvent() {

		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem item1 = new JMenuItem("Remove Selection");
		final JMenuItem item2 = new JMenuItem("Remove All");
		//popup.setOpaque(true);
		item1.setOpaque(false);
		item2.setOpaque(false);
		popup.setBackground(Utils.toColor("ScrollBar.track"));

		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelection();
			}
		});

		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_listModel.removeAllElements();
				data.clear();
				propertyChangeListener.firePropertyChangeEvent(Constants.EVT_STOP);
			}
		});

		item1.setFont(new java.awt.Font("Dialog", 0, 10));
		item2.setFont(new java.awt.Font("Dialog", 0, 10));

		popup.add(item1);
		popup.add(item2);
		popup.setInvoker(this);

		// Don't go out of screen
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int h = popup.getHeight();
		// h = 0 !!! Dont know why.
		h = (h == 0) ? 43 : h;
		final int taskbar = 30;
		final int x = this.getLocationOnScreen().x + (int)del.getLocation().getX();
		int y = this.getLocationOnScreen().y + (int)del.getLocation().getY() /*+ this.scrollPane.getHeight()*/
		+del.getPreferredSize().height;
		y = (y + h >= screenSize.getHeight() - taskbar) ? y - h - del.getPreferredSize().height - 1 : y;

		popup.setLocation(x, y);
		popup.setVisible(true);

	}

	//----------------------------------------------------------
	synchronized final void deleteSelection() {
		final int[] selection = m_list.getSelectedIndices();
		for (int i = selection.length - 1; i >= 0; i--) {
			delete(selection[i]);
		}
	}

	//----------------------------------------------------------
	private final void delete(int i) {
		int size = m_listModel.size();
		int indexPlaying = data.getSelected();
		//Outil.p( " >>>>> i=" + i + "size=" + size + " indexPlaying=" + indexPlaying);
		if (i < size && 0 < size) {
			data.remove(i);
			m_listModel.remove(i);

			//--------
			for (int k = i; k < size - 1; k++) {
				m_listModel.set(k, data.getTag_infos().get(k));
			}

			if (i == size - 1) {
				m_list.setSelectedIndex(i - 1);
				if (indexPlaying == i) {
					data.setSelected(-1);
					propertyChangeListener.firePropertyChangeEvent(Constants.EVT_STOP);
				}
			} else {
				m_list.setSelectedIndex(i);

				if (indexPlaying > i) {
					data.setSelected(indexPlaying - 1);
				} else if (indexPlaying == i) {
					data.setSelected(-1);
					propertyChangeListener.firePropertyChangeEvent(Constants.EVT_STOP);
				} else {
					;
				}
			}
		}
	}

	//----------------------------------------------------------
	synchronized final void play() {
		data.setSelected(m_list.getSelectedIndex());
		m_list.repaint();
		propertyChangeListener.firePropertyChangeEvent(Constants.EVT_PLAY);
	}

	//----------------------------------------------------------
	public synchronized final void goNext() {

		//final int sel = m_list.getSelectedIndex();
		final int sel = data.getSelected();
		if (sel < m_listModel.size() - 1) {
			m_list.setSelectedIndex(sel + 1);
			data.setSelected(sel + 1);
		} else {
			m_list.setSelectedIndex(0);
			data.setSelected(0);
		}
		m_list.ensureIndexIsVisible(m_list.getSelectedIndex());
		m_list.repaint();
	}

	//----------------------------------------------------------
	public synchronized final void goPrev() {

		//final int sel = m_list.getSelectedIndex();
		final int sel = data.getSelected();
		if (sel > 0) {
			m_list.setSelectedIndex(sel - 1);
			data.setSelected(sel - 1);
		} else {
			m_list.setSelectedIndex(m_listModel.size() - 1);
			data.setSelected(m_listModel.size() - 1);
		}
		m_list.ensureIndexIsVisible(m_list.getSelectedIndex());
		m_list.repaint();
	}

	//----------------------------------------------------------
	public final Object getSource() {
		if (data.getSelected() < 0) {
			data.setSelected(0);
			m_list.repaint();
		}
		return data.getSource();
	}

	//----------------------------------------------------------
	public final Data getData() {
		return data;
	}

	//----------------------------------------------------------
	public final void eject() {
		addFilesActionEvent(true, true);
	}

	//----------------------------------------------------------
	public final Dimension getPreferredSize() {
		return new Dimension(Constants.PL_WIDTH, Constants.PL_HEIGHT);
	}

	/*================================================================*/
	/*---------------          AddThread inner class    --------------*/
	/*================================================================*/
	private final class AddFilesThread extends Thread {
		private final File[] files;
		private final boolean isFile;

		//----
		public AddFilesThread(boolean isFile, File[] files) {
			super("AddFilesThread");
			m_listModel = new DefaultListModel();
			this.files = files;
			this.isFile = isFile;
		}
		//----
		public final void run() {
			if (files != null) {
				synchronized (this) {
					parseFiles(isFile, files);
					load_PL();
					m_list.setModel(m_listModel);
				}
			}
		}
	}
	//============================

	/*================================================================*/
	/*--------------- UserlistCellRenderer inner class  --------------*/
	/*================================================================*/
	private final class UserlistCellRenderer extends DefaultListCellRenderer {

		public final Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (isSelected) {
				setIcon(Utils.getIcon("pl.icon.select"));
				setBackground(Utils.toColor("pl.list.select.bg", 50));
				setForeground(Utils.toColor("pl.list.select.bg"));
				//setBackground( Color.GREEN );
				//setOpaque(false);
			}

			if (index == data.getSelected()) {
				setIcon( Utils.getIcon("pl.icon.play"));
				setBackground(Utils.toColor("pl.list.select.bg", 100));
				setForeground(Utils.toColor("pl.list.select.bg").brighter());
				setFont(new Font("Dialog", Font.BOLD, 10));
			}

			return component;
		}
	}
	//============================

	//-------------------------------------------------------------
}
