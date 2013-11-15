package br.ufrj.cos.prisma.BPMNAPI;

public class BeginThenBPMN extends BPMNCode {

	
	public static final String BEGIN_IF = "BeginIf";
	public static final String P_CURRENT_ADDRESS = "CURRENT_ADDRESS";
	public static final String P_ESCAPE_ADDRESS = "ESCAPE_ADDRESS";	
	public static final String P_CONDITION_ID = "CONDITION_ID";
	
	public BeginThenBPMN(int curAddress, int escapeAddress, String condID) {
		this.name = BEGIN_IF;
		this.addParam(P_CURRENT_ADDRESS,String.valueOf(curAddress));
		this.addParam(P_ESCAPE_ADDRESS,String.valueOf(escapeAddress));
		this.addParam(P_CONDITION_ID,condID);
	}

	public void setAddress(String addr) {
		super.setAddress(addr);
		this.setParam(0, P_CURRENT_ADDRESS, addr);
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
	
//		sb.append("<!-- nodes -->");			
//		sb.append("<task id=\"_" + this.getAddress() + "\" name=\""+ name+"\" tns:taskName=\""+ name+"\">");
//		
//		
//		sb.append("<ioSpecification>");	
//		
//		for (int i=0; i <= parName.size()-1 ; i++){
//			sb.append("    <dataInput id=\"_" + this.getAddress() + parName.get(i)+ "Input\" name=\""+ parName.get(i)+ "\" />");	
//		}
//	        
//		sb.append("    <inputSet>");
//	
//		for (int i=0; i <= parName.size()-1 ; i++){
//			sb.append("      <dataInputRefs>_" + this.getAddress() + parName.get(i)+ "Input</dataInputRefs>");	
//		}
//		sb.append("    </inputSet>");	
//	        
//		sb.append("    <outputSet>");	
//		sb.append("    </outputSet>");	
//	        
//		sb.append("  </ioSpecification>");	
//	      
//			
//	
//		for (int i=0; i <= parName.size()-1 ; i++){
//			sb.append("  <dataInputAssociation>");
//			sb.append("    <targetRef>"+ "_" + this.getAddress() + parName.get(i)+ "Input</targetRef>");	
//			sb.append("    <assignment>");	
//			sb.append("      <from xsi:type=\"tFormalExpression\">" + parValue.get(i)+ "</from>");	
//			sb.append("      <to xsi:type=\"tFormalExpression\">"+"_" + this.getAddress() +""+ parName.get(i)+ "Input</to>");	
//			sb.append("    </assignment>");
//			sb.append("  </dataInputAssociation>");	
//		}
//	
//		sb.append("</task>");
		
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


