package com.huawei.network.opsdev.core.templet;


import java.util.Arrays;

import net.sf.json.JSONObject;

public class UrlPropertiesBean {
    private String name;
    private String type;
    private boolean isEnumerate = false;
    private String[] enumTexts;
    private int maxLength=-1;
    private int minLength=-1;
    private String maxVal=-1+"";
    private String minVal=-1+"";
    private String valicateText;
    private String value;
    private String example;
    private boolean isBoolean = false;
    private String access;
    private boolean key = false;
    
    
    
    
    
    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean isBoolean) {
        this.isBoolean = isBoolean;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String value) {
        if(value==""||value==null){
            this.value = value;
            return name+ "'s value can't be null";
        }

        if ("IPV4".equals(type)
                && !value.matches("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")) {
            return name+ "'s value must be IPV4";
        }
        
        if("ULONG".equals(type)||"UINT".equals(type)||"INT".equals(type)||"LONG".equals(type)||"USHORT".equals(type)||"SHORT".equals(type)){
            if(!checkIsAllNum(value)){
               return name+ "'s value must be numbers"; 
            }
        }
        if ("UINT32".equals(type)) {
 
                int x = Integer.parseInt(value);
                if (!(x > -32768 && x < 32767)) {
                    return name+ "'s value lenth is too long"; 

                }
        }
        if ("IPV4IPV6".equals(type)
                && !value
                        .matches("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$" +
                        		"|(((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0" +
                        		"-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-" +
                        		"4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-" +
                        		"9]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1" +
                        		",4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4" +
                        		"][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9" +
                        		"]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1," +
                        		"4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1," +
                        		"4})?:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(" +
                        		".(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))" +
                        		"|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1," +
                        		"4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4][0-9]|" +
                        		"1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9" +
                        		"][0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){2" +
                        		"}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0," +
                        		"3}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(" +
                        		"25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:" +
                        		"))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6}" +
                        		")|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4][0-9]|1[" +
                        		"0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][" +
                        		"0-9]|[1-9]?[0-9])){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){" +
                        		"1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4][0-9" +
                        		"]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0" +
                        		"-9][0-9]|[1-9]?[0-9])){3}))|:)))(%.+)?)")) {
            return name+ "'s value must be ipv4 or ipv6"; 

        }
        if ("IPV6".equals(type)
                && !value
                        .matches("((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-" +
                        		"9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4" +
                        		"][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9" +
                        		"]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1," +
                        		"4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]" +
                        		"[0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]" +
                        		"|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1,4" +
                        		"}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4" +
                        		"})?:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(." +
                        		"(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|" +
                        		":))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4" +
                        		"})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4][0-9]|1" +
                        		"[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9]" +
                        		"[0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}" +
                        		"(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3" +
                        		"}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(2" +
                        		"5[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:)" +
                        		")|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})" +
                        		"|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4][0-9]|1[0" +
                        		"-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9][0" +
                        		"-9]|[1-9]?[0-9])){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1" +
                        		",7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4][0-9]" +
                        		"|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-" +
                        		"9][0-9]|[1-9]?[0-9])){3}))|:)))(%.+)?")) {
            return name+ "'s value must be ipv6"; 
        }
        if(minLength!=-1&&value.length()<minLength){
            return name+ "'s value muts be longer than "+ minLength; 

        } 
        if(maxLength!=-1&&value.length()>maxLength){
            return name+ " value's maxLength is "+maxLength; 
        }
        if(!minVal.equals(-1+"")&&compare(value,minVal)<0){
            return name+ " value's minVal is "+minVal; 

        }
        if(!maxVal.equals(-1+"")&&compare(value,maxVal)>0){
            return name+ " value's maxVal is "+maxVal; 
        }
        this.value = value;
        return null;
    }
    public boolean checkIsAllNum(String str){
        for(char cha:str.toCharArray()){
            if(cha<'0'||cha>'9'){
                return false;
            }
        }
        return true;
    }
    public void setValicateText(String valicateText) {
        this.valicateText = valicateText;
    }
    

    
    
    public String getValicateText() {
        return valicateText;
    }

    public String[] getEnumTexts() {
        return enumTexts;
    }

    public void setEnumTexts(String[] enumTexts) {
        this.enumTexts = enumTexts;
    }

    public void setValicateText(JSONObject obj) {
        valicateText = obj.toString();
        if (obj.containsKey("data-type")) {
            setType(obj.get("data-type").toString());
            if ("ENUM".equals(type)) {
                isEnumerate = true;
                if (obj.containsKey("data-type")) {
                    setEnumTexts(obj.get("expected-values").toString().split(";"));
                }
            }if("BOOL".equals(type)){
                isBoolean = true;
            }
        }
        if (obj.containsKey("max")) {
            setMaxLength(Integer.parseInt((obj.get("max").toString())));
        }
        if (obj.containsKey("min")) {
            setMinLength(Integer.parseInt((obj.get("min").toString())));
        }
        if (obj.containsKey("max-val")) {
            setMaxVal(obj.get("max-val").toString());
        }
        if (obj.containsKey("min-val")) {
            setMinVal(obj.get("min-val").toString());
        }
        if (obj.containsKey("example")) {
            setExample(obj.get("example").toString());
        }
        if (obj.containsKey("access")){
            setAccess(obj.get("access").toString());
        }
        if (obj.containsKey("key")){
            setKey("true".equalsIgnoreCase(obj.get("key").toString()));
        }
    }
    
    

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public int compare(String str1 , String str2){
        if(str1.length()>str2.length()){
            return 1;
        }
        if(str1.length()<str2.length()){
            return -1;
        }
        for(int i=0;i<str1.length();i++){
            if(str1.charAt(i)<str2.charAt(i)){
                return -1;
            }
            if(str1.charAt(i)>str2.charAt(i)){
                return 1;
            }
        }
        return 0;
    }
    
    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }


    public String toString() {
        return "UrlPropertiesBean [name=" + name + ", type=" + type + ", isEnumerate=" + isEnumerate + ", enumTexts="
                + Arrays.toString(enumTexts) + ", maxLength=" + maxLength + ", minLength=" + minLength + ", maxVal="
                + maxVal + ", minVal=" + minVal + ", valicateText=" + valicateText + ", value=" + value + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnumerate() {
        return isEnumerate;
    }

    public void setEnumerate(boolean isEnumerate) {
        this.isEnumerate = isEnumerate;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public String getMinVal() {
        return minVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }

}
