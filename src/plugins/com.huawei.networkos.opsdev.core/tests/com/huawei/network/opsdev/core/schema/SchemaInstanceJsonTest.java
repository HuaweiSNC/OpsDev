package com.huawei.network.opsdev.core.schema;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;


public class SchemaInstanceJsonTest extends TestCase{

	
 
	
	@Before
	public void setUp() throws Exception {
		
	 
		
	}

	@After
	public void tearDown() throws Exception {
		 
	}
	
	public void testXsdInstance()
	{
	 /*   List<String> lstSchemaFile = new ArrayList<String> ();
	    lstSchemaFile.add("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\aaa.xsd");*/
	    RestfulApiSchemaManager restfulApi = new RestfulApiSchemaManager();
	    restfulApi.processXsd("E:\\ops-eclipse\\opsdev6\\plugins\\com.huawei.networkos.opsdev.core\\OPS2.0\\ops\\resource\\ifm_action.xsd", true);
	    RestApiManager apiManager =  restfulApi.getRestApiManager();
	    System.out.println(apiManager.getMapRestApi());
	    System.out.println(apiManager.getMapFileNameUrl());
	    apiManager.refurbishAllRestApi();
	    System.out.println(apiManager.getRestApiJsonByUrl("/bfd/bfdResetSessSta"));
        
	    //RestfulApiSchemaManager.processXsd("E:\\OPScode\\成果\\XSD_TO_REST\\xsd2rest\\xsd2rest\\resource\\ifm.xsd", false);
	    //RestfulApiSchemaManager.processXsd("E:\\OPScode\\成果\\XSD_TO_REST\\xsd2rest\\xsd2rest\\resource\\ifm.xsd", false);
       // schemaInstance.xsdInstance("E:\\OPScode\\成果\\XSD_TO_REST\\xsd2rest\\xsd2rest\\resource\\l3vpn.xsd", "l3vpn");
  	   /*  restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\acl.xsd", true);
         apiManager =  restfulApi.getRestApiManager();
        System.out.println(apiManager.getMapRestApi());
        System.out.println(apiManager.getMapFileNameUrl()); 
        
       restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\arp.xsd", true);
         apiManager =  restfulApi.getRestApiManager();
        System.out.println(apiManager.getMapRestApi());
        System.out.println(apiManager.getMapFileNameUrl());
 
  
        
        restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\bgp.xsd", true);
         apiManager =  restfulApi.getRestApiManager();
        System.out.println(apiManager.getMapRestApi());
        System.out.println(apiManager.getMapFileNameUrl());
       
         restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\cfg.xsd", true);
         apiManager =  restfulApi.getRestApiManager();
        System.out.println(apiManager.getMapRestApi());
        System.out.println(apiManager.getMapFileNameUrl());
        
 
        restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\bfd.xsd", true);
        apiManager =  restfulApi.getRestApiManager();
       System.out.println(apiManager.getMapRestApi());
       System.out.println(apiManager.getMapFileNameUrl()); 
        
	    
       restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\devm.xsd", true);
       apiManager =  restfulApi.getRestApiManager();
      System.out.println(apiManager.getMapRestApi());
      System.out.println(apiManager.getMapFileNameUrl()); 
       
      
      restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\dgmp.xsd", true);
      apiManager =  restfulApi.getRestApiManager();
     System.out.println(apiManager.getMapRestApi());
     System.out.println(apiManager.getMapFileNameUrl()); 
     
     
     restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\dhcp.xsd", true);
     apiManager =  restfulApi.getRestApiManager();
    System.out.println(apiManager.getMapRestApi());
    System.out.println(apiManager.getMapFileNameUrl()); 
    
    
    
   restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\directrt.xsd", true);
   apiManager =  restfulApi.getRestApiManager();
  System.out.println(apiManager.getMapRestApi());
  System.out.println(apiManager.getMapFileNameUrl()); 
  
  
  restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\dldp.xsd", true);
  apiManager =  restfulApi.getRestApiManager();
 System.out.println(apiManager.getMapRestApi());
 System.out.println(apiManager.getMapFileNameUrl()); 
  
 
 restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\dtools.xsd", true);
 apiManager =  restfulApi.getRestApiManager();
System.out.println(apiManager.getMapRestApi());
System.out.println(apiManager.getMapFileNameUrl()); 


restfulApi.processXsd("E:\\ops-eclipse\\opsdev\\plugins\\org.python.pydev\\OPS2.0\\ops\\resource\\efm.xsd", true);
apiManager =  restfulApi.getRestApiManager();
System.out.println(apiManager.getMapRestApi());
System.out.println(apiManager.getMapFileNameUrl()); */
  
	}

}
