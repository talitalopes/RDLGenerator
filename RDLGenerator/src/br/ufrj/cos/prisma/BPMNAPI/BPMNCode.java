package br.ufrj.cos.prisma.BPMNAPI;

import java.util.ArrayList;

public abstract class BPMNCode {
	
	
	// instruction address
	String address;
	// address next instruction
	String addressNext;
	// if the instruction modifies the natural sequential order
	protected boolean modifiesSequence = false;

	protected String name = "";
	// both arrays form a tuple <name,value> at the i-th position
	protected ArrayList<String> parName = new ArrayList<String>();
	protected ArrayList<String> parValue = new ArrayList<String>();

	protected void addParam(String name, String value){
		parName.add(name);
		parValue.add(value);
	}
	
	protected void setParam(int index, String name, String value){
		parName.set(index,name);
		parValue.set(index,value);
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String addr) {
		this.address = addr;
	}

	public String getAddressNext() {
		return addressNext;
	}

	public void setAddressNext(String next) {
		this.addressNext = next;
	}
	
	public String getCode() {
		StringBuffer sb = new StringBuffer();
	
		sb.append("<!-- nodes -->");			
		sb.append("<task id=\"_" + this.getAddress() + "\" name=\""+ name+"\" tns:taskName=\""+ name+"\">");
		
		
		sb.append("<ioSpecification>");	
		
		for (int i=0; i <= parName.size()-1 ; i++){
			sb.append("    <dataInput id=\"_" + this.getAddress() + parName.get(i)+ "Input\" name=\""+ parName.get(i)+ "\" />");	
		}
	        
		sb.append("    <inputSet>");
	
		for (int i=0; i <= parName.size()-1 ; i++){
			sb.append("      <dataInputRefs>_" + this.getAddress() + parName.get(i)+ "Input</dataInputRefs>");	
		}
		sb.append("    </inputSet>");	
	        
		sb.append("    <outputSet>");	
		sb.append("    </outputSet>");	
	        
		sb.append("  </ioSpecification>");	
	      
			
	
		for (int i=0; i <= parName.size()-1 ; i++){
			sb.append("  <dataInputAssociation>");
			sb.append("    <targetRef>"+ "_" + this.getAddress() + parName.get(i)+ "Input</targetRef>");	
			sb.append("    <assignment>");	
			sb.append("      <from xsi:type=\"tFormalExpression\">" + parValue.get(i)+ "</from>");	
			sb.append("      <to xsi:type=\"tFormalExpression\">"+"_" + this.getAddress() +""+ parName.get(i)+ "Input</to>");	
			sb.append("    </assignment>");
			sb.append("  </dataInputAssociation>");	
		}
	
		sb.append("</task>");
	
		sb.append("<!-- connections -->");
		sb.append(this.getTransitions());			
		return sb.toString();
	}
	
	public String getTransitions() {
		return "<sequenceFlow id=\"_" + this.getAddress() +"to_" + this.getAddressNext() +"\" sourceRef=\"_"+ this.getAddress()+ "\" targetRef=\"_" + this.getAddressNext() +"\" />";
	}

}

///MAYBE I CAN USE XML TO CONFIGURE.
//String xmlData = 
//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//"<action name=\"Assignment\">" + 
//"<params>"+
//"	<param name=\"VARNAME\" value='\""+varName+"\"'></param>"+
//"	<param name=\"EXPR\" value='\""+expr+"\"'></param>"+
//"</params>" + 
//"</action>";
//this.buildInfo(xmlData);

//protected void buildInfo(String xmlData) {	
//
//	try {
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		InputSource is = new InputSource();
//		is.setCharacterStream(new StringReader(xmlData));
//
//		Document doc = db.parse(is);
//		NodeList nodes = doc.getElementsByTagName("action");
//		Element elName = (Element)nodes.item(0);
//		// Getting the Name
//		this.name = elName.getAttribute("name");
//		// Getting the Parameters
//		nodes = doc.getElementsByTagName("params");
//		Element elParams = (Element) nodes.item(0);
//		// Getting
//		nodes = elParams.getElementsByTagName("param");
//		for (int i = 0; i < nodes.getLength(); i++) {
//	           Element elParam = (Element) nodes.item(i);
//	           System.out.println(" NAME -> " + elParam.getAttribute("name"));
//	           parName.add(elParam.getAttribute("name"));
//	           parValue.add(elParam.getAttribute("value"));
//		}
//
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//
//}

