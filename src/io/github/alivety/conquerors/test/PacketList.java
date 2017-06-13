package io.github.alivety.conquerors.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.common.base.Preconditions;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.Packet;

public class PacketList extends JFrame {

	public JPanel contentPane;
	public JTable table;
	public TableModel model;
	
	public void addRow(Object[] rowData) {
		//Preconditions.checkArgument(rowData.length==2, "Expected rowData of 2 elements, got %s", rowData.length);
		((DefaultTableModel)model).addRow(rowData);
	}

	/**
	 * Create the frame.
	 */
	public PacketList(final Test test) {
		setTitle("Packets");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1052, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1036, 261);
		contentPane.add(scrollPane);
		
		model=new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Origin","Packet ID", "Packet"
				}
			){
			public boolean isCellEditable(int row,int column) {
				return column==2;
			}
		};
		
		Action packet=new AbstractAction(){
			public void actionPerformed(ActionEvent e)
		    {
				Main.out.info("hello");
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        Packet p=(Packet) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 2);
		        try {
					new PacketBuilder(test,p,false).setVisible(true);
				} catch (IllegalAccessException e1) {
					Main.handleError(e1);
				};
		    }
		};
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		ButtonColumn buc=new ButtonColumn(table,packet,2);
		
	}

}
