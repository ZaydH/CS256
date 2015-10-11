package cs256_project;

import java.util.ArrayList;

public class Ingredient {

	private String name;
	private int[] cuisineUsage;
	int totalRecipeCount;
	
	
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
	 * @param type
	 */
	public void incrementCuisineUsage(CuisineType type){
		int ordinal = type.ordinal();
		cuisineUsage[ordinal]++;
		
		// Increment the total number of times this ingredient is used.
		totalRecipeCount++;
	}
	
}
