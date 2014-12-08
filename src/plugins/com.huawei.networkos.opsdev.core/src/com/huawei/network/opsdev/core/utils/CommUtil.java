package com.huawei.network.opsdev.core.utils;

/*
 * Copyright: Copyright 2010 Huawei Tech. Co. Ltd. All Rights Reserved.
 * product：Anyupgrade
 * description：
 * @version: 1.0 2010-5-27
 */

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * <p>
 * common util
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Huawei Technologies Co.,Ltd
 * </p>
 * 
 * @author x00106601
 */
public final class CommUtil
{
    /** 连接符号 **/
    private static final String JOIN_WORD = "-";

    // comma
    public static final String SIGN_COMMA = ",";

    // left parentheses
    public static final String SIGN_LEFT = "(";

    // right parentheses
    public static final String SIGN_RIGHT = ")";

    // Just a blank " " string
    public static final String BLANK_STRING2 = " ";

    /**
     * Just a blank "" string
     */
    public static final String BLANK_STRING = "";
    /**
     * left parentheses
     */
    public static final String FLOWER_LEFT = "{";
    /**
     * right parentheses
     */
    public static final String FLOWER_RIGHT = "}";
    // 中文字符的说明
    private static Pattern chinesepattrn = Pattern.compile("[\\u4e00-\\u9fa5]+");
    
    public static String DECODE_UTF8 = "UTF-8";
    
    public static String DECODE_UNICODE = "unicode";
    
    public static String DECODE_GB2312 = "GB2312";
    
    public static String DECODE_UTF16 = "UTF-16";
    
    

    private CommUtil()
    {

    }

    public static String getShortWorkdir(String dir, String filename)
    {
        if (!isBlank(dir) )
        {
            return dir + File.separatorChar + filename;
        }
        return filename;
    }

    public static String getV8pafShortFileName(String pafFileName)
    {
        int lastSpiltWord = 0;
        if (pafFileName.lastIndexOf("\\") != -1)
        {
            lastSpiltWord = pafFileName.lastIndexOf("\\") + 1;
        }
        if (pafFileName.lastIndexOf("/") != -1)
        {
            lastSpiltWord = pafFileName.lastIndexOf("/") + 1;
        }
        return pafFileName.substring(lastSpiltWord);
    }
 
 

    /**
     * @param params
     *            params
     * @return "(param1,param2,param3...)"
     */
    public static String getParamsString(Object... params)
    {
        StringBuffer msg = new StringBuffer();
        msg.append(SIGN_LEFT);
        int i = 0;
        for (Object para : params)
        {
            msg.append(para);
            if (i != params.length - 1)
            {
                msg.append(SIGN_COMMA);
            }
            i++;
        }
        msg.append(SIGN_RIGHT);
        return msg.toString();
    }

    /***
     * 十六进制转换为十进制整数输出
     * 
     * @param tenStr
     *            十六进制数
     * @return 返回十进制整数
     */
    public static Long ten2Long(String tenStr)
    {

        if (null == tenStr || tenStr.isEmpty())
        {
            return null;
        }

        Long ifn = null;
        try
        {

            ifn = Long.valueOf(tenStr);
            return ifn;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
    
    
    public static boolean isBlank(String sourceStr)
    {
        if (null == sourceStr || sourceStr.isEmpty())
        {
            return true;
        }
        return false;

    }

    /***
     * 十六进制转换为十进制整数输出
     * 
     * @param hexStr
     *            十六进制数
     * @return 返回十进制整数
     */
    public static Long hex2Long(String hexStr)
    {

        if (isBlank(hexStr))
        {
            return null;
        }

        String hexStrValue = hexStr.replace("0x", "");
        hexStrValue = hexStrValue.replace("0X", "");

        Long ifn = null;
        try
        {

            ifn = Long.valueOf(hexStrValue, 16);
            return ifn;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    /***
     * 十六进制转换为十进制整数输出
     * 
     * @param hexStr
     *            十六进制数
     * @return 返回十进制整数
     */
    public static Integer hex2Integer(String hexStr)
    {

        if (isBlank(hexStr))
        {
            return null;
        }

        String hexStrValue = hexStr.replace("0x", "");
        hexStrValue = hexStrValue.replace("0X", "");

        Integer ifn = null;
        try
        {

            ifn = Integer.valueOf(hexStrValue, 16);
            return ifn;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }
 

    /**
     * return true if the string is not null and not empty
     * 
     * @param aString
     *            a
     * @return String
     * @author j00105185
     */
    public static String parseStr(String aString)
    {
        if (null == aString)
        {
            return "";
        }
        return aString;
    }


    public static String changeNullString(Boolean elemValue)
    {
        if (elemValue == null)
        {
            return "false";
        }
        return elemValue.toString();
    }
    
    /**
     * return element text, if element is null, return ""
     * 
     * @param elem
     *            Element
     * @return element text
     * @author j00105185
     */
/*    public static String getElementValue(Element elem)
    {
        String elemValue = "";
        if (null != elem)
        {
            elemValue = elem.getText();
        }
        return elemValue;
    }

    public static String changeNullString(String elemValue)
    {
        if (StringUtils.isBlank(elemValue))
        {
            return "";
        }
        return elemValue.trim();
    }*/

    /**
     * add Element CData value
     * 
     * @param elem
     *            Element
     * @param elemText
     *            elemText
     * @author j00105185
     */
/*    public static void addElementCData(Element elem, String elemText)
    {
        if (null == elem)
        {
            return;
        }
        elem.clearContent();
        elem.addCDATA(CommUtil.parseStr(elemText));
    }
*/
    /**
     * get current date
     * 
     * @return yyyyMMddHHmmss
     * @author j00105185
     */
    public static String getCurrentDate()
    {
        Format formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    /**
     * get current date
     * 
     * @return HH:mm:ss
     * @author j00105185
     */
    public static String getCurrentTime()
    {
        Format formatter = new java.text.SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date());
    }
    /**
     * @param aMsgType
     *            a
     * @param aMsgKey
     *            a
     * @param aValueArr
     *            a
     * @return String
     * @author s00900517
     */
   /* public static String parseMessageResource(MESSAGE_RESOURCES aMsgType, String aMsgKey, String[] aValueArr)
    {
        String lValue = aMsgKey;
        switch (aMsgType)
        {
            case UI :
                lValue = RBundleReader.getUICfgValue(aMsgKey);
                break;
            case COMMON :
                lValue = RBundleReader.getCommonCfgValue(aMsgKey);
                break;
            case CHANGE :
                lValue = RBundleReader.getChangeToolCfgValue(aMsgKey);
                break;
            case CHECK :
                lValue = RBundleReader.getCheckToolCfgValue(aMsgKey);
                break;
            case BASE :
                lValue = RBundleReader.getBaseCommonCfgValue(aMsgKey);
                break;
            default :
        }

        String positionStr = BLANK_STRING;
        for (int i = 0; i < aValueArr.length; i++)
        {
            positionStr = new StringBuffer(FLOWER_LEFT).append(i).append(FLOWER_RIGHT).toString();
            String replaceWith = aValueArr[i];
            if (StringUtils.isBlank(replaceWith))
            {
                replaceWith = BLANK_STRING;
            }
            lValue = lValue.replace(positionStr, replaceWith);
        }
        return lValue;
    }*/
    /**
     * 
     * @param sourceStr
     *            sourceStr
     * @param startc
     *            start char
     * @param endc
     *            end char
     * @param replaceStr
     *            replaceStr
     * @return new String
     * @author j00105185
     */
    public static String replaceSign(String sourceStr, char startc, char endc, String replaceStr)
    {

        char[] cs = sourceStr.toCharArray();
        int i = 0;
        int index = 0;
        int startindex = -1;
        int endindex = -1;
        List<int[]> list = new ArrayList<int[]>();
        for (char c : cs)
        {
            if (c == startc)
            {
                if (index == 0)
                {
                    startindex = i;
                }

                index++;
            }
            if (c == endc)
            {
                if (index == 1)
                {
                    endindex = i;
                    int[] indexs = {startindex, endindex};
                    list.add(indexs);

                    startindex = -1;
                    endindex = -1;
                }

                index--;
            }

            i++;
        }

        return replace(sourceStr, list, replaceStr);
    }

    /**
     * 
     * @param sourceStr
     *            sourceStr
     * @param indexList
     *            indexList
     * @param replaceStr
     *            replaceString
     * @return new String
     * @author j00105185
     */
    public static String replace(String sourceStr, List<int[]> indexList, String replaceStr)
    {
        if (indexList.isEmpty())
        {
            return sourceStr;
        }
        StringBuffer bf = new StringBuffer();
        int t = 0;
        int i = 0;
        for (int[] ii : indexList)
        {
            bf.append(sourceStr.substring(t, ii[0]).trim()).append(replaceStr);
            if (i == indexList.size() - 1)
            {
                bf.append(sourceStr.substring(ii[1] + 1).trim());
            }
            i++;
            t = ii[1] + 1;
        }
        return bf.toString().trim();
    }

    /**
     * change String to character[]
     * 
     * @param str
     *            string
     * @return Character[]
     */
    public static Character[] getCharsFromString(String str)
    {
        if (null == str)
        {
            return null;
        }
        char[] linetemp = str.toCharArray();
        Character[] lineChars = new Character[linetemp.length];
        for (int i = 0; i < linetemp.length; i++)
        {
            lineChars[i] = Character.valueOf(linetemp[i]);
        }
        return lineChars;
    }

 

    private static class ExceptionWriter extends Writer
    {
        public String exception = new String();

        /**
         * Overwritten IO function (from <tt>java.io.Writer</tt>)
         */
        public void close()
        {
        }

        /**
         * Overwritten IO function (from <tt>java.io.Writer</tt>)
         */
        public void flush()
        {
        }

        /**
         * Overwritten IO function (from <tt>java.io.Writer</tt>)
         */
        public void write(String string)
        {
        }

        /**
         * Overwritten IO function (from <tt>java.io.Writer</tt>)
         */
        public void write(char[] cbuf, int off, int len)
        {
            String str = new String(cbuf, off, len);
            exception += str + "\n";
        }

    };

    /**
     * Convert a <tt>Throwable</tt> into a <tt>String</tt>
     * 
     * @param throwable
     *            The <tt>Throwable</tt> to convert
     * @return A <tt>String</tt> describing the <tt>Throwable</tt>
     */
    public static String getStringForThrowable(Throwable throwable)
    {
        ExceptionWriter writer = new ExceptionWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.exception;
    }

    

    /***
     * 判断是否包括中文字符
     * 
     * @param value
     * @return
     */
    public static boolean isChinese(String value)
    {

        Matcher matcher = chinesepattrn.matcher(value);
        return matcher.find();
    }

    // 取得第一个逗号分隔的字符
    public static String getFirstWord(String word)
    {
        String[] spiltWord = word.split("-");
        if (spiltWord.length == 2)
        {
            return spiltWord[0];
        }
        if (spiltWord.length == 3 || spiltWord.length == 4)
        {
            if ("".equals(spiltWord[0]))
            {
                return JOIN_WORD + spiltWord[1];
            }
            return spiltWord[0];
        }
        return word;
    }

    // 取得第二个逗号分隔的字符
    public static String getLastWord(String word)
    {
        String[] spiltWord = word.split("-");
        if (spiltWord.length == 2)
        {
            return spiltWord[1];
        }
        if (spiltWord.length == 3)
        {
            if ("".equals(spiltWord[0]))
            {
                return spiltWord[2];
            }
            return JOIN_WORD + spiltWord[2];
        }

        if (spiltWord.length == 4)
        {
            return JOIN_WORD + spiltWord[3];
        }

        return word;

    }

    /***
     * 取得时间值
     * 
     * @param time1
     * @param time2
     * @return
     */
    public static String getEcapse(Date time1, Date time2)
    {
        if (time1 == null || time2 == null)
        {
            return "";
        }
        long l = time1.getTime() - time2.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return "" + day + " Day " + hour + " Hour " + min + " Min " + s + " Sec ";
    }

    /** */
    /**
     * 字符串编码转换的实现方法
     * 
     * @param str
     *            待转换的字符串
     * @param newCharset
     *            目标编码
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String newCharset)
    {
        if (str != null)
        {
            // 用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
            byte[] bs = str.getBytes();
            try
            {
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // 用新的字符编码生成字符串
        }
        return str;
    }

    /** */
    /**
     * 字符串编码转换的实现方法
     * 
     * @param str
     *            待转换的字符串
     * @param oldCharset
     *            源字符集
     * @param newCharset
     *            目标字符集
     */
    public static String changeCharset(String str, String oldCharset, String newCharset) 
    {
        if (str != null)
        {
            // 用源字符编码解码字符串
            try
            {
                byte[] bs = str.getBytes(oldCharset); 
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;
        }
        return null;
    }
    
    
    /** */
    /**
     * 字符串编码转换的实现方法
     * 
     * @param str
     *            待转换的字符串
     * @param oldCharset
     *            源字符集
     * @param newCharset
     *            目标字符集
     */
    public static String changeCurrentCharset(String str, String oldCharset)  
    {
        if (str != null)
        {
            // 用源字符编码解码字符串
            
            try
            {
                byte[] bs = str.getBytes(oldCharset);
                return new String(bs);
            } catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;
        }
        return null;
    }

    
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);

    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

}
