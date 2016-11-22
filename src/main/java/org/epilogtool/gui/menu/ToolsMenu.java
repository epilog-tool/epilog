package org.epilogtool.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

import org.epilogtool.gui.EpiGUI;

public class ToolsMenu {
	public static JMenu getMenu() {
		JMenu menu = new JMenu("Tools");
		menu.setMnemonic(KeyEvent.VK_T);

		menu.add(new SimulationAction());
		menu.add(new MonteCarloAction());

		return menu;
	}
}

class SimulationAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public SimulationAction() {
		super("Simulation");
		putValue(SHORT_DESCRIPTION, "Simulation");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().openSimulationTab();
	}
}

class MonteCarloAction extends AbstractAction {
	private static final long serialVersionUID = 1728730440633848251L;

	public MonteCarloAction() {
		super("Monte Carlo Simulation");
		putValue(SHORT_DESCRIPTION, "MonteCarlo");
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// FrameActionManager.MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EpiGUI.getInstance().openMonteCarloTab();
	}
}