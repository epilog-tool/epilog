package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumPhenotypes;
import org.epilogtool.core.EpitheliumPhenotypes.Phenotype;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.Project;

public class EpiTabPhenotypeDefinitions extends EpiTabDefinitions {
	private static final long serialVersionUID = 1L;

	private EpitheliumPhenotypes userPhenotypes;
	private JComboBox<String> modelSelector;
	private JTable table;
	private DefaultTableModel tableModel;
	private LogicalModel selectedModel;
	private JLabel overlapWarning;
	private boolean rebuilding;
	private boolean refreshingModels;
	private TabProbablyChanged changes;

	public EpiTabPhenotypeDefinitions(Epithelium epithelium, TreePath path, TabChangeNotifyProj tabChanged) {
		super(epithelium, path, tabChanged);
	}

	@Override
	public void initialize() {
		this.center.setLayout(new BorderLayout(5, 5));
		this.changes = new TabProbablyChanged();
		this.userPhenotypes = this.epithelium.getPhenotypes().clone();

		List<LogicalModel> models = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		List<String> modelNames = new ArrayList<String>();
		for (LogicalModel model : models) {
			modelNames.add(Project.getInstance().getProjectFeatures().getModelName(model));
			if (!this.userPhenotypes.getModelSet().contains(model)) {
				this.userPhenotypes.addModel(model);
			}
		}

		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		this.modelSelector = new JComboWideBox<String>(modelNames.toArray(new String[modelNames.size()]));
		this.modelSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (refreshingModels) {
					return;
				}
				String modelName = (String) modelSelector.getSelectedItem();
				if (modelName == null) {
					return;
				}
				selectedModel = Project.getInstance().getProjectFeatures().getModel(modelName);
				loadTable();
			}
		});
		header.add(this.modelSelector);
		this.center.add(header, BorderLayout.NORTH);

		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if ("tableCellEditor".equals(event.getPropertyName()) && table.isEditing()) {
					changes.setChanged();
				}
			}
		});
		this.center.add(new JScrollPane(this.table), BorderLayout.CENTER);

		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addButton(controls, "Add", new Runnable() {
			@Override
			public void run() {
				Object[] row = new Object[tableModel.getColumnCount()];
				row[0] = "Phenotype_" + (tableModel.getRowCount() + 1);
				for (int column = 1; column < row.length; column++) {
					row[column] = "*";
				}
				tableModel.addRow(row);
			}
		});
		addButton(controls, "Clone", new Runnable() {
			@Override
			public void run() {
				int row = table.getSelectedRow();
				if (row >= 0) {
					Object[] copy = new Object[tableModel.getColumnCount()];
					for (int column = 0; column < copy.length; column++) {
						copy[column] = tableModel.getValueAt(row, column);
					}
					copy[0] = copy[0] + "_copy";
					tableModel.insertRow(row + 1, copy);
					table.setRowSelectionInterval(row + 1, row + 1);
				}
			}
		});
		addButton(controls, "Remove", new Runnable() {
			@Override
			public void run() {
				int[] rows = table.getSelectedRows();
				for (int index = rows.length - 1; index >= 0; index--) {
					tableModel.removeRow(rows[index]);
				}
			}
		});
		addButton(controls, "↑", new Runnable() {
			@Override
			public void run() {
				moveSelectedRow(-1);
			}
		}).setToolTipText("Move phenotype up");
		addButton(controls, "↓", new Runnable() {
			@Override
			public void run() {
				moveSelectedRow(1);
			}
		}).setToolTipText("Move phenotype down");
		addButton(controls, "Edit by text", new Runnable() {
			@Override
			public void run() {
				openTextEditor();
			}
		});

		this.overlapWarning = new JLabel(" ");
		this.overlapWarning.setForeground(Color.RED);
		JPanel footer = new JPanel(new BorderLayout());
		footer.add(controls, BorderLayout.NORTH);
		footer.add(this.overlapWarning, BorderLayout.SOUTH);
		this.center.add(footer, BorderLayout.SOUTH);

		if (!modelNames.isEmpty()) {
			this.selectedModel = models.get(0);
			this.loadTable();
		}
		this.isInitialized = true;
	}

	private JButton addButton(JPanel panel, String label, final Runnable action) {
		JButton button = new JButton(label);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				action.run();
			}
		});
		panel.add(button);
		return button;
	}

	private void openTextEditor() {
		if (this.table.isEditing()) {
			this.table.getCellEditor().stopCellEditing();
		}
		this.updateStagedPhenotypes();

		JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit Phenotype definitions",
				Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setLayout(new BorderLayout(5, 5));
		JTextArea text = new JTextArea(this.getPhenotypeText(), 10, 50);
		text.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN,
				text.getFont().getSize() + 4));
		text.setLineWrap(false);
		JScrollPane textScroll = new JScrollPane(text);
		textScroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
		dialog.add(textScroll, BorderLayout.CENTER);

		JPanel footer = new JPanel(new BorderLayout());
		JTextPane help = new JTextPane();
		help.setContentType("text/html");
		help.setText(this.getPhenotypeHelpText());
		help.setEditable(false);
		help.setOpaque(false);
		help.setBorder(BorderFactory.createTitledBorder("Model data and editing tips"));
		footer.add(help, BorderLayout.CENTER);
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton save = new JButton("Save as");
		save.addActionListener(event -> savePhenotypeText(dialog, text));
		buttons.add(save);
		JButton load = new JButton("Load file");
		load.addActionListener(event -> loadPhenotypeText(dialog, text));
		buttons.add(load);
		JButton apply = new JButton("Apply and close");
		apply.addActionListener(event -> {
			try {
				EpitheliumPhenotypes parsed = parsePhenotypeText(text.getText());
				this.userPhenotypes = parsed;
				this.loadTable();
				this.updateStagedPhenotypes();
				dialog.dispose();
			} catch (IllegalArgumentException exception) {
				JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Invalid phenotype definitions",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		buttons.add(apply);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(event -> dialog.dispose());
		buttons.add(cancel);
		footer.add(buttons, BorderLayout.SOUTH);
		dialog.add(footer, BorderLayout.SOUTH);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private String getPhenotypeHelpText() {
		StringBuilder help = new StringBuilder("<html><body><b>Variable order:</b><br>");
		for (LogicalModel model : this.epithelium.getEpitheliumGrid().getModelSet()) {
			List<NodeInfo> components = model.getComponents();
			help.append(escapeHtml(Project.getInstance().getProjectFeatures().getModelName(model))).append(" (")
					.append(components.size()).append(components.size() == 1 ? " var): " : " vars): ");
			for (int index = 0; index < components.size(); index++) {
				if (index > 0) {
					help.append(',');
				}
				help.append(escapeHtml(components.get(index).getNodeID()));
			}
			help.append("<br>");
		}
		help.append("<br><b>Syntax:</b><br>PH [MODEL] [PHENOTYPE NAME] [PATTERN]<br>")
				.append("(Pattern values follow the variable order; use * or a valid node level for each variable)")
				.append("</body></html>");
		return help.toString();
	}

	private String escapeHtml(String value) {
		return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	private void savePhenotypeText(JDialog dialog, JTextArea text) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
		if (chooser.showSaveDialog(dialog) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selected = chooser.getSelectedFile();
		File destination = selected.getName().toLowerCase().endsWith(".txt") ? selected
				: new File(selected.getAbsolutePath() + ".txt");
		if (destination.exists() && JOptionPane.showConfirmDialog(dialog, "Replace file?", "Save phenotype definitions",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			Files.write(destination.toPath(), text.getText().getBytes(StandardCharsets.UTF_8));
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Cannot save phenotype definitions",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadPhenotypeText(JDialog dialog, JTextArea text) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
		if (chooser.showOpenDialog(dialog) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			text.setText(new String(Files.readAllBytes(chooser.getSelectedFile().toPath()), StandardCharsets.UTF_8));
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Cannot load phenotype definitions",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String getPhenotypeText() {
		StringBuilder text = new StringBuilder();
		for (LogicalModel model : this.epithelium.getEpitheliumGrid().getModelSet()) {
			String modelName = Project.getInstance().getProjectFeatures().getModelName(model);
			Set<Phenotype> phenotypes = this.userPhenotypes.getPhenotypes(model);
			if (phenotypes == null) {
				continue;
			}
			for (Phenotype phenotype : phenotypes) {
				text.append("PH ").append(modelName).append(' ').append(phenotype.getName()).append(' ')
						.append(phenotype.getPheno()).append('\n');
			}
		}
		return text.toString();
	}

	private EpitheliumPhenotypes parsePhenotypeText(String source) {
		EpitheliumPhenotypes parsed = new EpitheliumPhenotypes();
		List<LogicalModel> models = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model : models) {
			parsed.addModel(model);
		}

		String[] lines = source.split("\\R");
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			String line = lines[lineIndex].trim();
			if (line.isEmpty()) {
				continue;
			}
			String[] fields = line.split("\\s+", 3);
			if (fields.length != 3 || !"PH".equals(fields[0])) {
				throw new IllegalArgumentException("Line " + (lineIndex + 1)
						+ ": expected PH, model name, phenotype name, and pattern.");
			}
			LogicalModel model = Project.getInstance().getProjectFeatures().getModel(fields[1]);
			if (model == null || !this.epithelium.getEpitheliumGrid().getModelSet().contains(model)) {
				throw new IllegalArgumentException("Line " + (lineIndex + 1) + ": unknown model '" + fields[1] + "'.");
			}
			String phenotypeAndPattern = fields[2].trim();
			int lastSeparator = phenotypeAndPattern.lastIndexOf(' ');
			if (lastSeparator <= 0 || lastSeparator == phenotypeAndPattern.length() - 1) {
				throw new IllegalArgumentException("Line " + (lineIndex + 1)
						+ ": expected a phenotype name followed by a pattern.");
			}
			String phenotypeName = phenotypeAndPattern.substring(0, lastSeparator).trim();
			String pattern = phenotypeAndPattern.substring(lastSeparator + 1).trim();
			if (pattern.length() != model.getComponents().size()) {
				throw new IllegalArgumentException("Line " + (lineIndex + 1) + ": pattern must contain "
						+ model.getComponents().size() + " values in model component order.");
			}
			for (int index = 0; index < pattern.length(); index++) {
				char value = pattern.charAt(index);
				int maximum = model.getComponents().get(index).getMax();
				if (value != '*' && (value < '0' || value > '9' || value - '0' > maximum)) {
					throw new IllegalArgumentException("Line " + (lineIndex + 1) + ": invalid value '" + value
							+ "' for component " + model.getComponents().get(index).getNodeID() + ".");
				}
			}
			parsed.addPhenotype(model, phenotypeName, pattern);
		}
		return parsed;
	}

	private void loadTable() {
		if (this.selectedModel == null) {
			return;
		}
		this.rebuilding = true;
		List<NodeInfo> nodes = this.selectedModel.getComponents();
		String[] columns = new String[nodes.size() + 1];
		columns[0] = "Name";
		for (int index = 0; index < nodes.size(); index++) {
			columns[index + 1] = nodes.get(index).getNodeID();
		}
		this.tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return true;
			}
		};
		Set<Phenotype> phenotypes = this.userPhenotypes.getPhenotypes(this.selectedModel);
		if (phenotypes != null) {
			for (Phenotype phenotype : phenotypes) {
				Object[] row = new Object[columns.length];
				row[0] = phenotype.getName();
				for (int index = 0; index < phenotype.getPheno().length() && index + 1 < row.length; index++) {
					row[index + 1] = String.valueOf(phenotype.getPheno().charAt(index));
				}
				this.tableModel.addRow(row);
			}
		}
		this.table.setModel(this.tableModel);
		for (int index = 0; index < nodes.size(); index++) {
			List<String> values = new ArrayList<String>();
			values.add("*");
			for (int value = 0; value <= nodes.get(index).getMax(); value++) {
				values.add(String.valueOf(value));
			}
			this.table.getColumnModel().getColumn(index + 1)
					.setCellEditor(new DefaultCellEditor(new JComboBox<String>(values.toArray(new String[values.size()]))));
		}
		this.tableModel.addTableModelListener(event -> {
			if (!this.rebuilding) {
				updateStagedPhenotypes();
			}
		});
		this.rebuilding = false;
		this.updateOverlapWarning();
	}

	private void moveSelectedRow(int offset) {
		int row = this.table.getSelectedRow();
		int target = row + offset;
		if (row < 0 || target < 0 || target >= this.tableModel.getRowCount()) {
			return;
		}
		this.tableModel.moveRow(row, row, target);
		this.table.setRowSelectionInterval(target, target);
	}

	private void updateStagedPhenotypes() {
		this.userPhenotypes.removeModel(this.selectedModel);
		this.userPhenotypes.addModel(this.selectedModel);
		for (int row = 0; row < this.tableModel.getRowCount(); row++) {
			Object nameValue = this.tableModel.getValueAt(row, 0);
			if (nameValue == null || nameValue.toString().trim().isEmpty()) {
				continue;
			}
			StringBuilder pattern = new StringBuilder();
			boolean complete = true;
			for (int column = 1; column < this.tableModel.getColumnCount(); column++) {
				Object value = this.tableModel.getValueAt(row, column);
				if (value == null) {
					complete = false;
					break;
				}
				pattern.append(value.toString());
			}
			if (complete) {
				this.userPhenotypes.addPhenotype(this.selectedModel, nameValue.toString().trim(), pattern.toString());
			}
		}
		this.changes.setChanged();
		this.updateOverlapWarning();
	}

	private void updateOverlapWarning() {
		if (this.overlapWarning == null || this.selectedModel == null) {
			return;
		}
		this.overlapWarning.setText(this.userPhenotypes.getOverlap(this.selectedModel).isEmpty()
				? " " : "Some phenotype patterns overlap!");
	}

	@Override
	protected void buttonReset() {
		this.userPhenotypes = this.epithelium.getPhenotypes().clone();
		this.loadTable();
	}

	@Override
	protected void buttonAccept() {
		if (this.table.isEditing()) {
			this.table.getCellEditor().stopCellEditing();
		}
		this.updateStagedPhenotypes();
		this.epithelium.setPhenotypes(this.userPhenotypes.clone());
		this.userPhenotypes = this.epithelium.getPhenotypes().clone();
		this.loadTable();
	}

	@Override
	protected boolean isChanged() {
		if (this.table.isEditing()) {
			this.table.getCellEditor().stopCellEditing();
		}
		this.updateStagedPhenotypes();
		return !this.userPhenotypes.equals(this.epithelium.getPhenotypes());
	}

	@Override
	public void applyChange() {
		if (this.table.isEditing()) {
			this.table.getCellEditor().stopCellEditing();
		}
		String previousModelName = this.modelSelector.getSelectedItem() == null ? null
				: this.modelSelector.getSelectedItem().toString();
		List<LogicalModel> models = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		Set<LogicalModel> currentModels = new java.util.HashSet<LogicalModel>(models);
		for (LogicalModel model : new ArrayList<LogicalModel>(this.userPhenotypes.getModelSet())) {
			if (!currentModels.contains(model)) {
				this.userPhenotypes.removeModel(model);
			}
		}
		for (LogicalModel model : models) {
			if (!this.userPhenotypes.getModelSet().contains(model)) {
				this.userPhenotypes.addModel(model);
			}
		}

		this.refreshingModels = true;
		try {
			this.modelSelector.removeAllItems();
			for (LogicalModel model : models) {
				this.modelSelector.addItem(Project.getInstance().getProjectFeatures().getModelName(model));
			}
			if (previousModelName != null) {
				for (int index = 0; index < this.modelSelector.getItemCount(); index++) {
					if (previousModelName.equals(this.modelSelector.getItemAt(index))) {
						this.modelSelector.setSelectedIndex(index);
						break;
					}
				}
			}
		} finally {
			this.refreshingModels = false;
		}
		if (!models.isEmpty()) {
			String selectedModelName = (String) this.modelSelector.getSelectedItem();
			this.selectedModel = selectedModelName == null ? models.get(0)
					: Project.getInstance().getProjectFeatures().getModel(selectedModelName);
			if (this.selectedModel == null) {
				this.selectedModel = models.get(0);
			}
			this.loadTable();
		}
	}
}