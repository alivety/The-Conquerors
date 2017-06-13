package io.github.alivety.conquerors.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.common.collect.Maps;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.Packet;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.event.ActionEvent;

public class PacketBuilder extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	
	private int tf_bounds_1=427,tf_bounds_2=8,tf_bounds_3=428,tf_bounds_4=20;
	private int l_bounds_1=10,l_bounds_2=11,l_bounds_3=417,l_bounds_4=14;
	private int incr=25;
	
//	public PacketBuilder(Packet p) {
//		this.p=p;
//		setTitle("Packet Builder: "+p.getClass().getName());
//		textField.setText(p.getClass().getName());
//	}
	
	/**
	 * Create the frame.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public PacketBuilder(final Test test,final Packet p,boolean builder) throws IllegalArgumentException, IllegalAccessException {
		if (builder)setTitle("Packet Builder");
		else setTitle("Packet Info");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 891, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPacketClass = new JLabel("Packet Class");
		lblPacketClass.setBounds(l_bounds_1, l_bounds_2, l_bounds_3, l_bounds_4);
		contentPane.add(lblPacketClass);
		
		textField = new JTextField(p.getClass().getName());
		textField.setEditable(false);
		textField.setBounds(tf_bounds_1, tf_bounds_2, tf_bounds_3, tf_bounds_4);
		contentPane.add(textField);
		textField.setColumns(10);
		
		Class<?> cls=p.getClass();
		final HashMap<Entry<String,JTextField>,Class<?>> typeMap=new HashMap<Entry<String,JTextField>, Class<?>>();
		for (Field f: cls.getFields()) {
			tf_bounds_2+=incr;l_bounds_2+=incr;
			Class<?> type=f.getType();
			JLabel label=new JLabel(f.getName()+" ("+type.getSimpleName()+")");
			label.setBounds(l_bounds_1,l_bounds_2,l_bounds_3,l_bounds_4);
			contentPane.add(label);
			JTextField tf=new JTextField();
			tf.setColumns(10);
			tf.setBounds(tf_bounds_1, tf_bounds_2, tf_bounds_3, tf_bounds_4);
			tf.setEditable(builder);
			if (!builder) {
				f.setAccessible(true);
				Object o=f.get(p);
				String s;
				if (o instanceof Object[]) {
					s=Arrays.asList(((Object[])o)).toString();
				}
				else {
					s=o.toString();
				}
				tf.setText(s);
			}
			typeMap.put(Maps.immutableEntry(f.getName(), tf), type);
			contentPane.add(tf);
		}
		
		if (builder) {
		JButton btnNewButton = new JButton("Build and Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Set<Entry<String,JTextField>> fields=typeMap.keySet();
					Iterator<Entry<String,JTextField>> iter=fields.iterator();
					while (iter.hasNext()) {
						Entry<String,JTextField> vals=iter.next();
						String name=vals.getKey();
						String val=vals.getValue().getText();
						Class<?> type=typeMap.get(vals);
						p.set(name, ObjectConverter.convert(val, type));
					}
					test.server.write(Main.encode(p));
					test.pl.addRow(new Object[]{"Client",p.getPacketID(),p});
				} catch (Exception e) {
					Main.handleError(e);
				}
				PacketBuilder.this.dispose();
			}
		});
		btnNewButton.setBounds(10, 227, 855, 23);
		contentPane.add(btnNewButton);
		}
	}

}
