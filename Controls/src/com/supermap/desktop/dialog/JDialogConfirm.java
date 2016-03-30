package com.supermap.desktop.dialog;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class JDialogConfirm extends SmDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SmButton buttonOK;
	private SmButton buttonCancel;
	private JCheckBox checkBoxConfirm; // 是否保持本次设置，后面不再提示
	private JTextArea textAreaMessage;

	/**
	 * Create the dialog.
	 */
	public JDialogConfirm() {
		initializeComponents();
		this.buttonOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogConfirm.this.dialogResult = DialogResult.OK;
				JDialogConfirm.this.setVisible(false);
			}
		});

		this.buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogConfirm.this.dialogResult = DialogResult.CANCEL;
				JDialogConfirm.this.setVisible(false);
			}
		});
	}

	public JDialogConfirm(String message) {
		this();
		this.textAreaMessage.setText(message);
	}

	public JDialogConfirm(String message, boolean isUsedAsDefault) {
		this(message);
		this.checkBoxConfirm.setSelected(isUsedAsDefault);
	}

	public boolean isUsedAsDefault() {
		return this.checkBoxConfirm.isSelected();
	}

	public void setMessage(String message) {
		this.textAreaMessage.setText(message);
	}

	private void initializeComponents() {
		setTitle(CoreProperties.getString("String_Prompt"));
		setResizable(false);
		setSize(new Dimension(450, 150));
		setLocationRelativeTo(null);

		this.buttonOK = new SmButton(CommonProperties.getString(CommonProperties.True));
		this.buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.False));
		this.checkBoxConfirm = new JCheckBox(ControlsProperties.getString("String_MessageBox_Checked"));
		this.checkBoxConfirm.setSelected(true);
		this.textAreaMessage = new JTextArea();
		this.textAreaMessage.setEditable(false);
		this.textAreaMessage.setBackground(this.getBackground());
		this.textAreaMessage.setLineWrap(true);
		this.textAreaMessage.setBorder(null);
		this.getRootPane().setDefaultButton(this.buttonOK);
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(this.textAreaMessage,400,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.checkBoxConfirm)
						.addGap(10,10,Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.textAreaMessage,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxConfirm)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	@Override
	protected JRootPane createRootPane() {
		return keyBoardPressed();
	}

	@Override
	public JRootPane keyBoardPressed() {
		JRootPane rootPane = new JRootPane();
		KeyStroke strokeForEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogConfirm.this.dialogResult = DialogResult.OK;
				JDialogConfirm.this.dispose();
			}
		}, strokeForEnter, JComponent.WHEN_IN_FOCUSED_WINDOW);
		KeyStroke strokeForEsc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogConfirm.this.dialogResult = DialogResult.CANCEL;
				JDialogConfirm.this.dispose();
			}
		}, strokeForEsc, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
