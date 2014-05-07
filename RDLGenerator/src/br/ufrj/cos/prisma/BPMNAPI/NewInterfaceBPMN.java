package br.ufrj.cos.prisma.BPMNAPI;

public class NewInterfaceBPMN extends BPMNCode {

	
	public static final String NEW_INTERFACE= "NewInterface";
	public static final String P_PACKAGE_NAME = "PACKAGE_NAME";
	public static final String P_INTERF_NAME = "INTERF_NAME";

	public NewInterfaceBPMN(String packageName, String interfName) {
		this.name = NEW_INTERFACE;
		this.addParam(P_PACKAGE_NAME, packageName);
		this.addParam(P_INTERF_NAME, interfName);
	}

}
