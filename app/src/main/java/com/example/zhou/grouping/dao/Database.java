package com.example.zhou.grouping.dao;

import com.example.zhou.grouping.Bean.Customers;
import com.example.zhou.grouping.Bean.Groups;
import com.example.zhou.grouping.Bean.bGroupMember;
import com.example.zhou.grouping.Bean.sGroupMember;
import com.example.zhou.grouping.group.Currents;
import com.example.zhou.grouping.group.SearchGrpActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

public class Database {

	public static final String IP = "192.168.1.2";
	// 登陆
	public static class Login implements Callable<Boolean> {
		private boolean ifFit = false;
		private String cID;
		private String cName = "";
		private String cPasswd = "";
		private String sqlPasswd = "";
		private int cSex = 0;
		private String cClass = "";

		public Login(String cID, String cPasswd) {
			this.cID = cID;
			this.cPasswd = cPasswd;
		}

		public Boolean call() throws Exception {
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost("http://"+IP+"/login.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);// iso-8859-1
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();

			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);
			cName = json_data.getString("cName");
			sqlPasswd = json_data.getString("cPasswd");

			cSex = json_data.getInt("cSex");
			cClass = json_data.getString("cClass");

			if ((this.cPasswd).equals(sqlPasswd)) {
				ifFit = true;
				Currents.currentCustomer.setcID(cID);
				Currents.currentCustomer.setcName(cName);
				Currents.currentCustomer.setcPasswd(cPasswd);
				Currents.currentCustomer.setcSex(cSex);
				Currents.currentCustomer.setcClass(cClass);
				return ifFit;

			}
			return ifFit;
		}
	}

	// 注册，返回false则该用户已被注册
	public static class addCustomers implements Callable<Boolean> {
		String cID, cName, cPasswd, cClass;
		int cSex;

		public addCustomers(String cID, String cName, String cPasswd,
				String cClass, int cSex) {
			this.cID = cID;
			this.cName = cName;
			this.cPasswd = cPasswd;
			this.cClass = "默认";
			this.cSex = 1;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("cName", cName));
			nameValuePairs.add(new BasicNameValuePair("cPasswd", cPasswd));
			nameValuePairs.add(new BasicNameValuePair("cSex", cSex + ""));
			nameValuePairs.add(new BasicNameValuePair("cClass", cClass));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/register.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));
				// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result")) {
					return true;
				} else {
					return false;
				}

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 加载用户加入的群
	public static class LoadProjects implements Callable<Boolean> {

		private String cID;
		private boolean ifSuccess = false;

		public LoadProjects(String cID) {
			this.cID = cID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadProjects.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();

			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Groups g = new Groups();
				g.setgID(json_data.getString("gID"));
				g.setgName(json_data.getString("gName"));
				g.setgPasswd(json_data.getString("gPasswd"));
				g.setgMin(json_data.getInt("gMin"));
				g.setgMax(json_data.getInt("gMax"));
				Currents.GroupsList.add(g);
				ifSuccess = true;

			}
			return ifSuccess;
		}
	}

	// 根据群号获取所有组
	public static class loadSGroups implements Callable<Boolean> {

		private String gID, cName;
		private int sgID;
		private boolean ifSuccess = false;

		public loadSGroups(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadSGroups.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();

			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				sGroupMember sgm = new sGroupMember();
				sgID = json_data.getInt("sgID");
				cName = json_data.getString("cName");
				sgm.setSgID(sgID);
				sgm.setcName(cName);
				Currents.SGroupsList.add(sgm);
				ifSuccess = true;

			}
			return ifSuccess;
		}
	}

	// 根据群号组号取所有成员ID,Name
	public static class LoadSGroupMembers implements Callable<Boolean> {

		private String gID, cID, cName;
		private int sgID;
		private boolean ifSuccess = false;

		public LoadSGroupMembers(String gID, int sgID) {
			this.gID = gID;
			this.sgID = sgID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadSGroupMembers.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();

			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Customers c = new Customers();
				cID = json_data.getString("cID");
				cName = json_data.getString("cName");
				c.setcID(cID);
				c.setcName(cName);
				Currents.sgCustomersList.add(c);
				ifSuccess = true;

			}
			return ifSuccess;
		}
	}

	// 新建群
	public static class CreateNewGroup implements Callable<Boolean> {
		String gID, gName, gPasswd;
		int gMin, gMax, ifOpen;

		public CreateNewGroup(String gID, String gName, String gPasswd,
				int gMin, int gMax, int ifopen) {
			this.gID = gID;
			this.gName = gName;
			this.gPasswd = gPasswd;
			this.gMin = gMin;
			this.gMax = gMax;
			this.ifOpen = 1;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("gName", gName));
			nameValuePairs.add(new BasicNameValuePair("gPasswd", gPasswd));
			nameValuePairs.add(new BasicNameValuePair("gMin", gMin + ""));
			nameValuePairs.add(new BasicNameValuePair("gMax", gMax + ""));
			nameValuePairs.add(new BasicNameValuePair("ifOpen", ifOpen + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/createNewGroup.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 加入群
	public static class JoinInGroup implements Callable<Boolean> {
		private String gID, cID;
		private int ifOwner;

		public JoinInGroup(String gID, String cID, int ifOwner) {
			this.gID = gID;
			this.cID = cID;
			this.ifOwner = ifOwner;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("ifOwner", ifOwner + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/joinInGroup.php");

				/* 设置请求的数据 */

				// /////////////注意修改

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 加入组
	public static class JoinInSGroup implements Callable<Boolean> {
		String cID, gID;
		int sgID;
		int ifsgOwner;

		public JoinInSGroup(int sgID, String cID, String gID, int ifsgOwner) {
			this.cID = cID;
			this.sgID = sgID;
			this.gID = gID;
			this.ifsgOwner = ifsgOwner;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("ifsgOwner", ifsgOwner
					+ ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/joinInSGroup.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 根据群ID加载群信息
	public static class LoadGroupInfo implements Callable<Boolean> {

		private String gID, gName, gPasswd;
		private int gMin, gMax;
		private boolean ifSuccess = false;

		public LoadGroupInfo(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadGroupInfro.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);
			gName = json_data.getString("gName");
			gPasswd = json_data.getString("gPasswd");
			gMin = json_data.getInt("gMin");
			gMax = json_data.getInt("gMax");
			SearchGrpActivity.grouplist.add(gID + gName);
			Currents.TSGroupform.setgID(gID);
			Currents.TSGroupform.setgName(gName);
			Currents.TSGroupform.setgPasswd(gPasswd);
			Currents.TSGroupform.setgMin(gMin);
			Currents.TSGroupform.setgMax(gMax);
			ifSuccess = true;

			return ifSuccess;
		}
	}

	// 判断是否为群主
	public static class SelectIfOwnerOFGroup implements Callable<Boolean> {
		private String gID, cID;
		private int ifOwner;
		private boolean ifIS = false;

		public SelectIfOwnerOFGroup(String gID, String cID) {
			this.cID = cID;
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/selectIfOwnerOFGroup.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);// iso-8859-1
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// result= new String(result.getBytes(),"gb2312");
			// System.out.println("3526565656"+result);
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			// for (int i = 0; i < jArray.length(); i++) {
			JSONObject json_data = jArray.getJSONObject(0);
			ifOwner = json_data.getInt("ifOwner");
			if (ifOwner == 1) {
				ifIS = true;
			} else {
				ifIS = false;
			}

			return ifIS;

		}
	}

	// 群主删除群
	public static class deleteGroup implements Callable<Boolean> {
		String gID;

		public deleteGroup(String gID) {
			this.gID = gID;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/deleteGroup.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);
				JSONObject json_data;
				json_data = jArray.getJSONObject(0);
				return json_data.getBoolean("result");

			}

			catch (JSONException e) {
				return null;
			}
		}
	}

	// 群成员退出群
	public static class ExitFromGroup implements Callable<Boolean> {
		String gID, cID;

		public ExitFromGroup(String gID, String cID) {
			this.gID = gID;
			this.cID = cID;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/exitFromGroup.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);
				JSONObject json_data;
				json_data = jArray.getJSONObject(0);
				return json_data.getBoolean("result");

			}

			catch (JSONException e) {
				return null;
			}
		}
	}

	// 根据群ID返回所有成员
	public static class LoadGroupMembers implements Callable<Boolean> {

		private String gID, cID, cName;
		private boolean ifSuccess = false;

		public LoadGroupMembers(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadGroupMembers.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				cID = json_data.getString("cID");
				cName = json_data.getString("cName");
				Customers c = new Customers();
				c.setcID(cID);
				c.setcName(cName);
				Currents.gCustomersList.add(c);
				ifSuccess = true;

			}
			return ifSuccess;
		}
	}

	// 根据cID获取个人信息
	public static class LoadPersonalInfo implements Callable<Boolean> {

		private String cID;
		private boolean ifSuccess = false;

		public LoadPersonalInfo(String cID) {
			this.cID = cID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/loadPersonalInfo.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);

			JSONObject json_data = jArray.getJSONObject(0);
			Currents.currentOtherCustomer
					.setcName(json_data.getString("cName"));
			Currents.currentOtherCustomer.setcClass(json_data
					.getString("cClass"));
			Currents.currentOtherCustomer.setcSex(json_data.getInt("cSex"));

			ifSuccess = true;

			return ifSuccess;
		}
	}

	// 判断用户是否在当前组内
	public static class IfExitInSGroup implements Callable<Boolean> {
		String gID, cID;
		int sgID;

		public IfExitInSGroup(String gID, String cID, int sgID) {
			this.sgID = sgID;
			this.gID = gID;
			this.cID = cID;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/ifExitInSGroup.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);
				JSONObject json_data;
				json_data = jArray.getJSONObject(0);
				return json_data.getBoolean("result");

			}

			catch (JSONException e) {
				return null;
			}
		}
	}

	// 判断是否为组长
	public static class SelectIfsgOwnerOfSGroup implements Callable<Boolean> {
		private String gID, cID;
		private int ifsgOwner, sgID;
		private boolean ifIS;

		public SelectIfsgOwnerOfSGroup(String gID, String cID, int sgID) {
			this.cID = cID;
			this.gID = gID;
			this.sgID = sgID;
		}

		public Boolean call() throws Exception {
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));

			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/selectIfsgOwnerOfSGroup.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);// iso-8859-1
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// result= new String(result.getBytes(),"gb2312");
			// System.out.println("3526565656"+result);
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			// for (int i = 0; i < jArray.length(); i++) {
			JSONObject json_data = jArray.getJSONObject(0);
			ifsgOwner = json_data.getInt("ifsgOwner");

			if (ifsgOwner == 1) {
				ifIS = true;
			} else {
				ifIS = false;
			}
			return ifIS;

			// }

		}
	}

	// 退出组
	public static class ExitFromSGroup implements Callable<Boolean> {
		String gID, cID;
		int sgID;

		public ExitFromSGroup(String gID, String cID, int sgID) {
			this.sgID = sgID;
			this.gID = gID;
			this.cID = cID;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/exitFromSGroup.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);
				JSONObject json_data;
				json_data = jArray.getJSONObject(0);
				return json_data.getBoolean("result");

			}

			catch (JSONException e) {
				return null;
			}
		}
	}

	// 解散组
	public static class deleteSGroup implements Callable<Boolean> {
		String gID;
		int sgID;

		public deleteSGroup(String gID, int sgID) {
			this.sgID = sgID;
			this.gID = gID;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("sgID", sgID + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/deleteSGroup.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);
				JSONObject json_data;
				json_data = jArray.getJSONObject(0);
				return json_data.getBoolean("result");

			}

			catch (JSONException e) {
				return null;
			}
		}
	}

	// 创建组
	public static class CreateNewSGroup implements Callable<Boolean> {
		String gID, cID;
		int ifsgOwner;

		public CreateNewSGroup(String gID, String cID, int ifsgOwner) {
			this.gID = gID;
			this.cID = cID;
			this.ifsgOwner = ifsgOwner;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("ifsgOwner", ifsgOwner
					+ ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/createNewSGroup.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */
				// //////改为UTF-8
				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 修改个人姓名

	public static class UpdateName implements Callable<Boolean> {
		String cID, cName;

		public UpdateName(String cID, String cName) {
			this.cID = cID;
			this.cName = cName;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("cName", cName));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/updateName.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 修改班级

	public static class UpdatecClass implements Callable<Boolean> {
		String cID, cClass;

		public UpdatecClass(String cID, String cClass) {
			this.cID = cID;
			this.cClass = cClass;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("cClass", cClass));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/updateClass.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 修改性别

	public static class UpdatecSex implements Callable<Boolean> {
		String cID;
		int cSex;

		public UpdatecSex(String cID, int cSex) {
			this.cID = cID;
			this.cSex = cSex;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("cSex", cSex + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/updatecSex.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 修改密码
	public static class UpdatePassword implements Callable<Boolean> {
		String cID, cPasswd;

		public UpdatePassword(String cID, String cPasswd) {
			this.cID = cID;
			this.cPasswd = cPasswd;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("cID", cID));
			nameValuePairs.add(new BasicNameValuePair("cPasswd", cPasswd));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/updatePassword.php");

				/* 设置请求的数据 */
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "utf-8"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 获取当前群的状态
	public static class SelectIfOpen implements Callable<Boolean> {
		private String gID;
		private int ifOpen;
		private boolean ifIS;

		public SelectIfOpen(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));

			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/selectIfOpen.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);// iso-8859-1
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// result= new String(result.getBytes(),"gb2312");
			// System.out.println("3526565656"+result);
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			// for (int i = 0; i < jArray.length(); i++) {
			JSONObject json_data = jArray.getJSONObject(0);
			ifOpen = json_data.getInt("ifOpen");
			if (ifOpen == 1) {
				ifIS = true;
			} else {
				ifIS = false;
			}
			return ifIS;

			// }

		}
	}

	// 修改当前群状态
	public static class UpdateIfOpen implements Callable<Boolean> {
		String gID;
		int ifOpen;

		public UpdateIfOpen(String gID, int ifOpen) {
			this.gID = gID;
			this.ifOpen = ifOpen;
		}

		public Boolean call() {
			String result = "";
			/* 将要发送的数据封包 */
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			nameValuePairs.add(new BasicNameValuePair("ifOpen", ifOpen + ""));

			InputStream is = null;

			try {

				/* 创建一个HttpClient的一个对象 */

				HttpClient httpclient = new DefaultHttpClient();

				/* 创建一个HttpPost的对象 */

				HttpPost httppost = new HttpPost(
						"http://"+IP+"/updateIfOpen.php");

				/* 设置请求的数据 */

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				/* 创建HttpResponse对象 */

				HttpResponse response = httpclient.execute(httppost);

				/* 获取这次回应的消息实体 */

				HttpEntity entity = response.getEntity();

				/* 创建一个指向对象实体的数据流 */

				is = entity.getContent();

			} catch (Exception e) {

				System.out.println("Connectiong Error");

			}

			// convert response to string

			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);

				StringBuilder sb = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {

					sb.append(line + "/n");

				}

				is.close();
				result = sb.toString();

			} catch (Exception e) {

			}

			try {

				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);

				if (json_data.getBoolean("result"))
					return true;
				else
					return false;

			}

			catch (JSONException e) {
				return false;
			}
		}
	}

	// 获取还未分组的成员名单
	public static class SelectNotInSGroup implements Callable<Boolean> {

		private String gID;
		private boolean ifSuccess = false;

		public SelectNotInSGroup(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));
			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/selectNotInSGroup.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			// 从字符串result创建一个JSONArray对象
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				// 这里获取的字段是gID，cID
				bGroupMember bg = new bGroupMember();
				bg.setcID(json_data.getString("cID"));
				// bg.setgID(json_data.getString("gID"));
				Currents.NotInSGroupCustomer.add(bg);
				ifSuccess = true;
			}
			return ifSuccess;
		}
	}

	public static class SelectMaxSGroupID implements Callable<Boolean> {
		private String gID;
		private int sgID;
		private boolean i = true;

		public SelectMaxSGroupID(String gID) {
			this.gID = gID;
		}

		public Boolean call() throws Exception {
			String result = "";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gID", gID));

			InputStream is = null;
			// 创建一个HttpClient的一个对象

			HttpClient httpclient = new DefaultHttpClient();
			// 创建一个HttpPost的对象
			HttpPost httppost = new HttpPost(
					"http://"+IP+"/selectMaxSGroupID.php");
			// 设置请求的数据
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			// 创建HttpResponse对象
			HttpResponse response = httpclient.execute(httppost);
			// 获取这次回应的消息实体
			HttpEntity entity = response.getEntity();
			// 创建一个指向对象实体的数据流
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);// iso-8859-1
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
			is.close();
			result = sb.toString();
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);
			sgID = json_data.getInt("MAX(sgID)");
			Currents.maxsgid = sgID;

			return i;

		}
	}

	// 获取当前系统时间，群号
	public String getGroupID() {
		String gID = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		gID = df.format(new Date());
		return gID;
	}

}
