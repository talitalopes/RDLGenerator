package br.ufrj.cos.prisma.BPMNAPI;

public class BeginLoopBPMN extends BPMNCode {
	
	public static final String BEGIN_LOOP = "BeginLoop";
	public static final String P_CURRENT_ADDRESS = "CURRENT_ADDRESS";

	public BeginLoopBPMN(int iiAddress) {
		this.name = BEGIN_LOOP;
		this.addParam(P_CURRENT_ADDRESS,String.valueOf(iiAddress));
	}

	
	public String getCode() {
		StringBuffer sb = new StringBuffer();	
			
		sb.append("<exclusiveGateway id=\"_" + this.getAddress() + "\" name=\"Gateway\" gatewayDirection=\"Converging\" />");
		sb.append(this.getTransitions());			
		return sb.toString();
	}

}
