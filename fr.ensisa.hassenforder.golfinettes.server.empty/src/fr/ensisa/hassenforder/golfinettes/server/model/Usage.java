package fr.ensisa.hassenforder.golfinettes.server.model;

public class Usage {
	
	public enum BorrowerEvent {
		FREE,
		BORROW,
		RETURN,
	}
	
	public enum UsageState {
		STEADY_NORMAL, STEADY_LONG,
		MOVING_NORMAL, MOVING_BACK,
	}
	
	private long borrower;
	private BorrowerEvent event;
	private UsageState usage;
	private int detail;
	private int alarm;
	
	public Usage(long borrower, BorrowerEvent event, UsageState usage, int detail, int alarm) {
		super();
		this.borrower = borrower;
		this.event = event;
		this.usage = usage;
		this.detail = detail;
		this.alarm = alarm;
	}

	public long getBorrower() {
		return borrower;
	}

	public BorrowerEvent getEvent() {
		return event;
	}

	public UsageState getUsage() {
		return usage;
	}

	public int getDetail() {
		return detail;
	}

	public int getAlarm() {
		return alarm;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" Usage ");
		switch (event) {
		case FREE :		builder.append("--"); break;
		case BORROW :	builder.append("->"); builder.append(borrower); break;
		case RETURN :	builder.append(borrower); builder.append("<-"); break;
		}
		switch (usage) {
		case STEADY_NORMAL:	builder.append("SN"); break;
		case STEADY_LONG:	builder.append("SL"); break;
		case MOVING_NORMAL:	builder.append("MN"); break;
		case MOVING_BACK:	builder.append("ML"); break;
		}
		if (detail != 0) {
			builder.append("%");
			builder.append(detail);
		}
		if (alarm != 0) {
			builder.append(" !");
			builder.append(alarm);
			builder.append("!");
		}
		return builder.toString();
	}
	
}

