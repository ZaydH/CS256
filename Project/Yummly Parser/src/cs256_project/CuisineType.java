package cs256_project;

public enum CuisineType {

	brazilian,
	british,
	cajun_creole,
	chinese,
	filipino,
	french,
	greek,
	indian,
	irish,
	italian,
	jamaican,
	japanese,
	korean,
	mexican,
	moroccan,
	russian,
	southern_us,
	spanish,
	thai,
	vietnamese;
	
	private int recipeCount;
	
	CuisineType(){
		recipeCount = 0;
	}
	
//	/**
//	 * Returns the cuisine type's ID.  This is useful when
//	 * storing cuisine type information in an array.
//	 * 
//	 * @return Identification number between 0 and the Number of Cuisine Types (e.g. 20) minus 1.
//	 */
//	public int getID(){
//		return id;
//	}
//	
//	
//	/**
//	 * Returns the name of the enumerated cuisine type as a String.
//	 * 
//	 * @return  String - enumerated object's name
//	 */
//	public String getName(){
//		return name;
//	}
	
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
	
	/**
	 * 
	 * @param ordinalNumb - CuisineType ordinal number.
	 * @return CuisineType 
	 */
	public CuisineType fromInt(int ordinalNumb){
		return CuisineType.values()[int ordinalNumb];
	}
	
	
	@Override
	public String toString(){
		return this.name();
	}
	
}
