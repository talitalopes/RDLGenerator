package br.ufrj.cos.prisma.BPMNAPI;

public class ImportModelBPMN extends BPMNCode {

	public static final String IMPORT_MODEL = "ImportModel";
	public static final String P_MODEL_URL = "MODEL_URL";
	
	public ImportModelBPMN(String modelURL) {
		
		this.name = IMPORT_MODEL;
		this.addParam(P_MODEL_URL, modelURL);
	
	}



}
