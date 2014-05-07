package br.ufrj.cos.prisma.BPMNAPI;

public class ClassExtensionBPMN extends BPMNCode {
	
	public static final String CLASS_EXTENSION = "ClassExtension";
	public static final String P_SUPER_NAME = "SUPER_NAME";
	public static final String P_SUB_NAME = "SUB_NAME";
	public static final String P_SUB_PACKNAME= "SUB_PNAME";

	public ClassExtensionBPMN(String superName, String subPackName, String subName) {
		
		this.name = CLASS_EXTENSION;
		this.addParam(P_SUPER_NAME, superName);
		this.addParam(P_SUB_PACKNAME, subPackName);
		this.addParam(P_SUB_NAME, subName);
		
	}

}
