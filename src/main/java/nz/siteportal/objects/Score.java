package nz.siteportal.objects;

import java.util.List;

public class Score {

	private String group_id;
	private String group_text;
	private String score_id;
	private String score_text;
	private String score_type;
	private List<String> possibleAnswers;
	private String answer;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_text() {
		return group_text;
	}

	public void setGroup_text(String group_text) {
		this.group_text = group_text;
	}

	public void setScore_id(String id) {
		this.score_id = id;
	}

	public String getScore_id() {
		return score_id;
	}

	public String getScore_text() {
		return score_text;
	}

	public void setScore_text(String score_text) {
		this.score_text = score_text;
	}

	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<String> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Score)) {
			return false;
		}
		Score that = (Score) o;
		if (that.getScore_id() != null && this.getScore_id() != null) {
			System.out.println("Got IDs of that : " + that.getScore_id() + " and this : " + this.getScore_id());
			return that.getScore_id().equals(this.getScore_id());
		}
		if (that.getScore_text() != null && this.getScore_text() != null) {
			System.out.println("Both Score Objects have Question Texts...");
			System.out.printf("Question -> that : [%s], this : [%s]\n", that.getScore_text(), this.getScore_text());
			if (!that.getScore_text().equals(this.getScore_text())) {
				System.out.println("The questions don't match, so they aren't equal.");
				return false;
			}
			if (that.getGroup_id() != null && this.getGroup_id() != null) {
				System.out.println("Both the Score Objects have Group IDs...");
				System.out.printf("Group ID -> that : [%s], this : [%s]\n", that.getGroup_id(), this.getGroup_id());
				return that.getGroup_id().equals(this.getGroup_id());
			}
			if (that.getGroup_text() != null && this.getGroup_text() != null) {
				System.out.println("Both the Score Objects have Group Texts...");
				System.out.printf("Group Texts -> that : [%s], this : [%s]\n", that.getGroup_text(),
						this.getGroup_text());
				return that.getGroup_text().equals(this.getGroup_text());
			}
			System.out.println("None of the attributes match. Returning false");
		}
		return false;
	}

	public String getScore_type() {
		return score_type;
	}

	public void setScore_type(String score_type) {
		this.score_type = score_type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Score : ");
		sb.append("\n\t");
		sb.append("Group : " + correct(getGroup_text()));
		sb.append("\n\t");
		sb.append("Group ID : " + correct(getGroup_id()));
		sb.append("\n\t");
		sb.append("Question : " + correct(getScore_text()));
		sb.append("\n\t");
		sb.append("Question ID : " + correct(getScore_id()));
		sb.append("\n\t");
		sb.append("Answers could be : " + ((possibleAnswers == null) ? "[NOT SPECIFIED]" : possibleAnswers.toString()));
		sb.append("\n\t");
		sb.append("Answers type : " + correct(getScore_type()));
		return sb.toString();
	}

	private String correct(String text) {
		return (text == null) ? "[NOT SPECIFIED]" : text;
	}

}
