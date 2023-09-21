package simplenote.filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class write {
	private static File file;
	private static FileWriter fileWriter;
	private static String temp;
	
	public static void SectionGroup(String name) {
		file=new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+"/"+simplenote.publics.datas.IndexExt);
		if (!file.isFile()) {
			try{
				file.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			for(int i=0;i<simplenote.publics.datas.IndexCache.size();i++){
				temp=(Integer)simplenote.publics.datas.IndexCache.get(i).GroupSign + " ";
				fileWriter.write(temp);
				temp=(String)simplenote.publics.datas.IndexCache.get(i).name+"\n";
				fileWriter.write(temp);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void Section(String name) {
		file=new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+simplenote.publics.datas.Ext);
		if (!file.isFile()) {
			try{
				file.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			for(int i=0;i<simplenote.publics.datas.SectionCache.size();i++){
				temp=simplenote.publics.datas.SectionCache.get(i).level+" ";
				fileWriter.write(temp);
				temp=(simplenote.publics.datas.SectionCache.get(i).fold)+" ";
				fileWriter.write(temp);
				temp=simplenote.publics.datas.SectionCache.get(i).content.length()+"\n";
				fileWriter.write(temp);
				temp=simplenote.publics.datas.SectionCache.get(i).title+"\n";
				fileWriter.write(temp);
				temp=simplenote.publics.datas.SectionCache.get(i).content+"\n\n";
				fileWriter.write(temp);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
