package io.github.alivety.conquerors.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.google.common.collect.Maps;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.packets.PacketData;
import io.github.alivety.ppl.Packet;

public class PacketBuilder extends JFrame {
	/**
	 *
	 */
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

	// public PacketBuilder(Packet p) {
	// this.p=p;
	// setTitle("Packet Builder: "+p.getClass().getName());
	// textField.setText(p.getClass().getName());
	// }

	/**
	 * Create the frame.
	 *
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public PacketBuilder(final Test test, final Packet p, final boolean builder) throws IllegalArgumentException, IllegalAccessException {
		if (builder) {
			this.setTitle("Packet Builder");
		} else {
			this.setTitle("Packet Info");
		}

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
			textfieldtext = data.description();
		} else {
			textfieldtext = packet.getName();
		}
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
			tf.setEditable(builder);
			if (!builder) {
				f.setAccessible(true);
				final Object o = f.get(p);
				String s;
				if (o instanceof Object[]) {
					s = Arrays.asList(((Object[]) o)).toString();
				} else {
					s = o.toString();
				}
				tf.setText(s);
			}
			typeMap.put(Maps.immutableEntry(f.getName(), tf), type);
			this.contentPane.add(tf);
		}

		if (builder) {
			final JButton btnNewButton = new JButton("Build and Send");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent arg0) {
					try {
						final Set<Entry<String, JTextField>> fields = typeMap.keySet();
						final Iterator<Entry<String, JTextField>> iter = fields.iterator();
						while (iter.hasNext()) {
							final Entry<String, JTextField> vals = iter.next();
							final String name = vals.getKey();
							final String val = vals.getValue().getText();
							final Class<?> type = typeMap.get(vals);
							p.set(name, ObjectConverter.convert(val, type));
						}
						test.server.write(Main.encode(p));
						test.pl.addRow(new Object[] { "Client", p.getPacketID(), p });
					} catch (final Exception e) {
						Main.handleError(e);
					}
					PacketBuilder.this.dispose();
				}
			});
			btnNewButton.setBounds(10, 227, 855, 23);
			this.contentPane.add(btnNewButton);
		}
	}

}
