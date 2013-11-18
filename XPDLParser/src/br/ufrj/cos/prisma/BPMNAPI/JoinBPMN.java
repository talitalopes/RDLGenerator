package br.ufrj.cos.prisma.BPMNAPI;

public class JoinBPMN extends BPMNCode {

	public JoinBPMN(int size) {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		StringBuffer sb = new StringBuffer();	
			
		sb.append("<exclusiveGateway id=\"_" + this.getAddress() + "\" name=\"Gateway\" gatewayDirection=\"Converging\" />");
		sb.append(this.getTransitions());			
		return sb.toString();
	}

}
