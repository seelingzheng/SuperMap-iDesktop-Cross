package com.supermap.desktop.CtrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.JDialogTabularUpdateColumn;

public class CtrlActionTabularUpdateColumn extends CtrlAction {

	public CtrlActionTabularUpdateColumn(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}
	@Override
	public void run() {
		IFormTabular tabular = (IFormTabular) Application.getActiveApplication().getActiveForm();
		JDialogTabularUpdateColumn dialogTabularUpdateColumn = new JDialogTabularUpdateColumn(tabular);
		dialogTabularUpdateColumn.showDialog();
	}

	@Override
	public boolean enable() {
		boolean flag = false;
		if (Application.getActiveApplication().getActiveForm() instanceof IFormTabular && ((IFormTabular) Application.getActiveApplication().getActiveForm()).getRowCount() > 0) {
				flag = true;
		}
		return flag;
	}
}
