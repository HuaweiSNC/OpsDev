package com.huawei.networkos.ops;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool; 

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.huawei.networkos.ops"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public static IProject[] iProjects;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener(){

			public void resourceChanged(IResourceChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				Map<String, Object> mapEventValue =  new LinkedHashMap<String, Object> ();
				
				for (IProject iProject :iProjects)
				{
					mapEventValue.put(iProject.getName(), iProject);
				}
				
				OpsManagerProjectTool.notifyEvent(OpsManagerProjectTool.OPSEVENT.PROJECT_REFRUSH, mapEventValue);
				
			}});
			
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
