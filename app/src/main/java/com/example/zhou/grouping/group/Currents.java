package com.example.zhou.grouping.group;

import com.example.zhou.grouping.Bean.Customers;
import com.example.zhou.grouping.Bean.Groups;
import com.example.zhou.grouping.Bean.bGroupMember;
import com.example.zhou.grouping.Bean.sGroupMember;
import com.example.zhou.grouping.Bean.sGroups;

import java.util.ArrayList;

public class Currents {

	public static boolean test;
//	public static boolean ifgroupowner = false;// 是否为当前群的群主
	public static boolean ifsgroupowner = false;// 是否为当前组的组长
	public static boolean ifsgroupMemberr = false;// 是否在组内
	public static boolean ifjoinedsgroup = false;// 是否已加入某个组
	public static boolean groupstate = false;// 当前群状态，1true，0false

	public static int maxsgid = 0;

	public static Customers currentCustomer = new Customers();// 当前的用户
	public static ArrayList<Customers> gCustomersList = new ArrayList<Customers>();// 当前进入的群的成员名单列表
	public static ArrayList<Customers> sgCustomersList = new ArrayList<Customers>();// 当前进入的组的成员名单列表
	public static ArrayList<bGroupMember> NotInSGroupCustomer = new ArrayList<bGroupMember>();// 群被群主关闭后仍未分组的成员名单

	public static Customers currentOtherCustomer = new Customers();// 当前查看的用户信息

	public static Groups currentGroup = new Groups();// 当前进入的群
	public static ArrayList<Groups> GroupsList = new ArrayList<Groups>();// 加入的群
	public static Groups TSGroupform = new Groups();// 搜索群和查看群信息时的暂存群信息

	public static sGroups currentSGroup = new sGroups();// 当前进入的组
	public static ArrayList<sGroupMember> SGroupsList = new ArrayList<sGroupMember>();// 当前群的所有组

}
