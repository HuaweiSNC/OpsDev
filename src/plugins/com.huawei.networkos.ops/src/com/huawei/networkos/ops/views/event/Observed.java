package com.huawei.networkos.ops.views.event;

import java.util.Observable;
import org.eclipse.core.resources.IProject;

public class Observed extends Observable {

	public void notifyObserver(IProject[] iProjects) {
		this.setChanged();
		this.notifyObservers(iProjects);
	}
}
