package cs256_project;

public enum CuisineType {

	brazilian ("brazilian", 0),
	british ("british", 1),
	cajun_creole ("cajun_creole", 2),
	chinese ("chinese", 3),
	filipino ("filipino", 4),
	french ("french", 5),
	greek ("greek", 6),
	indian ("indian", 7),
	irish ("irish", 8),
	italian ("italian", 9),
	jamaican ("jamaican", 10),
	japanese ("japanese", 11),
	korean ("korean", 12),
	mexican ("mexican", 13),
	moroccan ("moroccan", 14),
	russian ("russian", 15),
	southern_us ("southern_us", 16),
	spanish ("spanish", 17),
	thai ("thai", 18),
	vietnamese ("vietnamese", 19);
	
	private final String name;
	private final int id;
	private int recipeCount;
	
	CuisineType(String name, int id){
		this.id = id;
		this.name = name;
		recipeCount = 0;
	}
	
	/**
	 * Returns the cuisine type's ID.  This is useful when
	 * storing cuisine type information in an array.
	 * 
	 * @return Identification number between 0 and the Number of Cuisine Types (e.g. 20) minus 1.
	 */
	public int getID(){
		return id;
	}
	
	
	/**
	 * Returns the name of the enumerated cuisine type as a String.
	 * 
	 * @return  String - enumerated object's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Each enumerated object contains a count for the number of recipes of that
	 * cuisine type.  This tracks that counter.
	 */
	public void incrementRecipeCount(){
		recipeCount++;
	}
	
	/**
	 * Returns the number of recipes in the dataset for this cuisine type.
	 * 
	 * @return Integer - Number of recipes in the dataset of this cuisine type.
	 */
	public int getRecipeCount(){
		return recipeCount;
	}
	
	
	@Override
	public String toString(){
		return name;
	}
	
}
