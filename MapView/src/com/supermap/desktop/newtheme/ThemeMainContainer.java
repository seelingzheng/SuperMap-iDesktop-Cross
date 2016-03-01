package com.supermap.desktop.newtheme;

import com.supermap.desktop.Application;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;

public class ThemeMainContainer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel labelThemeLayer = new JLabel();
	private JComboBox<String> comboBoxThemeLayer = new JComboBox<String>();
	private JPanel panelThemeInfo = new JPanel();
	private JCheckBox checkBoxRefreshAtOnce = new JCheckBox();
	private JButton buttonApply = new JButton();
	private ThemeChangePanel panel;

	private Map map;

	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();
	private LocalItemListener itemListener = new LocalItemListener();
	private LocalTreeMouseListener localMouseListener = new LocalTreeMouseListener();
	private LocalTreeSelectListener treeSelectListener = new LocalTreeSelectListener();
	private LocalActionListener actionListener = new LocalActionListener();
	private ActionListener refreshAtOnceListener = new RefreshAtOnceListener();
	private Layer newLayer;
	// 标记位，用于标记当前
	private boolean layerPropertyChanged = false;
	public Layer oldLayer;
	public ThemeChangePanel lastChangePanel;
	public Theme lastTheme;

	public ThemeMainContainer() {
		initComponents();
		initResources();
		registActionListener();
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.labelThemeLayer.setText(MapViewProperties.getString("String_Themelayers"));
		this.checkBoxRefreshAtOnce.setText(MapViewProperties.getString("String_RefreshAtOnce"));
		this.buttonApply.setText(MapViewProperties.getString("String_Button_Apply"));
	}

	/**
	 * 界面布局入口
	 */
	private void initComponents() {
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		this.comboBoxThemeLayer.setEditable(true);
		this.buttonApply.setEnabled(false);
		this.setLayout(new GridBagLayout());
		// @formatter:off
		this.checkBoxRefreshAtOnce.setSelected(true);
		this.add(labelThemeLayer,       new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(10, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.WEST));
		this.add(comboBoxThemeLayer,    new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(90, 0).setInsets(10, 10, 5, 10).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(panelThemeInfo,        new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(100, 75).setInsets(5).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		this.add(checkBoxRefreshAtOnce, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.WEST));
		this.add(buttonApply,           new GridBagConstraintsHelper(1, 2, 1, 1).setWeight(0, 0).setInsets(0,10,5,10).setAnchor(GridBagConstraints.EAST));
		// @formatter:on
	}

	/**
	 * 得到中间界面
	 *
	 * @return
	 */
	public JPanel getPanelThemeInfo() {
		return panelThemeInfo;
	}

	public void setPanelThemeInfo(JPanel panelThemeInfo) {
		this.panelThemeInfo = panelThemeInfo;
	}

	/**
	 * 注册事件
	 */
	private void registActionListener() {
		this.comboBoxThemeLayer.addItemListener(this.itemListener);
		this.layersTree.addMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().addTreeSelectionListener(this.treeSelectListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.checkBoxRefreshAtOnce.addActionListener(this.refreshAtOnceListener);
	}

	/**
	 * 注销事件
	 */
	public void unregistActionListener() {
		this.comboBoxThemeLayer.removeItemListener(this.itemListener);
		this.layersTree.removeMouseListener(this.localMouseListener);
		this.layersTree.getSelectionModel().removeTreeSelectionListener(this.treeSelectListener);
		this.buttonApply.removeActionListener(this.actionListener);
		this.checkBoxRefreshAtOnce.removeActionListener(this.refreshAtOnceListener);
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (null != panel) {
				panel.refreshMapAndLayer();
				buttonApply.setEnabled(false);
				setLayerPropertyChanged(false);
			}
		}
	}

	public JComboBox<String> getComboBoxThemeLayer() {
		return comboBoxThemeLayer;
	}

	public void setComboBoxThemeLayer(JComboBox<String> comboBoxThemeLayer) {
		this.comboBoxThemeLayer = comboBoxThemeLayer;
	}

	/**
	 * 刷新专题图主界面
	 *
	 * @param layer
	 */
	private void refreshThemeMainContainer(Layer layer) {
		if (null != layer) {
			if (null != layer.getTheme()) {
				ThemeGuideFactory.modifyTheme(layer);
			} else {
				updateThemeMainContainer();
			}
		}
	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED && e.getSource() == comboBoxThemeLayer) {
				String layerCaption = (String) comboBoxThemeLayer.getSelectedItem();
				if (null != ThemeGuideFactory.getMapControl()) {
					map = ThemeGuideFactory.getMapControl().getMap();
					Layer layer = MapUtilties.findLayerByCaption(map, layerCaption);
					refreshThemeMainContainer(layer);
				}
			}
		}
	}

	class RefreshAtOnceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean selected = checkBoxRefreshAtOnce.isSelected();
			if (null != panel && selected) {
				panel.setRefreshAtOnce(selected);
				panel.refreshMapAndLayer();
				buttonApply.setEnabled(false);
			} else if (null != panel && false == selected) {
				panel.setRefreshAtOnce(selected);
			}
		}
	}

	/**
	 * 移除所有的控件
	 */
	private void updateThemeMainContainer() {
		if (null != this.panel) {
			ThemeMainContainer.this.remove(this.panel);
		}
		ThemeMainContainer.this.add(panelThemeInfo, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER)
				.setIpad(0, 0).setFill(GridBagConstraints.BOTH));
		ThemeMainContainer.this.repaint();
		ThemeMainContainer.this.comboBoxThemeLayer.removeAllItems();
	}

	/**
	 * 刷新标签项下拉框中显示的子项
	 */
	private void refreshComboBoxThemeLayer() {
		comboBoxThemeLayer.removeAllItems();
		map = ThemeGuideFactory.getMapControl().getMap();
		Layers layers = map.getLayers();
		for (int i = 0; i < layers.getCount(); i++) {
			addItemToComboBox(layers.get(i));
		}
	}

	/**
	 * 利用递归将当前地图的专题图图层标题添加到combobox中
	 *
	 * @param layer
	 */
	public void addItemToComboBox(Layer layer) {
		if (layer instanceof LayerGroup) {
			LayerGroup tempLayer = (LayerGroup) layer;
			for (int i = 0; i < tempLayer.getCount(); i++) {
				addItemToComboBox(tempLayer.get(i));
			}
		} else if (null != layer.getTheme()) {
			comboBoxThemeLayer.addItem(layer.getCaption());
		}
	}

	private void resetThemeMainContainer(Layer layer) {
		if (null != layer) {
			refreshComboBoxThemeLayer();
			if (comboBoxThemeLayer.getItemCount() > 0 && null != layer.getTheme()) {
				// 有专题图图层，comboBoxThemeLayer中有对应的专题图图层，刷新ThemeMainContainer
				comboBoxThemeLayer.setSelectedItem(layer.getCaption());
			} else {
				// 没有专题图图层，重置ThemeMainContainer
				comboBoxThemeLayer.removeAllItems();
				comboBoxThemeLayer.setSelectedItem("");
				updateThemeMainContainer();
				ThemeMainContainer.this.updateUI();
			}
		} else {
			HashMap<String, ThemeChangePanel> themeContainers = ThemeGuideFactory.themeTypeContainer;
			Iterator<?> iterator = themeContainers.entrySet().iterator();
			while (iterator.hasNext()) {
				java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
				((ThemeChangePanel) entry.getValue()).unregistActionListener();
			}
			ThemeGuideFactory.themeTypeContainer.clear();
			updateThemeMainContainer();
		}
	}

	class LocalTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (2 == e.getClickCount() && null != newLayer && null != newLayer.getTheme()) {
				resetThemeMainContainer(newLayer);
				ThemeGuideFactory.getDockbarThemeContainer().setVisible(true);
			}
		}
	}

	class LocalTreeSelectListener implements TreeSelectionListener {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			try {
				newLayer = getLayerByPath(e.getNewLeadSelectionPath());
				oldLayer = getLayerByPath(e.getOldLeadSelectionPath());
				final TreePath oldPath = e.getOldLeadSelectionPath();
				final TreePath newPath = e.getNewLeadSelectionPath();
				final int row = layersTree.getRowForPath(newPath);
				if (null != ThemeGuideFactory.getDockbarThemeContainer()) {

					if (null != panel && !checkBoxRefreshAtOnce.isSelected() && isLayerPropertyChanged()) {

						if (JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialog(MapViewProperties.getString("String_LayerProperty_Message"))) {
							// 不保存修改
							ThemeGuideFactory.themeTypeContainer.remove(oldLayer.getCaption());
						} else if (panel != null) {
							// 保存修改并刷新
							// 新开一个线程来刷新layersTree避免当前线程中调用refreshNode中的
							// updateUI()后layersTree为空的异常
							new Thread() {
								@Override
								public void run() {
									panel = ThemeGuideFactory.themeTypeContainer.get(oldLayer.getCaption());
									if (panel instanceof ThemeLabelUniformContainer) {
										panel.refreshMapAndLayer();
									} else if (panel instanceof ThemeLabelRangeContainer) {
										((ThemeLabelRangeContainer) panel).getPanelAdvance().refreshMapAndLayer();
										((ThemeLabelRangeContainer) panel).getPanelProperty().refreshMapAndLayer();
										ThemeLabel themeLabel = (ThemeLabel) panel.getCurrentTheme();
										ThemeLabel nowThemeLabel = ((ThemeLabel) oldLayer.getTheme());
										nowThemeLabel.clear();
										if (0 < themeLabel.getCount()) {
											for (int i = 0; i < themeLabel.getCount(); i++) {
												if (null != themeLabel.getItem(i)) {
													nowThemeLabel.addToTail(themeLabel.getItem(i), true);
												}
											}
										}
										nowThemeLabel.setRangeExpression(themeLabel.getRangeExpression());
									} else {
										oldLayer.getTheme().fromXML(panel.getCurrentTheme().toXML());
									}
									map.refresh();
									layersTree.reload();
									layersTree.setSelectionRow(row);
								}
							}.start();

						}
						setLayerPropertyChanged(false);
					}
				}
				if (null != newLayer && null != newLayer.getTheme()) {
					ThemeGuideFactory.modifyTheme(newLayer);
				}
				resetThemeMainContainer(newLayer);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}

		}
	}

	/**
	 * 根据选中的treePath获取图层
	 *
	 * @param path
	 * @return
	 */
	private Layer getLayerByPath(TreePath path) {
		Layer layer = null;
		if (null != path && null != path.getLastPathComponent()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			Object obj = node.getUserObject();
			TreeNodeData controlNodeData = (TreeNodeData) obj;
			Object itemObj = controlNodeData.getData();
			if (itemObj instanceof Layer) {
				layer = (Layer) itemObj;
			} else {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getParentPath().getLastPathComponent();
				Object parentUserObj = parentNode.getUserObject();
				TreeNodeData nodeData = (TreeNodeData) parentUserObj;
				Object parentObj = nodeData.getData();
				if (parentObj instanceof Layer) {
					layer = (Layer) parentObj;
				}
			}
		}
		return layer;
	}

	public ThemeChangePanel getPanel() {
		return panel;
	}

	public void setPanel(ThemeChangePanel panel) {
		if (null != panelThemeInfo) {
			remove(panelThemeInfo);
		}
		for (int i = getComponents().length - 1; i >= 0; i--) {
			if (getComponent(i) instanceof JPanel) {
				remove(getComponent(i));
			}
		}
		add(panel,
				new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(3, 3).setInsets(5).setAnchor(GridBagConstraints.CENTER).setIpad(0, 0)
						.setFill(GridBagConstraints.BOTH));
		repaint();
		this.panel = panel;
		if (null != panel) {
			this.panel.setRefreshAtOnce(this.checkBoxRefreshAtOnce.isSelected());
			this.buttonApply.setEnabled(false);
		}
	}

	public boolean isLayerPropertyChanged() {
		return layerPropertyChanged;
	}

	public void setLayerPropertyChanged(boolean layerPropertyChanged) {
		this.layerPropertyChanged = layerPropertyChanged;
	}

	public JCheckBox getCheckBoxRefreshAtOnce() {
		return checkBoxRefreshAtOnce;
	}

	public JButton getButtonApply() {
		return buttonApply;
	}

}