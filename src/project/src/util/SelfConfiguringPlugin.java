/*********************************************************************
Copyright, 2008-2010, Huawei Tech. Co., Ltd.
All Rights Reserved
----------------------------------------------------------------------
Project Code  : VIBE V3.0
*********************************************************************/
package util;

import net.sf.json.JSONArray;
import org.w3c.dom.Element;

 
public interface SelfConfiguringPlugin {

    /**
     * @param element
     */
    void configure(Element element) throws Exception;

        /**
     * @param element
     */
    void configure(JSONArray element) throws Exception;

}
