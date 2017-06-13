package io.github.alivety.conquerors.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import io.github.alivety.ppl.Packet;

/**
 * The ButtonColumn class provides a renderer and an editor that looks like a
 * JButton. The renderer and editor will then be used for a specified column in
 * the table. The TableModel will contain the String to be displayed on the
 * button.
 *
 * The button can be invoked by a mouse click or by pressing the space bar when
 * the cell has focus. Optionally a mnemonic can be set to invoke the button.
 * When the button is invoked the provided Action is invoked. The source of the
 * Action will be the table. The action command will contain the model row
 * number of the button that was clicked.
 *
 */
public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
	/**
	 *
	 */
	private static final long serialVersionUID = -2994470830409907638L;
	private final JTable table;
	private final Action action;
	private int mnemonic;
	private final Border originalBorder;
	private Border focusBorder;
	
	private final JButton renderButton;
	private final JButton editButton;
	private Object editorValue;
	private boolean isButtonColumnEditor;
	
	/**
	 * Create the ButtonColumn to be used as a renderer and editor. The renderer
	 * and editor will automatically be installed on the TableColumn of the
	 * specified column.
	 *
	 * @param table
	 *            the table containing the button renderer/editor
	 * @param action
	 *            the Action to be invoked when the button is invoked
	 * @param column
	 *            the column to which the button renderer/editor is added
	 */
	public ButtonColumn(final JTable table, final Action action, final int column) {
		this.table = table;
		this.action = action;
		
		this.renderButton = new JButton();
		this.editButton = new JButton();
		this.editButton.setFocusPainted(false);
		this.editButton.addActionListener(this);
		this.originalBorder = this.editButton.getBorder();
		this.setFocusBorder(new LineBorder(Color.BLUE));
		
		final TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);
		table.addMouseListener(this);
	}
	
	//
	// Implement ActionListener interface
	//
	/*
	 * The button has been pressed. Stop editing and invoke the custom Action
	 */
	public void actionPerformed(final ActionEvent e) {
		final int row = this.table.convertRowIndexToModel(this.table.getEditingRow());
		this.fireEditingStopped();
		
		// Invoke the Action
		
		final ActionEvent event = new ActionEvent(this.table, ActionEvent.ACTION_PERFORMED, "" + row);
		this.action.actionPerformed(event);
	}
	
	public Object getCellEditorValue() {
		return this.editorValue;
	}
	
	/**
	 * Get foreground color of the button when the cell has focus
	 *
	 * @return the foreground color
	 */
	public Border getFocusBorder() {
		return this.focusBorder;
	}
	
	public int getMnemonic() {
		return this.mnemonic;
	}
	
	public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
		if (value == null) {
			this.editButton.setText("");
			this.editButton.setIcon(null);
		} else if (value instanceof Icon) {
			this.editButton.setText("");
			this.editButton.setIcon((Icon) value);
		} else if (value instanceof Packet) {
			this.editButton.setText("View Packet Data");
			this.editButton.setIcon(null);
		} else {
			this.editButton.setText(value.toString());
			this.editButton.setIcon(null);
		}
		
		this.editorValue = value;
		return this.editButton;
	}
	
	//
	// Implement TableCellRenderer interface
	//
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
		if (isSelected) {
			this.renderButton.setForeground(table.getSelectionForeground());
			this.renderButton.setBackground(table.getSelectionBackground());
		} else {
			this.renderButton.setForeground(table.getForeground());
			this.renderButton.setBackground(UIManager.getColor("Button.background"));
		}
		
		if (hasFocus)
			this.renderButton.setBorder(this.focusBorder);
		else
			this.renderButton.setBorder(this.originalBorder);
		
		// renderButton.setText( (value == null) ? "" : value.toString() );
		if (value == null) {
			this.renderButton.setText("");
			this.renderButton.setIcon(null);
		} else if (value instanceof Icon) {
			this.renderButton.setText("");
			this.renderButton.setIcon((Icon) value);
		} else if (value instanceof Packet) {
			this.renderButton.setText("View Packet Data");
			this.renderButton.setIcon(null);
		} else {
			this.renderButton.setText(value.toString());
			this.renderButton.setIcon(null);
		}
		
		return this.renderButton;
	}
	
	public void mouseClicked(final MouseEvent e) {}
	
	public void mouseEntered(final MouseEvent e) {}
	
	public void mouseExited(final MouseEvent e) {}
	
	//
	// Implement MouseListener interface
	//
	/*
	 * When the mouse is pressed the editor is invoked. If you then then drag
	 * the mouse to another cell before releasing it, the editor is still
	 * active. Make sure editing is stopped when the mouse is released.
	 */
	public void mousePressed(final MouseEvent e) {
		if (this.table.isEditing() && (this.table.getCellEditor() == this))
			this.isButtonColumnEditor = true;
	}
	
	public void mouseReleased(final MouseEvent e) {
		if (this.isButtonColumnEditor && this.table.isEditing())
			this.table.getCellEditor().stopCellEditing();
		
		this.isButtonColumnEditor = false;
	}
	
	/**
	 * The foreground color of the button when the cell has focus
	 *
	 * @param focusBorder
	 *            the foreground color
	 */
	public void setFocusBorder(final Border focusBorder) {
		this.focusBorder = focusBorder;
		this.editButton.setBorder(focusBorder);
	}
	
	/**
	 * The mnemonic to activate the button when the cell has focus
	 *
	 * @param mnemonic
	 *            the mnemonic
	 */
	public void setMnemonic(final int mnemonic) {
		this.mnemonic = mnemonic;
		this.renderButton.setMnemonic(mnemonic);
		this.editButton.setMnemonic(mnemonic);
	}
}