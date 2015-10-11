package cs256_project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class RecipeCollection {

	private Hashtable<Integer, Recipe> allRecipes = new Hashtable<Integer,Recipe>();
	private Hashtable<String, Integer> cuisineTypes = new Hashtable<String,Integer>();
	private Hashtable<String, Integer> allIngredients = new Hashtable<String,Integer>();
	private int badRecordCount = 0;
	
	public static void main(String[] args){
		
		if(args.length != 1){
			System.out.println("Error: Invalid number of input arguments.  A single input argument is required.");
			return;
		}
		
		RecipeCollection col = RecipeCollection.getRecipeCollection(args[0]);
		
		int i = 1;
		
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
		
		// Iterate through the file
		RecipeCollection tempRC = new RecipeCollection();
		final String RECORD_START_CHAR = "{";
		final String RECORD_END_CHAR = "}";
		String line;
		StringBuffer recipeInfo = new StringBuffer();
		while(fileIn.hasNextLine()){
			
			line = fileIn.nextLine();
			// Check if the StringBuffer needs to be cleared.
			if(line.indexOf(RECORD_START_CHAR) > 0 ) recipeInfo.setLength(0);
			
			// Append line to the String Buffer
			recipeInfo.append(line);
			
			// Find the end of the record
			if(line.indexOf(RECORD_END_CHAR) > 0 ){
				line = line.replace("},", "}");
				recipeInfo.append(line);
				Recipe newRecipe = Recipe.getRecipe(recipeInfo.toString());
				// Check for bad records.
				if(newRecipe == null){
					tempRC.badRecordCount++; // Increment the bad record counter
					continue; // Return to the next while loop.
				}

				// Add new recipe to the list
				tempRC.allRecipes.put(newRecipe.getID(), newRecipe);
				
				// Update recipe frequency count
				int cuisineTypeCount;
				String tempType = newRecipe.getCuisineType();
				if(tempRC.cuisineTypes.containsKey(tempType))
					cuisineTypeCount = tempRC.cuisineTypes.get(tempType).intValue() + 1; 
				else cuisineTypeCount = 1;
				// Update the cuisine type count in the hashtable
				tempRC.cuisineTypes.put(tempType, cuisineTypeCount);
				
				// Update recipe frequency count
				String[] recipeIngredients = newRecipe.getIngredients();
				for(String ingredient : recipeIngredients){
					int ingredientCount;
					if(tempRC.cuisineTypes.containsKey(tempType))
						ingredientCount = tempRC.allIngredients.get(ingredient).intValue() + 1; 
					else ingredientCount = 1;
					// Update the ingredients hash table
					tempRC.allIngredients.put(ingredient, ingredientCount);
				}
			} //if(line.indexOf(RECORD_END_CHAR) > 0 )
		} //while(fileIn.hasNextLine())
		
		fileIn.close(); // Close the scanner
		return tempRC;	// Return the collection of recipe information.
	}

}


