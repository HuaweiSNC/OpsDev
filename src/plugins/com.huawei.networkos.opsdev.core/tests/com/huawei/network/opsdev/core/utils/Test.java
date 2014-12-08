package com.huawei.network.opsdev.core.utils;

public class Test {
    public static void main(String[] args) {
        System.out.println("7777".compareTo("888"));
        System.out.println(compare("699", "888"));
    }
    
    public static int compare(String str1 , String str2){
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
}
