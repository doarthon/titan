package com.thinkaurelius.titan.graphdb.database.serialize;

import com.thinkaurelius.titan.core.Directionality;
import com.thinkaurelius.titan.core.EdgeCategory;
import com.thinkaurelius.titan.core.PropertyIndex;
import com.thinkaurelius.titan.core.attribute.AttributeInt;
import com.thinkaurelius.titan.core.attribute.AttributeLong;
import com.thinkaurelius.titan.core.attribute.AttributeReal;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.AttributeIntSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.AttributeLongSerializer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.AttributeRealSerializer;
import com.thinkaurelius.titan.graphdb.edgetypes.EdgeTypeVisibility;
import com.thinkaurelius.titan.graphdb.edgetypes.StandardPropertyType;
import com.thinkaurelius.titan.graphdb.edgetypes.StandardRelationshipType;
import com.thinkaurelius.titan.graphdb.edgetypes.group.StandardEdgeTypeGroup;

public class SerializerInitialization {

	public static final void initialize(Serializer serializer) {
		serializer.registerClass(StandardPropertyType.class);
		serializer.registerClass(StandardRelationshipType.class);
		serializer.registerClass(PropertyIndex.class);
		serializer.registerClass(EdgeTypeVisibility.class);
		serializer.registerClass(Directionality.class);
		serializer.registerClass(EdgeCategory.class);
		serializer.registerClass(StandardEdgeTypeGroup.class);
		serializer.registerClass(AttributeInt.class,new AttributeIntSerializer());
		serializer.registerClass(AttributeReal.class,new AttributeRealSerializer());
		serializer.registerClass(AttributeLong.class,new AttributeLongSerializer());
	}
	
}
