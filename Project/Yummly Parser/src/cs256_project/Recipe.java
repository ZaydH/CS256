package cs256_project;

import java.util.ArrayList;

public class Recipe {

	private ArrayList<String> ingredients;
	private int id;
	private String type;
	
	private final String ID_LINE_KEY = "\"id\":";
	private final String CUISINE_LINE_KEY = "\"CUISINE\":";
	private String INGREDIENTS_START_KEY = "[";
	private String INGREDIENTS_END_KEY = "[";
	
	
	
	public static Recipe getRecipe(String recordInfo){
		
	}
	
	private static String extractParameter(String record, String keyStart, String keyEnd){
		
	}
	
	
	/**
	 * Extracts the Recipe's identification number.
	 * 
	 * @return Recipe's ID number
	 */
	public int getID(){
		return id;
	}
	

	public String getCuisineType(){
		return type;
	}
	
	
	public String[] getIngredients(){
		String[] arrIngredients = new String[ingredients.size()];
		for(int i = 0; i < ingredients.size(); i++)
			arrIngredients[i] = ingredients.get(i);
		return arrIngredients;
	}
	
}
