/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.view.swing.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.freeplane.features.nodestyle.ShapeConfigurationModel;

class WideHexagonMainView extends VariableInsetsMainView {
	private static final double VERTICAL_MARGIN_FACTOR = Math.sqrt(2);
	private static final double UNIFORM_HEIGHT_TO_WIDTH_RELATION = Math.sqrt(3)/2;
	private static final double HORIZONTAL_MARGIN_FACTOR = Math.sqrt(2)/ UNIFORM_HEIGHT_TO_WIDTH_RELATION;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WideHexagonMainView(ShapeConfigurationModel shapeConfigurationModel) {
		super(shapeConfigurationModel);
	}

	protected double getVerticalMarginFactor() {
		return VERTICAL_MARGIN_FACTOR;
	}
	
	protected double getHorizontalMarginFactor() {
		return HORIZONTAL_MARGIN_FACTOR;
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		if(getShapeConfiguration().isUniform()) {
			final Dimension prefSize = getPreferredRectangleSizeWithoutMargin(getMaximumWidth());
			double w = prefSize.width + getZoom() * getMinimumHorizontalInset();
			double h = prefSize.height + getZoom() * getMinimumVerticalInset();
			double diameter = Math.sqrt(w * w + h * h);
			double width = limitWidth (diameter/ UNIFORM_HEIGHT_TO_WIDTH_RELATION);
			prefSize.width = (int) Math.ceil(width);
			prefSize.height = (int) (width * UNIFORM_HEIGHT_TO_WIDTH_RELATION);
			return prefSize;
		}
		else
			return super.getPreferredSize();
	}

	
	@Override
	protected void paintNodeShape(final Graphics2D g) {
		Polygon polygon = getPaintedShape();
		g.draw(polygon);
	}

	protected Polygon getPaintedShape() {
		final Polygon polygon;
		if(getShapeConfiguration().isUniform()) {
			int[] xCoords = new int[]{0,   getWidth()/4, 3 * getWidth() /4 , getWidth(),      3 * getWidth() / 4, getWidth() / 4};
			int[] yCoords = new int[]{getHeight() / 2, 0,  0,  getHeight() / 2, getHeight() - 1, getHeight() - 1};
			polygon = new Polygon(xCoords, yCoords, xCoords.length);
		}
		else {
			final int zoomedHorizontalInset = (int) (getWidth() * (1 - 1 / getHorizontalMarginFactor()) / 2);
			int[] xCoords = new int[]{0,               zoomedHorizontalInset, getWidth() - zoomedHorizontalInset - 1, getWidth(),      getWidth() - zoomedHorizontalInset - 1, zoomedHorizontalInset};
			int[] yCoords = new int[]{getHeight() / 2, 0,                     0,                                      getHeight() / 2, getHeight() - 1,                        getHeight() - 1};
			polygon = new Polygon(xCoords, yCoords, xCoords.length);
		}
		return polygon;
	}

	@Override
	protected void paintBackground(final Graphics2D graphics, final Color color) {
		graphics.setColor(color);
		graphics.fill(getPaintedShape());
	}
}
