package br.ufrj.cos.prisma.BPMNAPI;

public class AddCodeBPMN extends BPMNCode {
	
	public static final String ADD_CODE = "AddCode";
	public static final String P_CLASS_NAME = "CLASS_NAME";
	public static final String P_OPER_NAME = "OPER_NAME";
	public static final String P_CODE = "CODE";
	
	public AddCodeBPMN(String className, String operName, String code) {
		this.name = ADD_CODE;
		this.addParam(P_CLASS_NAME, className);
		this.addParam(P_OPER_NAME, operName);
		this.addParam(P_CODE, code);
		
	}

}
