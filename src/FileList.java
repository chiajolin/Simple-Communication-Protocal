

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class FileList {
	private String sendingFilePath;
	private ArrayList<String> filenameList = new ArrayList<String>();
	private Properties prop;
	
	public FileList() throws IOException{
		getProperties();
		sendingFilePath = getServerFilePath();
		setFileLists();
	}
	
	//to load config.properties
	private void getProperties() throws IOException{
		prop = new Properties();
		prop.load(new FileInputStream("../src/config.properties"));
	}
	
	//to get server file path
	public String getServerFilePath(){
		return prop.getProperty("SERVER_FILE_PATH");
	}
	
	//to get client file path
	public String getClientFilePath(){
		return prop.getProperty("CLIENT_FILE_PATH");
	}
	
	private void setFileLists(){
		File[] files = new File(sendingFilePath).listFiles();
		for(File file : files){
			filenameList.add(file.getName());
		}
	}
	
	public ArrayList<String> getFileLists(){
		return filenameList;
	}
}
