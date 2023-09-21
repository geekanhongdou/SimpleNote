package simplenote.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import simplenote.publics.element;
import simplenote.publics.record;

public class read {
	private static File file;
	private static FileReader fileReader;
	private static BufferedReader fileBufferedReader;
	private static element singleElement;
	private static record singleRecord;
	private static String temp;
	
	public static void SectionGroup(String name) {
		simplenote.publics.datas.IndexCache = new ArrayList<element>();
		file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+"/"+simplenote.publics.datas.IndexExt);
		if (!file.isFile()) {
			try{
				file.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileBufferedReader = new BufferedReader(fileReader);
		try{
			temp = fileBufferedReader.readLine();
			while (temp!=null){
				singleElement = new element();
				singleElement.GroupSign = Integer.parseInt(temp.substring(0,1));
				singleElement.name = temp.substring(2,temp.length());
		        simplenote.publics.datas.IndexCache.add(singleElement);  
		        temp = fileBufferedReader.readLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void Section(String name) {
		simplenote.publics.datas.SectionCache = new ArrayList<record>();
		file=new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+simplenote.publics.datas.Ext);
		if (!file.isFile()) {
			return;
		}
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileBufferedReader = new BufferedReader(fileReader);
		try{
			temp = fileBufferedReader.readLine();
			while (temp!=null){
				singleRecord = new record();
				singleRecord.level = Integer.parseInt(temp.substring(0, simplenote.publics.datas.FindSpace(temp)));
				temp = temp.substring(simplenote.publics.datas.FindSpace(temp)+1,temp.length());
				singleRecord.fold = (Integer.parseInt(temp.substring(0, simplenote.publics.datas.FindSpace(temp))));
				temp = temp.substring(simplenote.publics.datas.FindSpace(temp)+1,temp.length());
				int contentLength = Integer.parseInt(temp);
				singleRecord.title = fileBufferedReader.readLine();
				char[] tempChars = new char[contentLength];
				fileBufferedReader.read(tempChars, 0, contentLength);
				singleRecord.content = new String(tempChars);
				fileBufferedReader.skip(2);
		        simplenote.publics.datas.SectionCache.add(singleRecord);  
		        temp = fileBufferedReader.readLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}