package jdk8.objectpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;

public class MyPoolFactory extends BasePooledObjectFactory<String>{

	@Override
	public String create() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PooledObject<String> wrap(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}