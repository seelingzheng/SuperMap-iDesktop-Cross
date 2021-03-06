package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

/**
 * 关联窗口接口
 * 
 * @author xie
 *
 */
public interface IBindWindow {

	/**
	 * 选择集是否变化
	 * 
	 * @return
	 */
	public boolean isSelectionHasChanged();

	/**
	 * 设置当前活动的图层
	 * 
	 * @param layer
	 */
	public void setActiveLayer(Layer layer);

	/**
	 * 获取当前活动的图层
	 * 
	 * @param layer
	 */
	public Layer getActiveLayer();

	/**
	 * 刷新属性表
	 */
	public void refreshFormTabular(int[] addRows);

	/**
	 * 移除事件
	 */
	public void removeEvents();

	/**
	 * 销毁类，释放资源
	 */
	public void dispose();

	public void addMapSelectionChangeListener(MapSelectionChangeListener l);

	public void removeMapSelectionChangeListener(MapSelectionChangeListener l);

    public void fireSelectionChanged(Selection selection, Layer layer);

	/**
	 * 获取属性表
	 */
	public IFormTabular getTabular();
}
