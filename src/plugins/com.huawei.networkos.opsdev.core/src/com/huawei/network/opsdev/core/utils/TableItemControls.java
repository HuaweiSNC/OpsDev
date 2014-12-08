package com.huawei.network.opsdev.core.utils;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class TableItemControls {
    Text text;
    CCombo cCombo;
    Button button;
    TableEditor[] tableEditor;
    public TableItemControls(Text text,CCombo cCombo,Button button , TableEditor[] editors){
        this.text = text;
        this.cCombo = cCombo;
        this.button = button;
        tableEditor = editors;
    }
    public void dispose(){
        if(text!=null){
            text.dispose();
        }
        if(cCombo!=null){
            cCombo.dispose();
        }
        if(button!=null){
        button.dispose();
        }
        for(TableEditor editor: tableEditor){
            editor.dispose();
        }
    }
    
}
