package org.nihul5.other;

public class Consensus {
	public final String desc;
	public final Status status;
	public final int id;
	
	public static enum Status {
		ACCEPTED {
			@Override
			public String toString() {
				return "ACCEPTED";
			}
		},
		NOT_ACCEPTED {
			@Override
			public String toString() {
				return "NOT_ACCPTED";
			}
		}
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
}
