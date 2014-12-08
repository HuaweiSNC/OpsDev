package com.huawei.networkos.ops.views.event;

import java.util.Map;

import com.huawei.network.opsdev.core.event.OpsEventListener;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool.OPSEVENT;
import com.huawei.networkos.ops.views.DeviceView;

public class DeviceViewListener implements OpsEventListener {

	private DeviceView deviceView = null;
	
	public DeviceViewListener() {
		
	}

    public DeviceViewListener(DeviceView deviceView ) {
		this.deviceView = deviceView;
	}


	public void notifyEvent(OPSEVENT event, Map<String, Object> mapEventValue) {

		if (null == deviceView)
		{
			return;
		}
		
		if (event == OPSEVENT.PROJECT_REFRUSH)
		{
			deviceView.resetProjectList(mapEventValue.values());
		}
 
	}
	
 
}
