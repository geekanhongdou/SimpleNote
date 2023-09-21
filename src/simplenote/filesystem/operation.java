package simplenote.filesystem;

import java.io.File;
import java.io.IOException;

public class operation {
	private static File file;
	private static File newfile;
	private static File[] files;

	public static class Create {
		public static void Notebook() {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath);
			file.mkdirs();
		}
		public static void SectionGroup(String name) {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name);
			file.mkdirs();
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
		}
	}
	public static class Delete {
		private static void rDelete(File file) {
			if (file.exists()) { 
				if (file.isFile()) { 
					file.delete(); 
				} else if (file.isDirectory()) { 
					files = file.listFiles(); 
					for (int i = 0; i < files.length; i++) {
						rDelete(files[i]); 
					}
				}
				file.delete();
			} else {
			}
		}
		
		public static void SectionGroup(String name) {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name);
			rDelete(file);
		}
			
		public static boolean Section(String name) {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+simplenote.publics.datas.Ext);
			if (file == null || !file.exists() || file.isDirectory())  return false;   
			file.delete();
			return true;
		}
	}
		
	public static class Rename {
		public static void SectionGroup(String name, String newname) {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name);
			newfile = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+newname);
			file.renameTo(newfile);
		}
		public static void Section(String name, String newname) {
			file = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name+simplenote.publics.datas.Ext);
			newfile = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+newname+simplenote.publics.datas.Ext);
			file.renameTo(newfile);
		}
	}
}
