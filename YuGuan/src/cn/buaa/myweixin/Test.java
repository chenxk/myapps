package cn.buaa.myweixin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsons = "{'actions':["
    			+ "{'bTime':'2014��05��31�� 14:00','id':13,'mall':'����˶���','pic':'m_1399812448626.JPG','score':0.0,'title':'����ʱ���˶�'},"
    			+ "{'bTime':'2014��05��28�� 10:00','id':11,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'�����������'},"
    			+ "{'bTime':'2014��05��25�� 15:00','id':14,'mall':'������ë��ݡ�˫��·�ֲ�','pic':'m_1399812794313.JPG','score':0.0,'title':'���ֻ����ƶ�����'},"
    			+ "{'bTime':'2014��05��24�� 15:00','id':12,'mall':'����������ë���','pic':'m_1399735056391.PNG','score':0.0,'title':'˫�򾺼���'},"
    			+ "{'bTime':'2014��05��03�� 14:00','id':10,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'�����Ԥѡ��'},"
    			+ "{'bTime':'2014��05��02�� 14:00','id':9,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'˫��������'},"
    			+ "{'bTime':'2014��05��01�� 14:00','id':4,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'��ë������'},"
    			+ "{'bTime':'2014��05��01�� 09:00','id':8,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'����������'},"
    			+ "{'bTime':'2014��04��30�� 16:00','id':3,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'��ë�����ӻ'},"
    			+ "{'bTime':'2014��04��30�� 15:00','id':2,'mall':'����������ë���','pic':'action.png','score':0.0,'title':'��ë����Ů������'}"
    			+ "],'huodongList':'success','nums':14,'pg':'1','rg':'0','sr':'0'}";
    	
    	try {
			JSONObject jsonObject = new JSONObject(jsons);
			JSONArray jsonArray = jsonObject.getJSONArray("actions");
			for(int i = 0; i< jsonArray.length(); i++){
				JSONObject json = (JSONObject) jsonArray.get(i);
				System.out.println(json.get("bTime"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
