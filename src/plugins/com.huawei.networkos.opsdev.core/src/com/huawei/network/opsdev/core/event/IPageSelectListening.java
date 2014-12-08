package com.huawei.network.opsdev.core.event;

import java.util.EventListener;

public interface IPageSelectListening extends EventListener {
    public void pageSelectChangeEvent(PageSelectChangeEvent event);

}
