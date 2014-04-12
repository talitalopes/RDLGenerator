package br.ufrj.cos.prisma.util;

public class Constants {

	public static final CharSequence CLASS_EXTENSION_PREFIX = "ClassExtension"; //"CLASSEXTENSION";
	
	public static final boolean VERBOSE = false;

	//------------
	// XPDL tags |
	//------------

	public static String XPDL_ACTIVITY_TAG = "Activity";
	public static String XPDL_TRANSITION_TAG = "Transition";
//	public static final String XPDLFile = "input/5-commits-log.xpdl";
	public static final String XPDLFile = "input/graphiti1.xpdl";
//	public static final String XPDLFile = "input/test.xpdl.xml";

	//------------------
	// Causal net tags |
	//------------------
	
	public static String CNET_ACTIVITY_TAG = "node";
	public static String CNET_TRANSITION_TAG = "arc";
	public static String CNET_TRANSITION_SOURCE_TAG = "source";
	public static String CNET_TRANSITION_TARGET_TAG = "target";
	public static final String CNETFile = "input/causal-net.flex";
	
	//----------------
	// RDL Constants |
	//----------------
	public static final String RDL_OUTPUT = "output/graphiti1-rdl.xml";
	public static final String MODEL_URL = "models/gef.uml";
	public static final String MODEL_OUTPUT_URL = "models/app.uml";
	public static final String PACKAGE_NAME = "?";
	public static final String PACKAGE_VAR_NAME = "appPack";
	public static final String TEMP_VAR_ASSIGNMENT = "$0000RDLTempVar";

}
