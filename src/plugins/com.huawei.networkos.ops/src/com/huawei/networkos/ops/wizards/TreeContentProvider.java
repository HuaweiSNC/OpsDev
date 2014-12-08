package com.huawei.networkos.ops.wizards;


import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.huawei.network.opsdev.core.schema.OpsSchemaApiItem;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;



public class TreeContentProvider implements ITreeContentProvider {

    private List<OpsSchemaApiItem> lstOpsSchema = new ArrayList<OpsSchemaApiItem>();
	private ProjectTempletManager templetManager =null;

	public void setManager(ProjectTempletManager manager) {
		this.templetManager = manager;
	}

	
	public Object[] getChildren(Object arg0) {
		
		List<OpsSchemaApiItem> lstOpsSchema1  = new ArrayList<OpsSchemaApiItem>();
		if (arg0 instanceof OpsSchemaApiItem)
		{
			OpsSchemaApiItem item = null;
			OpsSchemaApiItem opsschema = (OpsSchemaApiItem)arg0;
			if (opsschema.isSchemaFile() && templetManager != null)
			{
				//  if parsed ,  get schema apis from memory
				if(opsschema.hasChildren())
				{
					return opsschema.getLstChild().toArray();
				}
				
				String schemaFile = opsschema.getSchemaFile();
				templetManager.addFile(schemaFile);
				 Map<String, List<String>>  lstMan = templetManager.getApiUrls();
				 String schemaName = (new File(schemaFile)).getName();

				for (String str : templetManager.getApiUrlsByName(schemaName)) {
					//TreeItem item = new TreeItem(rustfulTree.getSelection()[0], SWT.BORDER);
					//item.setText(str);
					item =  new OpsSchemaApiItem(schemaFile, str);
					lstOpsSchema1.add(item);
				} 
				opsschema.addChild(lstOpsSchema1);
			}
		}
		return lstOpsSchema1.toArray();
	}

	
	public Object getParent(Object arg0) {
		return null;
	}

	
	public boolean hasChildren(Object arg0) {
		
		if (arg0 instanceof OpsSchemaApiItem)
		{
			OpsSchemaApiItem opsschema = (OpsSchemaApiItem)arg0;
			return opsschema.isSchemaFile();
		}
		return false;
	}

 
	
	public Object[] getElements(Object arg0) {
		return lstOpsSchema.toArray();
	}

	
	public void dispose() {
	}

	
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				
	}
	
	public void addOpsSchema(List<OpsSchemaApiItem> lstOpsScheam)
	{
		lstOpsSchema.addAll(lstOpsScheam);
	}

}