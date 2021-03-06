package com.supermap.desktop.http;

import com.supermap.Interface.ITask;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import java.awt.*;
import java.util.ArrayList;

public class FileManagerContainer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ITask> items;

	GroupLayout groupLayout;
	Group horizontalGroup = null;
	Group verticalGroup = null;

	public FileManagerContainer() {
		items = new ArrayList<ITask>();

		initializeComponents();
		initializeResources();
	}

	private void initializeComponents() {

		groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		updateItems();
	}

	private void initializeResources() {
		// this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
	}

	private void updateItems() {
		removeAll();
		horizontalGroup = groupLayout.createParallelGroup(Alignment.CENTER);
		groupLayout.setHorizontalGroup(horizontalGroup);

		verticalGroup = groupLayout.createSequentialGroup();
		groupLayout.setVerticalGroup(verticalGroup);

		for (ITask item : items) {
			horizontalGroup.addComponent((Component) item);
			verticalGroup.addComponent((Component) item);
		}
	}

	public void addItem(ITask task) {
		items.add(task);
		updateItems();
	}

	public void removeItem(ITask task) {
		for (ITask item : items) {
			if (item.equals(task)) {
				items.remove(item);
				groupLayout.removeLayoutComponent((Component) item);
				break;
			}
		}
		updateItems();
	}
}
