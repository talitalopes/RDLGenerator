package br.ufrj.cos.prisma.BPMNAPI;

public class NewRealizationBPMN extends BPMNCode {

	public static final String NEW_REALIZATION = "NewRealization";
	public static final String P_CLASS_NAME = "CLASS_NAME";
	public static final String P_INTERFACE_NAME = "INTERFACE_NAME";

	public NewRealizationBPMN(String className, String interfName) {
		this.name = NEW_REALIZATION;
		this.addParam(P_CLASS_NAME, className);
		this.addParam(P_INTERFACE_NAME, interfName);
	}

}
