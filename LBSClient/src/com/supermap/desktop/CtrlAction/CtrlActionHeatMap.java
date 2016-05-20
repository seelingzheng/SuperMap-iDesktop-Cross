package com.supermap.desktop.CtrlAction;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.CtrlActionFindTrack.WorkThead;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;

public class CtrlActionHeatMap extends CtrlAction {

	String topicNameRespond = "KernelDensity_Respond";
	
	public CtrlActionHeatMap(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			JFrame parent = (JFrame)Application.getActiveApplication().getMainFrame();
			JDialogHeatMap dialog = new JDialogHeatMap(parent, true);
			DialogResult result = dialog.showDialog();
			if (result == DialogResult.OK || result == DialogResult.APPLY) {
				WorkThead thread = new WorkThead();
				thread.start();
			}		
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean enable() {
		return true;
	}
	
	class WorkThead extends Thread {

		@Override
		public void run() {
			try {
				lbsResultConsumer consumer = new lbsResultConsumer();
				consumer.doWork(topicNameRespond);
			} finally {
			}
		}
	}
}