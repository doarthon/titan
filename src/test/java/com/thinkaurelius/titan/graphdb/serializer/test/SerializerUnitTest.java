package com.thinkaurelius.titan.graphdb.serializer.test;


import com.thinkaurelius.titan.core.Directionality;
import com.thinkaurelius.titan.core.EdgeCategory;
import com.thinkaurelius.titan.core.PropertyIndex;
import com.thinkaurelius.titan.graphdb.database.serialize.DataOutput;
import com.thinkaurelius.titan.graphdb.database.serialize.Serializer;
import com.thinkaurelius.titan.graphdb.database.serialize.kryo.KryoSerializer;
import com.thinkaurelius.titan.graphdb.edgetypes.EdgeTypeVisibility;
import com.thinkaurelius.titan.graphdb.edgetypes.StandardPropertyType;
import com.thinkaurelius.titan.graphdb.edgetypes.StandardRelationshipType;
import com.thinkaurelius.titan.graphdb.edgetypes.group.StandardEdgeTypeGroup;
import com.thinkaurelius.titan.graphdb.edgetypes.system.SystemEdgeTypeManager;
import com.thinkaurelius.titan.util.test.PerformanceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SerializerUnitTest {
	
	private static final Logger log =
		LoggerFactory.getLogger(SerializerUnitTest.class);

	Serializer serialize;
	boolean printStats;
	
	@Before
	public void setUp() throws Exception {
		serialize = new KryoSerializer();
		serialize.registerClass(TestEnum.class);
		serialize.registerClass(TestClass.class);
		serialize.registerClass(short[].class);
		serialize.registerClass(StandardRelationshipType.class);
		serialize.registerClass(Directionality.class);
		serialize.registerClass(EdgeCategory.class);
		serialize.registerClass(EdgeTypeVisibility.class);
		serialize.registerClass(StandardEdgeTypeGroup.class);
		serialize.registerClass(PropertyIndex.class);
		serialize.registerClass(StandardPropertyType.class);
		
		printStats = true;
	}

	@Test
	public void objectWriteRead() {
		//serialize.registerClass(short[].class);
		//serialize.registerClass(TestClass.class);
		DataOutput out = serialize.getDataOutput(128, true);
		String str = "This is a test";
		int i = 5;
		TestClass c = new TestClass(5,8,new short[]{1,2,3,4,5},TestEnum.Two);
		Number n = new Double(3.555);
		out.writeObjectNotNull(str);
		out.putInt(i);
		out.writeObject(c);
		out.writeClassAndObject(n);
		ByteBuffer b = out.getByteBuffer();
		if (printStats) log.debug(bufferStats(b));
		String str2=serialize.readObjectNotNull(b, String.class);
		assertEquals(str,str2);
		assertEquals(b.getInt(),i);
		TestClass c2 = serialize.readObject(b, TestClass.class);
		assertEquals(c,c2);
		assertEquals(n,serialize.readClassAndObject(b));
		assertFalse(b.hasRemaining());
	}
	
	@Test
	public void longWriteTest() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //26 chars
		int no = 100;
		DataOutput out = serialize.getDataOutput(128, true);
		for (int i=0;i<no;i++) {
			String str = base + (i+1);
			out.writeObjectNotNull(str);
		}
		ByteBuffer b = out.getByteBuffer();
		if (printStats) log.debug(bufferStats(b));
		for (int i=0;i<no;i++) {
			String str = base + (i+1);
			String read = serialize.readObjectNotNull(b, String.class);
			assertEquals(str,read);
		}
		assertFalse(b.hasRemaining());
	}
	
	@Test
	public void largeWriteTest() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //26 chars
		String str = "";
		for (int i=0;i<100;i++) str+=base;
		DataOutput out = serialize.getDataOutput(128, true);
		out.writeObjectNotNull(str);
		ByteBuffer b = out.getByteBuffer();
		if (printStats) log.debug(bufferStats(b));
		assertEquals(str,serialize.readObjectNotNull(b, String.class));
		assertFalse(b.hasRemaining());
	}
	
	@Test
	public void enumSerializeTest() {
		DataOutput out = serialize.getDataOutput(128, true);
		out.writeObjectNotNull(TestEnum.Two);
		ByteBuffer b = out.getByteBuffer();
		if (printStats) log.debug(bufferStats(b));
		assertEquals(TestEnum.Two,serialize.readObjectNotNull(b, TestEnum.class));
		assertFalse(b.hasRemaining());

	}
	
	@Test
	public void serializeRelationshipType() {
		StandardRelationshipType relType = new StandardRelationshipType("testName",EdgeCategory.Simple,
				Directionality.Directed,EdgeTypeVisibility.Modifiable,false,
				new String[]{},new String[]{}, SystemEdgeTypeManager.systemETgroup);
		StandardPropertyType propType = new StandardPropertyType("testName",EdgeCategory.Simple,
				Directionality.Directed,EdgeTypeVisibility.Modifiable,false,
				new String[]{},new String[]{}, SystemEdgeTypeManager.systemETgroup,true,PropertyIndex.Standard,String.class);
		DataOutput out = serialize.getDataOutput(128, true);
		out.writeObjectNotNull(relType);
		out.writeObjectNotNull(propType);
		ByteBuffer b = out.getByteBuffer();
		if (printStats) log.debug(bufferStats(b));
		assertEquals("testName",serialize.readObjectNotNull(b, StandardRelationshipType.class).name);
		assertEquals(String.class,serialize.readObjectNotNull(b, StandardPropertyType.class).getDataType());
		assertFalse(b.hasRemaining());
	}
	
	@Test
	public void performanceTestLong() {
		int runs = 10000;
		printStats = false;
		PerformanceTest p = new PerformanceTest(true);
		for (int i=0;i<runs;i++) {
			longWriteTest();
		}
		p.end();
		log.debug("LONG: Avg micro time: " + (p.getMicroTime()/runs));		
	}
	
	@Test
	public void performanceTestShort() {
		int runs = 10000;
		printStats = false;
		PerformanceTest p = new PerformanceTest(true);
		for (int i=0;i<runs;i++) {
			objectWriteRead();
		}
		p.end();
		log.debug("SHORT: Avg micro time: " + (p.getMicroTime()/runs));		
	}
	
	public static String bufferStats(ByteBuffer b) {
		return "ByteBuffer size: " + b.limit() + " position: " + b.position();
	}
	
	@Test(expected=IllegalArgumentException.class) public void checkNonObject() {
		DataOutput out = serialize.getDataOutput(128, false);
		out.writeObject("This is a test");
	}
	
	
	
	@After
	public void tearDown() throws Exception {
	}

}

