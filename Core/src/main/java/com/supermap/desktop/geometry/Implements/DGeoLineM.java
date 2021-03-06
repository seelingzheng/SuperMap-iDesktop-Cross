package com.supermap.desktop.geometry.Implements;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.*;

public class DGeoLineM extends AbstractGeometry implements ILineMFeature, IMultiPartFeature<PointMs>, ILineConvertor, IRegionConvertor, IReverse {

	private GeoLineM geoLineM;

	protected DGeoLineM(GeoLineM geoLineM) {
		super(geoLineM);
		this.geoLineM = geoLineM;
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoRegion convertToRegion(int segment) {
		return this.geoLineM == null ? null : this.geoLineM.convertToRegion();
	}

	/**
	 * @param segment
	 *            本类本参数无效
	 * @return
	 */
	@Override
	public GeoLine convertToLine(int segment) {
		return this.geoLineM == null ? null : this.geoLineM.convertToLine();
	}

	@Override
	public int getPartCount() {
		return this.geoLineM == null ? -1 : this.geoLineM.getPartCount();
	}

	@Override
	public PointMs getPart(int index) {
		return this.geoLineM == null ? null : this.geoLineM.getPart(index);
	}

	@Override
	public void addPart(PointMs part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPart(Geometry geometry) {
		try {
			if (this.geoLineM != null && geometry instanceof GeoLineM) {
				GeoLineM geoLineM = (GeoLineM) geometry;

				for (int i = 0; i < geoLineM.getPartCount(); i++) {
					this.geoLineM.addPart(geoLineM.getPart(i));
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (geometry != null) {
				geometry.dispose();
			}
		}
	}

	@Override
	public Geometry[] divide() {
		if (this.geoLineM != null) {
			Geometry[] geometries = new Geometry[this.geoLineM.getPartCount()];

			for (int i = 0; i < this.geoLineM.getPartCount(); i++) {
				geometries[i] = new GeoLineM(this.geoLineM.getPart(i));
			}
			return geometries;
		}

		return null;
	}

	@Override
	public boolean setPart(int partIndex, PointMs part) {
		return this.geoLineM != null && this.geoLineM.setPart(partIndex, part);
	}

	@Override
	public Geometry reverse() {
		GeoLineM reverseLineM = this.geoLineM.clone();
		reverseLineM.reverse();
		return reverseLineM;
	}
}
