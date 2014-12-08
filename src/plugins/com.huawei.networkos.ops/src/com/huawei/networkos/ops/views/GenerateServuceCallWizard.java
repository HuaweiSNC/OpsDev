package com.huawei.networkos.ops.views;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.huawei.network.opsdev.core.templet.ProjectTempletCreater;
import com.huawei.network.opsdev.core.templet.TempletAutoPythonNode;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;

public class GenerateServuceCallWizard extends Wizard {
	GenerateServiceCallWizardPage callWizardPage;
	TempletAutoPythonNode templetAutoPythonNode;
	IProject iProject;

	public GenerateServuceCallWizard(OpsService opsService,
			OpsServiceUrlHandle handle, IProject iProject) {
		setWindowTitle("GenerateServiceCallPage");
		this.iProject = iProject;
		templetAutoPythonNode = new TempletAutoPythonNode(opsService, handle,
				iProject);

		String vmFile = OpsManagerProjectTool.getVMTempletFileName(
				this.iProject,
				OpsManagerProjectTool.DEVICEMANAGER_CALL_HANDLE_POINT);
		callWizardPage = new GenerateServiceCallWizardPage(this.iProject, templetAutoPythonNode, vmFile);

	}

	public void addPages() {
		addPage(callWizardPage);
	}

	public boolean performFinish() {
		IFile file = null;
		BufferedInputStream buff = null;
		Reader r = null;
		BufferedReader br = null;
		String vmFile = OpsManagerProjectTool.getVMTempletFileName(
				this.iProject,
				OpsManagerProjectTool.DEVICEMANAGER_HANDLE_POINT);
		String outputFile = OpsManagerProjectTool.getOutputFileName(
				this.iProject,
				OpsManagerProjectTool.DEVICEMANAGER_HANDLE_POINT);
		file = iProject.getFile(outputFile);
		String fileLocation = file.getLocation().toOSString();
		
		String plugid = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
		ProjectTempletCreater creater = new ProjectTempletCreater(plugid);
		try {
			// 更具判断是否是python工程
			if (file.getName().contains(".py")) {

				file.appendContents(new ByteArrayInputStream(creater
						.startAntoInfo(templetAutoPythonNode, vmFile)
						.toString().getBytes()), IFile.KEEP_HISTORY, null);
			} else if (file.getName().contains(".java")) {
				InputStream fileStream = file.getContents();
				buff = new BufferedInputStream(fileStream);
				r = new InputStreamReader(buff, "utf-8");
				br = new BufferedReader(r);

				String strLine = null;
				StringBuffer fileBuffer = new StringBuffer("");
				while ((strLine = br.readLine()) != null) {
					fileBuffer.append(strLine + "\r\n");
				}
				String javaFileContents = fileBuffer.toString();
				javaFileContents = javaFileContents.substring(0,
						javaFileContents.lastIndexOf('}')) + "\n";

				// vm模板内容
				String vmFileContents = creater.startAntoInfo(
						templetAutoPythonNode, vmFile).toString();
				String fileContents = javaFileContents + vmFileContents + "\n"
						+ "}";

				// 将内容输出到文件
				File javaFile = new File(fileLocation);
				// 删除之前文件
				if (javaFile.exists()) {
					javaFile.delete();
				}
				javaFile.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(javaFile));
				bw.write(fileContents);
				bw.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (null != r) {
					r.close();
				}
				if (null != buff) {
					buff.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
		}

		return true;
	}
}
