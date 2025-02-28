package dicomp.debateit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import SharedModels.Debate;

/**
 * Created by Yasin on 25.04.2018.
 */

public class CustomListViewAdapter extends ArrayAdapter<Debate> {
    private final LayoutInflater inflater;
    private int lastPushed;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<Debate> debates;

    public CustomListViewAdapter(Context context, ArrayList<Debate> debates) {
        super(context,0, debates);
        this.context = context;
        this.debates = debates;
        inflater = LayoutInflater.from(context);
    }
    public int getCount(){
        return debates.size();
    }

    public Debate getDebate(int pos){
        return debates.get(pos);
    }

    public int getDebateID(int pos){
        return debates.get(pos).getDebateID();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        holder.replayDebate.setVisibility(View.VISIBLE);
        holder.viewDebate.setVisibility(View.VISIBLE);
        long viewId = view.getId();
        view.setTag(holder);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_items, parent, false);

            holder.bar = (ProgressBar) convertView.findViewById(R.id.bar);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.replayDebate = convertView.findViewById(R.id.replay);
            holder.viewDebate = convertView.findViewById(R.id.view);
            holder.viewDebate.setVisibility(View.GONE);
            holder.replayDebate.setVisibility(View.GONE);
            convertView.setTag(holder);
        }
        else{
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }
        Debate debate = debates.get(position);
        if(debate != null){
            System.out.println("debate != null'DAAAAAAAAAAAAAAAAA");
            holder.bar.setProgress(debate.getProgress());
            holder.text.setText(debate.menuDisplay());
        }
        return convertView;
    }
    //View Holder Pattern for better performance
    private class ViewHolder {
        TextView text;
        ProgressBar bar;
        Button replayDebate;
        Button viewDebate;
    }
}

