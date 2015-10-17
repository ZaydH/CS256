package cs256_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

public class Recipe {

	private ArrayList<String> ingredients = new ArrayList<String>();
	private int id;
	private String type;
	
	private static final String ID_LINE_KEY = "\"id\":";
	private static final String CUISINE_LINE_KEY = "\"cuisine\":";
	private static final String INGREDIENTS_START_KEY = "\"ingredients\": [";
	private static final String INGREDIENTS_END_KEY = "]";
	
	
	/**
	 * Hidden constructor.  Must use the FactoryMethod to get ingredients.
	 */
	private Recipe(){
		id = -1;
		type = "UNINITIALIZED";
	}
	
	/**
	 * This method uses the FactoryMethod pattern to generate recipes.  
	 * The method is passed a string that contains the information of a Recipe 
	 * record.  If the string can be successfully parsed, it returns a Recipe object.
	 * Otherwise it returns false.
	 * 
	 * @param recordInfo 	String containing a Recipe record's information
	 * @return 				A Recipe record if the information is valid and null otherwise.
	 */
	public static Recipe getRecipe(String recordInfo){
		
		Recipe tempRecipe = new Recipe();
		
		// Extract the record's ID
		String idStr = extractParameter(recordInfo, ID_LINE_KEY, ",");
		try{
			idStr = idStr.replace(" ", "");
			tempRecipe.id = Integer.parseInt(idStr);
		}
		catch(Exception e){ return null; }
		
		// Extract the record's cuisine type
		try{ 
			String typeInfo = extractParameter(recordInfo, CUISINE_LINE_KEY, ",");
			tempRecipe.type = extractParameter(typeInfo, "\"", "\"");
		}
		catch(Exception e){ return null; }
		if(tempRecipe.type == null) return null;
		
		// Get the list of ingredients
		String[] splitIngredients;
		try{
			String allIngredients = extractParameter(recordInfo, INGREDIENTS_START_KEY, INGREDIENTS_END_KEY);
			splitIngredients = allIngredients.split("\n");
			// All recipes have at least two ingredients
			if(splitIngredients.length < 2) return null;
		}
		catch(Exception e){ return null; }
		// Extract the ingredients and return them.
		for(int i = 1; i < splitIngredients.length -1; i++ ){ // Ignore the first and last record since a blank before the closing bracket
			String ingredient = splitIngredients[i];
			ingredient = extractParameter(ingredient, "\"", "\"");
			if(ingredient == null) return null;
			tempRecipe.ingredients.add(ingredient);
		}
		
		// Sort the recipe's ingredients before returning it.
		tempRecipe.sortIngredientsAndRemoveDuplicates();
		
		// Everything parsed correctly so return the ingredient list
		return tempRecipe;
		
	}
	
	
	private static String extractParameter(String record, String keyStart, String keyEnd){
		// Find start of the parameter
		int startLoc = record.indexOf(keyStart) + keyStart.length();
		if(startLoc == -1) return null;
		// Find end of the parameter
		int endLoc = record.indexOf(keyEnd, startLoc);
		if(endLoc == -1) return null;
		
		return record.substring(startLoc, endLoc);
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
	
//	/**
//	 * For time saving purposes, it may be beneficial to have the ingredients sorted
//	 * in the Recipe object.  Adding this functionality just in case.
//	 */
//	private void sortIngredients(){
//		Collections.sort(ingredients);
//	}
	
	
	/**
	 * This methods sorts the list of ingredients for this recipe
	 * and removes any duplicate ones.
	 */
	private void sortIngredientsAndRemoveDuplicates(){
		Collections.sort(ingredients);
		
		for(int i = 1; i < ingredients.size(); i++){
			if( ingredients.get(i).equals(ingredients.get(i-1)) )
				ingredients.remove(i);
		}		
	}
	
	/**
	 * Prints the Recipe in JSON format.
	 */
	@Override
	public String toString(){
	
		String spacer = "  ";
		StringBuffer outputBuffer = new StringBuffer();
		
		outputBuffer.append(spacer + "{\n");
		outputBuffer.append(spacer + spacer + ID_LINE_KEY + " " + this.getID() +",\n" );
		outputBuffer.append(spacer + spacer + CUISINE_LINE_KEY + " \"" + this.getCuisineType() +"\",\n" );
		outputBuffer.append(spacer + spacer + INGREDIENTS_START_KEY + "\n" );
		
		// 
		String ingredientStartString = spacer + spacer + spacer;
		for(int i = 0; i < this.ingredients.size(); i++){
			// Add after all ingredients except the last one.
			if(i != 0 ) outputBuffer.append(",\n");
			// Add the ingredient information.
			outputBuffer.append(ingredientStartString);
			outputBuffer.append("\"" + this.ingredients.get(i) +  "\"");
		}
		outputBuffer.append("\n"); // Need a new line after the last ingredient
		outputBuffer.append(spacer + spacer + INGREDIENTS_END_KEY + "\n" );
		// Close the JSON record
		outputBuffer.append(spacer + "}");
		
		return outputBuffer.toString();
	
	}
	

	
}
