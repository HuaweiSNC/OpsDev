package com.huawei.networkos.ops.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.huawei.network.opsdev.core.utils.PathTools;
/**
 * OpsDev菜单栏中关于OpsDev的相关信息
 *
 */
public class AboutOpsDevPage extends Dialog {

    /**
     * Create the dialog.
     * @param parentShell
     */
    public AboutOpsDevPage(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Create contents of the dialog.
     * @param parent
     */

    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(3, false));
        
        Label label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setText("         ");
        
        Label label = new Label(container, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
        label.setText("Version:");
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText(PathTools.getPlugVersion());

        new Label(container, SWT.NONE);
        
        Label lblCopyrightcHuawei = new Label(container, SWT.NONE);
        lblCopyrightcHuawei.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 1));
        lblCopyrightcHuawei.setText("Copyright (C) Huawei Technologires All Right Reserved");
        new Label(container, SWT.NONE);
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setText("Author:");
        
        Label lblLequanXie = new Label(container, SWT.NONE);
        lblLequanXie.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
        lblLequanXie.setText("LeQuan Xie , Xi Qiao , PengYu Zhang , ShaoPeng Huang");

        return container;
    }

    /**
     * Create contents of the button bar.
     * @param parent
     */
    protected void createButtonsForButtonBar(Composite parent) {
        Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                close();
            }
        });
    }

    /**
     * Return the initial size of the dialog.
     */
    protected Point getInitialSize() {
        return new Point(444, 167);
    }
    
}
