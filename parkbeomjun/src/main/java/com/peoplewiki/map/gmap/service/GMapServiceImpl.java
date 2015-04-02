package com.peoplewiki.map.gmap.service;

import org.springframework.stereotype.Service;

@Service("gMapService")
public class GMapServiceImpl implements GMapService {

	@Override
	public void test(int y, int z) throws Exception {

		int x = y / z;


		// throw new Exception();
	}

}
