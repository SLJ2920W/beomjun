package jdk8.lambda4;

@FunctionalInterface
public interface FruitPredicate{
	
	/**
	 * @param type
	 * @return
	 */
	public boolean test(Object type);
	
	
	default public boolean testType(){
		return false;
	}
}
