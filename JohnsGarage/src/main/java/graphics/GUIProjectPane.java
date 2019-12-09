/**
 * TODO
 */
package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import insides.FileTree;
import insides.Project;
import insides.Tab;

public class GUIProjectPane extends JPanel
{
	private static final long serialVersionUID = 4753492397994159278L;

	private FileTree theFileTree;
	
	private Tab theTab;
	
	private JList projectList;
	
	private JScrollPane scrollPane;
	
	private GridBagConstraints constraints;
	
	/**
	 * TODO
	 * @param fileTree
	 * @param currentTab
	 */
	public GUIProjectPane(FileTree fileTree, Tab currentTab)
	{
		theFileTree = fileTree;
		theTab = currentTab;
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		
		JLabel title = new JLabel(theTab.getName());
		title.setFont(new Font("Tahoma", Font.BOLD, 24));
		setConstraints(0,0,1,1,0.8,0.05);
		add(title, constraints);
		
		createAddRemove();
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		scrollPane = new JScrollPane(panel);
		
		projectList = loadProjects();
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(projectList);
		setConstraints(0,1,3,1,1,0.95);
		add(scrollPane, constraints);
	}
	
	/**
	 * TODO
	 */
	private void createAddRemove()
	{
		final JButton removeProject = new JButton("Remove Project");
		setConstraints(1,0,1,1,0.1,0.05);
		removeProject.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Project selected = getSelected();
				if (selected == null) JOptionPane.showMessageDialog(GUIProjectPane.this, "You must select a Project", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					int confirm = JOptionPane.showConfirmDialog(GUIProjectPane.this, "Are you sure you want to remove \"" + selected + "\"?", "Remove Project", JOptionPane.YES_NO_OPTION);
					if (confirm == 0)
					{
						theFileTree.delete(selected, theTab);
						if (theTab.getContents().size() == 0) removeProject.setEnabled(false);
						System.out.println("Project removed...");
					}
				}
				refresh();
			}
		});
		add(removeProject, constraints);
		
		JButton addProject = new JButton("Add Project");
		setConstraints(2,0,1,1,0.1,0.05);
		addProject.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = JOptionPane.showInputDialog("Enter Project name:", null);
				if (name != null && !name.equals(""))
				{
					theFileTree.newProject(name, theTab);
					removeProject.setEnabled(true);
					System.out.println("Project added...");
				}
				refresh();
			}
		});
		add(addProject, constraints);
		
		if (theTab.getContents().size() == 0) removeProject.setEnabled(false);
	}
	
	/**
	 * TODO
	 * @return
	 */
	public JList loadProjects()
	{
		final JList list = new JList(theTab.getContents().toArray());
		list.setFont(new Font("Tahoma", Font.BOLD, 20));
		list.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2 || e.getClickCount() == 3)
				{
					Project p = (Project) list.getSelectedValue();
					new GUIProjectView(theFileTree, p);
				}
			}
		});
		return list;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param wx
	 * @param wy
	 */
	private void setConstraints(int x, int y, int w, int h, double wx, double wy)
	{
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = wx;
		constraints.weighty = wy;
		constraints.insets = new Insets(5, 0, 0, 0);
		constraints.fill = GridBagConstraints.BOTH;
	}
	
	public Project getSelected()
	{
		return (Project) projectList.getSelectedValue();
	}
	
	private void refresh()
	{
		projectList = loadProjects();
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(projectList);
	}
}
