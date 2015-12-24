package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferAnalystParameter;
import com.supermap.analyst.spatialanalyst.BufferEndType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.data.Unit;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class PanelLineBufferAnalyst extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelBasic;
	private JPanel panelBufferRadius;
	private JPanel panelBufferType;
	private JPanel panelBasicLeft;
	private JPanel panelBasicRight;
	private JLabel labelUnit;
	private JLabel labelLeftNumericRadius;
	private JLabel labelRightNumericRadius;
	private JLabel labelLeftFieldRadius;
	private JLabel labelRightFieldRadius;

	private JRadioButton radioButtonNumeric;
	private JRadioButton radioButtonField;
	private JComboBox<Unit> comboBoxUnit;
	private SMFormattedTextField textFieldNumericLeft;
	private SMFormattedTextField textFieldNumericRight;
	private JComboBox<Object> comboBoxFieldLeft;
	private JComboBox<Object> comboBoxFieldRight;
	private JRadioButton radioButtonBufferTypeRound;
	private JRadioButton radioButtonBufferTypeFlat;
	private JCheckBox checkBoxBufferLeft;
	private JCheckBox checkBoxBufferRight;
	private PanelBufferData panelBufferData;
	private PanelResultData panelResultData;
	private PanelResultSet panelResultSet;
	private MapControl mapControl;
	private Recordset recordset;
	private Object radiusLeft;
	private Object radiusRight;
	private String resultDatasetName;
	private boolean bufferCreate;
	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();
	private LocalKeylistener localKeylistener = new LocalKeylistener();
	private LocalItemListener localItemListener = new LocalItemListener();
	private ComboBoxField comboBoxField;
	private boolean isButtonOkEnabled;
	private boolean isEnabled;
	private DoSome some;

	public void setSome(DoSome some) {
		this.some = some;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public PanelBufferData getPanelBufferData() {
		return panelBufferData;
	}

	public void setPanelBufferData(PanelBufferData panelBufferData) {
		this.panelBufferData = panelBufferData;
	}

	public boolean isButtonOkEnabled() {
		return isButtonOkEnabled;
	}

	public void setButtonOkEnabled(boolean isButtonOkEnabled) {
		this.isButtonOkEnabled = isButtonOkEnabled;
		if (some != null) {
			some.doSome(isButtonOkEnabled);
		}
	}

	public PanelLineBufferAnalyst() {
		initComponent();
		initResources();
		setPanelLineBufferAnalyst();

	}

	private void initComponent() {
		this.panelBasic = new JPanel();
		this.panelBasicLeft = new JPanel();
		this.panelBasicRight = new JPanel();
		this.panelBufferRadius = new JPanel();
		this.panelBufferType = new JPanel();
		this.panelBufferData = new PanelBufferData();
		this.panelResultData = new PanelResultData();
		this.panelResultSet = new PanelResultSet();
		this.add(this.panelBasic);
		initComponentBufferType();
		initComponentBufferRadius();
		setPanelBasicLayout();
		setPanelBasicLeftLayout();
		setPanelBasicRightLayout();

	}

	private void initResources() {
		this.panelBufferRadius.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferRadius")));
		this.panelBufferType.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferType")));
		this.labelUnit.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
		this.labelLeftNumericRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightNumericRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));
		this.labelLeftFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));
		this.radioButtonNumeric.setText(SpatialAnalystProperties.getString("String_NumericBufferRadius"));
		this.radioButtonField.setText(SpatialAnalystProperties.getString("String_FieldBufferRadius"));
		this.radioButtonBufferTypeRound.setText(SpatialAnalystProperties.getString("String_BufferTypeRound"));
		this.radioButtonBufferTypeFlat.setText(SpatialAnalystProperties.getString("String_BufferTypeFlat"));
		this.checkBoxBufferLeft.setText(SpatialAnalystProperties.getString("String_BufferTypeLeft"));
		this.checkBoxBufferRight.setText(SpatialAnalystProperties.getString("String_BufferTypeRight"));
	}

	private void setPanelLineBufferAnalyst() {
		setPanelBufferData();
		setPanelResultSet();
		setPanelBufferType();
		setPanelBuffeRadius();
		registerEvent();
	}

	private void initComponentBufferType() {
		this.radioButtonBufferTypeRound = new JRadioButton("BufferTypeRound");
		this.radioButtonBufferTypeFlat = new JRadioButton("BufferTypeFlat");
		this.checkBoxBufferLeft = new JCheckBox("BufferTypeLeft");
		this.checkBoxBufferRight = new JCheckBox("BufferTypeRight");

		ButtonGroup bufferTypeButtonGroup = new ButtonGroup();
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeRound);
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeFlat);

		GroupLayout panelBufferTypeLayout = new GroupLayout(this.panelBufferType);
		this.panelBufferType.setLayout(panelBufferTypeLayout);

		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.radioButtonBufferTypeFlat)).addGap(50)
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxBufferLeft)
						.addComponent(this.checkBoxBufferRight)).addGap(75));
		
		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.checkBoxBufferLeft)).addGap(10)
				.addGroup(panelBufferTypeLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeFlat)
						.addComponent(this.checkBoxBufferRight)));
		//@formatter:on

	}

	private void initComponentBufferRadius() {
		this.labelUnit = new JLabel("Unit");
		this.labelLeftNumericRadius = new JLabel("LeftNumericRadius");
		this.labelRightNumericRadius = new JLabel("RightNumericRadius");
		this.labelLeftFieldRadius = new JLabel("RightFieldRadius");
		this.labelRightFieldRadius = new JLabel("RightFieldRadius");
		this.radioButtonNumeric = new JRadioButton("Numeric");
		this.radioButtonField = new JRadioButton("Field");
		this.comboBoxUnit = initComboBoxUnit.createComboBoxUnit();

		NumberFormatter numberFormatter = new NumberFormatter();
		numberFormatter.setValueClass(Integer.class);
		this.textFieldNumericLeft = new SMFormattedTextField(numberFormatter);
		this.textFieldNumericRight = new SMFormattedTextField(numberFormatter);
		this.textFieldNumericLeft.setValue(10);
		this.textFieldNumericRight.setValue(10);

		this.comboBoxFieldLeft = new JComboBox<Object>();
		this.comboBoxFieldRight = new JComboBox<Object>();

		ButtonGroup bufferRadiusButtonGroup = new ButtonGroup();
		bufferRadiusButtonGroup.add(this.radioButtonNumeric);
		bufferRadiusButtonGroup.add(this.radioButtonField);

		//@formatter:off
		GroupLayout panelBufferRadiusLayout = new GroupLayout(this.panelBufferRadius);
		this.panelBufferRadius.setLayout(panelBufferRadiusLayout);
		
		panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.radioButtonNumeric)
						.addComponent(this.labelLeftNumericRadius)
						.addComponent(this.labelRightNumericRadius)
						.addComponent(this.radioButtonField)
						.addComponent(this.labelLeftFieldRadius)
						.addComponent(this.labelRightFieldRadius)).addGap(10)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.comboBoxUnit)
						.addComponent(this.textFieldNumericLeft)
						.addComponent(this.textFieldNumericRight)
						.addComponent(this.comboBoxFieldLeft)
						.addComponent(this.comboBoxFieldRight)).addContainerGap());
		
		panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit)).addGap(8)
			    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.radioButtonNumeric)).addGap(5)
			    .addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
					   .addComponent(this.labelLeftNumericRadius)
					   .addComponent(this.textFieldNumericLeft)).addGap(15)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelRightNumericRadius)
						.addComponent(this.textFieldNumericRight)).addGap(5)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.radioButtonField)).addGap(5)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelLeftFieldRadius)
						.addComponent(this.comboBoxFieldLeft)).addGap(15)
				.addGroup(panelBufferRadiusLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelRightFieldRadius)
						.addComponent(this.comboBoxFieldRight)).addContainerGap().addGap(5));
		//@formatter:on

	}

	private void setPanelBasicLayout() {
		GroupLayout panelBasicLayout = new GroupLayout(this.panelBasic);
		this.panelBasic.setLayout(panelBasicLayout);

		//@formatter:off
	    panelBasicLayout.setHorizontalGroup(panelBasicLayout.createSequentialGroup()
	    		.addContainerGap()
	    		.addComponent(this.panelBasicLeft)
	    		.addComponent(this.panelBasicRight).addContainerGap());
	    
	    panelBasicLayout.setVerticalGroup(panelBasicLayout.createSequentialGroup()
	    		.addContainerGap()
	    		.addGroup(panelBasicLayout.createParallelGroup(Alignment.CENTER)
	    				.addComponent(this.panelBasicLeft)
	    				.addComponent(this.panelBasicRight)).addContainerGap());
	    //@formatter:on
		panelBasicLayout.linkSize(this.panelBasicLeft, this.panelBasicRight);

	}

	private void setPanelBasicLeftLayout() {
		GroupLayout panelBasicLeftLayout = new GroupLayout(this.panelBasicLeft);
		this.panelBasicLeft.setLayout(panelBasicLeftLayout);

		//@formatter:off
		panelBasicLeftLayout.setHorizontalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addGroup(panelBasicLeftLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferData)
						.addComponent(this.panelResultSet)
						.addComponent(this.panelResultData)));
		
		panelBasicLeftLayout.setVerticalGroup(panelBasicLeftLayout.createSequentialGroup()
				.addComponent(this.panelBufferData)
				.addComponent(this.panelResultSet)
				.addComponent(this.panelResultData).addContainerGap());
		//@formatter:on

	}

	private void setPanelBasicRightLayout() {
		GroupLayout panelBasicRightLayout = new GroupLayout(this.panelBasicRight);
		this.panelBasicRight.setLayout(panelBasicRightLayout);

		//@formatter:off
		panelBasicRightLayout.setHorizontalGroup(panelBasicRightLayout.createSequentialGroup()
				.addGroup(panelBasicRightLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.panelBufferType)
						.addComponent(this.panelBufferRadius)));
		
		panelBasicRightLayout.setVerticalGroup(panelBasicRightLayout.createSequentialGroup()
				.addComponent(this.panelBufferType)
				.addComponent(this.panelBufferRadius).addContainerGap());
		//@formatter:on

	}

	/**
	 * 当窗体界面打开时，且打开的窗体是地图时，如果数据集不是线或者网络数据集，设置选中数据集的数据源的第一个线或者网络数据集，否则设置数据集为选中地图的第一个数据集 如果窗体没有打开，获取工作空间树选中节点,得到选中的数据集，数据源
	 */
	private void setPanelBufferData() {
		setComboBoxDatasetType();
		int layersCount;
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			layersCount = this.mapControl.getMap().getLayers().getCount();
			if (layersCount > 0) {
				for (int i = 0; i < layersCount; i++) {
					Layer[] activeLayer = new Layer[layersCount];
					activeLayer[i] = mapControl.getMap().getLayers().get(i);

					if (activeLayer[i].getDataset().getType() == DatasetType.LINE || activeLayer[i].getDataset().getType() == DatasetType.LINE3D
							|| activeLayer[i].getDataset().getType() == DatasetType.NETWORK || activeLayer[i].getDataset().getType() == DatasetType.NETWORK3D) {
						if (activeLayer[i].getSelection() != null && activeLayer[i].getSelection().getCount() != 0) {
							this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(activeLayer[i].getDataset().getDatasource());
							this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(activeLayer[i].getDataset().getDatasource());
							this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(activeLayer[i].getDataset().getDatasource().getDatasets());
							this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(activeLayer[i].getDataset());
							recordset = activeLayer[i].getSelection().toRecordset();
							this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
							this.panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
							setComponentEnabled();

						} else {
							setWorkspaceTreeNode();
						}
						return;
					} else {
						setWorkspaceTreeNode();
					}
				}
			}
		} else {
			setWorkspaceTreeNode();
		}
	}

	private void setWorkspaceTreeNode() {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
			if (nodeData.getData() instanceof Datasource) {
				Datasource selectedDatasource = (Datasource) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDatasource);
				this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(selectedDatasource);
				this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDatasource.getDatasets());
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
				if (selectedDataset.getType() == DatasetType.LINE || selectedDataset.getType() == DatasetType.LINE3D
						|| selectedDataset.getType() == DatasetType.NETWORK || selectedDataset.getType() == DatasetType.NETWORK3D) {
					this.panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
				}
			} else {
				initDatasourceAndDataSet();
			}
		} else {
			initDatasourceAndDataSet();
		}
		this.panelBufferData.getCheckBoxGeometrySelect().setEnabled(false);

	}

	/**
	 * 对结果面板进行设置
	 */
	private void setPanelResultSet() {
		this.panelResultSet.getCheckBoxDisplayInMap().setSelected(true);
		this.panelResultSet.getCheckBoxRemainAttributes().setSelected(true);
	}

	private void setPanelBufferType() {
		this.radioButtonBufferTypeRound.setSelected(true);
		this.checkBoxBufferLeft.setSelected(true);
		this.checkBoxBufferRight.setSelected(true);
		setComponentEnabled();
	}

	private void setPanelBuffeRadius() {
		this.radioButtonNumeric.setSelected(true);
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			Dataset comboBoxDataset = this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			this.comboBoxField = new ComboBoxField(comboBoxDataset, this.comboBoxFieldLeft, this.comboBoxFieldRight);
			this.comboBoxField.createComboBoxField(comboBoxDataset, this.comboBoxFieldLeft, this.comboBoxFieldRight);
		}
		setComponentEnabled();
	}

	private void registerEvent() {
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(new LocalItemListener());
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(new LocalItemListener());
		this.panelBufferData.getCheckBoxGeometrySelect().addItemListener(new LocalItemListener());
		this.checkBoxBufferLeft.addItemListener(new LocalItemListener());
		this.checkBoxBufferRight.addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInMap().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxDisplayInScene().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxRemainAttributes().addItemListener(new LocalItemListener());
		this.panelResultSet.getCheckBoxUnionBuffer().addItemListener(new LocalItemListener());
		this.radioButtonBufferTypeFlat.addActionListener(new LocalActionListener());
		this.radioButtonBufferTypeRound.addActionListener(new LocalActionListener());
		this.radioButtonNumeric.addActionListener(new LocalActionListener());
		this.radioButtonField.addActionListener(new LocalActionListener());
		this.comboBoxFieldLeft.addItemListener(new LocalItemListener());
		this.comboBoxFieldRight.addItemListener(new LocalItemListener());
		this.textFieldNumericLeft.addKeyListener(localKeylistener);
		this.textFieldNumericRight.addKeyListener(localKeylistener);
	}

	private void setComboBoxDatasetType() {
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		datasetTypes.add(DatasetType.LINE);
		datasetTypes.add(DatasetType.LINE3D);
		datasetTypes.add(DatasetType.NETWORK);
		datasetTypes.add(DatasetType.NETWORK3D);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}

	private void initDatasourceAndDataSet() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelResultData.getComboBoxResultDataDatasource().setSelectedDatasource(defaultDatasource);
		this.panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComponentEnabled() {
		this.panelBufferData.getComboBoxBufferDataDataset().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.panelBufferData.getComboBoxBufferDataDatasource().setEnabled(!this.panelBufferData.getCheckBoxGeometrySelect().isSelected());
		this.comboBoxFieldLeft.setEnabled(this.radioButtonField.isSelected() && this.checkBoxBufferLeft.isSelected());
		this.comboBoxFieldRight.setEnabled(this.radioButtonField.isSelected() && this.checkBoxBufferRight.isSelected());
		this.textFieldNumericLeft.setEnabled(this.radioButtonNumeric.isSelected() && this.checkBoxBufferLeft.isSelected());
		this.textFieldNumericRight.setEnabled(this.radioButtonNumeric.isSelected() && this.checkBoxBufferRight.isSelected());
		this.panelResultSet.getCheckBoxRemainAttributes().setEnabled(!this.panelResultSet.getCheckBoxUnionBuffer().isSelected());
		this.checkBoxBufferLeft.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
		this.checkBoxBufferRight.setEnabled(!this.radioButtonBufferTypeRound.isSelected());
	}

	public void addListener() {
		this.panelResultSet.getTextFieldSemicircleLineSegment().getDocument().addDocumentListener(new LocalDocumentListener());
		this.panelResultSet.getTextFieldSemicircleLineSegment().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub

			}
		});
		this.panelBufferData.getComboBoxBufferDataDataset().addItemListener(localItemListener);
		this.panelBufferData.getComboBoxBufferDataDatasource().addItemListener(localItemListener);
	}

	/**
	 * 创建缓冲区分析
	 */
	public boolean CreateCurrentBuffer() {
		bufferCreate = false;
		if (this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
			DatasetVector sourceDatasetVector = (DatasetVector) this.panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset();
			BufferAnalystParameter bufferAnalystParameter = new BufferAnalystParameter();
			if (sourceDatasetVector.getRecordCount() != 0) {
				// 创建缓冲区数据集
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources()
						.get(this.panelResultData.getComboBoxResultDataDatasource().getSelectedDatasource().getAlias());
				DatasetVectorInfo resultDatasetVectorInfo = new DatasetVectorInfo();
				this.resultDatasetName = this.panelResultData.getTextFieldResultDataDataset().getText();
				String datasetName = datasource.getDatasets().getAvailableDatasetName(this.resultDatasetName);
				resultDatasetVectorInfo.setName(datasetName);

				resultDatasetVectorInfo.setType(DatasetType.REGION);
				DatasetVector resultDatasetVector = datasource.getDatasets().create(resultDatasetVectorInfo);
				resultDatasetVector.setPrjCoordSys(sourceDatasetVector.getPrjCoordSys());
				// radioButtonNumeric被选中，当数据集类型为点对象时，缓冲半径取绝对值
				if (this.radioButtonNumeric.isSelected()) {
					this.radiusLeft = Math.abs(Double.parseDouble(textFieldNumericLeft.getValue().toString()));
					this.radiusRight = Math.abs(Double.parseDouble(textFieldNumericRight.getValue().toString()));
				}

				if (this.radioButtonBufferTypeRound.isSelected()) {
					bufferAnalystParameter.setEndType(BufferEndType.ROUND);
					bufferAnalystParameter.setLeftDistance(this.radiusLeft);
					bufferAnalystParameter.setRightDistance(this.radiusRight);
				} else if (this.radioButtonBufferTypeFlat.isSelected()) {
					bufferAnalystParameter.setEndType(BufferEndType.FLAT);
					if (this.checkBoxBufferLeft.isSelected()) {
						bufferAnalystParameter.setLeftDistance(this.radiusLeft);
					}
					if (this.checkBoxBufferRight.isSelected()) {
						bufferAnalystParameter.setRightDistance(this.radiusRight);
					}
				}
				bufferAnalystParameter.setRadiusUnit(initComboBoxUnit.getBufferRadiusUnit((Unit) this.comboBoxUnit.getSelectedItem()));
				bufferAnalystParameter.setSemicircleLineSegment(Integer.parseInt(this.panelResultSet.getTextFieldSemicircleLineSegment().getText()));

				FormProgress formProgress = new FormProgress();
				// 当CheckBoxGeometrySelect()选中时，进行记录集缓冲分析，否则进行数据集缓冲分析
				if (this.panelBufferData.getCheckBoxGeometrySelect().isSelected()) {
					formProgress.doWork(new BufferProgressCallable(recordset, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
							.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected()));
				} else {
					formProgress.doWork(new BufferProgressCallable(sourceDatasetVector, resultDatasetVector, bufferAnalystParameter, this.panelResultSet
							.getCheckBoxUnionBuffer().isSelected(), this.panelResultSet.getCheckBoxRemainAttributes().isSelected()));
				}
				bufferCreate = true;
			} else {
				bufferCreate = false;
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreating"));
				Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_BufferCreatedFailed"));
			}
		}

		return bufferCreate;
	}

	class LocalItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {

			if (e.getSource() == panelBufferData.getComboBoxBufferDataDatasource()) {
				panelBufferData.getComboBoxBufferDataDataset().removeAllItems();
				String datasourceAlis = e.getItem().toString();
				Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().get(datasourceAlis);
				if (e.getStateChange() == ItemEvent.SELECTED) {
					panelBufferData.getComboBoxBufferDataDataset().setDatasets(datasource.getDatasets());
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
						// 切换comboBoxDatasource时，如果comboBoxDataset为空时将字段选项置灰，默认选中数值型
						comboBoxFieldLeft.removeAllItems();
						comboBoxFieldRight.removeAllItems();
						setEnabled(false);
					} else {
						setEnabled(true);
					}
				}
				// 切换数据源后，如果ComboBoxDataset为空时，清除字段选项

				setComponentEnabled();
			} else if (e.getSource() == panelBufferData.getComboBoxBufferDataDataset()) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxFieldLeft.removeAllItems();
					comboBoxFieldRight.removeAllItems();
					// 如果所选数据集不为空，创建字段ComboBox，否则不做操作
					if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() != null) {
						Dataset datasetItem = panelBufferData.getComboBoxBufferDataDatasource().getSelectedDatasource().getDatasets()
								.get(e.getItem().toString());
						comboBoxField = new ComboBoxField(datasetItem, comboBoxFieldLeft, comboBoxFieldRight);
						comboBoxField.createComboBoxField(datasetItem, comboBoxFieldLeft, comboBoxFieldRight);
						setEnabled(true);
					} else {
						setEnabled(false);
					}
				}
			} else if (e.getSource() == comboBoxFieldLeft) {
				if (comboBoxFieldLeft.getSelectedItem() != null) {
					if (radioButtonBufferTypeRound.isSelected()) {
						comboBoxFieldRight.setSelectedItem(comboBoxFieldLeft.getSelectedItem());
						radiusLeft = comboBoxFieldLeft.getSelectedItem().toString();
					}

				}

			} else if (e.getSource() == comboBoxFieldRight) {
				if (comboBoxFieldRight.getSelectedItem() != null) {
					radiusRight = comboBoxFieldRight.getSelectedItem().toString();
				}
			} else if (e.getSource() == panelBufferData.getCheckBoxGeometrySelect()) {
				setComponentEnabled();
			} else if (e.getSource() == panelResultSet.getCheckBoxUnionBuffer()) {
				setComponentEnabled();
				panelResultSet.getCheckBoxRemainAttributes().setSelected(false);
			} else if (e.getSource() == checkBoxBufferLeft) {
				setComponentEnabled();
			} else if (e.getSource() == checkBoxBufferRight) {
				setComponentEnabled();
			}
		}
	}

	class LocalActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonBufferTypeRound) {
				textFieldNumericRight.setText(textFieldNumericLeft.getText());
				textFieldNumericLeft.addKeyListener(localKeylistener);
				textFieldNumericRight.addKeyListener(localKeylistener);
				if (comboBoxFieldLeft.getSelectedItem() != null) {
					comboBoxFieldLeft.setSelectedIndex(0);
					comboBoxFieldRight.setSelectedIndex(0);
				}
				checkBoxBufferLeft.setSelected(true);
				checkBoxBufferRight.setSelected(true);
				setComponentEnabled();
			} else if (e.getSource() == radioButtonBufferTypeFlat) {
				textFieldNumericLeft.removeKeyListener(localKeylistener);
				textFieldNumericRight.removeKeyListener(localKeylistener);

				setComponentEnabled();
			} else if (e.getSource() == radioButtonField) {
				if (comboBoxFieldLeft.getSelectedItem() != null) {
					radiusLeft = comboBoxFieldLeft.getSelectedItem().toString();
				}
				if (comboBoxFieldRight.getSelectedItem() != null) {
					radiusRight = comboBoxFieldRight.getSelectedItem().toString();
				}
				setComponentEnabled();
			} else if (e.getSource() == radioButtonNumeric) {
				radiusLeft = Math.abs(Double.parseDouble(textFieldNumericLeft.getValue().toString()));
				radiusRight = Math.abs(Double.parseDouble(textFieldNumericRight.getValue().toString()));
				setComponentEnabled();
			}
		}
	}

	class LocalKeylistener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			textFieldNumericRight.setText(((JTextField) e.getSource()).getText());
			textFieldNumericLeft.setText(((JTextField) e.getSource()).getText());
			updateUI();
		}
	}

	class LocalDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			getButtonOkEnabled(e.getDocument());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			getButtonOkEnabled(e.getDocument());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			getButtonOkEnabled(e.getDocument());
		}

		private void getButtonOkEnabled(Document document) {
			try {
				long value = Long.parseLong(document.getText(0, document.getLength()));
				if (value < 4 || value > 200) {
					setButtonOkEnabled(false);
				} else {
					setButtonOkEnabled(true);
				}
			} catch (Exception e) {
				setButtonOkEnabled(false);
			}

		}
	}
}
