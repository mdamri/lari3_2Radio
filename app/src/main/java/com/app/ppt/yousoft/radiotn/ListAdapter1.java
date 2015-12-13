package com.app.ppt.yousoft.radiotn;
/**
 * Created by omar reg on 21/10/2015.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListAdapter1 extends BaseAdapter implements Filterable
{
    Context context;
    ArrayList<RowItem> rowItems;//hethi feha les elts mte3 el liste el kol
    CustomFilter filter;
    ArrayList<RowItem> filterList;//hethi feha les elts elli bech ya93dou ba3d el filtrage

    public ListAdapter1(Context context, ArrayList<RowItem> items)
    {

        this.context = context;
        this.rowItems = items;
        this.filterList= items;

    }

    @Override
    public Filter getFilter()
    {
        if(filter==null)
        {
           filter=new CustomFilter();
        }
        return filter;
    }

    private class ViewHolder
    {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(com.app.ppt.yousoft.radiotn.R.layout.listelement, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(com.app.ppt.yousoft.radiotn.R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(com.app.ppt.yousoft.radiotn.R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(com.app.ppt.yousoft.radiotn.R.id.icon);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txtDesc.setText(rowItems.get(position).getDesc());
        holder.txtTitle.setText(rowItems.get(position).getTitle());
        holder.imageView.setImageResource(rowItems.get(position).getImageId());

        return convertView;
    }

    @Override
    public int getCount()
    {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return rowItems.indexOf(getItem(position));
    }




    class CustomFilter extends Filter//hetha el filter ennajmou n7ottouh 7atta fel main mais 7attitou houni bech me tetbalbazech
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results=new FilterResults();
            if (constraint != null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();
                ArrayList<RowItem> filters=new ArrayList<RowItem>();
                for(int i=0;i<filterList.size();i++ )
                {
                    if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                    {
                        RowItem r=new RowItem(filterList.get(i).getImageId(),filterList.get(i).getTitle(),filterList.get(i).getDesc());
                        filters.add(r);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }
            else
            {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rowItems=(ArrayList<RowItem>) results.values;
            notifyDataSetChanged();

        }
    }

}
