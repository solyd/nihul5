package org.nihul5.other;

import java.util.List;

public class Consensus {
	public int eventid;
	public String desc;
	public Status status;
	public int id;
	public int nvotesForChange;
	
	public List<String> users;
	
	public static enum Status {
		ACCEPTED,
		NOT_ACCEPTED
	}
	
	public static Status getConsensusStatus(String str) {
		if (str.equals(Status.ACCEPTED.toString()))
			return Status.ACCEPTED;
		if (str.equals(Status.NOT_ACCEPTED.toString()))
			return Status.NOT_ACCEPTED;
		
		return null;
	}
	
	public Consensus(int id, String desc, Status status) {
		this.id = id;
		this.desc = desc;
		this.status = status;
	}
	
	public Consensus() {}
}
