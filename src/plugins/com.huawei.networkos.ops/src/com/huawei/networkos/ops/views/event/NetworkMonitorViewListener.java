package com.huawei.networkos.ops.views.event;

import java.util.Map;

import com.huawei.network.opsdev.core.event.OpsEventListener;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool.OPSEVENT;
import com.huawei.networkos.ops.views.NetworkMonitorView;

public class NetworkMonitorViewListener implements OpsEventListener {

	private NetworkMonitorView networkMonitor = null;
	
	public NetworkMonitorViewListener() {
		// TODO Auto-generated constructor stub
	}

	public NetworkMonitorViewListener(NetworkMonitorView networkMonitor) {
		this.networkMonitor = networkMonitor;
	}

	public void notifyEvent(OPSEVENT event, Map<String, Object> mapEventValue) {

		if (null == networkMonitor)
		{
			return;
		}
		
		if (event == OPSEVENT.PROJECT_REFRUSH)
		{
			networkMonitor.resetProjectList(mapEventValue.values());
		}
 
	}

}
