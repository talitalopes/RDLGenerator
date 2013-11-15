package br.ufrj.cos.prisma.BPMNAPI;

public class EndLoopBPMN extends BPMNCode {

	public static final String END_LOOP = "EndLoop";
	public static final String P_CURRENT_ADDRESS = "CURRENT_ADDRESS";
	public static final String P_ESCAPE_ADDRESS = "ESCAPE_ADDRESS";
	public static final String P_COND = "CONDITION";
	
	public EndLoopBPMN(int iiAddress, int escapeAddress, String condID) {
		this.name = END_LOOP;
		this.addParam(P_CURRENT_ADDRESS,String.valueOf(iiAddress));
		this.addParam(P_ESCAPE_ADDRESS,String.valueOf(escapeAddress));
		this.addParam(P_COND, condID);

	}

	public void setEscapeAddress(String escapeLabel) {
		// changing the value of the escapeAddress
		this.setParam(1, P_ESCAPE_ADDRESS, escapeLabel);
		
	}
	
	public String getEscapeAddress(){
		return parValue.get(1);
	}
	
	public String getConditionID(){
		return parValue.get(2);
	}

	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exclusiveGateway id=\"_" + this.getAddress() + "\" name=\"Gateway\" gatewayDirection=\"Diverging\" />");
		sb.append(this.getTransitions());			
		return sb.toString();
	}
	
	public String getTransitions() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("<sequenceFlow id=\"_" + this.getAddress() +"to_" + this.getAddressNext() +"\" sourceRef=\"_"+ this.getAddress()+ "\" targetRef=\"_" + this.getAddressNext() +"\" >");
		sb.append("		<conditionExpression xsi:type=\"tFormalExpression\" language=\"http://www.java.com/java\" >return "+ this.getConditionID()+";</conditionExpression>");
		sb.append("</sequenceFlow>");
		
		sb.append("<sequenceFlow id=\"_" + this.getAddress() +"to_" + this.getEscapeAddress() +"\" sourceRef=\"_"+ this.getAddress()+ "\" targetRef=\"_" + this.getEscapeAddress() +"\" >");
		sb.append("		<conditionExpression xsi:type=\"tFormalExpression\" language=\"http://www.java.com/java\" >return !"+ this.getConditionID()+";</conditionExpression>");
		sb.append("</sequenceFlow>");		
		return sb.toString();
				
	}
}
