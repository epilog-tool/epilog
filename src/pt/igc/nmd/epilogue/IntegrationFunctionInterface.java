package pt.igc.nmd.epilogue;

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

import org.antlr.runtime.RecognitionException;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

public class IntegrationFunctionInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel integrationPanel;
	private JButton closeIntegrationPanel;
	private NodeInfo node;
	private SetupConditions setupConditions;
	private Hashtable<Byte, String> integrationFunctionStrings;
	private SphericalEpithelium epithelium;
	

	public IntegrationFunctionInterface(SphericalEpithelium epithelium, SetupConditions setupConditions,
			final NodeInfo node) {
		super("Insert Integration Function");
		this.setupConditions = setupConditions;
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
			JPanel textFieldPanel = new JPanel();
			final JTextField valueTextField = new JTextField();
			final JTextField functionTextField = new JTextField();

			valueTextField.setText("" + targetValue);
			

			valueTextField.setPreferredSize(new Dimension(30, 24));
			functionTextField.setPreferredSize(new Dimension(400, 24));
			textFieldPanel.setPreferredSize(new Dimension(450, 30));
			
//			functionTextField.setText(setupConditions.getIntegrationFunction(integrationFunctionStrings).get(Byte.parseByte(valueTextField.getText())));
//			System.out.println("integrationfunctioninterface" + setupConditions.getIntegrationFunction());
			
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
					setInitialSetupHasChanged(true);
				}
			});

			integrationPanel.add(textFieldPanel);
			integrationPanel.revalidate();
			integrationPanel.repaint();
		}

		closeIntegrationPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
				setIntegrationFunction(node, integrationFunctionStrings);
			}
		});
		pack();
	}

	protected void setIntegrationFunction(NodeInfo node,
			Hashtable<Byte, String> integrationFunctionStrings2) {

			IntegrationFunctionSpecification spec = new IntegrationFunctionSpecification();
			IntegrationExpression expression = null;
			Hashtable<Byte, IntegrationExpression> valueOfIntegrationFunction = new Hashtable<Byte, IntegrationExpression>();

			for (byte targetValue : integrationFunctionStrings2.keySet()) {

				try {
					expression = spec.parse(integrationFunctionStrings2
							.get(targetValue));
				} catch (RecognitionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				valueOfIntegrationFunction.put(targetValue, expression);
			}
		
		epithelium.setIntegrationFunctions(node, valueOfIntegrationFunction);
	}

	private void setInitialSetupHasChanged(boolean b) {
		setupConditions.setInitialSetupHasChanged(b);
	}

}
