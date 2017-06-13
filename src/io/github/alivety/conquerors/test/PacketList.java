package io.github.alivety.conquerors.test;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.Packet;

public class PacketList extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -1638241863909262836L;
	public JPanel contentPane;
	public JTable table;
	public TableModel model;

	/**
	 * Create the frame.
	 */
	public PacketList(final Test test) {
		this.setTitle("Packets");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1052, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1036, 261);
		this.contentPane.add(scrollPane);

		this.model = new DefaultTableModel(new Object[][] {}, new String[] { "Origin", "Packet ID", "Packet" }) {
			/**
			 *
			 */
			private static final long serialVersionUID = 9096671483734523652L;

			@Override
			public boolean isCellEditable(final int row, final int column) {
				return column == 2;
			}
		};

		final Action packet = new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = -2431380960329863102L;

			public void actionPerformed(final ActionEvent e) {
				Main.out.info("hello");
				final JTable table = (JTable) e.getSource();
				final int modelRow = Integer.valueOf(e.getActionCommand());
				final Packet p = (Packet) ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2);
				try {
					new PacketBuilder(test, p, false).setVisible(true);
				} catch (final IllegalAccessException e1) {
					Main.handleError(e1);
				}
				;
			}
		};

		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.setModel(this.model);
		scrollPane.setViewportView(this.table);

		new ButtonColumn(this.table, packet, 2);

	}

	public void addRow(final Object[] rowData) {
		// Preconditions.checkArgument(rowData.length==2, "Expected rowData of 2
		// elements, got %s", rowData.length);
		((DefaultTableModel) this.model).addRow(rowData);
	}

}
