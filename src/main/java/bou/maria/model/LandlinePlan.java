package bou.maria.model;

public class LandlinePlan extends Plan {

	private int speedLine;
	private LineType lineType;

	public LandlinePlan(String companyName, int freeCalls, double planCost, int speedLine, LineType lineType) {

		super(companyName, freeCalls, planCost);
		this.speedLine = speedLine;
		this.lineType = lineType;
	}

	public void setSpeedLine(int speedLine) {

		this.speedLine = speedLine;
	}

	public int getSpeedLine() {

		return speedLine;
	}

	public void setLineType(LineType lineType) {

		this.lineType = lineType;
	}

	public LineType getLineType() {

		return lineType;
	}
}
