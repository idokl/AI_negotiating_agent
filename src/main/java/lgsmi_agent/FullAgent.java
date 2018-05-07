package lgsmi_agent;

import negotiator.boaframework.*;

public class FullAgent extends BOAagent {
    @Override
    public void agentSetup () {
        OpponentModel om = new OpponentModel_lgsmi();
        OMStrategy oms = new OMStrategy_lgsmi();
        OfferingStrategy offering = new OfferingStrategy_lgsmi();
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

}
