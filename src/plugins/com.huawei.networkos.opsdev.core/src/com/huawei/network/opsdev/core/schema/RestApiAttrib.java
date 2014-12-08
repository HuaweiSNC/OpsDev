package com.huawei.network.opsdev.core.schema;

/**
 * 
 */

/**
 * @author gWX179864
 *
 */
public class RestApiAttrib  {

	private String name;
	private String value;
	private String access;
	private String datetype;
	private String length;
	private String mandator;
	private String exceptedValue;
	private String max;
	private String min;
	private String url;
	private String parentUrl;
	private String defJson;
	private String example;
	private String minValue;
    private String maxValue;
    
	public String getParentUrl() {
        return parentUrl;
    }
    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }
    public String getMinValue() {
        return minValue;
    }
    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }
    public String getExample() {
        return example;
    }
    public void setExample(String example) {
        this.example = example;
    }
    public String getDefJson() {
        return defJson;
    }
    public void setDefJson(String defJson) {
        this.defJson = defJson;
    }
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getDatetype() {
		return datetype;
	}
	public void setDatetype(String datetype) {
		this.datetype = datetype;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getMandator() {
		return mandator;
	}
	public void setMandator(String mandator) {
		this.mandator = mandator;
	}
	public String getExceptedValue() {
		return exceptedValue;
	}
	public void setExceptedValue(String exceptedValue) {
		this.exceptedValue = exceptedValue;
	}
 
	   
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}

}
