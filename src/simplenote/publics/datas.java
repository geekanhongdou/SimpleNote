package simplenote.publics;

import java.util.ArrayList;
import java.util.HashMap;

public class datas {
	public static String AbsolutePath;
	public static String CurrentPath;
	public static final String Ext = ".sn";
	public static final String IndexExt = ".index";
	//------------------------------------------------
	public static ArrayList<HashMap<String, Object>> HRList;
	public static ArrayList<element> IndexCache;
	/*
	int GroupSign: 0 - Section, 1 - Section Group. 
	String name: fuckin' title. 
	*/
	public static ArrayList<record> SectionCache;
	/*
	int level: the level of your fuckin' record. If you want a fuckin' foldin' effect, you need it. 
	boolean fold: 0 - Expanded, 1 - Folded
	String title: the title of your record. 
	String content: the content of your record. fuckin' \n and other shitholes presented. 
	*/
	//-------------------------------------------------------
	public static int Find(String S)
	{
		int i=S.length()-1;
		while(i>=0 && S.charAt(i)!='/'){i--;}
		return i+1;
	}
	public static int FindSpace(String S)
	{
		int i=0;
		while(i<S.length()-1 && S.charAt(i)!=' '){i++;}
		return i;
	}
	public static String getParentDic(String Path)
	{
		int i=Path.length()-2;
		while(i>=0 && Path.charAt(i)!='/'){i--;}
		return Path.substring(0,i) + "/";
	}
	public static String limit(String O)
	{
		final int lengthLimited = 20;
		if (O.length() <= lengthLimited) return O; else return O.substring(0, lengthLimited)+"...";
		
	}
}