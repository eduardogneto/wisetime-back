package com.wisetime.wisetime.DTO.punch;

public class PunchSummaryDTO {
    private String date;
    public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getEntryCount() {
		return entryCount;
	}
	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}
	public int getExitCount() {
		return exitCount;
	}
	public void setExitCount(int exitCount) {
		this.exitCount = exitCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private int entryCount;
    private int exitCount;
    private String status;

    // Getters and Setters
}
