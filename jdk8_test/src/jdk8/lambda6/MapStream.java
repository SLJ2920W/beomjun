package jdk8.lambda6;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStream {

	public static void main(String[] args) {
		Map<String, String> imageMap = new HashMap<String, String>();
		imageMap.put("sfdjklvj01", "2016-07-17 121706_정보배_34기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_34기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj02", "2016-07-17 121706_정보배_35기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_35기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj03", "2016-07-17 121706_정보배_36기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_36기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj04", "2016-07-17 121706_정보배_37기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_37기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj05", "2016-07-17 121706_정보배_38기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_38기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj06", "2016-07-17 121706_정보배_39기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_39기 신입사원 Action Learning 관련 설문.jpg");
		imageMap.put("sfdjklvj07", "2016-07-17 121706_정보배_40기 신입사원 Action Learning 관련 설문/2016-07-17 121706_정보배_40기 신입사원 Action Learning 관련 설문.jpg");
		
		Map<String, String> changeImageMap = imageMap.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(), p -> "PPP" + p.getValue() + "HHO"));
		
//		System.out.println(changeImageMap);
		
		changeImageMap.entrySet().stream().forEach(System.out::println);

	}

}
