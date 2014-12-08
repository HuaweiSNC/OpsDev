package com.huawei.network.opsdev.core.event;

import java.util.Map;

import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool.OPSEVENT;

public interface OpsEventListener {
    
   void notifyEvent(OPSEVENT event, Map<String, Object> mapEventValue);
    
}
