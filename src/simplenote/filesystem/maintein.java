package simplenote.filesystem;

import java.io.File;
import java.util.HashMap;
import simplenote.publics.element;

public class maintein {
	private static File sectionGroup;
	private static File[] realFilelist;
	public static element singleElement;
	private static String SNRemoved;
	
	
	public static void Notebook() {
		SectionGroup(""); 
		//if you like a fuckin' fake increment in code length,  let's copy the code below in there.
		//But currently i don't wanna do that. 
	}
	public static void SectionGroup(String name) {
		simplenote.filesystem.read.SectionGroup(name);
		sectionGroup = new File(simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath+name);
		realFilelist = sectionGroup.listFiles();
		//-----------File type check: make the files whose extension ain't .sn disappear. -------------------
		//realFilelist[realFilelist.length-1] = null; //kill the fuckin' .index first. others tomorrow. -----it's not a good idea. 
		for (int Real=0;Real<realFilelist.length;Real++)
		{
			{
				if (!realFilelist[Real].isDirectory()){
					if (!realFilelist[Real].getName().endsWith(name+simplenote.publics.datas.Ext)){
						realFilelist[Real] = null;
					}
				}
			}
		}
		//-----------delete the elements that's NOT existed in Real filesystem BUT existed in IndexCache-----
		for (int Index=0;Index<simplenote.publics.datas.IndexCache.size();)
		{
			int Real=0;
			while (Real<realFilelist.length && (realFilelist[Real] == null || !RemoveSN(realFilelist[Real]).equals((String)simplenote.publics.datas.IndexCache.get(Index).name)) ){Real++;}
			if (Real>=realFilelist.length){
				simplenote.publics.datas.IndexCache.remove(Index);
			} else {
				realFilelist[Real] = null; 
				Index++;
				//if what existed in Real filesystem also exists in IndexCache, or 
				//what existed in IndexCache also exists in Real filesystem 
				//(Whatever you say), remove it in the realFilelist by make it void. 
			}
		}
		//-----------append the elements that's NOT existed in IndexCache BUT existed in Real filesystem-----
		for (int Real=0;Real<realFilelist.length;Real++)
		{
			if (realFilelist[Real] != null){
				singleElement = new element();
				singleElement.GroupSign = realFilelist[Real].isDirectory() ? 1 : 0;
				singleElement.name = realFilelist[Real].isDirectory() ? realFilelist[Real].getName() : realFilelist[Real].getName().substring(0, realFilelist[Real].getName().length()-3); //kill the fuckin' .sn;
				simplenote.publics.datas.IndexCache.add(singleElement);   
			}
		} 
		simplenote.filesystem.write.SectionGroup(name);
	}
	//------------Kill the fuckin' .sn. But sometimes you need a Method to kill it. So that's the ugly stuff. -------------
	private static String RemoveSN(File O){
		if (O != null) {
			SNRemoved = O.isDirectory() ? O.getName() : O.getName().substring(0, O.getName().length()-3); //kill the fuckin' .sn;
		}
		return SNRemoved;
	}
}