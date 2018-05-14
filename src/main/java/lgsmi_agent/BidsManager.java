package lgsmi_agent;

import negotiator.Bid;
import negotiator.boaframework.NegotiationSession;

public class BidsManager {

	private NegotiationSession negotiationSession;
	
	private Bid maximalOppBid = null;
	private double utilityOfMaximalOppBid = 0;
	
	private Bid maximalBidThatWasAccepted = null;
	private double utilityOfMaximalBidThatWasAccepted = 0;
	
	public BidsManager(NegotiationSession negotiationSession) {
		this.negotiationSession = negotiationSession;
	}
	
	public void reportNewBid(Bid oppBid) {
		//get our undiscounted utility that corresponds to this bid
		double utility = negotiationSession.getUtilitySpace().getUtility(oppBid);
		if (utility > this.utilityOfMaximalOppBid) {
			this.maximalOppBid = oppBid;
			this.utilityOfMaximalOppBid = utility;
		}
	}
	
	public void reportAcceptanceOfBid(Bid oppBid) {
		//get our undiscounted utility that corresponds to this bid
		double utility = negotiationSession.getUtilitySpace().getUtility(oppBid);
		if (utility > this.utilityOfMaximalBidThatWasAccepted) {
			this.maximalBidThatWasAccepted = oppBid;
			this.utilityOfMaximalBidThatWasAccepted = utility;
		}
	}

}
