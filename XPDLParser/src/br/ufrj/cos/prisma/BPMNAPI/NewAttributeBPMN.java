package br.ufrj.cos.prisma.BPMNAPI;

public class NewAttributeBPMN extends BPMNCode {
	
	public static final String NEW_ATTRIBUTE = "NewAttribute";
	public static final String P_CLASS_NAME = "CLASS_NAME";
	public static final String P_ATTRIB_NAME = "ATTRIB_NAME";
	public static final String P_TYPE_NAME= "TYPE_NAME";

	public NewAttributeBPMN(String className, String attribName, String typeName) {
		
		this.name = NEW_ATTRIBUTE;
		this.addParam(P_CLASS_NAME, className);
		this.addParam(P_ATTRIB_NAME, attribName);
		this.addParam(P_TYPE_NAME, typeName);		
		
	}

}
