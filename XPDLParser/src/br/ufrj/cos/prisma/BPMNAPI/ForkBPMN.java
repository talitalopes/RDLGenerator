package br.ufrj.cos.prisma.BPMNAPI;

public class ForkBPMN extends BPMNCode {

	public ForkBPMN(int size) {
		// TODO Auto-generated constructor stub
	}

	public void addTransitionAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	public String getCode() {
		StringBuffer sb = new StringBuffer();	
			
		sb.append("<exclusiveGateway id=\"_" + this.getAddress() + "\" name=\"Gateway\" gatewayDirection=\"Diverging\" />");
		sb.append(this.getTransitions());			
		return sb.toString();
	}

}
