package io.github.alivety.conquerors.common;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.common.collect.Maps;

import io.github.alivety.conquerors.test.ButtonColumn;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;

public class PPLListener extends JFrame {
	
	private static final long serialVersionUID = -1638241863909262836L;
	public JPanel contentPane;
	public JTable table;
	public TableModel model;
	
	public PPLListener() {
		this.setTitle("PPL Listener");
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
			private static final long serialVersionUID = 9096671483734523652L;
			
			@Override
			public boolean isCellEditable(final int row, final int column) {
				return column == 2;
			}
		};
		
		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.setModel(this.model);
		scrollPane.setViewportView(this.table);
		
		final Action packet = new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = -2431380960329863102L;
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JTable table = (JTable) e.getSource();
				final int modelRow = Integer.valueOf(e.getActionCommand());
				final Packet p = (Packet) ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 2);
				try {
					new PacketBuilder(p).setVisible(true);
				} catch (final IllegalAccessException e1) {
					Main.handleError(e1);
				}
				;
			}
		};
		
		new ButtonColumn(this.table, packet, 2);
		
	}
	
	public void addRow(final Object[] rowData) {
		((DefaultTableModel) this.model).addRow(rowData);
	}
	
	private static class PacketBuilder extends JFrame {
		private static final long serialVersionUID = 6091057864086680639L;
		private final JPanel contentPane;
		private final JTextField textField;
		
		private final int tf_bounds_1 = 427;
		private int tf_bounds_2 = 8;
		private final int tf_bounds_3 = 428;
		private final int tf_bounds_4 = 20;
		private final int l_bounds_1 = 10;
		private int l_bounds_2 = 11;
		private final int l_bounds_3 = 417;
		private final int l_bounds_4 = 14;
		private final int incr = 25;
		
		public PacketBuilder(final Packet p) throws IllegalArgumentException, IllegalAccessException {
			this.setTitle(p.toString());
			
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setBounds(100, 100, 891, 300);
			this.contentPane = new JPanel();
			this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.setContentPane(this.contentPane);
			this.contentPane.setLayout(null);
			
			final JLabel lblPacketClass = new JLabel("Packet Class");
			lblPacketClass.setBounds(this.l_bounds_1, this.l_bounds_2, this.l_bounds_3, this.l_bounds_4);
			this.contentPane.add(lblPacketClass);
			
			final Class<?> packet = p.getClass();
			String textfieldtext;
			if (packet.isAnnotationPresent(PacketData.class)) {
				final PacketData data = packet.getAnnotation(PacketData.class);
				textfieldtext = data.desc();
			} else
				textfieldtext = packet.getName();
			this.textField = new JTextField(textfieldtext);
			this.textField.setEditable(false);
			this.textField.setBounds(this.tf_bounds_1, this.tf_bounds_2, this.tf_bounds_3, this.tf_bounds_4);
			this.contentPane.add(this.textField);
			this.textField.setColumns(10);
			
			final Class<?> cls = p.getClass();
			final HashMap<Entry<String, JTextField>, Class<?>> typeMap = new HashMap<Entry<String, JTextField>, Class<?>>();
			for (final Field f : cls.getFields()) {
				this.tf_bounds_2 += this.incr;
				this.l_bounds_2 += this.incr;
				final Class<?> type = f.getType();
				final JLabel label = new JLabel(f.getName() + " (" + type.getSimpleName() + ")");
				label.setBounds(this.l_bounds_1, this.l_bounds_2, this.l_bounds_3, this.l_bounds_4);
				this.contentPane.add(label);
				final JTextField tf = new JTextField();
				tf.setColumns(10);
				tf.setBounds(this.tf_bounds_1, this.tf_bounds_2, this.tf_bounds_3, this.tf_bounds_4);
				tf.setEditable(false);
				f.setAccessible(true);
				final Object o = f.get(p);
				String s;
				if (o instanceof Object[])
					s = Arrays.asList(((Object[]) o)).toString();
				else
					s = o.toString();
				tf.setText(s);
				typeMap.put(Maps.immutableEntry(f.getName(), tf), type);
				this.contentPane.add(tf);
			}
		}
		
	}
}
