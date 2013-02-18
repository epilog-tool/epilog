package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.ItemSelectable;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.NodeInfo;

public class DrawPolygon extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6126822003689575762L;

	private double height = 0.0, radius = 0.0;

	private Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };

	private Color background = Color.WHITE;
	private MapColorPanel mpanel = null;
	private MainPanel mainPanel = null;

	public DrawPolygon(MainPanel frame) {

		this.mainPanel = frame;
		this.setBackground(background);
		this.setPreferredSize(new Dimension(500, 500));
		this.mainPanel.setTitle("Hexagons");
		this.mainPanel.setBackground(Color.cyan);
		Container contentPane = this.mainPanel.getContentPane();
		contentPane.add(this);

	}

	public MainPanel getMainPanel() {
		return this.mainPanel;
	}

	public void drawHexagon(int i, int j, Graphics g) {
		double centerX = 0, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon.addPoint((int) (x), (int) (y));

		}

		g.fillPolygon(polygon);
	}

	public void clearHexagon(int i, int j, Graphics g) {
		double centerX = 0, centerY = 0, x = 0, y = 0;

		if (i % 2 == 0) {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0)
					/ 2;
		} else {
			centerX = (1.5 * radius * (i)) + radius;
			centerY = (j) * radius * Math.sqrt(3.0) + radius * Math.sqrt(3.0);
		}

		Polygon polygon = new Polygon();

		for (int k = 0; k < 6; k++) {

			x = centerX + radius * Math.cos(k * 2 * Math.PI / 6);
			y = centerY + radius * Math.sin(k * 2 * Math.PI / 6);
			polygon.addPoint((int) (x), (int) (y));

		}

		g.fillPolygon(polygon);
		g.drawPolygon(polygon);

	}

	public void paintComponent(Graphics g) {
		int width = 0, height = 0, max = 0;

		this.getMainPanel();
		width = MainPanel.getGridWidth();
		this.getMainPanel();
		height = MainPanel.getGridHeight();
		max = Math.max(width, height);


		double pxHeight = 0.0;

		if (width > 0 && height > 0) {
			if (height == max) {

				pxHeight = 500 / max;
				pxHeight = (500 - 1 * pxHeight) / (max);
				radius = pxHeight / Math.sqrt(3.0);
			}

			else {
				radius = (500 / width) / 2;
				pxHeight = radius * Math.sqrt(3.0);
			}

			double x = 0, y = 0, centerX = radius, centerY = 0;

			super.paintComponent(g);

			for (int k = 0; k < width; k++) {
				if (k == 0)
					centerX = radius;
				else
					centerX = (1.5 * radius * (k)) + radius;
				if (k % 2 == 0)
					centerY = radius * Math.sqrt(3.0) / 2;// h
				else
					centerY = radius * Math.sqrt(3.0);// h+h/2
				for (int j = 0; j < height; j++) {

					Polygon polygon = new Polygon();

					for (int i = 0; i < 6; i++) {

						x = centerX + radius * Math.cos(i * 2 * Math.PI / 6);
						y = centerY + radius * Math.sin(i * 2 * Math.PI / 6);
						polygon.addPoint((int) (x), (int) (y));

					}

					g.drawPolygon(polygon);
					// System.out.println(centerY);

					if (k % 2 == 0)
						centerY = (j + 1 + 0.5) * radius * Math.sqrt(3.0);
					else
						centerY = (j + 2) * radius * Math.sqrt(3.0);

				}

				// System.out.println(centerX);

			}
			// System.out.println("count "+count+ " h "+radius*Math.sqrt(3.0)+
			// " Y "+centerY+" "+(500-radius*Math.sqrt(3.0))+" nh "+numberHex);
		} else {
			System.out.println("XX e YY têm que ser maiores do que zero");

		}

	}

	public void hexagonsLoadModel(Graphics g, ArrayList<ArrayList<Cell>> cells,
			MapColorPanel panel, List<NodeInfo> listNodes, String sbmlpath) {

		int XX = 0, YY = 0; // centers
		int max = 0;

		int variables = listNodes.size();
		int pos = 0;
		int green = 0;
		int newColor = 30;
		this.mpanel = panel;
		final String SBMLpath = sbmlpath;

		for (int k = 0; k < variables; k++) {
			pos = k;

			JCheckBox j = new JCheckBox(listNodes.get(k).getNodeID());

			// ---------

			final ColorButton l = new ColorButton(panel);
			JComboBox jc1 = new JComboBox();


			j.setBackground(Color.white);
			j.setBounds(10, 10 + pos * 60, 50, 30);

			if (listNodes.get(k).isInput()) {

				mpanel.mapcolor = l.getBackground(); // ask button directly

				jc1.setBounds(100, 10 + pos * 60, 120, 30);
				jc1.addItem("Select input");
				jc1.addItem("Env input");
				jc1.addItem("Int input");

				jc1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						System.out.println("Command: "
								+ actionEvent.getActionCommand());
						ItemSelectable is = (ItemSelectable) actionEvent
								.getSource();
						System.out.println(", Selected: " + selectedString(is));

						if (selectedString(is).equalsIgnoreCase("Env input")) {
							System.out.println("teste");

							// ---
							JButton btnDraw = new JButton("Draw");
							btnDraw.setBounds(0, 0, 100, 30);
							background = l.getBackground();
							// btnDraw.addActionListener(new ActionListener() {
							// public void actionPerformed(ActionEvent e) {
							// new Map(textFX.getText(), textFY.getText(),
							// l.getBackground(), SBMLpath,
							// nodeId, maxId).initialize();
							//
							// }
							// });

							JButton btnLoad = new JButton("Load");
							btnLoad.setBounds(110, 0, 100, 30);
							background = l.getBackground();
//							btnLoad.addActionListener(new ActionListener() {
//								public void actionPerformed(ActionEvent e) {
//									JFileChooser fc = new JFileChooser();
//
//									fc.setDialogTitle("Choose file");
//									int open = fc.showOpenDialog(null);
//									if (open == 0) {
//										File file = fc.getSelectedFile();
//
//										System.out.println(l.getBackground());
//										Map map = new Map(textFX.getText(),
//												textFY.getText(), l
//														.getBackground(),
//												SBMLpath, nodeId, maxId);
//										final ArrayList<ArrayList<Cell>> cells = getMappedCells(file
//												.getAbsolutePath());
//										map.initialize(cells);
//
//									}
//
//								}
//							});

							mpanel.revalidate();
							mpanel.repaint();

						}

						else if (selectedString(is).equalsIgnoreCase(
								"Int input")) {
							final JTextField textFormula = new JTextField();
							textFormula.setBounds(0, 0, 150, 30);
							mpanel.add(textFormula);
							mpanel.revalidate();
							mpanel.repaint();

							textFormula.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									String logicalFunction = textFormula
											.getText();

								}
							});

						} else {

							mpanel.revalidate();
							mpanel.repaint();
						}
					}
				});

				panel.add(jc1);

			}

			l.setBounds(60, 10 + pos * 60, 30, 30);
			panel.add(j);
			panel.add(l);

			panel.revalidate();
			panel.repaint();

		}

		try {
			XX = MainPanel.getGridWidth();
			YY = MainPanel.getGridHeight();
			max = Math.max(XX, YY);
			System.out.println("max " + max);
			System.out.println("XX " + XX);
		} catch (NumberFormatException e) {
			System.out
					.println("java.lang.NumberFormatException: For input string: ''");

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (XX > 0 && YY > 0) {
			if (YY == max) {

				height = 500 / max;
				height = (500 - 1 * height) / (max);
				radius = height / Math.sqrt(3.0);

			}

			else {
				radius = (500 / XX) / 2;

				height = radius * Math.sqrt(3.0);
			}
			System.out.println("height: " + height);
			System.out.println("radius: " + radius);

			double x = 0, y = 0, centerX = radius, centerY = 0;

			int numberHex = 500 / ((int) (radius * Math.sqrt(3.0)) + 1);

			super.paintComponent(g);
			g.setColor(Color.black);

			int count = 0;

			for (int k = 0; k < XX; k++) {
				if (k == 0)
					centerX = radius;
				else
					centerX = (1.5 * radius * (k)) + radius;
				if (k % 2 == 0)
					centerY = radius * Math.sqrt(3.0) / 2;// h
				else
					centerY = radius * Math.sqrt(3.0);// h+h/2
				for (int j = 0; j < YY; j++) {

					Polygon polygon2 = new Polygon();

					for (int i = 0; i < 6; i++) {

						x = centerX + radius * Math.cos(i * 2 * Math.PI / 6);
						y = centerY + radius * Math.sin(i * 2 * Math.PI / 6);
						polygon2.addPoint((int) (x), (int) (y));

					}

					g.setColor(Color.black);
					g.drawPolygon(polygon2);
					// System.out.println(centerY);
					/*
					 * if(cells.get(k).get(j).G0==0){ g.setColor(Color.blue);
					 * g.fillPolygon(polygon2);
					 * 
					 * }
					 */
					if (k % 2 == 0)
						centerY = (j + 1 + 0.5) * radius * Math.sqrt(3.0);
					else
						centerY = (j + 2) * radius * Math.sqrt(3.0);

				}

				// System.out.println(centerX);

			}
			// System.out.println("count "+count+ " h "+radius*Math.sqrt(3.0)+
			// " Y "+centerY+" "+(500-radius*Math.sqrt(3.0))+" nh "+numberHex);
		} else {
			System.out.println("XX e YY têm que ser maiores do que zero");

		}

	}

	static private String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	public static ArrayList<ArrayList<Cell>> getMappedCells(String file_name) {
		ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file_name, true));
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					file_name));

			cells = (ArrayList<ArrayList<Cell>>) ois.readObject();
		} catch (FileNotFoundException e) { // TODO Auto-generated catch block

		} catch (IOException e) { // TODO Auto-generated catch block

		} catch (ClassNotFoundException e) { // TODO Auto-generated catch block

		}

		finally {
			return cells;

		}
	}

}