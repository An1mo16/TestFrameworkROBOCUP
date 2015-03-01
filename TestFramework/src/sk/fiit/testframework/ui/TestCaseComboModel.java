/**
 * Name:    TestCaseComboModel.java
 * Created: Mar 28, 2012
 * 
 * @author: Peto
 */
package sk.fiit.testframework.ui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import sk.fiit.testframework.ui.TestCaseList.TestHolder;

public class TestCaseComboModel implements ComboBoxModel {
	
	private List<ListDataListener> listeners;
	private TestHolder selectedCase = null;
	
	public TestCaseComboModel() {
		listeners = new LinkedList<ListDataListener>();
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public Object getElementAt(int at) {
		return TestCaseList.testCaseList.get(at);
	}

	@Override
	public int getSize() {
		return TestCaseList.testCaseList.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public Object getSelectedItem() {
		if (getSize() == 0)
			return null;
		if (selectedCase == null)
			return getElementAt(0);
		return selectedCase;
	}

	@Override
	public void setSelectedItem(Object obj) {
		selectedCase = (TestHolder)obj;
	}
}
