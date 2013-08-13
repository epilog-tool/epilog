package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.SphericalEpithelium;

public class IntegrationFunctionInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel integrationPanel;
	private JButton closeIntegrationPanel;
	private Hashtable<Byte, String> integrationFunctionStrings;
	private SphericalEpithelium epithelium;
	private MainFrame mainFrame;

	/**
	 * Summons the integration functions edition panel.
	 * 
	 * @param epithelium
	 *            epithelium model used
	 * @param node
	 *            node associated with the integration functions
	 */
	public IntegrationFunctionInterface(SphericalEpithelium epithelium,
			MainFrame mainFrame, final NodeInfo node) {
		super("Insert Integration Function");
		this.epithelium = epithelium;
		this.mainFrame = mainFrame;

		integrationFunctionStrings = new Hashtable<Byte, String>();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		setLayout(null);
		setVisible(true);

		getContentPane().setPreferredSize(new Dimension(500, 200));

		integrationPanel = new JPanel();
		closeIntegrationPanel = new JButton("Close");
		integrationPanel.setBounds(0, 0, 500, 200);
		integrationPanel.setLayout(null);
		integrationPanel.setBackground(Color.gray);
		integrationPanel.setLayout(layout);

		integrationPanel.setVisible(true);
		integrationPanel.add(closeIntegrationPanel);
		getContentPane().add(integrationPanel);

		for (byte targetValue = 1; targetValue <= node.getMax(); targetValue++) {
			// System.out.println("teste"+epithelium.getIntegrationFunctions().get(node)
			// .get(targetValue));
			JPanel textFieldPanel = new JPanel();
			final JTextField valueTextField = new JTextField();
			final JTextField functionTextField = new JTextField();

			valueTextField.setText("" + targetValue);

			if (epithelium.isIntegrationComponent(node))
				functionTextField.setText(""
						+ epithelium.getIntegrationFunction(node, targetValue));

			valueTextField.setPreferredSize(new Dimension(30, 24));
			functionTextField.setPreferredSize(new Dimension(400, 24));
			textFieldPanel.setPreferredSize(new Dimension(450, 30));

			textFieldPanel.add(valueTextField);
			textFieldPanel.add(functionTextField);

			functionTextField.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					functionTextField.getText();
					integrationFunctionStrings.put(
							Byte.parseByte(valueTextField.getText()),
							functionTextField.getText());
				}
			});

			integrationPanel.add(textFieldPanel);
			integrationPanel.revalidate();
			integrationPanel.repaint();
		}

		closeIntegrationPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO: CHECK
				if (checkFunction(node, integrationFunctionStrings)) {
					setIntegrationFunction(node, integrationFunctionStrings);
					dispose();
					resetErrorMessage();
				}
				
			}
		});
		pack();
	}

	private void resetErrorMessage(){
		mainFrame.setUserMessageEmpty();
	}
	
	/**
	 * Saves the integration functions written in the epithelium model.
	 * 
	 * @param integrationFunctionStrings
	 *            hashtable that relates the target value with the integration
	 *            function
	 * @param node
	 *            node associated with the integration functions
	 */
	public void setIntegrationFunction(NodeInfo node,
			Hashtable<Byte, String> integrationFunctionStrings) {

		for (byte targetValue : integrationFunctionStrings.keySet())

			epithelium.setIntegrationFunctions(node, targetValue,
					integrationFunctionStrings.get(targetValue));

	}

	private boolean checkFunction(NodeInfo node,
			Hashtable<Byte, String> integrationFunctionStrings) {

		for (String integrationFunction : integrationFunctionStrings.values()) {
			integrationFunction.replace(" ", "");
			String nodeString = integrationFunction.split("\\(")[0];

			
			//CHECKS IF NODE BELONGS TO NODELIST
			String nodeList = "";

			for (NodeInfo nodeAux : epithelium.getUnitaryModel().getNodeOrder())
				if (!epithelium.isIntegrationComponent(nodeAux))
					nodeList = nodeList + nodeAux.getNodeID() + " ";

			if (!checkNode(nodeString)) {
				mainFrame.setUserMessage(MsgStatus.ERROR, "Regulating node does not exist. ( Nodes: "
								+ nodeList + " )");
				return false;
			}
			
			//CHECKS IF MINIMUM IS LOWER OR EQUAL THAN MAX
			

		}
		return true;
	}

	private boolean checkNode(String nodeString) {
		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder())
			if (nodeString.equals(node.getNodeID()))
				return true;
		return false;
	}
}
