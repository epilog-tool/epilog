package org.ginsim.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ginsim.epilog.gui.color.ColorUtils;

public class NewProjectDialog extends JPanel {
	private static final long serialVersionUID = 1877338344309723137L;

	private final String DEFAULT_WIDTH = "20";
	private final String DEFAULT_HEIGHT = "20";

	private JTextField jtfWidth;
	private JTextField jtfHeight;
	private DefaultListModel<String> dlmModels;

	private List<File> fileList;
	private int width;
	private int height;
	private boolean bIsOK;

	public NewProjectDialog() {
		setLayout(new BorderLayout());
		this.fileList = new ArrayList<File>();

		// PAGE_START begin
		JPanel top = new JPanel(new FlowLayout());
		top.add(new JLabel("Width"));
		jtfWidth = new JTextField(DEFAULT_WIDTH, 5);
		jtfWidth.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextField(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		top.add(jtfWidth);
		top.add(new JLabel("Height"));
		jtfHeight = new JTextField(DEFAULT_HEIGHT, 5);
		jtfHeight.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateTextField(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		top.add(jtfHeight);
		this.add(top, BorderLayout.PAGE_START);

		// CENTER begin
		JPanel center = new JPanel(new BorderLayout());
		dlmModels = new DefaultListModel<String>();
		JList<String> list = new JList<String>(dlmModels);
		center.add(list, BorderLayout.CENTER);
		JPanel line = new JPanel(new FlowLayout());
		JLabel label = new JLabel("Add SBML");
		line.add(label);
		JButton buttonChooser = new JButton("Browse");
		buttonChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseSBMLFile();
			}
		});
		line.add(buttonChooser);
		center.add(line, BorderLayout.PAGE_END);
		this.add(center, BorderLayout.CENTER);

		// PAGE_END begin
		JPanel bottom = new JPanel(new FlowLayout());
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(false);
			}
		});
		bottom.add(cancel);
		JButton next = new JButton("OK");
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAction(true);
			}
		});
		bottom.add(next);
		this.add(bottom, BorderLayout.PAGE_END);
	}

	private void validateTextField(KeyEvent e) {
		JTextField jtf = (JTextField) e.getSource();
		boolean valid = false;
		try {
			Integer.parseInt(jtf.getText());
			valid = true;
		} catch (NumberFormatException nfe) {
		}
		if (!valid) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		} else {
			jtf.setBackground(Color.WHITE);
		}
	}

	private void buttonAction(boolean option) {
		this.bIsOK = option;
		if (option) {
			try {
				this.width = Integer.parseInt(this.jtfWidth.getText());
				this.height = Integer.parseInt(this.jtfHeight.getText());
			} catch (NumberFormatException nfe) {
				return;
			}
		}
		Window win = SwingUtilities.getWindowAncestor(this);
		if (win != null) {
			win.dispose();
		}
	}

	public int getProjWidth() {
		return this.width;
	}

	public int getProjHeight() {
		return this.height;
	}

	public List<File> getFileList() {
		return this.fileList;
	}

	public boolean isDefined() {
		return this.bIsOK;
	}

	private void chooseSBMLFile() {
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
				"sbml files (*.sbml)", "sbml");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(xmlfilter);
		fc.setDialogTitle("Open file");
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			for (File f : this.fileList) {
				if (fc.getSelectedFile().getName().equals(f.getName())) {
					return;
				}
			}
			this.fileList.add(fc.getSelectedFile());
			this.dlmModels.addElement(fc.getSelectedFile()
					.getAbsolutePath());
		}
	}
}
