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
package org.freeplane.view.swing.map.attribute;

import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.freeplane.core.model.ModeController;
import org.freeplane.core.model.NodeModel;
import org.freeplane.features.common.attribute.AttributeController;
import org.freeplane.features.common.attribute.AttributeRegistry;
import org.freeplane.features.common.attribute.IAttributeTableModel;
import org.freeplane.features.common.attribute.NodeAttributeTableModel;

/**
 * @author Dimitry Polivaev
 */
abstract class AttributeTableModelDecoratorAdapter extends AbstractTableModel implements IAttributeTableModel,
        TableModelListener, ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private AttributeController attributeController;
	private AttributeRegistry attributeRegistry;
	private NodeAttributeTableModel nodeAttributeModel;

	public AttributeTableModelDecoratorAdapter(final AttributeView attrView) {
		super();
		setNodeAttributeModel(attrView.getAttributes());
		setAttributeRegistry(attrView.getAttributeRegistry());
		getNodeAttributeModel().getNode();
		final ModeController modeController = attrView.getMapView().getModeController();
		attributeController = AttributeController.getController(modeController);
		addListeners();
	}

	private void addListeners() {
		getNodeAttributeModel().addTableModelListener(this);
		getAttributeRegistry().addChangeListener(this);
	}

	/**
	 * @param view
	 */
	public abstract boolean areAttributesVisible();

	public void editingCanceled() {
	}

	public AttributeController getAttributeController() {
		return attributeController;
	}

	public AttributeRegistry getAttributeRegistry() {
		return attributeRegistry;
	}

	@Override
	public Class getColumnClass(final int columnIndex) {
		return getNodeAttributeModel().getColumnClass(columnIndex);
	}

	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return getNodeAttributeModel().getColumnName(columnIndex);
	}

	public int getColumnWidth(final int col) {
		return getNodeAttributeModel().getColumnWidth(col);
	}

	public NodeModel getNode() {
		return getNodeAttributeModel().getNode();
	}

	public NodeAttributeTableModel getNodeAttributeModel() {
		return nodeAttributeModel;
	}

	private void removeListeners() {
		getNodeAttributeModel().removeTableModelListener(this);
		getAttributeRegistry().removeChangeListener(this);
	}

	public void setAttributeRegistry(final AttributeRegistry attributeRegistry) {
		this.attributeRegistry = attributeRegistry;
	}

	public void setColumnWidth(final int col, final int width) {
		getAttributeController().performSetColumnWidth(getNodeAttributeModel(), col, width);
	}

	public void setNodeAttributeModel(final NodeAttributeTableModel nodeAttributeModel) {
		this.nodeAttributeModel = nodeAttributeModel;
	}

	/*
	 * (non-Javadoc)
	 * @seejavax.swing.event.TableModelListener#tableChanged(javax.swing.event.
	 * TableModelEvent)
	 */
	public void tableChanged(final TableModelEvent e) {
		fireTableDataChanged();
	}

	public void viewRemoved() {
		removeListeners();
	}
}
