<?xml version="1.0" encoding="UTF-8"?>
<cnet>
   <net type="http://www.processmining.org" id="aC-Net of Framework process" />
   <name>aC-Net of Framework process</name>
   <node id="0" isInvisible="false">
      <name>END</name>
   </node>
   <node id="1" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature</name>
   </node>
   <node id="2" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature</name>
   </node>
   <node id="3" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.AbstractAddFeature</name>
   </node>
   <node id="4" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.DefaultReconnectionFeature</name>
   </node>
   <node id="5" isInvisible="false">
      <name>START</name>
   </node>
   <node id="6" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.AbstractLayoutFeature</name>
   </node>
   <node id="7" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature</name>
   </node>
   <node id="8" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.DefaultRemoveFeature</name>
   </node>
   <node id="9" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.tb.DefaultToolBehaviorProvider</name>
   </node>
   <node id="10" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.custom.AbstractCustomFeature</name>
   </node>
   <node id="11" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.AbstractUpdateFeature</name>
   </node>
   <node id="12" isInvisible="false">
      <name>CLASS_EXTENSION_org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature</name>
   </node>
   <startTaskNode id="5" />
   <endTaskNode id="0" />
   <inputNode id="0">
      <inputSet>
         <node id="12" />
      </inputSet>
      <inputSet>
         <node id="9" />
      </inputSet>
      <inputSet>
         <node id="1" />
      </inputSet>
   </inputNode>
   <inputNode id="1">
      <inputSet>
         <node id="5" />
      </inputSet>
      <inputSet>
         <node id="10" />
      </inputSet>
      <inputSet>
         <node id="8" />
      </inputSet>
   </inputNode>
   <outputNode id="1">
      <outputSet>
         <node id="0" />
      </outputSet>
      <outputSet>
         <node id="10" />
      </outputSet>
   </outputNode>
   <inputNode id="2">
      <inputSet>
         <node id="12" />
      </inputSet>
   </inputNode>
   <outputNode id="2">
      <outputSet>
         <node id="12" />
      </outputSet>
   </outputNode>
   <inputNode id="3">
      <inputSet>
         <node id="3" />
      </inputSet>
      <inputSet>
         <node id="7" />
      </inputSet>
   </inputNode>
   <outputNode id="3">
      <outputSet>
         <node id="3" />
      </outputSet>
      <outputSet>
         <node id="6" />
      </outputSet>
   </outputNode>
   <inputNode id="4">
      <inputSet>
         <node id="6" />
      </inputSet>
   </inputNode>
   <outputNode id="4">
      <outputSet>
         <node id="12" />
      </outputSet>
   </outputNode>
   <outputNode id="5">
      <outputSet>
         <node id="11" />
      </outputSet>
      <outputSet>
         <node id="7" />
      </outputSet>
      <outputSet>
         <node id="1" />
      </outputSet>
   </outputNode>
   <inputNode id="6">
      <inputSet>
         <node id="3" />
      </inputSet>
   </inputNode>
   <outputNode id="6">
      <outputSet>
         <node id="4" />
      </outputSet>
   </outputNode>
   <inputNode id="7">
      <inputSet>
         <node id="5" />
      </inputSet>
   </inputNode>
   <outputNode id="7">
      <outputSet>
         <node id="3" />
      </outputSet>
   </outputNode>
   <inputNode id="8">
      <inputSet>
         <node id="10" />
      </inputSet>
      <inputSet>
         <node id="8" />
      </inputSet>
   </inputNode>
   <outputNode id="8">
      <outputSet>
         <node id="8" />
      </outputSet>
      <outputSet>
         <node id="1" />
      </outputSet>
   </outputNode>
   <inputNode id="9">
      <inputSet>
         <node id="11" />
      </inputSet>
   </inputNode>
   <outputNode id="9">
      <outputSet>
         <node id="0" />
      </outputSet>
   </outputNode>
   <inputNode id="10">
      <inputSet>
         <node id="1" />
      </inputSet>
   </inputNode>
   <outputNode id="10">
      <outputSet>
         <node id="8" />
      </outputSet>
      <outputSet>
         <node id="1" />
      </outputSet>
   </outputNode>
   <inputNode id="11">
      <inputSet>
         <node id="5" />
      </inputSet>
   </inputNode>
   <outputNode id="11">
      <outputSet>
         <node id="9" />
      </outputSet>
   </outputNode>
   <inputNode id="12">
      <inputSet>
         <node id="2" />
      </inputSet>
      <inputSet>
         <node id="4" />
      </inputSet>
   </inputNode>
   <outputNode id="12">
      <outputSet>
         <node id="0" />
      </outputSet>
      <outputSet>
         <node id="2" />
      </outputSet>
   </outputNode>
   <arc id="13" source="5" target="7" />
   <arc id="14" source="4" target="12" />
   <arc id="15" source="5" target="1" />
   <arc id="16" source="3" target="3" />
   <arc id="17" source="7" target="3" />
   <arc id="18" source="2" target="12" />
   <arc id="19" source="5" target="11" />
   <arc id="20" source="12" target="0" />
   <arc id="21" source="12" target="2" />
   <arc id="22" source="9" target="0" />
   <arc id="23" source="10" target="8" />
   <arc id="24" source="1" target="0" />
   <arc id="25" source="3" target="6" />
   <arc id="26" source="8" target="1" />
   <arc id="27" source="8" target="8" />
   <arc id="28" source="6" target="4" />
   <arc id="29" source="10" target="1" />
   <arc id="30" source="1" target="10" />
   <arc id="31" source="11" target="9" />
</cnet>
