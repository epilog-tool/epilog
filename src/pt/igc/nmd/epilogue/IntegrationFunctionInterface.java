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

public class IntegrationFunctionInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel integrationPanel;
	private JPanel textFieldPanel;
	private JButton AddIntegrationFunctionTextField;
	private JButton closeIntegrationPanel;

	private InitialConditions initialConditions;
	private Hashtable<Byte, String> integrationFunctionStrings;

	public IntegrationFunctionInterface(InitialConditions initialConditions) {
		super("Insert Integration Function");
		this.initialConditions = initialConditions;
		integrationFunctionStrings = new Hashtable<Byte, String>();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		
		setLayout(null);
		setVisible(true);

		getContentPane().setPreferredSize(new Dimension(500, 200));

		integrationPanel = new JPanel();
		textFieldPanel = new JPanel();
		AddIntegrationFunctionTextField = new JButton("Add");
		closeIntegrationPanel = new JButton("Close");

		//integrationPanel.setPreferredSize(new Dimension(500, 200));
		integrationPanel.setBounds(0, 0, 500, 200);
		integrationPanel.setLayout(null);
		integrationPanel.setBackground(Color.gray);
		integrationPanel.setLayout(layout);

		integrationPanel.setVisible(true);
		integrationPanel.add(AddIntegrationFunctionTextField);
		integrationPanel.add(closeIntegrationPanel);
		getContentPane().add(integrationPanel);

		AddIntegrationFunctionTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPanel textFieldPanel = new JPanel();
				final JTextField valueTextField = new JTextField();
				final JTextField functionTextField = new JTextField();
				
				valueTextField.setPreferredSize(new Dimension(30, 24));
				functionTextField.setPreferredSize(new Dimension(400, 24));
				textFieldPanel.setPreferredSize(new Dimension(450, 30));

				textFieldPanel.add(valueTextField);
				textFieldPanel.add(functionTextField);

				valueTextField.addFocusListener(new FocusListener() {

					@Override
					public void focusGained(FocusEvent arg0) {
					}

					@Override
					public void focusLost(FocusEvent arg0) {
						valueTextField.getText();

						integrationFunctionStrings.put(
								Byte.parseByte(valueTextField.getText()),
								functionTextField.getText());
						setInitialSetupHasChanged(true);
					}
				});

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
		});

		closeIntegrationPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
				// System.out.println(integrationFunctionStrings);
				// System.out.println("estou aqui");
				setIntegrationFunction(integrationFunctionStrings);
			}
		});
		pack();
	}

	protected void setIntegrationFunction(
			Hashtable<Byte, String> integrationFunctionStrings2) {
		initialConditions.setIntegrationFunction(integrationFunctionStrings);
	}

	private void setInitialSetupHasChanged(boolean b) {
		initialConditions.setInitialSetupHasChanged(b);
	}

}
