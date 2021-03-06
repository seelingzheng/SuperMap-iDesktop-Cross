package com.supermap.desktop.ui.controls;

import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 填充库节点装饰器
 * 
 * @author xuzw
 *
 */
class SymbolFillLibNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.SYMBOL_FILL_LIBRARY)) {
			label.setText(ControlsProperties.getString(ControlsProperties.SymbolFillLibNodeName));
			ImageIcon icon = (ImageIcon) label.getIcon();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			graphics.drawImage(InternalImageIconFactory.SYMBOLFillLIB.getImage(), 0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

}
