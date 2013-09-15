package com.greedygame.apprunner;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.greedygame.aap.Record;

public class ScoresActivity extends Activity{

	ArrayList<Record> scores;
	ScoreAdapter adapter;
	
	JSONParser parser;
	ScoreoidWrapper scoreoidObject;
	
	static public Activity activity;
	private String response = null;

	//Player's Rank
	long startPosition = 1;
	long myPosition = -1;
	long myScore = -1;
	String myusername = null;
	String myname = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        scores=new ArrayList<Record>();
        
        activity = this;
                
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	response = getIntent().getStringExtra("respone");
        	myPosition = getIntent().getLongExtra("rank", -1);
        	myScore = getIntent().getLongExtra("score", -1);
        	myusername = getIntent().getStringExtra("username");
        	myname = getIntent().getStringExtra("name");
        }
                
        ListView lv=(ListView) findViewById(R.id.scoreList);
        adapter=new ScoreAdapter(getApplicationContext(), scores);
        lv.setAdapter(adapter);
        parser=new JSONParser();
        scoreoidObject=new ScoreoidWrapper();
        
             
		if(response!=null && response!=""){
			List<Record> tempScores= parser.ParseScores(response);
			if(tempScores!=null && tempScores.size()>0){
				scores.clear();
				boolean foundme = false;
				for(Record s:tempScores){
					if(foundme == false && s.getUsername().equals(myusername)){						
						s.setName(myname, "(me)");
						foundme = true;
					}
					s.setRank((int)startPosition);
					scores.add(s);
					startPosition++;
				}
				if(foundme == false){
					Record r = new Record();
					r.setUsername(myusername);
					r.setName(myname, "(me)");
					r.setScore(myScore);
					r.setRank(myPosition);
					scores.add(r);
					
				}
				//To notify the list view to show the updated scores
				adapter.notifyDataSetChanged();
			}
		}
	}
		

	
	class ScoreAdapter extends BaseAdapter{
		List<Record> scores;

		LayoutInflater inflater;
		
		public ScoreAdapter(Context ctx,List<Record> scores){
			inflater = (LayoutInflater) ctx
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.scores=scores;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			if (scores != null)
				return scores.size();
			return 0;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (scores != null && position >= 0 && position < getCount())
				return scores.get(position);
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (scores != null && position >= 0 && position < getCount())
				return position;
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Record sc = scores.get(position);
			if (view == null) {
				view = inflater.inflate(R.layout.list_score_item, parent, false);
			}
			TextView pTitle = (TextView) view.findViewById(R.id.playerTitle);
			TextView pScore = (TextView) view.findViewById(R.id.playerScore);
			TextView pPos=(TextView) view.findViewById(R.id.playerPosition);
			if (pTitle != null){
				String n = sc.getUsername();
				if(sc.getFirstName() != null && sc.getFirstName() != ""){
					n = sc.getFirstName()+" "+sc.getLasttName();
				}
				pTitle.setText(n);				
			}
			if (pScore != null)
				pScore.setText((long)sc.getScore()+"");
			if(pPos!=null)
				pPos.setText(""+String.valueOf(sc.getRank())+".");

			return view;
		}

		
	}
}
