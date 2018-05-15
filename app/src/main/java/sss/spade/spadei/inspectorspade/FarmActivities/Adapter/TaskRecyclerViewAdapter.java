package sss.spade.spadei.inspectorspade.FarmActivities.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sss.spade.spadei.inspectorspade.DataHandler.DataHandler;
import sss.spade.spadei.inspectorspade.FarmActivities.FarmActionReplyActivity;
import sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter.Taskdata;
import sss.spade.spadei.inspectorspade.LandingActivity.models.Farmers;
import sss.spade.spadei.inspectorspade.R;

/**
 * Created by user on 15/12/17.
 */

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>  {
    private String[] mDataSet;
    Context context;
    private List<Taskdata> taskdatum;
    String fromActivity="";
    String isdone="";
    String farm_num;
    public TaskRecyclerViewAdapter(List<Taskdata> taskdatum, Context context, String fromActivity){
        this.context=context;
        this.taskdatum=taskdatum;
        this.fromActivity=fromActivity;
        Log.d("Data:","TaskRecyclerAdapter :"+taskdatum);
    }

    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        farm_num= DataHandler.newInstance().getFarmnum();

        if(fromActivity.equals("Verify")){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vieew_for_tasklist_activity,parent,false);

        }else{
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_text_view,parent,false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(final ViewHolder holder, int position){
        Taskdata taskdata=taskdatum.get(position);
        if(taskdata.getTaskTitle()!=null){
            holder.tvtitle.setText(taskdata.getTaskTitle());
        }


        if(taskdata.getImgBgLink()!=null){
            String image_link = taskdata.getImgBgLink();
            String isDone = taskdata.getIsDone();
            Log.e("isDone",isDone);
            String taskDate = taskdata.getTaskDate();
            String date_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String verification_date=taskdata.getVerification_date();
            if(fromActivity.equals("Verify")){

            }else {
                if (isDone.equals("Y")) {
                    if(verification_date.equals("0000-00-00")){
                        holder.verify_activity_butt.setVisibility(View.VISIBLE);
                    }else{
                        holder.verify_activity_butt.setVisibility(View.GONE);
                    }
                    holder.taskInnerLinearLayout.setBackgroundColor(Color.parseColor("#3CB371"));
                } else {
                    if (taskDate.compareTo(date_today) < 0) {
                        holder.taskInnerLinearLayout.setBackgroundColor(Color.parseColor("#FF6347"));
                    } else {
                        holder.taskInnerLinearLayout.setBackgroundColor(Color.parseColor("#79CDCD"));
                    }
                }
            }
            Uri uri = Uri.parse(image_link);
//            Context context = holder.task_recycler_single_view_relative.getContext();
//            Picasso.with(context).load(uri).into(holder.taskLinearLayout);
            Picasso.with(holder.imageView.getContext()).load(uri).into(holder.imageView);
           // holder.imageView.setAlpha(200);
           /* Picasso.with(context).load(uri).into(new Target() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.task_recycler_single_view_relative.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });*/
//            ImageView imageView;
//            imageView = null;

//            Drawable drawable =imageView.getDrawable();
//            holder.taskLinearLayout.setBackground(drawable);
        }
        if(taskdata.getTaskDate()!=null){
            holder.tvdate.setText(taskdata.getTaskDate());
        }
        if(taskdata.getTaskDescription()!=null){
            holder.tvdescripion.setText(taskdata.getTaskDescription());
        }
        if(taskdata.getTaskId()!=null){

        }

        if(fromActivity.equals("Verify")) {

           /* if (taskdata.getIsDone() != null) {
                if(taskdata.getIsDone().equals("Y")){
                    isdone="Done";

                }
                else
                {
                    isdone="Not Done";
                }
                holder.tvstatus.setText(isdone);
                holder.tvstatuslabel.setText("Task Status : ");

            }*/
        }



       // holder.mtextView.setText(mDataSet[position]);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public int getItemCount(){
        return taskdatum.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvtitle,tvdescripion,tvdate,tvstatus,tvstatuslabel;
        public ToggleButton mtoggleButton;
        public LinearLayout taskLinearLayout,taskInnerLinearLayout;
        public RelativeLayout task_recycler_single_view_relative;
        public ImageView imageView;
        public ImageView farm_latest_image;
        public Button verify_activity_butt;

        public ViewHolder(View v){
            super(v);
            tvtitle = (TextView)v.findViewById(R.id.task_title);
            tvdescripion=(TextView)v.findViewById(R.id.task_description);
            tvdate=(TextView)v.findViewById(R.id.task_date);
           // tvstatus=(TextView)v.findViewById(R.id.task_status);
           // tvstatuslabel=(TextView)v.findViewById(R.id.tvstatuslabel);

            taskInnerLinearLayout = (LinearLayout)v.findViewById(R.id.recycler_inner_linear);
//            taskLinearLayout=(LinearLayout)v.findViewById(R.id.task_linear_layout);
            //task_recycler_single_view_relative=(RelativeLayout)v.findViewById(R.id.recycler_single_view_relative);
            imageView=(ImageView)v.findViewById(R.id.recycler_single_view_relative);
            verify_activity_butt=(Button)v.findViewById(R.id.verify_activity_ins);

            verify_activity_butt.setVisibility(View.GONE);

            verify_activity_butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  =   getAdapterPosition();
                    final Taskdata taskdata = taskdatum.get(position);
                    Intent intent = new Intent(context, FarmActionReplyActivity.class);
                    intent.putExtra("id",taskdata.getFarm_dwork_num());
                    intent.putExtra("task_date",taskdata.getTaskDate());
                    intent.putExtra("fromactivity","task_list");
                    intent.putExtra("farm_num",farm_num);
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();                }
            });

            taskInnerLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  =   getAdapterPosition();
                    final Taskdata taskdata = taskdatum.get(position);
                    Intent intent = new Intent(context, FarmActionReplyActivity.class);
                    intent.putExtra("id",taskdata.getFarm_dwork_num());
                    intent.putExtra("task_date",taskdata.getTaskDate());
                    intent.putExtra("fromactivity","pager");
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).finish();
                }
            });


           // farm_latest_image=(ImageView)di

           /* mtoggleButton = (ToggleButton)v.findViewById(R.id.toggleButton2);
            mtoggleButton.setText("Task Not Done");
            mtoggleButton.setTextOff("Task Not Done");
            mtoggleButton.setTextOn("Task is Done");*/

        }
    }


}
