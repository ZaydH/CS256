package cs256_project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;


public class RecipeCollection {

	private Hashtable<Integer, Recipe> allRecipes = new Hashtable<Integer,Recipe>();
	private Hashtable<String, CuisineType> cuisineTypes = new Hashtable<String,CuisineType>();
	private Hashtable<String, Ingredient> allIngredients = new Hashtable<String,Ingredient>();
	@SuppressWarnings("unused")
	private int badRecordCount = 0;
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("Error: Invalid number of input arguments.  A single input argument is required.");
			return;
		}
		
		RecipeCollection col = RecipeCollection.getRecipeCollection(args[0]);
		
		col.outputCuisineTypes("Cuisines.txt");
		col.outputIngredients("Ingredients.txt");

		col.print("Recipes.txt");
		
	}

	/**
	 * 
	 * Factory method to build a recipe collection from a file.
	 * 
	 * @param filePath		File containing the recipe information.
	 * @return				RecipeCollection if file parsing successful.  Null otherwise.
	 */
	public static RecipeCollection getRecipeCollection(String filePath){
		
		File file = new File(filePath);
		
		//----- Once the file extension has been verified, try opening the file.
		Scanner fileIn;
		try{
			fileIn = new Scanner(new FileReader(file));
		}
		catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "No file with the specified name exists.  Please specify a valid file and try again.");
			return null;
		}
	
		// Initialize the RecipeCollection to output
		RecipeCollection tempRC = new RecipeCollection();

		// Initialize the cuisine list.
		CuisineType allTypes[] = CuisineType.values();
		for(CuisineType type : allTypes){
			tempRC.cuisineTypes.put(type.name(), type);
		}
		
		// Iterate through the recipe file and build the 
		final String RECORD_START_CHAR = "{";
		final String RECORD_END_CHAR = "}";
		String line;
		StringBuffer recipeInfo = new StringBuffer();
		while(fileIn.hasNextLine()){
			
			line = fileIn.nextLine();
			// Check if the StringBuffer needs to be cleared.
			if(line.indexOf(RECORD_START_CHAR) != -1 ) recipeInfo.setLength(0);
			
			// Append line to the String Buffer
			recipeInfo.append(line);
			recipeInfo.append("\n");
			
			// Find the end of the record
			if(line.indexOf(RECORD_END_CHAR) != -1 ){
				line = line.replace("},", "}");
				recipeInfo.append(line);
				Recipe newRecipe = Recipe.getRecipe(recipeInfo.toString());
				// Check for bad records.
				if(newRecipe == null){
					String badRecipeInfo = recipeInfo.toString();
					
					System.out.print("Bad Record: " + badRecipeInfo + "\n\n");
					tempRC.badRecordCount++; // Increment the bad record counter
					continue; // Return to the next while loop.
				}

				// Add new recipe to the list
				tempRC.allRecipes.put(newRecipe.getID(), newRecipe);
					
				// Update the cuisine type count in the hashtable
				CuisineType type = tempRC.cuisineTypes.get(newRecipe.getCuisineType()); 
				type.incrementRecipeCount();
				tempRC.cuisineTypes.put(newRecipe.getCuisineType(), type);
				
				// Update recipe frequency count
				String[] recipeIngredientNames = newRecipe.getIngredients();
				for(String ingredientName : recipeIngredientNames){
					
					// Extract the ingredient if it already exists.  
					Ingredient ingredient = tempRC.allIngredients.get(ingredientName);
					// Build a new ingredient if this ingredient does not already exist.
					if(ingredient == null)	ingredient = new Ingredient(ingredientName);
					
					// Increment the usage of the ingredient for this recipe's cuisine type
					ingredient.incrementCuisineTypeCount(type);
					// Update the ingredients hash table
					tempRC.allIngredients.put(ingredientName, ingredient);
				}
			} //if(line.indexOf(RECORD_END_CHAR) > 0 )
		} //while(fileIn.hasNextLine())
		
		fileIn.close(); // Close the scanner
		return tempRC;	// Return the collection of recipe information.
	}
	
	
	/**
	 * 
	 * For a RecipeCollection, it prints to a file all of the cuisine types
	 * and the number of recipes of that type.
	 * 
	 * @param filePath 	Path of the file to write containing cuisine information.
	 */
	public void outputCuisineTypes(String filePath){
		

		// Extract the list of 
		ArrayList<String> cuisineTypeList = new ArrayList<String>();
		Set<String> keys = cuisineTypes.keySet();
		for(String key: keys)
			cuisineTypeList.add(key);
		// Sort the cuisine types
	    Collections.sort(cuisineTypeList);
	    
	    // Print the Cuisine Information to a file
        try{
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(filePath));
			
			for(int i = 0; i < cuisineTypeList.size(); i++){
				// Separate each cuisine type by a new line
				if(i != 0){
					fileOut.write(",");
					fileOut.newLine();
				}
				// Output the cuisine type and total number of recipes of that type
				String cType = cuisineTypeList.get(i);
				fileOut.write(cType);
			}
			fileOut.write(";");
			fileOut.close(); //--- Close the file writing.

		}
		catch(IOException e){
			System.out.print("Error: Unable to write output file.");
		}
		
	}
	
	/**
	 * Prints a RecipeCollection's set of ingredients to a file.
	 * It is printed in descending order of the ingredient's prevalence.  
	 * Format of each line is:
	 * 
	 * ingredientName,TotalRecipeCount,numbRecipesOfCuisineType00,numbRecipesOfCuisineType01,numbRecipesOfCuisineType02,...
	 * 
	 * @param filePath	String - Path of the file to print.
	 */
	public void outputIngredients(String filePath){
		// Extract the list of ingredients
		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>(allIngredients.values());

		// Sort the cuisine types
	    Collections.sort(ingredientList, new Ingredient.RecipeCountCompareDescending() );
	    
	    // Print the Cuisine Information to a file
        try{
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(filePath));
			
			for(int i = 0; i < ingredientList.size(); i++){
				Ingredient ingredient = ingredientList.get(i);
				// Separate each cuisine type by a new line
				if(i != 0){
					fileOut.newLine();
				}
				// Output the cuisine type and total number of recipes of that type
				fileOut.write(ingredient.getName() + "," + ingredient.getTotalRecipeCount());
				
				int[] ingredientTypeCount = ingredient.getAllCuisineTypesCount();
				for(int j = 0; j < ingredientTypeCount.length; j++)
					fileOut.write("," + ingredientTypeCount[j]);
			}
			fileOut.close(); //--- Close the file writing.

		}
		catch(IOException e){
			System.out.print("Error: Unable to write output file.");
		}
	}
	
	
	/**
	 * 
	 * Prints the Recipe File to a text file.  This can be used
	 * to simplify the creation of training files so as a generic importer can
	 * be used.
	 * 
	 * @param filePath	Path to the recipe file
	 */
	public void print(String filePath){
		// 
		ArrayList<Recipe> recipeList = new ArrayList<Recipe>(allRecipes.values());
		RecipeCollection.printRecipesFile("Recipes.txt", recipeList);
	}
	
	
	/**
	 * Helper method to print a set of recipes to a file.
	 * 
	 * @param filePath 	String - Path to the file to be printed
	 * @param recipes	Collection<Recipe> List of recipes to print to a file.
	 */
	public static void printRecipesFile(String filePath, List<Recipe> recipes){
	    
	    // Print the Cuisine Information to a file
        try{
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(filePath));
			
			// Put the beginning of the JSON file.
			fileOut.write("[");
			fileOut.newLine();
			
			for(int i = 0; i < recipes.size(); i++){
				// Separate each cuisine type by a new line
				if(i != 0){
					fileOut.write(",");
					fileOut.newLine();
				}
				// Output recipe information
				String[] recipeOutput = recipes.get(i).toString().split("\n");
				for( int j = 0;  j < recipeOutput.length; j++){
					if(j != 0) fileOut.newLine();
					fileOut.write(recipeOutput[j]);
				}
			}
			
			// Put the end of the JSON file then close it.
			fileOut.newLine();
			fileOut.write("]");
			fileOut.close();

		}
		catch(IOException e){
			System.out.print("Error: Unable to write output file for the recipes.");
		}
	}

	/** 
	 * 
	 * Extracts the recipes in the recipe collection.
	 * 
	 * @return List of recipes
	 */
	private Recipe[] getRecipes(){
		Recipe[] recipeArr = new Recipe[allRecipes.size()];
		Integer[] keys = allRecipes.keySet().toArray();
		for(int i = 0; i < keys.length; i++)
			recipeArr[i] = allRecipes.get(keys[i]);
		return recipeArr;
	}
	
	
	
	public RecipeResult performKNearestNeighbor(RecipeCollection testRecipeCollection, int k, RecipeDistance dist){
	
		// Helper class for sorting on recipes.
		class RecipeWrapper implements Comparable<RecipeWrapper>{
			Recipe recipe;
			double distance;
			
			public RecipeWrapper(Recipe recipe, double distance){
				this.recipe = recipe;
				this.distance = distance;
			}
			
			public Recipe getRecipe(){ return recipe; }
			
			@Override
			public int compareTo(RecipeWrapper other){
				if(this.distance < other.distance) return -1;
				else if(this.distance == other.distance) return 0;
				else return 1;
			}
			
		}
		
		RecipeResult results = new RecipeResult();
		Recipe[] testRecipes = testRecipeCollection.getRecipes();
		Recipe[] trainingRecipes = this.getRecipes();
		int correctClassifications = 0;
		for(int i = 0; i < testRecipes.length; i++){
			
			RecipeWrapper[] sortedRecipes = new RecipeWrapper[trainingRecipes.length];
			// Calculate the inter-recipe distance for each recipe and then sort by distance 
			for(int j = 0; j < trainingRecipes.length; j++){
				// Determine the distance between 
				double recipeDistance = dist.compare(testRecipes[i], trainingRecipes[j]);
				sortedRecipes[j] = new RecipeWrapper(trainingRecipes[j], new Double(recipeDistance));
			}
			Arrays.sort(sortedRecipes);
			
			// Iterate through the sorted recipes and find the most common cuisine types
			int[] cuisineTypeCount = new int[CuisineType.values().length];
			for(int j = 0; j < k; j++){
				// Get the cuisine type for this recipe
				CuisineType type = CuisineType.valueOf(sortedRecipes[k].getRecipe().getCuisineType());
				cuisineTypeCount[type.ordinal()]++;
			}
			// Find the id of the cuisine type with the most matches
			int maxId = 0;
			for(int j = 1; j < k; j++){
				if(cuisineTypeCount[j] > cuisineTypeCount[maxId]) maxId = j;
			}
				
			
			String selectedCuisineType;
			// Check if the classification is correct
			if(selectedCuisineType.equals(testRecipes[i].getCuisineType()))
				correctClassifications++;	
		}
		
		// Calculate the overall accuracy
		results.setAccuracy((double)correctClassifications/testRecipes.length);

		return results;
		
	}
	

	public class RecipeResult {
	
		double accuracy;
		
		
		public double getAccuracy(){ return accuracy; }
		public void setAccuracy(double accuracy){ this.accuracy = accuracy; }
		
	}


	
	/**
	 * 
	 * This interface is used to allow one to specify different
	 * possible algorithms for calculating the distance between two recipes.
	 * 
	 * It is intended as a use of the STRATEGY PATTERN.  For more information on the 
	 * strategy pattern, see below:
	 * 
	 * https://en.wikipedia.org/wiki/Strategy_pattern
	 *
	 */
	public interface RecipeDistance{
		/**
		 * 
		 * Calculates the distance between r1 and r2.  The implementation
		 * is intended to be associative.
		 * 
		 * @param r1 A recipe
		 * @param r2 Another recipe
		 * @return The distance (i.e. similarity between r1 and r2.  The lower the
		 * return value, the more similar the two recipes are.  If two recipes 
		 * are identical, the return value should be 0.
		 * 
		 */
		double compare(Recipe r1, Recipe r2);
	}
	
	
	/**
	 * 
	 * Used as a STRATEGY Method for determining the difference between
	 * two recipes in the collection.
	 *
	 */
	public class ValueDistanceMetricCompare implements RecipeDistance{
		
		Hashtable<String, Double> ingredientDistance = new Hashtable<String, Double>();
		
		@Override
		public double compare(Recipe r1, Recipe r2){
			
			double totalDistance = 0;
			double calculatedDistance;
			Double storedDistance;
			String i1Name, i2Name;
			Ingredient i1, i2;
			String[] r1Ingredients = r1.getIngredients();
			String[] r2Ingredients = r2.getIngredients();
			
			// Iterate through each ingredient list
			for(int i = 0; i < r1Ingredients.length; i++){
				for(int j = 0; j < r2Ingredients.length; j++){
					
					i1Name = r1Ingredients[i];
					i2Name = r2Ingredients[j];
					
					// If the ingredients are identical, go to the next ingredient.
					if(i1Name.equals(i2Name)) continue;
					
					// See if the ingredient distance is store
					storedDistance = ingredientDistance.get(getKeyName(i1Name, i2Name));
					if(storedDistance != null){
						totalDistance += storedDistance.doubleValue();
						continue;
					}
					
					// Ingredient distance is not stored so calculate it.
					i1 = allIngredients.get(i1Name);
					i2 = allIngredients.get(i2Name);
					calculatedDistance = 0;
					for(int k = 0; k < cuisineTypes.size(); k++){
						calculatedDistance += Math.abs((double)(i1.getCuisineTypeCount(k))/i1.getTotalRecipeCount() 
												       - (double)(i2.getCuisineTypeCount(k))/i2.getTotalRecipeCount());
					}
					// Store the inter-ingredient distance in the collection.
					storedDistance = calculatedDistance;
					ingredientDistance.put(getKeyName(i1Name, i2Name), storedDistance);
		
				}
			}
			
			// Normalize the distance to recipe length.
			totalDistance /= (r1Ingredients.length + r2Ingredients.length);
			return totalDistance;
			
		}
		
		/**
		 * 
		 * Builds a key given two ingredient names
		 * 
		 * @param i1 Name of the first ingredient
		 * @param i2 Name of the second ingredient
		 * 
		 * @return Hash table key name for two ingredients.
		 */
		private String getKeyName(String i1, String i2){
			
			// If i1 is alphabetically before i2, then it is the first in the key.
			if(i1.compareTo(i2) < 0) return i1 + "_" + i2;
			else 					 return i2 + "_" + i1;
			
			
		}
		
	}

}


