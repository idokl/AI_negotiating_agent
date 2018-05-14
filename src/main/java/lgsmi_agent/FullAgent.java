package lgsmi_agent;

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.EndNegotiation;
import negotiator.actions.Inform;
import negotiator.actions.Offer;
import negotiator.bidding.BidDetails;
import negotiator.boaframework.*;

public class FullAgent extends BOAagent {
 
	private BidsManager bidsManager;
	
	@Override
    public void agentSetup () {
    	this.bidsManager = new BidsManager(this.negotiationSession);
    	
        OpponentModel om = new OpponentModel_lgsmi();
        OMStrategy oms = new OMStrategy_lgsmi();
        OfferingStrategy offering = new OfferingStrategy_lgsmi(bidsManager);
        AcceptanceStrategy ac = new AcceptanceStrategy_lgsmi();
        om.init(this.negotiationSession,om.getParameters());
        oms.init(this.negotiationSession,om,oms.getParameters());
        try {
            offering.init(this.negotiationSession, om, oms, offering.getParameters());
        } catch (Exception e){
            System.out.println("offering exception:");
            System.out.println(e.getMessage());
        }
        try {
            ac.init(this.negotiationSession,offering,om,ac.getParameters());
        } catch (Exception e){
            System.out.println("acceptance exception:");
            System.out.println(e.getMessage());
        }

        setDecoupledComponents(ac, offering, om, oms);
    }
    @Override
    public String getName () {
        return "FullAgent" ;
    }

    
//	/**
//	 * Chooses an action to perform.
//	 * 
//	 * @return Action the agent performs
//	 */
//	@Override
//	public Action chooseAction() {
//
//		BidDetails bid;
//
//		// if our history is empty, then make an opening bid
//		if (negotiationSession.getOwnBidHistory().getHistory().isEmpty()) {
//			bid = offeringStrategy.determineOpeningBid();
//		} else {
//			// else make a normal bid
//			bid = offeringStrategy.determineNextBid();
//			if (offeringStrategy.isEndNegotiation()) {
//				return new EndNegotiation(getAgentID());
//			}
//		}
//
//		// if the offering strategy made a mistake and didn't set a bid: accept
//		if (bid == null) {
//			System.out.println("Error in code, null bid was given");
//			return new Accept(getAgentID(), oppBid);
//		} else {
//			offeringStrategy.setNextBid(bid);
//		}
//
//		// check if the opponent bid should be accepted
//		Actions decision = Actions.Reject;
//		if (!negotiationSession.getOpponentBidHistory().getHistory().isEmpty()) {
//			decision = acceptConditions.determineAcceptability();
//		}
//
//		// check if the agent decided to break off the negotiation
//		if (decision.equals(Actions.Break)) {
//			System.out.println("send EndNegotiation");
//			return new EndNegotiation(getAgentID());
//		}
//		// if agent does not accept, it offers the counter bid
//		if (decision.equals(Actions.Reject)) {
//			negotiationSession.getOwnBidHistory().add(bid);
//			return new Offer(getAgentID(), bid.getBid());
//		} else {
//			return new Accept(getAgentID(), oppBid);
//		}
//	}
    
	/**
	 * Stores the actions made by a partner. First, it stores the bid in the
	 * history, then updates the opponent model.
	 * 
	 * @param opponentAction
	 *            by opponent in current turn
	 */
    @Override
	public void ReceiveMessage(Action opponentAction) {
    	super.ReceiveMessage(opponentAction);
    	// 1. if the opponent made a bid
		if (opponentAction instanceof Offer) {
			Bid oppBid = ((Offer) opponentAction).getBid();
			// 2. store the opponent's trace
			try {
				this.bidsManager.reportNewBid(oppBid); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (opponentAction instanceof Accept ) {
			Bid oppBid = ((Accept) opponentAction).getBid();
			this.bidsManager.reportAcceptanceOfBid(oppBid);
		}
	}


    
}
