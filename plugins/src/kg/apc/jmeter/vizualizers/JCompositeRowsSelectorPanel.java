package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.graphs.CompositeNotifierInterface;
import kg.apc.jmeter.graphs.GraphRendererInterface;
import java.awt.event.InputEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import kg.apc.jmeter.JMeterPluginsUtils;
import kg.apc.charting.AbstractGraphRow;

/**
 *
 * @author Stephane Hoblingre
 */
public class JCompositeRowsSelectorPanel extends javax.swing.JPanel implements GraphRendererInterface, CompositeNotifierInterface
{
    private CompositeModel compositeModel;

    private DefaultMutableTreeNode root1;
    private DefaultMutableTreeNode root2;
    private DefaultTreeModel model1;
    private DefaultTreeModel model2;
    private Icon folderIcon = new ImageIcon(JCompositeRowsSelectorPanel.class.getResource("folder.png"));
    private Icon folderLinkIcon = new ImageIcon(JCompositeRowsSelectorPanel.class.getResource("folderLink.png"));
    private Icon leafIcon = new ImageIcon(JCompositeRowsSelectorPanel.class.getResource("treeLeaf.png"));

    private static String tree1RootName = "Test Plan";
    private static String tree2RootName = "Composite Graph";

    private CompositeGraphGui gui;

    /** Creates new form JRowsSelectorPanel */
    public JCompositeRowsSelectorPanel(CompositeModel compositeModel, CompositeGraphGui gui)
    {
        this.compositeModel = compositeModel;
        this.gui = gui;
        initComponents();
        root1 = new DefaultMutableTreeNode(tree1RootName, true);
        model1 = new DefaultTreeModel(root1);
        root2 = new DefaultMutableTreeNode(tree2RootName, true);
        model2 = new DefaultTreeModel(root2);
        jTreeGraph1.setModel(model1);
        jTreeGraph2.setModel(model2);

        DefaultTreeCellRenderer renderer1 = new DefaultTreeCellRenderer();
        renderer1.setOpenIcon(folderIcon);
        renderer1.setClosedIcon(folderIcon);
        renderer1.setLeafIcon(leafIcon);
        jTreeGraph1.setCellRenderer(renderer1);

        DefaultTreeCellRenderer renderer2 = new DefaultTreeCellRenderer();
        renderer2.setOpenIcon(folderLinkIcon);
        renderer2.setClosedIcon(folderLinkIcon);
        renderer2.setLeafIcon(leafIcon);
        jTreeGraph2.setCellRenderer(renderer2);
    }

    public void updateGraph()
    {
        gui.updateGui();
    }

    public Iterator<String[]> getItems()
    {
        ConcurrentLinkedQueue<String[]> tmp = new ConcurrentLinkedQueue<String[]>();
        for (int i = 0; i < root2.getChildCount(); i++)
        {
            TreeNode nodeChart = root2.getChildAt(i);
            String chart = nodeChart.toString();
            for (int j = 0; j < nodeChart.getChildCount(); j++)
            {
                String[] item = new String[2];
                item[0] = chart;
                item[1] = nodeChart.getChildAt(j).toString();
                tmp.add(item);
            }
        }
        return tmp.iterator();
    }

    private void expandAll(JTree tree, boolean expand)
    {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand)
    {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (Enumeration e = node.children(); e.hasMoreElements();)
            {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand)
        {
            tree.expandPath(parent);
        } else
        {
            tree.collapsePath(parent);
        }
    }

    private boolean isNodeContained(String nodeName, DefaultMutableTreeNode root)
    {
        Enumeration children = root.children();
        while (children.hasMoreElements())
        {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (nodeName.equals(child.toString()))
            {
                return true;
            }
        }
        return false;
    }

    private DefaultMutableTreeNode getNode(String nodeName, DefaultMutableTreeNode root)
    {
        if(root != null)
        {
            Enumeration children = root.children();
            while (children.hasMoreElements())
            {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
                if (nodeName.equals(child.toString()))
                {
                    return child;
                }
            }
        }
        return null;
    }

    public void clearData()
    {
        gui.updateGui();
    }

    private synchronized void updateTree()
    {
        //get previous selection
        TreePath selection = jTreeGraph1.getSelectionPath();

        //rows will not disapear, only chart if cleared...
        boolean chartsUpdated = false;

        //first, check if we need to remove some visualizers
        for(int i=0; i<root1.getChildCount(); i++)
        {
            TreeNode node = root1.getChildAt(i);
            if(!compositeModel.containsVisualizer(node.toString()))
            {
                chartsUpdated = true;
                model1.removeNodeFromParent((MutableTreeNode) node);
                i--;
            }
        }

        Iterator<String> chartsIter = compositeModel.getVizualizerNamesIterator();
        while (chartsIter.hasNext())
        {
            String chartName = chartsIter.next();
            if (!isNodeContained(chartName, root1))
            {
                chartsUpdated = true;
                DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(chartName, true);
                root1.add(node1);
                Iterator<AbstractGraphRow> rowsIter = compositeModel.getRowsIterator(chartName);
                while (rowsIter.hasNext())
                {
                    AbstractGraphRow row = rowsIter.next();
                    node1.add(new DefaultMutableTreeNode(row.getLabel(), false));
                }
            } else
            {
                Iterator<AbstractGraphRow> rowsIter = compositeModel.getRowsIterator(chartName);
                DefaultMutableTreeNode chartNode1 = getNode(chartName, root1);

                while (rowsIter.hasNext())
                {
                    String rowName = rowsIter.next().getLabel();
                    if (!isNodeContained(rowName, chartNode1))
                    {
                        chartsUpdated = true;
                        chartNode1.add(new DefaultMutableTreeNode(rowName, false));
                    }
                }
            }
        }

        if (chartsUpdated)
        {
            model1.nodeStructureChanged(root1);
            expandAll(jTreeGraph1, true);
            //restore selection
            jTreeGraph1.setSelectionPath(selection);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
      java.awt.GridBagConstraints gridBagConstraints;

      jPanelLogo = new javax.swing.JPanel();
      jLabelLogo = new javax.swing.JLabel();
      jPanel1 = new javax.swing.JPanel();
      jPanelMain = new javax.swing.JPanel();
      jPanelRowsTable = new javax.swing.JPanel();
      jLabelGraph1 = new javax.swing.JLabel();
      jLabelGraph2 = new javax.swing.JLabel();
      jScrollPaneGraph1 = new javax.swing.JScrollPane();
      jTreeGraph1 = new javax.swing.JTree();
      jScrollPaneGraph = new javax.swing.JScrollPane();
      jTreeGraph2 = new javax.swing.JTree();
      jPanelButtons = new javax.swing.JPanel();
      jButtonAdd = new javax.swing.JButton();
      jButtonRemove = new javax.swing.JButton();
      jPanelGraphPreview = new javax.swing.JPanel();
      jLabelPreview = new javax.swing.JLabel();

      setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
      setLayout(new java.awt.BorderLayout());

      jPanelLogo.setLayout(new java.awt.GridBagLayout());

      jLabelLogo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/logoSimple.png"))); // NOI18N
      jLabelLogo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
      jLabelLogo.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabelLogoMouseClicked(evt);
         }
      });
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      jPanelLogo.add(jLabelLogo, gridBagConstraints);
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.weightx = 1.0;
      jPanelLogo.add(jPanel1, gridBagConstraints);

      add(jPanelLogo, java.awt.BorderLayout.PAGE_END);

      jPanelMain.setLayout(new java.awt.GridBagLayout());

      jPanelRowsTable.setMaximumSize(new java.awt.Dimension(206, 23));
      jPanelRowsTable.setPreferredSize(new java.awt.Dimension(206, 23));
      jPanelRowsTable.setLayout(new java.awt.GridBagLayout());

      jLabelGraph1.setText("Available Sources:");
      jLabelGraph1.setMaximumSize(new java.awt.Dimension(120, 14));
      jLabelGraph1.setMinimumSize(new java.awt.Dimension(120, 14));
      jLabelGraph1.setPreferredSize(new java.awt.Dimension(120, 14));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
      jPanelRowsTable.add(jLabelGraph1, gridBagConstraints);

      jLabelGraph2.setText("Composed Graph:");
      jLabelGraph2.setMaximumSize(new java.awt.Dimension(120, 14));
      jLabelGraph2.setMinimumSize(new java.awt.Dimension(120, 14));
      jLabelGraph2.setPreferredSize(new java.awt.Dimension(120, 14));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
      jPanelRowsTable.add(jLabelGraph2, gridBagConstraints);

      jScrollPaneGraph1.setMaximumSize(new java.awt.Dimension(72, 64));
      jScrollPaneGraph1.setMinimumSize(new java.awt.Dimension(72, 64));
      jScrollPaneGraph1.setPreferredSize(new java.awt.Dimension(72, 64));

      jTreeGraph1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTreeGraph1MouseClicked(evt);
         }
      });
      jScrollPaneGraph1.setViewportView(jTreeGraph1);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
      jPanelRowsTable.add(jScrollPaneGraph1, gridBagConstraints);

      jTreeGraph2.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTreeGraph2MouseClicked(evt);
         }
      });
      jScrollPaneGraph.setViewportView(jTreeGraph2);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
      jPanelRowsTable.add(jScrollPaneGraph, gridBagConstraints);

      jPanelButtons.setFocusable(false);
      jPanelButtons.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

      jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/rightArrow.png"))); // NOI18N
      jButtonAdd.setFocusable(false);
      jButtonAdd.setMaximumSize(new java.awt.Dimension(30, 25));
      jButtonAdd.setMinimumSize(new java.awt.Dimension(30, 25));
      jButtonAdd.setPreferredSize(new java.awt.Dimension(30, 25));
      jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonAddActionPerformed(evt);
         }
      });
      jPanelButtons.add(jButtonAdd);

      jButtonRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kg/apc/jmeter/vizualizers/leftArrow.png"))); // NOI18N
      jButtonRemove.setFocusable(false);
      jButtonRemove.setMaximumSize(new java.awt.Dimension(30, 25));
      jButtonRemove.setMinimumSize(new java.awt.Dimension(30, 25));
      jButtonRemove.setPreferredSize(new java.awt.Dimension(30, 25));
      jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonRemoveActionPerformed(evt);
         }
      });
      jPanelButtons.add(jButtonRemove);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      jPanelRowsTable.add(jPanelButtons, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      jPanelMain.add(jPanelRowsTable, gridBagConstraints);

      jPanelGraphPreview.setLayout(new java.awt.BorderLayout());
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
      jPanelMain.add(jPanelGraphPreview, gridBagConstraints);

      jLabelPreview.setText("Preview:");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
      jPanelMain.add(jLabelPreview, gridBagConstraints);

      add(jPanelMain, java.awt.BorderLayout.CENTER);
   }// </editor-fold>//GEN-END:initComponents

    public void addItemsToComposite(String testplan, String row)
    {
        String[] path = new String[3];
        path[0] = tree2RootName;
        path[1] = testplan;
        path[2] = row;

        TreePath[] tp = new TreePath[1];
        tp[0] = new TreePath(path);
        addItemsToComposite(tp);
    }

    private void addItemsToComposite(TreePath[] paths)
    {
        for (int i = 0; i < paths.length; i++)
        {
            if (paths[i].getPath().length == 1)
            {
                //do nothi8ng for now, root was selected
            } else if (paths[i].getPath().length == 2)
            {
                //a chart is selected, we add all nodes...
                String chartName = paths[i].getPath()[1].toString();
                DefaultMutableTreeNode chartNode = getNode(chartName, root1);
                DefaultMutableTreeNode chartNode2;

                if (!isNodeContained(chartName, root2))
                {
                    chartNode2 = new DefaultMutableTreeNode(chartName, true);
                    root2.add(chartNode2);
                } else {
                    chartNode2 = getNode(chartName, root2);
                }

                for(int n=0; n<chartNode.getChildCount(); n++)
                {
                    if (!isNodeContained(chartNode.getChildAt(n).toString(), chartNode2))
                    {
                        chartNode2.add(new DefaultMutableTreeNode(chartNode.getChildAt(n).toString(), false));
                        model2.nodeStructureChanged(root2);
                        expandAll(jTreeGraph2, true);
                    }
                }

            } else if (paths[i].getPath().length == 3)
            {
                String chartName = paths[i].getPath()[1].toString();

                DefaultMutableTreeNode chartNode;

                if (!isNodeContained(chartName, root2))
                {
                    chartNode = new DefaultMutableTreeNode(chartName, true);
                    root2.add(chartNode);
                } else
                {
                    chartNode = getNode(chartName, root2);
                }

                if (!isNodeContained(paths[i].getPath()[2].toString(), chartNode))
                {
                    chartNode.add(new DefaultMutableTreeNode(paths[i].getPath()[2].toString(), false));
                    model2.nodeStructureChanged(root2);
                    expandAll(jTreeGraph2, true);
                }
            }
        }
        updateGraph();
    }

    private void removeItemFromComposite(TreePath[] paths)
    {
        for (int i = 0; i < paths.length; i++)
        {
            if (paths[i].getPath().length == 1)
            {
                //do nothing root was selected
            } else if (paths[i].getPath().length == 2)
            {
                TreeNode node = getNode(paths[i].getPath()[1].toString(), root2);
                model2.removeNodeFromParent((MutableTreeNode) node);
            } else if (paths[i].getPath().length == 3)
            {
                TreeNode chartNode = getNode(paths[i].getPath()[1].toString(), root2);
                TreeNode rowNode = getNode(paths[i].getPath()[2].toString(), (DefaultMutableTreeNode) chartNode);
                if(rowNode != null)
                {
                    model2.removeNodeFromParent((MutableTreeNode) rowNode);
                }
                if(chartNode != null)
                {
                    if (chartNode.getChildCount() == 0)
                    {
                        model2.removeNodeFromParent((MutableTreeNode) chartNode);
                    }
                }
            }
        }
        model2.nodeStructureChanged(root2);
        expandAll(jTreeGraph2, true);
        updateGraph();
    }


    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonAddActionPerformed
    {//GEN-HEADEREND:event_jButtonAddActionPerformed
        TreePath[] paths = jTreeGraph1.getSelectionPaths();
        if(paths!= null)
        {
            addItemsToComposite(paths);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonRemoveActionPerformed
    {//GEN-HEADEREND:event_jButtonRemoveActionPerformed
        TreePath[] paths = jTreeGraph2.getSelectionPaths();
        if(paths!= null)
        {
            removeItemFromComposite(paths);
        }
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    private void jTreeGraph1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTreeGraph1MouseClicked
    {//GEN-HEADEREND:event_jTreeGraph1MouseClicked
        if(evt.getClickCount() == 2)
        {
            TreePath[] paths = jTreeGraph1.getSelectionPaths();
            if(paths!= null && paths.length == 1)
            {
                if(paths[0].getPath().length == 3)
                {
                    addItemsToComposite(paths);
                }
            }
        }
    }//GEN-LAST:event_jTreeGraph1MouseClicked

    private void jTreeGraph2MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTreeGraph2MouseClicked
    {//GEN-HEADEREND:event_jTreeGraph2MouseClicked
        if(evt.getClickCount() == 2)
        {
            TreePath[] paths = jTreeGraph2.getSelectionPaths();
            if(paths!= null && paths.length == 1)
            {
                if(paths[0].getPath().length == 3)
                {
                    removeItemFromComposite(paths);
                }
            }
        }
    }//GEN-LAST:event_jTreeGraph2MouseClicked

    private void jLabelLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelLogoMouseClicked
        if((evt.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            JMeterPluginsUtils.openInBrowser("http://code.google.com/p/jmeter-plugins/?utm_source=jmeter&utm_medium=logolink&utm_campaign="+gui.getWikiPage());
        }
    }//GEN-LAST:event_jLabelLogoMouseClicked

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButtonAdd;
   private javax.swing.JButton jButtonRemove;
   private javax.swing.JLabel jLabelGraph1;
   private javax.swing.JLabel jLabelGraph2;
   private javax.swing.JLabel jLabelLogo;
   private javax.swing.JLabel jLabelPreview;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanelButtons;
   private javax.swing.JPanel jPanelGraphPreview;
   private javax.swing.JPanel jPanelLogo;
   private javax.swing.JPanel jPanelMain;
   private javax.swing.JPanel jPanelRowsTable;
   private javax.swing.JScrollPane jScrollPaneGraph;
   private javax.swing.JScrollPane jScrollPaneGraph1;
   private javax.swing.JTree jTreeGraph1;
   private javax.swing.JTree jTreeGraph2;
   // End of variables declaration//GEN-END:variables

    @Override
    public JPanel getGraphDisplayPanel()
    {
        return jPanelGraphPreview;
    }

    @Override
    public boolean isPreview()
    {
        return true;
    }

    @Override
    public void refresh()
    {
        updateTree();
    }
}
