package cn.xvkang.dto.bjsubidpercent;

import java.util.Map;

public class BjSubidPercent {
	
	
	private double sportAvgScore;
	private String bjid;
	private double avg;
	private double max;
	private int bjStudentCount;
	private Map<String,BjPercent> levelBjPercentMap;
	
	public double getSportAvgScore() {
		return sportAvgScore;
	}
	public void setSportAvgScore(double sportAvgScore) {
		this.sportAvgScore = sportAvgScore;
	}
	public String getBjid() {
		return bjid;
	}
	public void setBjid(String bjid) {
		this.bjid = bjid;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public int getBjStudentCount() {
		return bjStudentCount;
	}
	public void setBjStudentCount(int bjStudentCount) {
		this.bjStudentCount = bjStudentCount;
	}
	public Map<String, BjPercent> getLevelBjPercentMap() {
		return levelBjPercentMap;
	}
	public void setLevelBjPercentMap(Map<String, BjPercent> levelBjPercentMap) {
		this.levelBjPercentMap = levelBjPercentMap;
	}
	

}
