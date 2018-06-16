package com.leanza.xpresseat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leanza.xpresseat.Data.Route;
import com.leanza.xpresseat.R;

import java.util.ArrayList;

/**
 * Created by leanza on 21/04/2018.
 */

public class RouteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Route> routes;

    public RouteAdapter(Context context, ArrayList<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int i) {
        return routes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = View.inflate(context, android.R.layout.simple_list_item_1, null);
        }

        ImageView images = view.findViewById(R.id.routeImgView);
        TextView name = view.findViewById(R.id.routeNameTV);
        Route route = routes.get(i);

        //images.setImageResource(route.getTest());
        name.setText(route.getName());

        return view;
    }
}
