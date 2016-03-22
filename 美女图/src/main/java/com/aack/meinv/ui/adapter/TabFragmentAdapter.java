package com.aack.meinv.ui.adapter;

import java.util.Iterator;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TabFragmentAdapter {

	
	FragmentManager fm;
	FragmentTransaction ft;
	List<Fragment> list;
	int curTab;
	
	int viewId;
	
	public TabFragmentAdapter(FragmentManager fm,List<Fragment> list,int viewId){
		this.fm=fm;
		this.list=list;
		ft=fm.beginTransaction();
		this.viewId=viewId;
		//Ĭ����ʾ��һ����Ƭ
		ft.add(viewId, list.get(curTab));
		ft.commit();
	}
	
	public void showFragment(int which){
		ft=obtainFragmentTransaction(which);
		Fragment fragment=list.get(which);
		getCurFragment().onPause();
		if(fragment.isAdded()){
			fragment.onResume();
		}else{
			ft.add(viewId, list.get(which));
		}

		for (int i = 0; i < list.size(); i++) {
			if(i==which){
				ft.show(list.get(i));
			}else{
				ft.hide(list.get(i));
			}
		}
		
		ft.commit();
		curTab=which;
	}
	
	public FragmentTransaction obtainFragmentTransaction(int index){
		 FragmentTransaction ft = fm.beginTransaction();

	        return ft;
	}
	
	
	public Fragment getCurFragment(){
		return list.get(curTab);
	}
	
	
}
