package sss.spade.spadei.inspectorspade.FarmActivities.Adapter;

/**
 * Created by user on 3/2/18.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.List;

import sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter.GetSet;
import sss.spade.spadei.inspectorspade.R;
import sss.spade.spadei.inspectorspade.Utiltiy.Utilities;

/**
 * Created by user on 2/2/18.
 */

public class CustomAdapter extends BaseAdapter implements MediaPlayer.OnCompletionListener{
    Context context;
    String countryList[];
    int flags[];
    LayoutInflater inflter;
    private Handler mHandler = new Handler();
    public List<GetSet> listData;
    String byInspector ="";
    String byFarmer ="";
    String byAdmin = "";
     public MediaPlayer[] player;
    private Utilities utils;
   public String adapter_audio_reply;
    String audioReply;




    public CustomAdapter(Context applicationContext, List<GetSet> listData) {
        this.context = applicationContext;
        this.listData = listData;
        inflter = (LayoutInflater.from(applicationContext));
        inflter = (LayoutInflater.from(applicationContext));
        MediaPlayer player[]=new MediaPlayer[listData.size()];
        this.player=player;
        for(int i=0;i<listData.size();i++){
            player[i]=new MediaPlayer();
        }
        this.adapter_audio_reply=audioReply;    }

    @Override
    public int getCount() {
        Log.e("checkArray", String.valueOf(listData.size()));

        return listData.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        GetSet getSet = listData.get(i);
//        view = inflter.inflate(R.layout.list_item_chat);
        view = inflter.inflate(R.layout.list_item_chat, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.listItemLL);
/*
        final TextView comment = (TextView) view.findViewById(R.id.label);
*/
        final TextView comment = (TextView) view.findViewById(R.id.label);

        final TextView dateTime = (TextView) view.findViewById(R.id.tvDateTime);
        TextView commentBy = (TextView)view.findViewById(R.id.commentBy);
        ImageView leftImage = (ImageView)view.findViewById(R.id.imageViewLeft);
        ImageView rightImage = (ImageView)view.findViewById(R.id.imageViewRight);
        ImageView chat_image=(ImageView)view.findViewById(R.id.chat_image);
        LinearLayout card_chat=(LinearLayout) view.findViewById(R.id.card_view_chat);
        final ImageButton play_img=(ImageButton) view.findViewById(R.id.playButtonn);
        RelativeLayout audio_rel_lay=(RelativeLayout)view.findViewById(R.id.audio_rel_lay);
       // Button butt_chat_stop=(Button)view.findViewById(R.id.butt_chat_stop);
        final TextView songCurrentDurationLabel;
        final TextView songTotalDurationLabel;
        final SeekBar seekBar_player;

        seekBar_player=(SeekBar)view.findViewById(R.id.seek_player);
        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.soil2)
                .error(R.drawable.soil2);

       /*
        */final String commentMsg = getSet.getMessage();
        final String date = getSet.getDate();
        byFarmer=getSet.getByFarmer();
        byInspector = getSet.getByInspector();
        Log.e("checkArray",byFarmer+" "+byInspector);
        byAdmin = getSet.getByAdmin();
        String msgType=getSet.getMsgType();
         audioReply=getSet.getAudioReply();



       /* btcommen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment.setText(etcomment.getText().toString());
                dateTime.setText(formattedDate);
            }
        });*/

        if(msgType.equals("1")) {
            if(!audioReply.equals("")&&!audioReply.equals("null")){
                audio_rel_lay.setVisibility(View.VISIBLE);
                Log.e("String Audio Reply :",audioReply);
                utils = new Utilities();
                //seekBar_player.setOnSeekBarChangeListener(this); // Important
                player[i].setOnCompletionListener(this); // Important
                player[i].reset();
                Uri uri[] = new Uri[listData.size()];
                uri[i]=Uri.parse(audioReply.trim());
                seekBar_player.setProgress(0);
                seekBar_player.setMax(100);
                //player = new MediaPlayer();
                player[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    player[i].setDataSource(context, uri[i]);
                    player[i].prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            final Runnable mUpdateTimeTask = new Runnable() {
                public void run() {
                    if(player[i]!=null) {
                        try {
                            long totalDuration = player[i].getDuration();
                            long currentDuration = player[i].getCurrentPosition();

                            // Displaying Total Duration time
                            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                            // Displaying time completed playing
                            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

                            // Updating progress bar
                            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                            //Log.d("Progress", ""+progress);
                            seekBar_player.setProgress(progress);

                            // Running this thread after 100 milliseconds
                            mHandler.postDelayed(this, 100);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };




            if (byFarmer.equals("Y")) {
                linearLayout.setBackgroundResource(R.drawable.rounded_corner_2);
                commentBy.setText("Farmer");
                leftImage.setVisibility(View.VISIBLE);
                rightImage.setVisibility(View.GONE);

                if(audioReply.trim().equals("")){
                }
                else{
                    audio_rel_lay.setVisibility(View.VISIBLE);

                    seekBar_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            mHandler.postDelayed(mUpdateTimeTask, 100);

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            mHandler.removeCallbacks(mUpdateTimeTask);

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            mHandler.removeCallbacks(mUpdateTimeTask);
                            int totalDuration = player[i].getDuration();
                            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                            // forward or backward to certain seconds
                            player[i].seekTo(currentPosition);

                            // update timer progress again
                            //updateProgressBar(i);
                            mHandler.postDelayed(mUpdateTimeTask, 100);

                        }
                    });

                    play_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(player[i].isPlaying()){
                                if(player[i]!=null){
                                    player[i].pause();
                                    // Changing button image to play button
                                    play_img.setImageResource(R.drawable.btn_play);
                                }
                            }else{
                                // Resume song
                                if(player[i]!=null){

                                    player[i].start();
                                    //updateProgressBar(i);
                                    mHandler.postDelayed(mUpdateTimeTask, 100);


                                    // Changing button image to pause button
                                    play_img.setImageResource(R.drawable.btn_pause);
                                }
                            }





                        }
                    });


                }
                rightImage.setVisibility(View.INVISIBLE);

            }  else if (byInspector.equals("Y")) {
                linearLayout.setBackgroundResource(R.drawable.rounded_corner);
                commentBy.setVisibility(View.GONE);
                leftImage.setVisibility(View.GONE);
                rightImage.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setBackgroundResource(R.drawable.rounded_corner_2);
                commentBy.setText("Admin");
                rightImage.setVisibility(View.GONE);
            }
            comment.setText(commentMsg);
            dateTime.setText(date);
        }else{
            Uri uriprofile = Uri.parse(commentMsg.trim());
            chat_image.setVisibility(View.VISIBLE);
            card_chat.setVisibility(View.VISIBLE);
            Glide.with(context).load(uriprofile).apply(options).into(chat_image);
            comment.setVisibility(View.GONE);
            leftImage.setVisibility(View.VISIBLE);
            rightImage.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
