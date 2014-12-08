package com.huawei.network.opsdev.core.utils;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
/**
 * 在插件的控制台输出信息
 * 对org.eclipse.ui.consonle.consoleFactories扩展点进行扩展
 * 当需要向控制台输出信息时，只需要调用printToConsole()方法即可
 * @author zWX199308
 *
 */

public class ConsoleFactory implements IConsoleFactory{
	
	private static MessageConsole console = new MessageConsole("",null);
	static boolean exists = false;
	
	/**
	 * 打开控制台
	 */
	
	public void openConsole() {
		showConsole();
	}
	
	/**
	 * 显示控制台
	 */
	private static void showConsole(){
		if(console != null){
			//得到默认控制台管理器
			IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
			
			//得到所有的控制台实例
			IConsole[] existing = manager.getConsoles();
			exists = false;
			
			//新创建的MessageConsole实例不存在就加入到控制台管理器，并显示出来
			for(int i = 0; i < existing.length; i++){
				if(console == existing[i]){
					exists = true;
				}
			}
			
			if(!exists){
				manager.addConsoles(new IConsole[] {console});
			}
		}
	}
	
	/**
	 * 关闭控制台
	 */
	public static void closeConsole(){
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		if(console != null){
			manager.removeConsoles(new IConsole[] {console});
		}
	}
	
	/**
	 * 获取控制台
	 * @return
	 */
	public static MessageConsole getConsole(){
		showConsole();
		return console;
	}
	
	/**
	 * 向控制台打印一条信息，并激活控制台
	 * @param message
	 * @param activate  是否激活控制台
	 */
	public static void printToConsole(String message,boolean activate){
		MessageConsoleStream printer = ConsoleFactory.getConsole().newMessageStream();
		printer.setActivateOnWrite(activate);
		printer.println(message);
	}


}

