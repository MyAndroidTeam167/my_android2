package sss.spade.spadei.inspectorspade.Notification.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


import org.w3c.dom.Text;

import sss.spade.spadei.inspectorspade.DatabaseHandler.Beans.GetProfile;
import sss.spade.spadei.inspectorspade.R;

/**
 * Created by hp on 9/13/2017.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private List<GetProfile> getProfiles;
    private Typeface tfnormal,tfbold,tfitalic;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title, idfornoti,notidescription,notitime;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.notimsgtitle);
            //genre = (TextView) view.findViewById(R.id.genre);
           // idfornoti = (TextView) view.findViewById(R.id.idfornoti);
            notidescription=(TextView)view.findViewById(R.id.notimsgdescription);
            notitime=(TextView)view.findViewById(R.id.notitime);


        }

    }

    public ProfileAdapter(List<GetProfile> getProfiles,Typeface tfnormal,Typeface tfbold,Typeface tfitalic)
    {this.getProfiles=getProfiles;
     this.tfnormal=tfnormal;
     this.tfbold=tfbold;
     this.tfitalic=tfitalic;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_list_row, parent, false);
        MyViewHolder layoutViewHolder=new MyViewHolder(itemView);

return layoutViewHolder;    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GetProfile getProfile = getProfiles.get(position);

        holder.title.setText(getProfile.getNotification());
        holder.title.setTypeface(tfnormal);
        holder.notidescription.setText(getProfile.getNoticationdescription());
        holder.notidescription.setTypeface(tfnormal);
        holder.notitime.setText(getProfile.getNotidate());
        holder.notitime.setTypeface(tfnormal);

//        Log.e("Description :",getProfile.getNoticationdescription());
        //  holder.idfornoti.setText("");
    }

    @Override
    public int getItemCount() {
        return getProfiles.size();
    }




}
