package com.greedygame.aap;

import com.greedygame.Constant;

public class Record {
	private String userName, firstName, lastName;
	private float score;
	private long rank;
	private boolean connected;
	private int coin;
	private int block;
	private int level;
	public Record(String username,String firstName, String lastName,int score){
		this.userName = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.score = score;
		this.connected = false;

		this.block = (int) Constant.BLOCK_TO_CLEAR_L1;
		
	}
	
	public Record(){
		this.userName = null;
		this.score = 0;
		this.connected = false;
		this.block = (int) Constant.BLOCK_TO_CLEAR_L1;
		this.level = 0;
	}
	
	
	public void setUsername(String u){
		this.userName = u;
	}
	
	public String getUsername(){
		return this.userName;
	}
	
	public void setRank(long s){
		this.rank = s;
	}
	
	public long getRank(){
		return this.rank;
	}
	
	public void resetScore(){
		this.score = 0;
	}
	
	public void reset(){
		this.score = 0;
		this.block = (int) Constant.BLOCK_TO_CLEAR_L1;
		this.coin = 0;
	}
	
	public float getScore(){
		return this.score;
	}
	
	public float incScore(float x){
		this.score += x;
		return this.score;
	}
	
	public int decBlock(){
		if(this.block>0){
			this.block -= 1;
		}
		return this.block;
	}
	
	public int getBlock(){
		return this.block;
	}
	
	public void resetBlock(){
		this.block = (int) Constant.BLOCK_TO_CLEAR_L1;
	}
	
	public float percentBlock(){
		return 1-this.block/Constant.BLOCK_TO_CLEAR_L1;
	}
	
	public float percentCoin(){
		return this.coin/10f;
	}
	
	public void incCoin(){
		this.coin += 1;
		if(this.coin > 10){
			this.coin = 0;
		}
	}
	
	public int getcoin(){
		return this.coin;
	}
	
	public void setConnected(boolean s ){
		this.connected = s;
	}
	
	public boolean isConnected(){
		if(this.userName!=null && this.connected){
			return true;
		}
		return false;
	}

	public void setName(String first, String last) {
		this.firstName = first;
		this.lastName = last;		
	}
	
	public String getFirstName(){
		return this.firstName;
	}

	public String getLasttName(){
		return this.lastName;
	}

	public void setScore(long myScore) {
		this.score = myScore;
		
	}

	public void setLevel(int level) {
		System.out.println("s "+level);
		this.level = level;		
	}
	
	public int getLevel(){
		return this.level;
	}
	
	
}
