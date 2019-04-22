package cn.xvkang.dto.studentbjrank;

import java.util.List;

public class StudentScoreRankVo {
	
	private String examcode;
	private String name;
	private List<SubjectScoreRank> subjectScoreRanks;
	
	
	
	
	

	
	public List<SubjectScoreRank> getSubjectScoreRanks() {
		return subjectScoreRanks;
	}






	public void setSubjectScoreRanks(List<SubjectScoreRank> subjectScoreRanks) {
		this.subjectScoreRanks = subjectScoreRanks;
	}






	public String getExamcode() {
		return examcode;
	}






	public void setExamcode(String examcode) {
		this.examcode = examcode;
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public static class SubjectScoreRank{
		private int score;
		private int xiaoRank;
		private int banRank;
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		public int getXiaoRank() {
			return xiaoRank;
		}
		public void setXiaoRank(int xiaoRank) {
			this.xiaoRank = xiaoRank;
		}
		public int getBanRank() {
			return banRank;
		}
		public void setBanRank(int banRank) {
			this.banRank = banRank;
		}
		
		
	}
}
