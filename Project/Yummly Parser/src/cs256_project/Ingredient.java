package cs256_project;

import java.util.Comparator;

public class Ingredient {

	private String name;
	private int[] cuisineUsage;
	private int totalRecipeCount;
	
	
	public Ingredient(String name){
		this.name = name;
		
		// Initialize the array of cuisine type counts.
		CuisineType[] allTypes = CuisineType.values();
		cuisineUsage = new int[allTypes.length];
		
		// Initialize the array of usage counts for all cuisine types to 0.
		for(int i = 0; i < cuisineUsage.length; i++)
			cuisineUsage[i] =0;
		
		// Sets the total recipe count of this ingredient
		totalRecipeCount = 0;
		
	}
	
	
	/**
	 * Returns the name of the ingredient as a string.
	 * 
	 * @return String - Name of the ingredient
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Increments the usage of specific cuisine type for this recipe.
	 * 
	 * @param type Cuisine Type whose count will be incremented.
	 */
	public void incrementCuisineTypeCount(CuisineType type){
		int ordinal = type.ordinal();
		cuisineUsage[ordinal]++;
		
		// Increment the total number of times this ingredient is used.
		totalRecipeCount++;
	}
	
	
	
	/**
	 * Extracts the number of times this ingredient is used for a particular
	 * cuisine type.
	 * 
	 * @return  int[] - For each of the CuisineType, this function returns the 
	 * number of recipes of that type containing this ingredient.
	 */
	public int[] getCuisineTypeCount(){
		int[] typeUsage = new int[cuisineUsage.length];
		
		for(int i = 0; i < typeUsage.length; i++)
			typeUsage[i] = cuisineUsage[i];
		
		return typeUsage;
	}
	
	
	public int getTotalRecipeCount(){ return totalRecipeCount;	}
	
	/**
	 * 
	 * Class object that allows for the sorting of ingredients by name.
	 * 
	 * @author Zayd
	 *
	 */
	public static class NameCompare implements Comparator<Ingredient>{
	    @Override
	    public int compare(Ingredient i1, Ingredient i2) {
	        return i1.getName().compareTo(i1.getName());
	    }
	}
	
	
	/**
	 * 
	 * Class object that allows for the sorting of ingredients by name.
	 * 
	 * @author Zayd
	 *
	 */
	public static class RecipeCountCompareDescending implements Comparator<Ingredient>{
	    @Override
	    public int compare(Ingredient i1, Ingredient i2) {
	        return i2.getTotalRecipeCount() - i1.getTotalRecipeCount() ;
	    }
	}
	

}
