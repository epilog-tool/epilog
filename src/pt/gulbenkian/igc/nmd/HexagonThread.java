package pt.gulbenkian.igc.nmd;



public class HexagonThread extends Thread {

	public DrawPolygon panel;
	int x, y;

	public HexagonThread(DrawPolygon panel, int x, int y) {
		super();
		this.panel = panel;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (true) {
			try {
				this.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			panel.clearHexagon(x, y, panel.getGraphics());
			panel.drawHexagon(x, y - 1, panel.getGraphics());
			panel.drawHexagon(x + 1, y - 1, panel.getGraphics());
			panel.drawHexagon(x + 1, y, panel.getGraphics());
			panel.drawHexagon(x, y + 1, panel.getGraphics());
			panel.drawHexagon(x - 1, y, panel.getGraphics());
			panel.drawHexagon(x - 1, y - 1, panel.getGraphics());
			try {
				this.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			panel.drawHexagon(x, y, panel.getGraphics());
			panel.clearHexagon(x, y - 1, panel.getGraphics());
			panel.clearHexagon(x + 1, y - 1, panel.getGraphics());
			panel.clearHexagon(x + 1, y, panel.getGraphics());
			panel.clearHexagon(x, y + 1, panel.getGraphics());
			panel.clearHexagon(x - 1, y, panel.getGraphics());
			panel.clearHexagon(x - 1, y - 1, panel.getGraphics());

		}

	}

}
