package br.ufrj.cos.prisma.BPMNAPI;

public class MethodExtensionBPMN extends BPMNCode {
	
	public static final String METHOD_EXTENSION = "MethodExtension";
	public static final String P_SUPER_NAME = "SUPER_NAME";
	public static final String P_SUB_NAME = "SUB_NAME";
	public static final String P_METH_NAME= "MethodName";

	public MethodExtensionBPMN(String superName, String subName, String methName) {
		this.name = METHOD_EXTENSION;
		this.addParam(P_SUPER_NAME, superName);
		this.addParam(P_SUB_NAME, subName);
		this.addParam(P_METH_NAME, methName);

	}

}
