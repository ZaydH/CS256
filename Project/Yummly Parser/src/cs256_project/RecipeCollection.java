package cs256_project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

public class RecipeCollection {

	private Hashtable<Integer, Recipe> allRecipes = new Hashtable<Integer,Recipe>();
	private Hashtable<String, CuisineType> cuisineTypes = new Hashtable<String,CuisineType>();
	private Hashtable<String, Ingredient> allIngredients = new Hashtable<String,Ingredient>();
	private int badRecordCount = 0;
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("Error: Invalid number of input arguments.  A single input argument is required.");
			return;
		}
		
		RecipeCollection col = RecipeCollection.getRecipeCollection(args[0]);
		
		col.outputCuisineTypes("Cuisines.txt");

		
	}

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
			tempRC.cuisineTypes.put(type.getName(), type);
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
					ingredient.incrementCuisineUsage(type);
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
				fileOut.write(cType + " (\"" + cType + "\", " + i + ")");
			}
			fileOut.write(";");
			fileOut.close(); //--- Close the file writing.

		}
		catch(IOException e){
			System.out.print("Error: Unable to write output file.");
		}
		
	}

}


