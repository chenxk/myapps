package com.yuguan.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class BaseStationLocation extends Activity{

	
	/**
	 * ��ȡ��վ��Ϣ
	 * 
	 * @throws Exception
	 */
	private  SCell getCellInfo() throws Exception {
		SCell cell = new SCell();

		/** ����API��ȡ��վ��Ϣ */
		TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
		if (location == null)
			throw new Exception("��ȡ��վ��Ϣʧ��");

		String operator = mTelNet.getNetworkOperator();
		int mcc = Integer.parseInt(operator.substring(0, 3));
		int mnc = Integer.parseInt(operator.substring(3));
		int cid = location.getCid();
		int lac = location.getLac();

		/** ����õ����ݷŵ��ṹ���� */
		cell.MCC = mcc;
		cell.MNC = mnc;
		cell.LAC = lac;
		cell.CID = cid;

		return cell;
	}
    
	/**
	 * ��ȡ��γ��
	 * 
	 * @throws Exception
	 */
	private  SItude getItude(SCell cell) throws Exception {
		SItude itude = new SItude();

		/** ����AndroidĬ�ϵ�HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** ����POST���� */
		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		try {
			/** ����POST��JSON���� */
			JSONObject holder = new JSONObject();
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("address_language", "zh_CN");
			holder.put("request_address", true);
			holder.put("radio_type", "gsm");
			holder.put("carrier", "HTC");

			JSONObject tower = new JSONObject();
			tower.put("mobile_country_code", cell.MCC);
			tower.put("mobile_network_code", cell.MNC);
			tower.put("cell_id", cell.CID);
			tower.put("location_area_code", cell.LAC);

			JSONArray towerarray = new JSONArray();
			towerarray.put(tower);
			holder.put("cell_towers", towerarray);

			StringEntity query = new StringEntity(holder.toString());
			post.setEntity(query);

			/** ����POST���ݲ���ȡ�������� */
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}

			/** �������ص�JSON���ݻ�þ�γ�� */
			JSONObject json = new JSONObject(strBuff.toString());
			JSONObject subjosn = new JSONObject(json.getString("location"));

			itude.latitude = subjosn.getString("latitude");
			itude.longitude = subjosn.getString("longitude");
			
			Log.i("Itude", itude.latitude + itude.longitude);
			
		} catch (Exception e) {
			Log.e(e.getMessage(), e.toString());
			throw new Exception("��ȡ��γ�ȳ��ִ���:"+e.getMessage());
		} finally{
			post.abort();
			client = null;
		}
		
    	return itude;
    }
    
	/**
	 * ��ȡ����λ��
	 * 
	 * @throws Exception
	 */
	private  String getLocation(SItude itude) throws Exception {
		String resultString = "";

		/** �������get������ֱ�ӽ������ӵ�URL�� */
		String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);
		Log.i("URL", urlString);

		/** �½�HttpClient */
		HttpClient client = new DefaultHttpClient();
		/** ����GET���� */
		HttpGet get = new HttpGet(urlString);
		try {
			/** ����GET���󲢻�÷������� */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			/** ����JSON���ݣ���������ַ */
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString("address");
				}
			}
		} catch (Exception e) {
			throw new Exception("��ȡ����λ�ó��ִ���:" + e.getMessage());
		} finally {
			get.abort();
			client = null;
		}

		return resultString;
	}
	
	
	public  String getLocation() {
		try {
			SCell cell = getCellInfo();
			SItude itude = getItude(cell);
			return getLocation(itude);
		} catch (Exception e) {
			return "";
		}
		
	}
	
}
