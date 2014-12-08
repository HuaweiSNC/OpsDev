package com.huawei.networkos.ops.wizards;


import org.eclipse.jface.viewers.LabelProvider;

public class TreeLabelProvider extends LabelProvider {
	
	public String getText(Object element) {

		return element.toString();
	}
}
