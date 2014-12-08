package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.BodyType;
import util.OpsRestCaller;
import util.OpsServiceManager;
import util.OpsXMLHelper;
import util.RestUtil;
import util.RetRpc;


public class StaticrtSrRoute {

    public final static String urlBody = "/staticrt/staticrtbase/srRoutes/srRoute";
    public final static String fullUrlbody = "/staticrt/staticrtbase/srRoutes/srRoute";
    public final static String S_BFDENABLE ="bfdEnable";
    public final static String S_AFTYPE ="afType";
    public final static String S_TAG ="tag";
    public final static String S_MASKLENGTH ="maskLength";
    public final static String S_TOPOLOGYNAME ="topologyName";
    public final static String S_NEXTHOP ="nexthop";
    public final static String S_DESTVRFNAME ="destVrfName";
    public final static String S_VRFNAME ="vrfName";
    public final static String S_SESSIONNAME ="sessionName";
    public final static String S_TRACKNQAADMINNAME ="trackNqaAdminName";
    public final static String S_PREFERENCE ="preference";
    public final static String S_DESCRIPTION ="description";
    public final static String S_PREFIX ="prefix";
    public final static String S_IFNAME ="ifName";
    public final static String S_TRACKNQATESTNAME ="trackNqaTestName";
    public final static String S_ISINHERITCOST ="isInheritCost";

    private Boolean readAccess = true;
    private OpsRestCaller rest = null;
	private static Map<String, String> criterions = null;
    private String errorstr = "";
	private String content = "";
    private int indexint = 0;
	private OneBody body = new OneBody();
	private List<OneBody> multiBody = new ArrayList<OneBody>();
	private Boolean bMultiOperate = false;
	private BodyType bodyType = BodyType.APPLICATION_XML;
	private Map<String, JSONObject> criterionsBuffer = new LinkedHashMap<String, JSONObject>();
	

    static {
	    criterions = new HashMap<String, String>();
        criterions.put("bfdEnable", "{'data-type':'BOOL','example':'false','key':'false','access':'readCreate'}");        
        criterions.put("afType", "{'data-type':'ENUM','expected-values':'ipv4unicast;ipv6unicast','example':'ipv4unicast','key':'true','access':'readCreate'}");        
        criterions.put("tag", "{'data-type':'UINT32','min-val':'1','max-val':'4294967295','example':'7','key':'false','access':'readCreate'}");        
        criterions.put("maskLength", "{'data-type':'UINT32','min-val':'0','max-val':'128','example':'7','key':'true','access':'readCreate'}");        
        criterions.put("topologyName", "{'data-type':'STRING','min':'1','max':'31','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("nexthop", "{'data-type':'STRING','min':'0','max':'255','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("destVrfName", "{'data-type':'STRING','min':'1','max':'31','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("vrfName", "{'data-type':'STRING','min':'1','max':'31','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("sessionName", "{'data-type':'STRING','min':'1','max':'15','example':'string','key':'false','access':'readCreate'}");        
        criterions.put("trackNqaAdminName", "{'data-type':'STRING','min':'1','max':'32','example':'string','key':'false','access':'readCreate'}");        
        criterions.put("preference", "{'data-type':'UINT32','min-val':'1','max-val':'255','example':'7','key':'false','access':'readCreate'}");        
        criterions.put("description", "{'data-type':'STRING','min':'0','max':'35','example':'string','key':'false','access':'readCreate'}");        
        criterions.put("prefix", "{'data-type':'STRING','min':'0','max':'255','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("ifName", "{'data-type':'STRING','min':'1','max':'63','example':'string','key':'true','access':'readCreate'}");        
        criterions.put("trackNqaTestName", "{'data-type':'STRING','min':'1','max':'32','example':'string','key':'false','access':'readCreate'}");        
        criterions.put("isInheritCost", "{'data-type':'BOOL','example':'true','key':'false','access':'readCreate'}");        
	}
    
    public class OneBody{
    
        private String bfdEnable ="";
        private String afType ="";
        private String tag ="";
        private String maskLength ="";
        private String topologyName ="";
        private String nexthop ="";
        private String destVrfName ="";
        private String vrfName ="";
        private String sessionName ="";
        private String trackNqaAdminName ="";
        private String preference ="";
        private String description ="";
        private String prefix ="";
        private String ifName ="";
        private String trackNqaTestName ="";
        private String isInheritCost ="";

        public void setBfdEnable(String bfdEnable){
    	
           if(RestUtil.validata(getCriterion(S_BFDENABLE), bfdEnable))
           {
               this.bfdEnable = bfdEnable;
               if(!checkAccessRead(S_BFDENABLE))
               {
                   readAccess = false;
                   System.out.println("set bfdEnable success");
               }
           }else{
               System.out.println( "bfdEnable do not macth rolls,please check it again");
           }
        }
    	
    	public String getBfdEnable()
        {
            return this.bfdEnable;
        }
        public void setAfType(String afType){
    	
           if(RestUtil.validata(getCriterion(S_AFTYPE), afType))
           {
               this.afType = afType;
               if(!checkAccessRead(S_AFTYPE))
               {
                   readAccess = false;
                   System.out.println("set afType success");
               }
           }else{
               System.out.println( "afType do not macth rolls,please check it again");
           }
        }
    	
    	public String getAfType()
        {
            return this.afType;
        }
        public void setTag(String tag){
    	
           if(RestUtil.validata(getCriterion(S_TAG), tag))
           {
               this.tag = tag;
               if(!checkAccessRead(S_TAG))
               {
                   readAccess = false;
                   System.out.println("set tag success");
               }
           }else{
               System.out.println( "tag do not macth rolls,please check it again");
           }
        }
    	
    	public String getTag()
        {
            return this.tag;
        }
        public void setMaskLength(String maskLength){
    	
           if(RestUtil.validata(getCriterion(S_MASKLENGTH), maskLength))
           {
               this.maskLength = maskLength;
               if(!checkAccessRead(S_MASKLENGTH))
               {
                   readAccess = false;
                   System.out.println("set maskLength success");
               }
           }else{
               System.out.println( "maskLength do not macth rolls,please check it again");
           }
        }
    	
    	public String getMaskLength()
        {
            return this.maskLength;
        }
        public void setTopologyName(String topologyName){
    	
           if(RestUtil.validata(getCriterion(S_TOPOLOGYNAME), topologyName))
           {
               this.topologyName = topologyName;
               if(!checkAccessRead(S_TOPOLOGYNAME))
               {
                   readAccess = false;
                   System.out.println("set topologyName success");
               }
           }else{
               System.out.println( "topologyName do not macth rolls,please check it again");
           }
        }
    	
    	public String getTopologyName()
        {
            return this.topologyName;
        }
        public void setNexthop(String nexthop){
    	
           if(RestUtil.validata(getCriterion(S_NEXTHOP), nexthop))
           {
               this.nexthop = nexthop;
               if(!checkAccessRead(S_NEXTHOP))
               {
                   readAccess = false;
                   System.out.println("set nexthop success");
               }
           }else{
               System.out.println( "nexthop do not macth rolls,please check it again");
           }
        }
    	
    	public String getNexthop()
        {
            return this.nexthop;
        }
        public void setDestVrfName(String destVrfName){
    	
           if(RestUtil.validata(getCriterion(S_DESTVRFNAME), destVrfName))
           {
               this.destVrfName = destVrfName;
               if(!checkAccessRead(S_DESTVRFNAME))
               {
                   readAccess = false;
                   System.out.println("set destVrfName success");
               }
           }else{
               System.out.println( "destVrfName do not macth rolls,please check it again");
           }
        }
    	
    	public String getDestVrfName()
        {
            return this.destVrfName;
        }
        public void setVrfName(String vrfName){
    	
           if(RestUtil.validata(getCriterion(S_VRFNAME), vrfName))
           {
               this.vrfName = vrfName;
               if(!checkAccessRead(S_VRFNAME))
               {
                   readAccess = false;
                   System.out.println("set vrfName success");
               }
           }else{
               System.out.println( "vrfName do not macth rolls,please check it again");
           }
        }
    	
    	public String getVrfName()
        {
            return this.vrfName;
        }
        public void setSessionName(String sessionName){
    	
           if(RestUtil.validata(getCriterion(S_SESSIONNAME), sessionName))
           {
               this.sessionName = sessionName;
               if(!checkAccessRead(S_SESSIONNAME))
               {
                   readAccess = false;
                   System.out.println("set sessionName success");
               }
           }else{
               System.out.println( "sessionName do not macth rolls,please check it again");
           }
        }
    	
    	public String getSessionName()
        {
            return this.sessionName;
        }
        public void setTrackNqaAdminName(String trackNqaAdminName){
    	
           if(RestUtil.validata(getCriterion(S_TRACKNQAADMINNAME), trackNqaAdminName))
           {
               this.trackNqaAdminName = trackNqaAdminName;
               if(!checkAccessRead(S_TRACKNQAADMINNAME))
               {
                   readAccess = false;
                   System.out.println("set trackNqaAdminName success");
               }
           }else{
               System.out.println( "trackNqaAdminName do not macth rolls,please check it again");
           }
        }
    	
    	public String getTrackNqaAdminName()
        {
            return this.trackNqaAdminName;
        }
        public void setPreference(String preference){
    	
           if(RestUtil.validata(getCriterion(S_PREFERENCE), preference))
           {
               this.preference = preference;
               if(!checkAccessRead(S_PREFERENCE))
               {
                   readAccess = false;
                   System.out.println("set preference success");
               }
           }else{
               System.out.println( "preference do not macth rolls,please check it again");
           }
        }
    	
    	public String getPreference()
        {
            return this.preference;
        }
        public void setDescription(String description){
    	
           if(RestUtil.validata(getCriterion(S_DESCRIPTION), description))
           {
               this.description = description;
               if(!checkAccessRead(S_DESCRIPTION))
               {
                   readAccess = false;
                   System.out.println("set description success");
               }
           }else{
               System.out.println( "description do not macth rolls,please check it again");
           }
        }
    	
    	public String getDescription()
        {
            return this.description;
        }
        public void setPrefix(String prefix){
    	
           if(RestUtil.validata(getCriterion(S_PREFIX), prefix))
           {
               this.prefix = prefix;
               if(!checkAccessRead(S_PREFIX))
               {
                   readAccess = false;
                   System.out.println("set prefix success");
               }
           }else{
               System.out.println( "prefix do not macth rolls,please check it again");
           }
        }
    	
    	public String getPrefix()
        {
            return this.prefix;
        }
        public void setIfName(String ifName){
    	
           if(RestUtil.validata(getCriterion(S_IFNAME), ifName))
           {
               this.ifName = ifName;
               if(!checkAccessRead(S_IFNAME))
               {
                   readAccess = false;
                   System.out.println("set ifName success");
               }
           }else{
               System.out.println( "ifName do not macth rolls,please check it again");
           }
        }
    	
    	public String getIfName()
        {
            return this.ifName;
        }
        public void setTrackNqaTestName(String trackNqaTestName){
    	
           if(RestUtil.validata(getCriterion(S_TRACKNQATESTNAME), trackNqaTestName))
           {
               this.trackNqaTestName = trackNqaTestName;
               if(!checkAccessRead(S_TRACKNQATESTNAME))
               {
                   readAccess = false;
                   System.out.println("set trackNqaTestName success");
               }
           }else{
               System.out.println( "trackNqaTestName do not macth rolls,please check it again");
           }
        }
    	
    	public String getTrackNqaTestName()
        {
            return this.trackNqaTestName;
        }
        public void setIsInheritCost(String isInheritCost){
    	
           if(RestUtil.validata(getCriterion(S_ISINHERITCOST), isInheritCost))
           {
               this.isInheritCost = isInheritCost;
               if(!checkAccessRead(S_ISINHERITCOST))
               {
                   readAccess = false;
                   System.out.println("set isInheritCost success");
               }
           }else{
               System.out.println( "isInheritCost do not macth rolls,please check it again");
           }
        }
    	
    	public String getIsInheritCost()
        {
            return this.isInheritCost;
        }
        public void clear()
		{
		
            this.bfdEnable="";
            this.afType="";
            this.tag="";
            this.maskLength="";
            this.topologyName="";
            this.nexthop="";
            this.destVrfName="";
            this.vrfName="";
            this.sessionName="";
            this.trackNqaAdminName="";
            this.preference="";
            this.description="";
            this.prefix="";
            this.ifName="";
            this.trackNqaTestName="";
            this.isInheritCost="";
		}
		
		public OneBody clone()
		{
		    OneBody cloneBody= new OneBody();
            cloneBody.setBfdEnable(this.bfdEnable);
            cloneBody.setAfType(this.afType);
            cloneBody.setTag(this.tag);
            cloneBody.setMaskLength(this.maskLength);
            cloneBody.setTopologyName(this.topologyName);
            cloneBody.setNexthop(this.nexthop);
            cloneBody.setDestVrfName(this.destVrfName);
            cloneBody.setVrfName(this.vrfName);
            cloneBody.setSessionName(this.sessionName);
            cloneBody.setTrackNqaAdminName(this.trackNqaAdminName);
            cloneBody.setPreference(this.preference);
            cloneBody.setDescription(this.description);
            cloneBody.setPrefix(this.prefix);
            cloneBody.setIfName(this.ifName);
            cloneBody.setTrackNqaTestName(this.trackNqaTestName);
            cloneBody.setIsInheritCost(this.isInheritCost);
            return cloneBody;
		}
		
		public String toString()
		{  
		     return toString(BodyType.APPLICATION_JSON, false);
		}
		
		public String toString(BodyType bodyType, boolean isWrite)
		{  
		    StringBuffer buf = new StringBuffer();
			if (bodyType == BodyType.APPLICATION_JSON)
			{
                if(!isWrite || checkAccessWrite(S_BFDENABLE))
				buf.append("{\"bfdEnable\": \"").append(this.bfdEnable).append("\"}");
                if(!isWrite || checkAccessWrite(S_AFTYPE))
	            buf.append(",{\"afType\": \"").append(this.afType).append("\"}");
                if(!isWrite || checkAccessWrite(S_TAG))
	            buf.append(",{\"tag\": \"").append(this.tag).append("\"}");
                if(!isWrite || checkAccessWrite(S_MASKLENGTH))
	            buf.append(",{\"maskLength\": \"").append(this.maskLength).append("\"}");
                if(!isWrite || checkAccessWrite(S_TOPOLOGYNAME))
	            buf.append(",{\"topologyName\": \"").append(this.topologyName).append("\"}");
                if(!isWrite || checkAccessWrite(S_NEXTHOP))
	            buf.append(",{\"nexthop\": \"").append(this.nexthop).append("\"}");
                if(!isWrite || checkAccessWrite(S_DESTVRFNAME))
	            buf.append(",{\"destVrfName\": \"").append(this.destVrfName).append("\"}");
                if(!isWrite || checkAccessWrite(S_VRFNAME))
	            buf.append(",{\"vrfName\": \"").append(this.vrfName).append("\"}");
                if(!isWrite || checkAccessWrite(S_SESSIONNAME))
	            buf.append(",{\"sessionName\": \"").append(this.sessionName).append("\"}");
                if(!isWrite || checkAccessWrite(S_TRACKNQAADMINNAME))
	            buf.append(",{\"trackNqaAdminName\": \"").append(this.trackNqaAdminName).append("\"}");
                if(!isWrite || checkAccessWrite(S_PREFERENCE))
	            buf.append(",{\"preference\": \"").append(this.preference).append("\"}");
                if(!isWrite || checkAccessWrite(S_DESCRIPTION))
	            buf.append(",{\"description\": \"").append(this.description).append("\"}");
                if(!isWrite || checkAccessWrite(S_PREFIX))
	            buf.append(",{\"prefix\": \"").append(this.prefix).append("\"}");
                if(!isWrite || checkAccessWrite(S_IFNAME))
	            buf.append(",{\"ifName\": \"").append(this.ifName).append("\"}");
                if(!isWrite || checkAccessWrite(S_TRACKNQATESTNAME))
	            buf.append(",{\"trackNqaTestName\": \"").append(this.trackNqaTestName).append("\"}");
                if(!isWrite || checkAccessWrite(S_ISINHERITCOST))
	            buf.append(",{\"isInheritCost\": \"").append(this.isInheritCost).append("\"}");
			}
			 
		    if (bodyType == BodyType.APPLICATION_XML)
		    {
		    	if (!isWrite || (RestUtil.isNotEmpty(this.bfdEnable)) && checkAccessWrite("bfdEnable"))
		    	{ buf.append("<bfdEnable>").append(this.bfdEnable).append("</bfdEnable>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.afType)) && checkAccessWrite("afType"))
		    	{ buf.append("<afType>").append(this.afType).append("</afType>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.tag)) && checkAccessWrite("tag"))
		    	{ buf.append("<tag>").append(this.tag).append("</tag>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.maskLength)) && checkAccessWrite("maskLength"))
		    	{ buf.append("<maskLength>").append(this.maskLength).append("</maskLength>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.topologyName)) && checkAccessWrite("topologyName"))
		    	{ buf.append("<topologyName>").append(this.topologyName).append("</topologyName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.nexthop)) && checkAccessWrite("nexthop"))
		    	{ buf.append("<nexthop>").append(this.nexthop).append("</nexthop>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.destVrfName)) && checkAccessWrite("destVrfName"))
		    	{ buf.append("<destVrfName>").append(this.destVrfName).append("</destVrfName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.vrfName)) && checkAccessWrite("vrfName"))
		    	{ buf.append("<vrfName>").append(this.vrfName).append("</vrfName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.sessionName)) && checkAccessWrite("sessionName"))
		    	{ buf.append("<sessionName>").append(this.sessionName).append("</sessionName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.trackNqaAdminName)) && checkAccessWrite("trackNqaAdminName"))
		    	{ buf.append("<trackNqaAdminName>").append(this.trackNqaAdminName).append("</trackNqaAdminName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.preference)) && checkAccessWrite("preference"))
		    	{ buf.append("<preference>").append(this.preference).append("</preference>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.description)) && checkAccessWrite("description"))
		    	{ buf.append("<description>").append(this.description).append("</description>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.prefix)) && checkAccessWrite("prefix"))
		    	{ buf.append("<prefix>").append(this.prefix).append("</prefix>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.ifName)) && checkAccessWrite("ifName"))
		    	{ buf.append("<ifName>").append(this.ifName).append("</ifName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.trackNqaTestName)) && checkAccessWrite("trackNqaTestName"))
		    	{ buf.append("<trackNqaTestName>").append(this.trackNqaTestName).append("</trackNqaTestName>\n"); }
		    	if (!isWrite || (RestUtil.isNotEmpty(this.isInheritCost)) && checkAccessWrite("isInheritCost"))
		    	{ buf.append("<isInheritCost>").append(this.isInheritCost).append("</isInheritCost>\n"); }
		    }
			 
		    return buf.toString();
		}
				
		public boolean validate()
		{
		
		     if (checkIsKey(S_BFDENABLE) && RestUtil.isEmpty(this.bfdEnable)) return false;
		     if (checkIsKey(S_AFTYPE) && RestUtil.isEmpty(this.afType)) return false;
		     if (checkIsKey(S_TAG) && RestUtil.isEmpty(this.tag)) return false;
		     if (checkIsKey(S_MASKLENGTH) && RestUtil.isEmpty(this.maskLength)) return false;
		     if (checkIsKey(S_TOPOLOGYNAME) && RestUtil.isEmpty(this.topologyName)) return false;
		     if (checkIsKey(S_NEXTHOP) && RestUtil.isEmpty(this.nexthop)) return false;
		     if (checkIsKey(S_DESTVRFNAME) && RestUtil.isEmpty(this.destVrfName)) return false;
		     if (checkIsKey(S_VRFNAME) && RestUtil.isEmpty(this.vrfName)) return false;
		     if (checkIsKey(S_SESSIONNAME) && RestUtil.isEmpty(this.sessionName)) return false;
		     if (checkIsKey(S_TRACKNQAADMINNAME) && RestUtil.isEmpty(this.trackNqaAdminName)) return false;
		     if (checkIsKey(S_PREFERENCE) && RestUtil.isEmpty(this.preference)) return false;
		     if (checkIsKey(S_DESCRIPTION) && RestUtil.isEmpty(this.description)) return false;
		     if (checkIsKey(S_PREFIX) && RestUtil.isEmpty(this.prefix)) return false;
		     if (checkIsKey(S_IFNAME) && RestUtil.isEmpty(this.ifName)) return false;
		     if (checkIsKey(S_TRACKNQATESTNAME) && RestUtil.isEmpty(this.trackNqaTestName)) return false;
		     if (checkIsKey(S_ISINHERITCOST) && RestUtil.isEmpty(this.isInheritCost)) return false;
             return true;
		}
		
    }
    
    public Boolean getbMultiOperate() {
		return bMultiOperate;
	}

	public void setbMultiOperate(Boolean bMultiOperate) {
		this.bMultiOperate = bMultiOperate;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}
	
    public StaticrtSrRoute(OpsRestCaller rest) {
        this.rest = rest;
    }
    
    public OpsRestCaller getRest() {
        return rest;
    }

    public void setRest(OpsRestCaller rest) {
        this.rest = rest;
    }
  
    private String getContent() {
        return body.toString();
    }

    private int getIndexint() {
        return indexint;
    }

    private void setIndexint(int index) {
        this.indexint = index;
    }
 

    public String getErrorstr() {
        return errorstr;
    }

    public void setErrorstr(String errorstr) {
        this.errorstr = errorstr;
    }

    private Map<String, String> getCriterions() {
        return criterions;
    }
  
 
    public String getUrlBody() {
     
           return this.fullUrlbody;
    } 
	
    public void parseRestful(RetRpc restfulJson) throws Exception
    {
    
       this.setIndexint(0);
        this.multiBody.clear();
        if(null == restfulJson)
        {
          throw new Exception(restfulJson+" error in StaticrtSrRoute.java");
        }
        if(200==restfulJson.getStatusCode())
        {
        	if (BodyType.APPLICATION_JSON == bodyType)
        	{
        		OpsXMLHelper xmlHelper = new OpsXMLHelper();
        		JSONObject jsonObject = JSONObject.fromObject(restfulJson.getContent());
        		xmlHelper.configure(jsonObject, fullUrlbody, this, StaticrtSrRoute.OneBody.class);
        		
        	} else {
        		Document doc = RestUtil.getDoc(restfulJson.getContent());
        		OpsXMLHelper xmlHelper = new OpsXMLHelper();
        		xmlHelper.configure(doc.getDocumentElement(), fullUrlbody, this, StaticrtSrRoute.OneBody.class);
        	}
        }
        else
        {
            throw new Exception(restfulJson.getContent()+",in IfmInterface.java");  
        }
 
    }
    
    public void reset()
    {
    	indexint = 0;
    }
    
    public void next()
    {
        body = null;
    	if (indexint >= 0 && indexint < multiBody.size())
    	{
    		body = multiBody.get(indexint);
    		indexint++;
    	}
    }

    public boolean hasNext()
    {
        return (getIndexint() < multiBody.size() && getIndexint() >= 0);
    }
    
    private void setAttributes(JSONArray attributeArray)
    {
	    
        Iterator iter = attributeArray.iterator();
        while(iter.hasNext())
        {
            JSONObject json = (JSONObject) iter.next();
			
	         
            if(json.containsKey(S_BFDENABLE))
            {body.setBfdEnable(json.getString(S_BFDENABLE));}
 	         
            if(json.containsKey(S_AFTYPE))
            {body.setAfType(json.getString(S_AFTYPE));}
 	         
            if(json.containsKey(S_TAG))
            {body.setTag(json.getString(S_TAG));}
 	         
            if(json.containsKey(S_MASKLENGTH))
            {body.setMaskLength(json.getString(S_MASKLENGTH));}
 	         
            if(json.containsKey(S_TOPOLOGYNAME))
            {body.setTopologyName(json.getString(S_TOPOLOGYNAME));}
 	         
            if(json.containsKey(S_NEXTHOP))
            {body.setNexthop(json.getString(S_NEXTHOP));}
 	         
            if(json.containsKey(S_DESTVRFNAME))
            {body.setDestVrfName(json.getString(S_DESTVRFNAME));}
 	         
            if(json.containsKey(S_VRFNAME))
            {body.setVrfName(json.getString(S_VRFNAME));}
 	         
            if(json.containsKey(S_SESSIONNAME))
            {body.setSessionName(json.getString(S_SESSIONNAME));}
 	         
            if(json.containsKey(S_TRACKNQAADMINNAME))
            {body.setTrackNqaAdminName(json.getString(S_TRACKNQAADMINNAME));}
 	         
            if(json.containsKey(S_PREFERENCE))
            {body.setPreference(json.getString(S_PREFERENCE));}
 	         
            if(json.containsKey(S_DESCRIPTION))
            {body.setDescription(json.getString(S_DESCRIPTION));}
 	         
            if(json.containsKey(S_PREFIX))
            {body.setPrefix(json.getString(S_PREFIX));}
 	         
            if(json.containsKey(S_IFNAME))
            {body.setIfName(json.getString(S_IFNAME));}
 	         
            if(json.containsKey(S_TRACKNQATESTNAME))
            {body.setTrackNqaTestName(json.getString(S_TRACKNQATESTNAME));}
 	         
            if(json.containsKey(S_ISINHERITCOST))
            {body.setIsInheritCost(json.getString(S_ISINHERITCOST));}
         }
     }
     
    public void clearAll()
    {
         body.clear();
         multiBody.clear();
         this.readAccess=true; 
    }
	
    public boolean addBody(OneBody myBody)
    {
         if( myBody == null)
         {
             return false;
         }
		 
         if (!myBody.validate())
         {
        	 return false;
         }
		 
		 multiBody.add(myBody);
		 return true;
    }
	
	public boolean setBody(OneBody myBody)
    {
         if( myBody == null)
         {
             return false;
         }
		 
         if (!myBody.validate())
         {
        	 return false;
         }
         this.body = myBody;
		 return true;
    }
  
    public boolean setBody(JSONArray array)
    {
         if( array == null)
         {
             return false;
         }
          
         List<JSONObject > lstJson = new ArrayList<JSONObject>();
         int icount = array.size();
         for(int i  =0; i <  icount; i ++ )
	     {
	        JSONObject object =  array.getJSONObject(i);
            if (object.get(S_BFDENABLE) != null)
	    	{
	    	    body.setBfdEnable(object.getString(S_BFDENABLE));
	    	}
            if (object.get(S_AFTYPE) != null)
	    	{
	    	    body.setAfType(object.getString(S_AFTYPE));
	    	}
            if (object.get(S_TAG) != null)
	    	{
	    	    body.setTag(object.getString(S_TAG));
	    	}
            if (object.get(S_MASKLENGTH) != null)
	    	{
	    	    body.setMaskLength(object.getString(S_MASKLENGTH));
	    	}
            if (object.get(S_TOPOLOGYNAME) != null)
	    	{
	    	    body.setTopologyName(object.getString(S_TOPOLOGYNAME));
	    	}
            if (object.get(S_NEXTHOP) != null)
	    	{
	    	    body.setNexthop(object.getString(S_NEXTHOP));
	    	}
            if (object.get(S_DESTVRFNAME) != null)
	    	{
	    	    body.setDestVrfName(object.getString(S_DESTVRFNAME));
	    	}
            if (object.get(S_VRFNAME) != null)
	    	{
	    	    body.setVrfName(object.getString(S_VRFNAME));
	    	}
            if (object.get(S_SESSIONNAME) != null)
	    	{
	    	    body.setSessionName(object.getString(S_SESSIONNAME));
	    	}
            if (object.get(S_TRACKNQAADMINNAME) != null)
	    	{
	    	    body.setTrackNqaAdminName(object.getString(S_TRACKNQAADMINNAME));
	    	}
            if (object.get(S_PREFERENCE) != null)
	    	{
	    	    body.setPreference(object.getString(S_PREFERENCE));
	    	}
            if (object.get(S_DESCRIPTION) != null)
	    	{
	    	    body.setDescription(object.getString(S_DESCRIPTION));
	    	}
            if (object.get(S_PREFIX) != null)
	    	{
	    	    body.setPrefix(object.getString(S_PREFIX));
	    	}
            if (object.get(S_IFNAME) != null)
	    	{
	    	    body.setIfName(object.getString(S_IFNAME));
	    	}
            if (object.get(S_TRACKNQATESTNAME) != null)
	    	{
	    	    body.setTrackNqaTestName(object.getString(S_TRACKNQATESTNAME));
	    	}
            if (object.get(S_ISINHERITCOST) != null)
	    	{
	    	    body.setIsInheritCost(object.getString(S_ISINHERITCOST));
	    	}
 
         }
		 
		 return this.body.validate();
    }
    
    public boolean setBodyFromJson(String content)
    {
         JSONArray array = new JSONArray();
         if (null != content && content.length() > 0)   
         { 
             array = JSONArray.fromObject(content);
         }
         return setBody(array);
    }
	

    
	/**
     * modify api, equal to method post of rest
     * @param attributes : URL parameters, for example "?Xxx=yy&aa=b"
     * @return get : url 
     * @throws Exception
     */
    public boolean modify() throws Exception
    {
        if (!validateBody()) {
            return false;
        }
        
        String urlPath = getUrl();
        RetRpc response = this.getRest().put(urlPath, getParameBody());
        int status = response.getStatusCode();
        if (status == 200) {
            return true;
        }
        if (status != 200)
            this.setErrorstr(response.toString());
        return false;
    }

	/**
     * create api, equal to method post of rest
     * @param attributes : URL parameters, for example "?Xxx=yy&aa=b"
     * @return get : url 
     * @throws Exception
     */
    public boolean create() throws Exception
    {
        if (!validateBody()) {
            return false;
        }
        
        String urlPath = getUrl();
        RetRpc response = this.getRest().post(urlPath, getParameBody());
        int status = response.getStatusCode();
        if (status == 200) {
            return true;
        }
        if (status != 200)
            this.setErrorstr(response.toString());
        return false;
    }
	
	/**
     * get api, equal to method get of rest
     * @param attributes : URL parameters, for example "?Xxx=yy&aa=b"
     * @return get : url 
     * @throws Exception
     */
    public boolean get() throws Exception
    {
        String urlPath = getUrl();
        RetRpc  response = this.getRest().get(urlPath);
        int status = response.getStatusCode();
        if (status == 200) {
            return true;
        }
        if (status != 200)
            this.setErrorstr(response.toString());
        return false;
    }

	/**
     * delete api, equal to method delete of rest
     * @param attributes : URL parameters, for example "?Xxx=yy&aa=b"
     * @return get : url 
     * @throws Exception
     */
    public boolean delete() throws Exception
    {
        String urlPath = getUrl();
        RetRpc response = this.getRest().delete(urlPath);
        int status = response.getStatusCode();
        if (status == 200) {
            return true;
        }
        if (status != 200)
            this.setErrorstr(response.toString());
        return false;
    }
	
	/**
     * getall api, equal to method get of rest
     * @param attributes : URL parameters, for example "?Xxx=yy&aa=b"
     * @return get : url 
     * @throws Exception
     */
    public RetRpc getall() throws Exception
    {
     
        if(this.getAllAccessWrite())
        {
            String urlPath = getUrl();
            RetRpc response = this.getRest().get(urlPath);
            return response; 
        }else
        {
            throw new Exception("You do not have permmition");
        }
    }
    
    public boolean validateBody()
    {
    	if (bMultiOperate)
    	{
    		if (multiBody.size() == 0)
    		{
    		    errorstr = "Error : case Multi-body much be have one body , please call addbody().";
    			return false;
    		}
    		
    		for (OneBody body : multiBody)
    		{
    			if (body != null && !body.validate())
    			{
    			    errorstr = "Error : case Multi-body, one body validate failed, please check it.";
    				return false;
    			}
    		}
    	}
    	
    	boolean ret = body.validate();
    	if(!ret)
    	{
    	     errorstr = "Error : case one-body, body validate failed, please check it.";
    		 return false;
    	}
    	return ret;
    }
    
	private JSONObject getCriterion(String name)
    {
    	JSONObject criterion = criterionsBuffer.get(name);
        if (null == criterion)
        {
        	if (null == criterions.get(name))
        	{
        		return null;
        	}
        	
        	criterion = JSONObject.fromObject(criterions.get(name));
        	if (criterion != null)
        	{
        		criterionsBuffer.put(name, criterion);
        	}
        }
        
        return criterion;
    }
    
    private Boolean checkIsKey(String name)
    {
    	JSONObject criterion = getCriterion(name);
        if (criterion != null && criterion.containsKey("key") && "true".equals(criterion.get("key")))
        {
            return true;
        }
        return false;
    }
	
    private Boolean checkAccessWrite(String name)
    {
    	JSONObject criterion = getCriterion(name);
        if (criterion != null && criterion.containsKey("access") && ("writeOnly".equals(criterion.get("access"))
                || "readCreate".equals(criterion.get("access"))))
        {
            return true;
        }
        return false;
    }

    private boolean checkAccessRead(String name)
    {

    	JSONObject criterion = getCriterion(name);
        if (criterion != null && criterion.containsKey("access") && ("readOnly".equals(criterion.get("access"))
                || "readCreate".equals(criterion.get("access"))  
                 || "readWrite".equals(criterion.get("access"))  ))
        {
            return true;
        }
        return false;
    }
    
    private Boolean getAllAccessWrite()
    {
         this.readAccess = (this.readAccess && this.checkAccessRead(S_BFDENABLE));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_AFTYPE));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_TAG));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_MASKLENGTH));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_TOPOLOGYNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_NEXTHOP));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_DESTVRFNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_VRFNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_SESSIONNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_TRACKNQAADMINNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_PREFERENCE));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_DESCRIPTION));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_PREFIX));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_IFNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_TRACKNQATESTNAME));
         this.readAccess = (this.readAccess && this.checkAccessRead(S_ISINHERITCOST));
         return this.readAccess;
    } 
    
    /**
     * parse str to list
     * @param str
     * @return
     */   
    private List<String> strToList(String str)
    {
        List<String> resultList = new ArrayList<String>();
        String[] strArr = str.split("]");
        for (String s : strArr) {
            if (s.startsWith("['"))
            {
                resultList.add(s.substring(s.indexOf("['") + 2, s.length() - 1));
            }
            else if (s.startsWith("["))
            {
                resultList.add(s.substring(s.indexOf("[") + 1, s.length()));
            }
        }
        return resultList;
    }
 
	
    /**
    * parse para for body
    * @return
    */
    private String getParameBody() {
    
        String urlPath = getUrlBody();
	    StringBuffer buf = new StringBuffer();
	    String bodyPrefix = RestUtil.getBodyPrefix(this.fullUrlbody, bMultiOperate, this.bodyType);
	    String bodysuffix = RestUtil.getBodySuffix(this.fullUrlbody, bMultiOperate, this.bodyType);
	    String prefix = RestUtil.getPrefix(urlPath, bMultiOperate, this.bodyType);
	    String sufffix = RestUtil.getSuffix(urlPath, bMultiOperate, this.bodyType);
	    
		// multi-body 
	    if (bMultiOperate)
		{
		    buf.append(prefix);
		    for(OneBody myBody : multiBody)
			{
			    buf.append(bodyPrefix);
			    buf.append(myBody.toString(this.bodyType, true));
				buf.append(bodysuffix);
			}
			buf.append(sufffix);
			return buf.toString();
		}
		
		// one body 
		buf.append(prefix);
		buf.append(bodyPrefix);
		buf.append(body.toString(this.bodyType, true));
		buf.append(bodysuffix);
		buf.append(sufffix);
		return buf.toString();
    }
	
     /**
     * 
     * @param attributes para of restful url
     * @return get url 
     * @throws Exception
     */
    private String getUrl() throws Exception {
		
        String urlPath = getUrlBody();

		// case mulit-body 
    	if(bMultiOperate)
    	{
    		int argIndex = urlPath.indexOf("?");
    		if (argIndex == -1){
    			int lastUrlPath = urlPath.lastIndexOf("/");
    			if (lastUrlPath != -1)
    			{
    				urlPath = urlPath.substring(0, lastUrlPath);
    			}
    		} else {
    			urlPath = urlPath.substring(0, argIndex);
    		}
    	}
		
    	// if body application/type is json
    	if (BodyType.APPLICATION_JSON == bodyType)
    	{
    		urlPath = urlPath + "/json";
    	}
    	
    	return urlPath;
    }
     
    public OneBody getBody() {
		return body;
	}

	public List<OneBody> getMultiBody() {
		return multiBody;
	}

	//main test
    public static void main(String[] args) 
	{
        StaticrtSrRoute newond = new StaticrtSrRoute(OpsServiceManager.getInstance().getDefaultOpsRestCall());
        try {
		   System.out.println(newond.getall());
        } catch (Exception e) 
        {
	       System.out.println(e.getMessage());
        }
    }
}  
    