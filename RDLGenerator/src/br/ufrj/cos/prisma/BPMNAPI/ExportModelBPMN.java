package br.ufrj.cos.prisma.BPMNAPI;

public class ExportModelBPMN extends BPMNCode {

	public static final String EXPORT_MODEL = "ExportModel";
	public static final String P_MODEL_URL = "MODEL_URL";
	
	public ExportModelBPMN(String modelURL) {

		this.name = EXPORT_MODEL;
		this.addParam(P_MODEL_URL, modelURL);
	
	}
	

}
