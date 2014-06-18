package cn.buaa.myweixin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsons = "{'actions':["
    			+ "{'bTime':'2014年05月31日 14:00','id':13,'mall':'鸿飞运动城','pic':'m_1399812448626.JPG','score':0.0,'title':'健康时尚运动'},"
    			+ "{'bTime':'2014年05月28日 10:00','id':11,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'羽冠王晋级赛'},"
    			+ "{'bTime':'2014年05月25日 15:00','id':14,'mall':'博宽羽毛球馆—双桥路分部','pic':'m_1399812794313.JPG','score':0.0,'title':'羽乐欢聚移动真情'},"
    			+ "{'bTime':'2014年05月24日 15:00','id':12,'mall':'航华二中羽毛球馆','pic':'m_1399735056391.PNG','score':0.0,'title':'双打竞技赛'},"
    			+ "{'bTime':'2014年05月03日 14:00','id':10,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'羽冠王预选赛'},"
    			+ "{'bTime':'2014年05月02日 14:00','id':9,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'双打争霸赛'},"
    			+ "{'bTime':'2014年05月01日 14:00','id':4,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'羽毛球相亲'},"
    			+ "{'bTime':'2014年05月01日 09:00','id':8,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'单打争霸赛'},"
    			+ "{'bTime':'2014年04月30日 16:00','id':3,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'羽毛球亲子活动'},"
    			+ "{'bTime':'2014年04月30日 15:00','id':2,'mall':'航华二中羽毛球馆','pic':'action.png','score':0.0,'title':'羽毛球男女邀请赛'}"
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
