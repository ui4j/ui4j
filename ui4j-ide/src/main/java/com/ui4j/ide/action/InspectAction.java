package com.ui4j.ide.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.script.Bindings;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ui4j.api.dom.Element;
import com.ui4j.ide.EditorManager;
import com.ui4j.ide.PageManager;
import com.ui4j.ide.ScriptManager;
import com.ui4j.ide.UIUtils;

public class InspectAction extends ExecuteAction {

	private static final long serialVersionUID = 4276920105440251959L;

	public static class InspectElementDialog extends JDialog {

		private static final long serialVersionUID = 1L;

		private JList<Element> list = new JList<>();

		private JScrollPane scroll = new JScrollPane();

		public InspectElementDialog(List<Element> elements) {
			setModalityType(ModalityType.APPLICATION_MODAL);

			setLayout(new BorderLayout());
			scroll.setViewportView(list);
			JLabel lblCnt = new JLabel(String.valueOf(elements.size()) + " Elements found");
			lblCnt.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			add(lblCnt, BorderLayout.NORTH);
			add(scroll, BorderLayout.CENTER);
			setPreferredSize(new Dimension(400, 400));
			list.setBorder(BorderFactory.createEmptyBorder());
			scroll.setBorder(BorderFactory.createEmptyBorder());

			DefaultListModel<Element> model = new DefaultListModel<Element>();
			list.setModel(model);

			for (Element next : elements) {
				model.addElement(next);
			}

			setTitle("Elements");
			
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			pack();

			setLocationRelativeTo(getParent());
			
			if (elements.size() > 0) {
				requestFocus();
				list.requestFocus();
				list.setSelectedIndex(0);
			}

			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			saveBorders(elements);

			list.setCellRenderer(new DefaultListCellRenderer() {

				private static final long serialVersionUID = 6691263485585044511L;

				@Override
				@SuppressWarnings("rawtypes")
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					Element element = (Element) value;
					
					StringBuilder builder = new StringBuilder();
					String id = element.getId();
					builder.append("[" + element.getTagName() + "] ");
					if (id != null && !id.trim().isEmpty()) {
						builder.append("[#" + id + "]");
					}
					List<String> classes = element.getClasses();
					if (!classes.isEmpty()) {
						builder.append(" [");
						int i = 0;
						for (String className : classes) {
							builder.append("." +  className);
							i += 1;
							if (i < classes.size()) {
								builder.append(", ");
							}
						}
						builder.append("]");
					}
					String text = element.getText();
					if (text != null && !text.trim().isEmpty()) {
						if (text.length() > 20) {
							text = text.substring(0, 20) + "...";
						}
						builder.append("['" + text + "']");
					}
					setText(builder.toString());
					return c;
				}
			});
			
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					restoreBorders(elements);
				}
			});
			
		    ActionListener escListener = new ActionListener() {

		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	restoreBorders(elements);
		        	dispose();
		        }
		    };

		    getRootPane().registerKeyboardAction(escListener,
		            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		            JComponent.WHEN_IN_FOCUSED_WINDOW);

			list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					restoreBorders(elements);
					if (!e.getValueIsAdjusting()) {
						int index = e.getFirstIndex();
						Element element = model.getElementAt(index);
						element.setCss("border", "2px dotted red");
						element.scrollIntoView(true);
					}
				}
			});
		}

		protected void saveBorders(List<Element> elements) {
			for (Element next : elements) {
				String border = next.getCss("border");
				if (border != null && !border.trim().isEmpty()) {
					next.setProperty("ui4j-ide-old-border", border);
				}
			}
		}

		protected void restoreBorders(List<Element> elements) {
			for (Element next : elements) {
				String border = (String) next.getProperty("ui4j-ide-old-border");
				if (border == null || border.trim().isEmpty()) {
					next.setCss("border", "");
				} else {
					next.setCss("border", border);
				}
			}
		}
	}

	public InspectAction(Component parent, PageManager pageManager, EditorManager editorManager,
			ScriptManager scriptManager) {
		super(parent, pageManager, editorManager, scriptManager);

		putValue(NAME, "Inspect");
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, mask));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object execute(String text, Bindings bindings) {
		Object result = super.execute(text, bindings);
		if (result instanceof List) {
			new InspectElementDialog((List<Element>) result).setVisible(true);
			return result;
		} else if (result instanceof Element) {
			List<Element> elements = Arrays.asList((Element) result);
			new InspectElementDialog(elements).setVisible(true);
			return result;
		}
		JLabel label = new JLabel("<html>" + String.valueOf(result) + "</html>");
		label.setPreferredSize(UIUtils.getPreferredSize(String.valueOf(result), true, 400));
		JOptionPane.showMessageDialog(getParent(), label, "Inspect Value", JOptionPane.INFORMATION_MESSAGE);
		return result;
	}
}
