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

    
	/**
	 * Chooses an action to perform.
	 * 
	 * @return Action the agent performs
	 */
	@Override
	public Action chooseAction() {
		this.bidsManager.ourTurnHasArrived();
		return super.chooseAction();
	}
    
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
