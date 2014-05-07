package br.ufrj.cos.prisma.BPMNAPI;

public class EndIfBPMN extends BPMNCode {

	
	public static final String END_IF = "EndIf";
	public static final String P_CURRENT_ADDRESS = "CURRENT_ADDRESS";
	public static final String P_GOTO_ADDRESS = "GOTO_ADDRESS";	

	
	public EndIfBPMN(int iiAddress, int gotoAddress) {
		
		this.name = END_IF;
		this.addParam(P_CURRENT_ADDRESS,String.valueOf(iiAddress));
		this.addParam(P_GOTO_ADDRESS,String.valueOf(gotoAddress));
		
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();	
			
		sb.append("<exclusiveGateway id=\"_" + this.getAddress() + "\" name=\"Gateway\" gatewayDirection=\"Converging\" />");
		sb.append(this.getTransitions());			
		return sb.toString();
	}
	
	public String getTransitions() {
		
		StringBuffer sb = new StringBuffer();
		
		
		sb.append("<sequenceFlow id=\"_" + this.getAddress() +"to_" + this.getAddressNext() +"\" sourceRef=\"_"+ this.getAddress()+ "\" targetRef=\"_" + this.getAddressNext() +"\" />");
		
		//sb.append("<sequenceFlow id=\"_" + this.getAddress() +"Testto_" + this.getEscapeAddress() +"\" sourceRef=\"_"+ this.getAddress()+ "Test\" targetRef=\"_" + this.getEscapeAddress() +"\" />");
		
		return sb.toString();
				
	}
	

	

}
