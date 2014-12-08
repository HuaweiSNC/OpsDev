package trap;







public class ReadConfig {

	private StringBuffer path;


/**
 * 获取文件路径目录
 */

	public StringBuffer getPath() {
		if(null == path || "".equals(path.toString())){
			path = new StringBuffer();
			String filePah=ReadConfig.class.getResource("/").getPath();
			filePah = filePah.replace("%20", " ");
		 if(filePah.contains("bin/")){
				path.append(filePah.substring(1, filePah.lastIndexOf("bin/")));
			}
		}
		return path;
	}
/**
 * 获取文件名
 */
	public String getExcel(String fileName){
		StringBuffer filePah=new StringBuffer(getPath());
		filePah.append("file/");
		filePah.append(fileName);
		return filePah.toString();
	}


}
