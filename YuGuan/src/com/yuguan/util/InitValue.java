package com.yuguan.util;

import android.content.SharedPreferences;

public class InitValue {

	public static String countyJson = "{'cid':'173','counties':["
			+ "{'city':'黄浦区','flag':null,'id':1786,'pid':null},"
			+ "{'city':'卢湾区','flag':null,'id':1787,'pid':null},"
			+ "{'city':'徐汇区','flag':null,'id':1788,'pid':null},"
			+ "{'city':'长宁区','flag':null,'id':1789,'pid':null},"
			+ "{'city':'静安区','flag':null,'id':1790,'pid':null},"
			+ "{'city':'普陀区','flag':null,'id':1791,'pid':null},"
			+ "{'city':'闸北区','flag':null,'id':1792,'pid':null},"
			+ "{'city':'虹口区','flag':null,'id':1793,'pid':null},"
			+ "{'city':'杨浦区','flag':null,'id':1794,'pid':null},"
			+ "{'city':'闵行区','flag':null,'id':1795,'pid':null},"
			+ "{'city':'宝山区','flag':null,'id':1796,'pid':null},"
			+ "{'city':'嘉定区','flag':null,'id':1797,'pid':null},"
			+ "{'city':'浦东新区','flag':null,'id':1798,'pid':null},"
			+ "{'city':'金山新新区','flag':null,'id':1799,'pid':null},"
			+ "{'city':'松江新新新区','flag':null,'id':1800,'pid':null},"
			+ "{'city':'青浦区','flag':null,'id':1801,'pid':null},"
			+ "{'city':'南汇区','flag':null,'id':1802,'pid':null},"
			+ "{'city':'奉贤区','flag':null,'id':1803,'pid':null},"
			+ "{'city':'崇明县','flag':null,'id':1804,'pid':null}]}";
	public static String activityJson = "{'actions':["
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
			+ "],'huodongList':'success','nums':14,'pg':'1','rg':'0','sr':'0'}";;
	private static StringBuffer jsons = new StringBuffer(
					"{'mallList':'success','malls':["
							+ "{'address':'闵行区航新路75号(航新路吴中路)','comments':18,'favorites':14,'id':1,"
							+ "'mdesc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。"
							+ "学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，"
							+ "大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
							+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
							+ "校园里充满了团结和谐、务实向上的良好氛围','phone':'021-64202335','pic':'mall.png','score':7.72222,"
							+ "'title':'航华二中羽毛球馆'},"
							+ "{'address':'闵行区航新路75号(航新路吴中路)','comments':18,'favorites':14,'id':1,"
							+ "'mdesc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。"
							+ "学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，"
							+ "大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
							+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
							+ "校园里充满了团结和谐、务实向上的良好氛围','phone':'021-64202335','pic':'mall.png','score':7.72222,"
							+ "'title':'航华二中羽毛球馆'},"
							+ "{'address':'闵行区航新路75号(航新路吴中路)','comments':18,'favorites':14,'id':1,"
							+ "'mdesc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。"
							+ "学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，"
							+ "大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
							+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
							+ "校园里充满了团结和谐、务实向上的良好氛围','phone':'021-64202335','pic':'mall.png','score':7.72222,"
							+ "'title':'航华二中羽毛球馆'},"
							+ "{'address':'闵行区航新路75号(航新路吴中路)','comments':18,'favorites':14,'id':1,"
							+ "'mdesc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。"
							+ "学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，"
							+ "大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
							+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
							+ "校园里充满了团结和谐、务实向上的良好氛围','phone':'021-64202335','pic':'mall.png','score':7.72222,"
							+ "'title':'航华二中羽毛球馆'},"
							+ "{'address':'闵行区航新路75号(航新路吴中路)','comments':18,'favorites':14,'id':1,"
							+ "'mdesc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。"
							+ "学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，"
							+ "大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
							+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
							+ "校园里充满了团结和谐、务实向上的良好氛围','phone':'021-64202335','pic':'mall.png','score':7.72222,"
							+ "'title':'航华二中羽毛球馆'},"
							+ "{'address':'双桥路302号','comments':7,'favorites':2,'id':2,"
							+ "'mdesc':'一流的设施和服务，是上海市各种业余比赛的首选场馆。馆内设有大型免费停车场、太阳能热水器、商品部、"
							+ "空调休息室、更衣室、洗 浴室等设施；馆内具有与国家羽毛球训练馆一致的灯光，照明均匀而又不晃眼，"
							+ "地面采用进口专业塑胶地板，弹性良好、脚感舒适，球场间的间隔非常宽松。场馆内另设桌球，乒乓球等设施。"
							+ "是运动、娱乐、休闲的好去处。','phone':'021-58655766','pic':'m_1399561345711.jpg','score':9.14286,"
							+ "'title':'博宽羽毛球馆—双桥路分部'},"
							+ "{'address':'一二八纪念路401号','comments':0,'favorites':0,'id':3,"
							+ "'mdesc':'鸿飞运动城为综合性体育运动场所，有羽毛球，足球，乒乓球，桌球等运动项目。运动城交通便利，"
							+ "地铁一号线与三号线之间，南北高架，中环线及外环线包围，紧邻宝山万达商业广场，四周居民区环绕。羽毛球馆内更"
							+ "是有14片专业标准塑胶双打场地，1片单打练习场，层高11.5米，木地板上铺设独特的4.5MM厚蓝色牛皮纹塑胶搭配黄线，"
							+ "配套设施完善。另有国家二级至国家健将级运动员助阵开设培训班。','phone':'13585724724',"
							+ "'pic':'m_1399690526172.PNG','score':0.0,'title':'鸿飞运动城'},"
							+ "{'address':'呼玛路888号近共和新路','comments':6,'favorites':0,'id':4,"
							+ "'mdesc':'共有7片专业运动塑胶地板，体育健身的好去处','phone':'13671822664','pic':'m_1400160105985.jpg',"
							+ "'score':5.66667,'title':'通河中学羽毛球馆'}"
							+ "],'nums':4,'pg':'1','rg':'0','sr':'0'}");
	public static String mallJson = jsons.toString();
	public static String friendJson = "{'cid':173,'frds':["
			+ "{'addr':'上海-闵行区','level':'LV1','name':'Johnney','pic':'img.jpg','rpu':10,'sex':1,'skill':10,'star':1,'uid':28},"
			+ "{'addr':'上海','level':'LV1','name':'Lehmann','pic':'qq.jpg','rpu':18,'sex':2,'skill':8,'star':1,'uid':29},"
			+ "{'addr':'上海','level':'LV1','name':'思密达','pic':'default.jpg','rpu':10,'sex':0,'skill':7,'star':1,'uid':30}"
			+ "],'num':3,'pn':1}";
	
	public static String actionInfoJson = "{'actionInfo':"
			+ "{'aName':'健康时尚运动','aid':13,'backPic':'m_1399812448626.JPG','contact':'Lehmann','desc':'飞一般的感觉',"
			+ "'eTime':'2014-05-31 17:00','mAddress':'一二八纪念路401号','mName':'鸿飞运动城','mid':3,'phone':'13817227701',"
			+ "'poster':'Lehmann','posterId':29,'price':20,'sTime':'2014-05-31 14:00','score':0.0,'userNum':12},"
			+ "'aid':'13','desc':null,'status':null,'tel':null,'uid':null,"
			+ "'users':["
			+ "{'name':'菠萝先生','pic':'img.jpg','sex':1,'sort':100,'uid':28},"
			+ "{'name':'Lehmann','pic':'qq.jpg','sex':2,'sort':100,'uid':29},"
			+ "{'name':'思密达','pic':'default.jpg','sex':0,'sort':100,'uid':30}"
			+ "]}";
	
	public static String commentJson = "{'aid':'13','commentid':'0','comments':["
			+ "{'comment':'大家多多支持呀','id':28,'postTime':'2014-05-17 12:36','uName':'菠萝先生','uPic':'img.jpg','uid':28},"
			+ "{'comment':'大家多多支持呀','id':27,'postTime':'2014-05-15 22:32','uName':'Lehmann','uPic':'qq.jpg','uid':29},"
			+ "{'comment':'时尚，健康，思密达...','id':24,'postTime':'2014-05-13 23:09','uName':'思密达','uPic':'default.jpg','uid':30}"
			+ "]}";
	// {"message":"用户名或者密码不存在","password":"1253456","user":null,"username":"kevin"}
	public static String loginJson = "{'message':null,'password':'123456','user':"
			+ "{'email':'yongzhong1@126.com','id':31,'name':'kevin','password':'E10ADC3949BA59ABBE56E057F20F883E','status':2,'vip':0},"
			+ "'username':'kevin'}";
	
	
	public static String mallsInfo = "{'commentid':null,'comments':null,'envInfo':null,'imgs':null,'mallEvents':null,"
			+ "'mallInfo':{'address':'闵行区航新路75号(航新路吴中路)','desc':'上海市航华第二中学创办于2003年6月，"
			+ "是在闵行区教育局直接领导下的一所公办初级中学。学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，"
			+ "教室及辅助用房40间，大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
			+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，"
			+ "校园里充满了团结和谐、务实向上的良好氛围','mid':1,'name':'航华二中羽毛球馆','phone':'021-64202335',"
			+ "'pic':'mall.png','score':8.14286},'mid':'1','status':null}";
	
	public static String picsInfoJson = "{'commentid':null,'comments':null,'envInfo':null,"
			+ "'imgs':['m1_1397922592581.png','m1_1397922592582.png','m1_1397922592583.png','m1_1397922592584.png',"
			+ "'m1_1397922592585.png','m1_1401116363797.PNG','m1_1401116363798.PNG','m1_1401117849516.PNG'],"
			+ "'mallEvents':null,'mallInfo':{'address':'闵行区航新路75号(航新路吴中路)',"
			+ "'desc':'上海市航华第二中学创办于2003年6月，是在闵行区教育局直接领导下的一所公办初级中学。学校占地17600平方米，"
			+ "校舍建筑面积9040平方米，绿化面积6200平方米，教室及辅助用房40间，大型室内体育场馆，250米塑胶跑道，"
			+ "及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，校园环境整洁美丽，办学条件优良。学校现已拥有"
			+ "一支较强的师资队伍，学校管理规范有序，教师工作认真负责，校园里充满了团结和谐、务实向上的良好氛围','mid':1,"
			+ "'name':'航华二中羽毛球馆','phone':'021-64202335','pic':'mall.png','score':8.14286},'mid':'1','status':null}";
	
	public static String envInfoJson = "{'commentid':null,'comments':null,"
			+ "'envInfo':{'bus':'枢纽5路 七宝3路 七宝2路','floor':'专业塑胶','height':14,'num':12,"
			+ "'openobj':'面向所有人开放，办理VIP有折扣','optime':'工作日：18:00—22:00 周末： 8:00—22:00',"
			+ "'parking':'室内地下收费停车场','position':'室内','sale':'售羽毛球 饮料 矿泉水 出租球拍','subway':'9号线七宝站',"
			+ "'wash':'热水淋浴','wifi':'免费WIFI'},'imgs':['m1_1397922592581.png','m1_1397922592582.png',"
			+ "'m1_1397922592583.png','m1_1397922592584.png','m1_1397922592585.png','m1_1401116363797.PNG',"
			+ "'m1_1401116363798.PNG','m1_1401117849516.PNG'],'mallEvents':null,"
			+ "'mallInfo':{'address':'闵行区航新路75号(航新路吴中路)','desc':'上海市航华第二中学创办于2003年6月，"
			+ "是在闵行区教育局直接领导下的一所公办初级中学。学校占地17600平方米，校舍建筑面积9040平方米，绿化面积6200平方米，"
			+ "教室及辅助用房40间，大型室内体育场馆，250米塑胶跑道，及各类专用教室一应俱全，教学设施齐全先进。校舍宽敞明亮，"
			+ "校园环境整洁美丽，办学条件优良。学校现已拥有一支较强的师资队伍，学校管理规范有序，教师工作认真负责，校园里充满"
			+ "了团结和谐、务实向上的良好氛围','mid':1,'name':'航华二中羽毛球馆','phone':'021-64202335','pic':'mall.png',"
			+ "'score':8.14286},'mid':'1','status':null}";
	
	public static SharedPreferences preferences;
	public InitValue() {
	}

}
