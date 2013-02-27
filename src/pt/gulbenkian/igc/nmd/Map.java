package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Map extends JFrame {

	public JPanel contentPane;
	public static DrawPolygonM MapPanel;
	public final Color color;
	public static ArrayList<ArrayList<Cell>> cells;
	public int startX;
	public int startY;
	public int endX;
	public int endY;
	public String nodeId;
	public static int maxId;
	public int count;
	//private JButton newbtn;
	public static JPanel panelLights;
	public static MainPanel mainPanel;

	public Map(int x, int y, final Color color, String nodeId, int maxId,
			MainPanel mainPanel) {
		super("Map");
		setLayout(null);
		this.color = color;
		this.contentPane = new JPanel();
		this.startX = 0;
		this.startY = 0;
		this.endX = 0;
		this.endY = 0;
		this.nodeId = nodeId;
		this.maxId = maxId;
		this.count = 0;
		this.mainPanel = mainPanel;

		MapPanel = new DrawPolygonM(this, x, y, nodeId, maxId, this.mainPanel);
		contentPane.setBackground(Color.white);

		MapPanel.setLayout(null);
		setResizable(false);
		cells = new ArrayList<ArrayList<Cell>>();
		JButton buttonExpression = new JButton("Expression Level");
		buttonExpression.setBounds(10, 500, 200, 30);
		MapPanel.add(buttonExpression);
		panelLights = new JPanel();
		panelLights.setBounds(10, 540, 200, 40);
		panelLights.setBackground(Color.white);
		MapPanel.add(panelLights);
		JComboBox expressionLevels = new JComboBox();
		expressionLevels.setBounds(220, 500, 70, 30);
		for (int i = 1; i <= this.maxId; i++) {
			expressionLevels.addItem(i);
		}
		MapPanel.add(expressionLevels);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(300, 500, 100, 30);
		MapPanel.add(btnSave);
		JButton buttonClose = new JButton("Close");
		buttonClose.setBounds(410, 500, 100, 30);
		MapPanel.add(buttonClose);
		final MainPanel Mainpanel = mainPanel;
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Mainpanel.hexagonsPanel.cells = cells;
				Mainpanel.hexagonsPanel.paintComponent(Mainpanel.hexagonsPanel
						.getGraphics());

			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				int returnVal = fc.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fc.getSelectedFile(); // This grabs the File you
					// typed
					System.out.println(file.getAbsolutePath());

					try {
						//MapPanel.repaint();
						FileOutputStream fos = new FileOutputStream(file
								.getAbsolutePath());

						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(cells);

						oos.close();

						System.out.println(file.getAbsolutePath() + " ");

					} catch (IOException e2)

					{
						e2.printStackTrace();
					}
				}

			}

		});

		contentPane.add(MapPanel);

		setContentPane(contentPane);

		pack();
		setLocationByPlatform(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}

//	private int toggleCell(int i, int j) {
//		if (cells.get(i).get(j).G0 == 1)
//			return 0;
//		else
//			return 1;
//	}

	public void initialize() {

		MapPanel.paintComponent(MapPanel.getGraphics());

		MapPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
						/ 2);
				double ind_jt = Math.floor(ind_yts / (MapPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
				double yt = ind_yts - ind_jt * (MapPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > MapPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > MapPanel.radius
						* Math.abs(0.5 - (yt / MapPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				//System.out.println("i " + i + ", j " + j + " "+ MainPanel.getGridWidth());

				// The mouse is over a cell that belongs to the grid

				if (i < mainPanel.getGridWidth()
						&& j < mainPanel.getGridHeight() && i >= 0 && j >= 0) {
					DrawPolygonM.drawHexagon((int) i, (int) j,
							MapPanel.getGraphics(), color);
					System.out.println(cells.get(i).get(j).G0);
					cells.get(i).get(j).G0 = 1;

					MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), color);
					cells.get(i).get(j).color1 = color;

				}

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		MapPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

				startX = arg0.getX();
				startY = arg0.getX();

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				MapPanel.setBackground(Color.white);
				endX = arg0.getX();
				endY = arg0.getX();

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
						/ 2);
				double ind_jt = Math.floor(ind_yts / (MapPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
				double yt = ind_yts - ind_jt * (MapPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > MapPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > MapPanel.radius
						* Math.abs(0.5 - (yt / MapPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				System.out.println("i " + i + ", j " + j + " "
						+ mainPanel.getGridWidth());

				// The mouse is over a cell that belongs to the grid

				if (i < mainPanel.getGridWidth()
						&& j < mainPanel.getGridHeight() && i >= 0 && j >= 0) {

					MarkCell(cells.get(i).get(j), i, j, color);

					// }
					/*
					 * else{
					 * //MapPanel.clearHexagon(i,j,MapPanel.getGraphics());
					 * cells.get(i).get(j).G0--; if(cells.get(i).get(j).G0<0)
					 * cells.get(i).get(j).G0=0;
					 * 
					 * }
					 */System.out.println("i " + i + " j " + j + " "
							+ cells.get(i).get(j).color1);

				}

			}

		});

	}

	public void initializeCells(ArrayList<ArrayList<Cell>> cells) {
		// adicionar try catch para textFx e fy

		for (int i = 0; i < mainPanel.getGridWidth(); i++) {

			cells.add(new ArrayList<Cell>());
			for (int j = 0; j < mainPanel.getGridHeight(); j++) {
				int G0 = 0;
				cells.get(i).add(new Cell(G0));
			}

		}

	}

	public static void MarkCell(Cell c, int i, int j, Color color) {

		if (c.G0 == 0) {
			MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), color);

			System.out.println(c.G0);
			c.G0++;
			c.color1 = color;
			System.out.println(c.color1);

		}

		else if (c.G0 > maxId) {
			System.out.println(c.G0);
			c.G0 = 0;
			System.out.println(c.G0);
			MapPanel.clearHexagon(i, j, MapPanel.getGraphics());
		} else {
			Color nColor = c.color1;
			// c.color1 = c.color1
			// .darker();

			float hsbVals[] = Color.RGBtoHSB(c.color1.getRed(),
					c.color1.getGreen(), c.color1.getBlue(), null);
			c.color1 = Color.getHSBColor(hsbVals[0], hsbVals[1],
					0.91f * hsbVals[2]);

			MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), c.color1);
			c.G0++;

		}

	}

	public void clearAllCells(ArrayList<ArrayList<Cell>> cells) {
		// adicionar try catch para textFx e fy

		for (int i = 0; i < mainPanel.getGridWidth(); i++) {
			for (int j = 0; j < mainPanel.getGridHeight(); j++) {
				MapPanel.clearHexagon(i, j, MapPanel.getGraphics());
				cells.get(i).get(j).G0 = 0;

			}

		}

	}

	public void markAllCells(ArrayList<ArrayList<Cell>> cells) {
		// adicionar try catch para textFx e fy
		clearAllCells(cells);
		for (int i = 0; i < mainPanel.getGridWidth(); i++) {
			for (int j = 0; j < mainPanel.getGridHeight(); j++) {

				MarkCell(cells.get(i).get(j), i, j, color);

			}

		}

	}

}
