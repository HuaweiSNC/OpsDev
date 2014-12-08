package com.huawei.networkos.ops.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import com.huawei.network.opsdev.core.inspectview.bean.ElementalHttpServer;
import com.huawei.network.opsdev.core.inspectview.bean.HttpServiceRequestBean;
import com.huawei.network.opsdev.core.inspectview.bean.HttpServiceRequestBeanManager;
import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.networkos.ops.Activator;
import com.huawei.networkos.ops.views.event.NetworkMonitorViewListener;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

/**
 * 网络监视器视图 打开、关闭监视器 对工程是否进行监听进行选择 返回监视内容
 * 
 * @modify zWX199308
 * 
 */
public class NetworkMonitorView extends ViewPart implements Observer {

	public static final String ID = "com.huawei.networkos.ops.views.NetworkMonitorView";

	private NetworkMonitorView NetworkMonitorView = this;
	private Table protertiesTable;
	private TabFolder tabFolder;
	private Tree requestTree;
	private Tree ResponseTree;
	private Timer timer = null;

	private List<IProject> currentChoseIproject = new ArrayList<IProject>();

	public List<HttpServiceRequestBean> httpServiceRequestBeans = new ArrayList<HttpServiceRequestBean>();
	private Table networkMonitorTable;

	public NetworkMonitorView() {
		// setPartName("tile");
		setTitleToolTip("network monitor");
		// setContentDescription("fghgh");
	}

	class LoopTimerTask extends TimerTask {
		public void run() {
			loadRequestData();
		}
	}

	public void clearTimer() {
		if (null != timer) {
			timer.cancel();
		}
	}

	public void actionTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new LoopTimerTask(), 1000, 1000);
	}

	public void resetProjectMonitor() {

		IProject[] iProjects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();

		// 遍历所有工程，如果工程的.opsdev文件存在则解析后reload工程
		for (int i = 0; i < iProjects.length; i++) {
			final IProject iproject = iProjects[i];
			String projectName = null;
			File file = new File(iproject.getLocation().toOSString()
					+ File.separator + ".opsdev");

			if (!file.exists()) {
				continue;
			}

			OpsManagerProjectTool.resetNetMonitorFile(iproject);
		}
	}

	public void resetNetworkMonitor() {

		// 获取已经存在的所有工程
		IProject[] iProjects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();

		networkMonitorTable.removeAll();
		networkMonitorTable.clearAll();

		TableColumn tblclmnNewColumn_6 = new TableColumn(networkMonitorTable,
				SWT.NONE);
		tblclmnNewColumn_6.setWidth(80);
		tblclmnNewColumn_6.setText("Project");

		TableColumn tblclmnNewColumn_7 = new TableColumn(networkMonitorTable,
				SWT.NONE);
		tblclmnNewColumn_7.setWidth(105);
		tblclmnNewColumn_7.setText("Enable Monitor");

		// 遍历所有工程，如果工程的.opsdev文件存在则解析后再reload工程
		for (int i = 0; i < iProjects.length; i++) {
			final IProject iproject = iProjects[i];
			String projectName = null;
			File file = new File(iproject.getLocation().toOSString()
					+ File.separator + ".opsdev");

			if (!file.exists()) {
				continue;
			}

			ProjectConfigXmlManager projectConfigXmlManager = new ProjectConfigXmlManager(
					iproject.getLocation().toOSString() + File.separator
							+ OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
			projectConfigXmlManager.parseFile();
			String projectType = projectConfigXmlManager.getProjectType();
			OpsManagerProjectTool.reLoadProject(iproject, null, projectType);

			projectName = iproject.getName();
			Boolean projectMonitor = (Boolean) OpsManagerProjectTool
					.getManagerFromProject(iproject,
							OpsManagerProjectTool.PROJECT_MONITOR_STATE);

			final TableItem item = new TableItem(networkMonitorTable, SWT.NULL);
			item.setText(0, projectName);

			// 设置勾选框
			final Button button = new Button(networkMonitorTable, SWT.CHECK);
			button.setData(iproject);

			button.setSelection(projectMonitor);

			button.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {

					// 如果勾选框被选择则表示用户选定该工程为runable，把监视器状态设置为打开并修改文件
					String projectn = "";
					Object selectBut = e.getSource();

					if (selectBut instanceof Button) {
						Button button = (Button) selectBut;
						Object selectProject = button.getData();
						if (selectProject instanceof IProject) {

							// 将勾选框的状态保存到工程中
							OpsManagerProjectTool
									.saveManagerInToProject(
											(IProject) selectProject,
											OpsManagerProjectTool.PROJECT_MONITOR_STATE,
											button.getSelection());
							OpsManagerProjectTool
									.resetNetMonitorFile((IProject) selectProject);
							projectn = ((IProject) selectProject).getName();
						}
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			TableEditor tedit = new TableEditor(networkMonitorTable);
			tedit.grabHorizontal = true;
			tedit.setEditor(button, item, 1);

		}

	}

	// 获取监视的内容
	public void loadRequestData() {

		Display display = protertiesTable.getDisplay();
		display.asyncExec(new Runnable() {

			public void run() {

				HttpServiceRequestBean bean = HttpServiceRequestBeanManager
						.getRequestBeanByOne();
				if (null == bean) {
					return;
				}

				final String operation = bean.getOperation();
				final String requestTime = bean.getRequestTime();
				final String responseTime = bean.getResponseTime();
				final String strUrl = bean.getUrl();
				final Long elapsedTime = bean.getElapsed();
				final String context = bean.getResponse();
				final String request = bean.getRequest();

				// add to httpServiceRequestBeans
				TableItem item = new TableItem(protertiesTable, SWT.NULL);
				item.setData("request", request);
				item.setData("response", context);
				item.setText(0, (strUrl.isEmpty() ? "no url" : strUrl));
				item.setText(1, (operation.isEmpty() ? "get" : operation));
				item.setText(2, (requestTime.isEmpty() ? "  " : requestTime));
				item.setText(3, (responseTime.isEmpty() ? "  " : responseTime));
				item.setText(4, elapsedTime.toString());
				item.setText(5, (strUrl.contains("/json") ? "json" : "xml"));

			}

		});

	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));

		protertiesTable = new Table(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		protertiesTable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				// show current line request or response
				int selectIndex = protertiesTable.getSelectionIndex();
				TableItem item = protertiesTable.getItem(selectIndex);

				ResponseTree.removeAll();
				requestTree.removeAll();

				if (null != item) {
					String strurl = (String) item.getText(0);
					String context = (String) item.getData("response");
					TreeItem responseTreeItem = new TreeItem(ResponseTree,
							SWT.NULL);

					if (strurl.trim().endsWith("json")) {
						DataFactory.setJsonRootTreeItem(ResponseTree, context);
					} else {
						DataFactory.setXmlRootTreeItem(responseTreeItem,
								context);
					}
					// set context
					responseTreeItem.setText("Response:");

					String requestText = (String) item.getData("request");
					// request tree
					TreeItem requestTreeItem = new TreeItem(requestTree,
							SWT.NULL);

					if (strurl.trim().endsWith("json")) {
						DataFactory.setJsonRootTreeItemWithRelpace(requestTree,
								requestText);
					} else {
						DataFactory.setXmlRootTreeItem(requestTreeItem,
								requestText);
					}

					// set requestTest
					requestTreeItem
							.setText(new String[] { "Request:", strurl });
					// requestTreeItem.setText("Request:");
				}
			}
		});

		protertiesTable.setHeaderVisible(true);
		protertiesTable.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn.setWidth(74);
		tblclmnNewColumn.setText("URL");

		TableColumn tblclmnNewColumn_5 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_5.setWidth(100);
		tblclmnNewColumn_5.setText("Operation");

		TableColumn tblclmnNewColumn_2 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("Request Time");

		TableColumn tblclmnNewColumn_3 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("Response Time");

		TableColumn tblclmnNewColumn_4 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("Elapsed(ms)");

		TableColumn tblclmnNewColumn_1 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_1.setWidth(77);
		tblclmnNewColumn_1.setText("Service");

		tabFolder = new TabFolder(sashForm, SWT.NONE);

		TabItem tbtmResquest = new TabItem(tabFolder, SWT.NONE);
		tbtmResquest.setText("Request");

		requestTree = new Tree(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);

		requestTree.setLinesVisible(true);
		requestTree.setHeaderVisible(true);

		tbtmResquest.setControl(requestTree);

		TreeColumn trclmnNewColumn = new TreeColumn(requestTree, SWT.NONE);

		// 隐藏根节点
		GridData gd_trclmnNewColumn = new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1);

		trclmnNewColumn.setWidth(216);
		trclmnNewColumn.setText("Name");
		gd_trclmnNewColumn.exclude = true;

		TreeColumn trclmnNewColumn_1 = new TreeColumn(requestTree, SWT.NONE);
		GridData gd_trclmnNewColumn_1 = new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1);

		trclmnNewColumn_1.setWidth(216);
		trclmnNewColumn_1.setText("Value");
		gd_trclmnNewColumn_1.exclude = true;

		TabItem tbtmResponse = new TabItem(tabFolder, SWT.NONE);
		tbtmResponse.setText("Response");

		ResponseTree = new Tree(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		ResponseTree.setLinesVisible(true);
		ResponseTree.setHeaderVisible(true);
		tbtmResponse.setControl(ResponseTree);

		TreeColumn trclmnNewColumn_2 = new TreeColumn(ResponseTree, SWT.NONE);

		trclmnNewColumn_2.setWidth(216);
		trclmnNewColumn_2.setText("Name");

		TreeColumn trclmnNewColumn_3 = new TreeColumn(ResponseTree, SWT.NONE);
		trclmnNewColumn_3.setWidth(216);
		trclmnNewColumn_3.setText("Value");

		// 添加监视器的勾选框
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("MonitorConfig");

		networkMonitorTable = new Table(tabFolder, SWT.BORDER
				| SWT.FULL_SELECTION);
		tabItem.setControl(networkMonitorTable);

		networkMonitorTable.setHeaderVisible(true);
		networkMonitorTable.setLinesVisible(true);

		resetNetworkMonitor();
		sashForm.setWeights(new int[] { 329, 363 });
		initializeToolBar();

		NetworkMonitorViewListener listener = new NetworkMonitorViewListener(
				this);
		OpsManagerProjectTool.addListener(listener);

	}

	public void resetProjectList(final Collection<Object> collection) {

		Runnable runnable = new Runnable() {
			public void run() {
				resetNetworkMonitor();
			}
		};

		Activator.getDefault().getWorkbench().getDisplay().syncExec(runnable);
	}

	// 添加监视器图标按钮
	private void initializeToolBar() {
		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();

		IAction enableMonitor = new Action() {
			public void run() {

				boolean currentStat = isChecked();
				if (currentStat) {
					// start web service
					ElementalHttpServer.startServer(8099);
					actionTimer();
					resetProjectMonitor();
					setHoverImageDescriptor(ResourceManager
							.getPluginImageDescriptor(
									PathTools.PATH_OPS_VIEW_PLUGIN_ID,
									"icons/enable.png"));

				} else {

					// stop web service
					ElementalHttpServer.closeServer();
					clearTimer();

					resetProjectMonitor();
					setHoverImageDescriptor(ResourceManager
							.getPluginImageDescriptor(
									PathTools.PATH_OPS_VIEW_PLUGIN_ID,
									"icons/enable_disabled.png"));

				}

			}

		};
		enableMonitor.setChecked(true);
		enableMonitor.setEnabled(false);

		enableMonitor.setHoverImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/enable_disabled.png"));
		enableMonitor.setDisabledImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/device_handle.png"));
		enableMonitor.setText("Enable");
		enableMonitor.setImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/device_handle.png"));
		tbm.add(enableMonitor);
		enableMonitor.setChecked(false);
		enableMonitor.setEnabled(true);

		// 清除监视数据
		IAction clearMonitor = new Action() {
			public void run() {

				protertiesTable.removeAll();
				requestTree.removeAll();
				ResponseTree.removeAll();
			}

		};
		clearMonitor.setHoverImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/clearAll.png"));
		clearMonitor.setDisabledImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/device_handle.png"));
		clearMonitor.setText("Clear");
		clearMonitor.setImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/clearAll.png"));
		tbm.add(clearMonitor);

		// 暂停监控
		IAction suspendMonitor = new Action() {
			public void run() {

				boolean currentsuspend = isChecked();
				if (currentsuspend) {
					actionTimer();

				} else {
					clearTimer();
				}
			}

		};

		suspendMonitor.setHoverImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/suspend_disabled.png"));
		suspendMonitor.setDisabledImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/device_handle.png"));
		suspendMonitor.setText("Suspend");
		suspendMonitor.setImageDescriptor(ResourceManager
				.getPluginImageDescriptor(PathTools.PATH_OPS_VIEW_PLUGIN_ID,
						"icons/clearAll.png"));
		tbm.add(suspendMonitor);
	}

	public void setFocus() {
		protertiesTable.setFocus();

	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	}
}
