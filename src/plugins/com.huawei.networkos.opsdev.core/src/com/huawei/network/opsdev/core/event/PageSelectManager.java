package com.huawei.network.opsdev.core.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class PageSelectManager {

    private Collection listeners;

    public void addPageSelectListener(IPageSelectListening listener) {

        if (listeners == null) {

            listeners = new HashSet();

        }

        listeners.add(listener);
    }

    public void removePageSelectListener(IPageSelectListening listener) {

        if (listeners == null)
            return;

        listeners.remove(listener);
    }

    protected void fireWorkspaceOpened() {

        if (listeners == null)
            return;

        PageSelectChangeEvent event = new PageSelectChangeEvent(this);

        notifyListeners(event);
    }

    protected void fireWorkspaceClosed() {

        if (listeners == null)

            return;

        PageSelectChangeEvent event = new PageSelectChangeEvent(this);

        notifyListeners(event);
    }

    private void notifyListeners(PageSelectChangeEvent event) {

        Iterator iter = listeners.iterator();

        while (iter.hasNext()) {

            IPageSelectListening listener = (IPageSelectListening) iter.next();

            listener.pageSelectChangeEvent(event);
        }
    }
}
