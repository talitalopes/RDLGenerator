package br.ufrj.cos.prisma.BPMNAPI;

public class DeclarationBPMN extends BPMNCode {
	
	public static final String DECLARATION= "Declaration";
	public static final String P_TYPE_NAME= "TYPE_NAME";
	public static final String P_VAR_NAME= "VAR_NAME";

	public DeclarationBPMN(String typeName, String varName) {
		this.name = DECLARATION;
		this.addParam(P_TYPE_NAME, typeName);
		this.addParam(P_VAR_NAME, varName);
	}

}
