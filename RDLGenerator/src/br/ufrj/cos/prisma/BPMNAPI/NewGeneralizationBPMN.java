package br.ufrj.cos.prisma.BPMNAPI;

public class NewGeneralizationBPMN extends BPMNCode {
	
	public static final String NEW_GENERALIZATION = "NewGeneralization";
	public static final String P_SUB_CLASS_NAME = "SUB_CLASS_NAME";
	public static final String P_SUPER_CLASS_NAME = "SUPER_CLASS_NAME";

	public NewGeneralizationBPMN(String subClassName, String superClassName) {
		this.name = NEW_GENERALIZATION;
		this.addParam(P_SUB_CLASS_NAME, subClassName);
		this.addParam(P_SUPER_CLASS_NAME, superClassName);
	}

}
