package com.supermap.desktop.CtrlAction.transformationForm.CtrlAction;

import com.supermap.data.Datasources;
import com.supermap.data.Transformation;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.transformationForm.Dialogs.JDialogTransformation;
import com.supermap.desktop.CtrlAction.transformationForm.beans.TransformationAddObjectBean;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.implement.CtrlAction;

/**
 * @author XiaJT
 */
public class CtrlActionTransform extends CtrlAction {
	public CtrlActionTransform(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormTransformation) {
			Transformation transformation = ((IFormTransformation) activeForm).getTransformation();
			if (transformation != null) {

				Object[] transformationObjects = ((IFormTransformation) activeForm).getTransformationObjects();
				if (transformationObjects.length > 0 && transformationObjects[0] instanceof TransformationAddObjectBean) {
					TransformationAddObjectBean[] transformationAddObjectBeen = new TransformationAddObjectBean[transformationObjects.length];
					for (int i = 0; i < transformationObjects.length; i++) {
						Object transformationObject = transformationObjects[i];
						transformationAddObjectBeen[i] = (TransformationAddObjectBean) transformationObject;
					}
					JDialogTransformation jDialogTransformation = new JDialogTransformation();
					jDialogTransformation.setTransformation(transformation);
					jDialogTransformation.addBeans(transformationAddObjectBeen);
					jDialogTransformation.setBeOnly();
					jDialogTransformation.showDialog();
				}
			}
		}
	}

	@Override
	public boolean enable() {
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (!(activeForm instanceof IFormTransformation)) {
			return false;
		}
		if (((IFormTransformation) activeForm).getTransformation() == null) {
			return false;
		}
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Datasources datasources = workspace.getDatasources();
		for (int count = datasources.getCount() - 1; count >= 0; count--) {
			if (datasources.get(count).isOpened() && !datasources.get(count).isReadOnly()) {
				return true;
			}
		}
		return false;
	}
}
