package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoLine;
import com.supermap.data.GeoRegion;
import com.supermap.data.Point2Ds;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.ILineConvertor;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IRegionConvertor;
import com.supermap.desktop.geometry.Abstract.IRegionFeature;

public class DGeoRegion extends AbstractGeometry implements IMultiPartFeature, IRegionFeature, IRegionConvertor, ILineConvertor {

	private GeoRegion geoRegion;

	protected DGeoRegion(GeoRegion geoRegion) {
		super(geoRegion);
		this.geoRegion = geoRegion;
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoRegion == null ? null : this.geoRegion.convertToLine();
	}

	/**
	 * 返回自己
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoRegion;
	}

	@Override
	public int getPartCount() {
		return this.geoRegion == null ? -1 : this.geoRegion.getPartCount();
	}

	@Override
	public Point2Ds getPart(int index) {
		return this.geoRegion == null ? null : this.geoRegion.getPart(index);
	}
}
