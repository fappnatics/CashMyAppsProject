package com.cashmyapps.core.cashmyappsproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {
private Activity activity;
ArrayList<Item_object> arrayitems;

	public NavigationAdapter(Activity activity, ArrayList<Item_object> list_array){
		super();
		this.activity = activity;
		this.arrayitems =list_array;
	}
		public Object getItem(int position){
			return arrayitems.get(position);
		}
	public int getCount(){
		return arrayitems.size();
	}

	public long getItemId(int position){
		return position;
	}
	
	public static class Fila
	{
		TextView titulo_itm;
		ImageView icono;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		Fila view;
		LayoutInflater inflator = activity.getLayoutInflater();
		if(convertView == null)
		{
			view = new Fila();
			Item_object itm = arrayitems.get(position);
			convertView = inflator.inflate(R.layout.itm, null);
			view.titulo_itm=(TextView)convertView.findViewById(R.id.title_item);
			view.titulo_itm.setText(itm.getTitulo());
			view.icono = (ImageView) convertView.findViewById(R.id.icon);
			view.icono.setImageResource(itm.getIcono());
		}
		else
		{
			view = (Fila)convertView.getTag();
		}
		
		return convertView;
	}
}
