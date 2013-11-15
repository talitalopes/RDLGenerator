package br.ufrj.cos.prisma.BPMNAPI;

public class NewMethodBPMN extends BPMNCode {


	
	public static final String NEW_METHOD= "NewMethod";
	public static final String P_CLASS_NAME = "CLASS_NAME";
	public static final String P_METHOD_NAME = "METHOD_NAME";

	public NewMethodBPMN(String className, String methodName) {
		this.name = NEW_METHOD;
		this.addParam(P_CLASS_NAME, className);
		this.addParam(P_METHOD_NAME, methodName);
	}

}
