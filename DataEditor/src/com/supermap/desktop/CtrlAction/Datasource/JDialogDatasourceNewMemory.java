package com.supermap.desktop.CtrlAction.Datasource;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit.DatasourceWrap;
import com.supermap.desktop.CtrlAction.Dataset.SpatialIndex.JDialogBulidSpatialIndex;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilties.DatasourceUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class JDialogDatasourceNewMemory extends SmDialog {

	private final JPanel contentPanel = new JPanel();
	private SmTextFieldLegit jTextFieldAlias;
	private JLabel jLabelAlias;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public JDialogDatasourceNewMemory() {
		setTitle("Create Memory Datasource");
		setBounds(100, 100, 340, 147);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		jLabelAlias = new JLabel("Alias:");
		jTextFieldAlias = new SmTextFieldLegit();
		jTextFieldAlias.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (null != textFieldValue && !textFieldValue.isEmpty()) {
					char c = textFieldValue.charAt(0);
					if ('_' == c || ('0' < c && c < '9')) {
						return false;
					}
				} else {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		jTextFieldAlias.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_contentPanel.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelAlias).addGap(40)
						.addComponent(jTextFieldAlias, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jTextFieldAlias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabelAlias)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		contentPanel.setLayout(gl_contentPanel);
		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(75, 23));
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		});
		cancelButton.setActionCommand("Cancel");
		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(75, 23));
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okButton_Click();
			}
		});
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_buttonPane.createSequentialGroup().addContainerGap(158, Short.MAX_VALUE)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_buttonPane
						.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(
								gl_buttonPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		buttonPane.setLayout(gl_buttonPane);

		initializeResources();
		this.setLocationRelativeTo(null);
		this.jTextFieldAlias.setText(getAvaliableDatasourceName("MemoryDatasource"));
	}

	private void initializeResources() {
		try {
			this.setTitle(DataEditorProperties.getString("String_Title_NewDatasourceMemory"));
			this.jLabelAlias.setText(CommonProperties.getString("String_Label_Datasource"));
			this.okButton.setText(CommonProperties.getString("String_Button_OK"));
			this.cancelButton.setText(CommonProperties.getString("String_Button_Cancel"));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private Datasource createMemoryDatasource() {
		Datasource result = null;
		try {
			DatasourceConnectionInfo info = new DatasourceConnectionInfo();
			info.setEngineType(EngineType.UDB);
			info.setServer(":memory:");
			String alias = jTextFieldAlias.getText();
			if (null != alias && !alias.isEmpty() && "" != alias) {
				alias = DatasourceUtilties.getAvailableDatasourceAlias(alias, 0);
				info.setAlias(alias);
				result = Application.getActiveApplication().getWorkspace().getDatasources().create(info);
			} else {
				UICommonToolkit.showMessageDialog(DataEditorProperties.getString("String_Message_DatasourceNameUnavaliable"));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private String getAvaliableDatasourceName(String name) {
		String result = name;
		try {
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			int index = 0;
			while (workspace.getDatasources().indexOf(result) != -1) {
				index++;
				result = name + "_" + String.valueOf(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	private void okButton_Click() {
		try {
			Datasource datasource = this.createMemoryDatasource();
			if (datasource != null) {
				UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
				this.dispose();
				this.dialogResult = DialogResult.OK;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void cancelButton_Click() {
		try {
			this.dispose();
			this.dialogResult = DialogResult.CANCEL;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	protected JRootPane createRootPane() {
		return keyBoardPressed();
	}

	@Override
	public JRootPane keyBoardPressed() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDialogResult(DialogResult.OK);
				okButton_Click();
			}
		}, strokForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokForEsc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButton_Click();
			}
		}, strokForEsc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
	
}
