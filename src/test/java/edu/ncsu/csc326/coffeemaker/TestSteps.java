/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modified 20171114 by Ian De Silva -- Updated to adhere to coding standards.
 * 
 */
package edu.ncsu.csc326.coffeemaker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc326.coffeemaker.CoffeeMaker;
import edu.ncsu.csc326.coffeemaker.UICmd.ChooseRecipe;
import edu.ncsu.csc326.coffeemaker.UICmd.ChooseService;
import edu.ncsu.csc326.coffeemaker.UICmd.DescribeRecipe;

/**
 * Contains the step definitions for the cucumber tests.  This parses the 
 * Gherkin steps and translates them into meaningful test steps.
 */
public class TestSteps {
	
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;
	private Recipe recipe5;
	private CoffeeMakerUI coffeeMakerMain; 
	private CoffeeMaker coffeeMaker;
	private RecipeBook recipeBook;

	private ChooseService chooseService;

	
	private void initialize() {
		recipeBook = new RecipeBook();
		coffeeMaker = new CoffeeMaker(recipeBook, new Inventory());
		coffeeMakerMain = new CoffeeMakerUI(coffeeMaker);
	}
	
    @Given("^an empty recipe book$")
    public void an_empty_recipe_book() throws Throwable {
        initialize();
    }


    @Given("a default recipe book")
	public void a_default_recipe_book() throws Throwable {
    	initialize();
    	
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
		
		//Set up for r5 (added by MWW)
		recipe5 = new Recipe();
		recipe5.setName("Super Hot Chocolate");
		recipe5.setAmtChocolate("6");
		recipe5.setAmtCoffee("0");
		recipe5.setAmtMilk("1");
		recipe5.setAmtSugar("1");
		recipe5.setPrice("100");

		recipeBook.addRecipe(recipe1);
		recipeBook.addRecipe(recipe2);
		recipeBook.addRecipe(recipe3);
		recipeBook.addRecipe(recipe4);
		
	}

	@When("^user inputs (\\d+)$")
	public void user_inputs(int option) throws Throwable {
		chooseService = new ChooseService(option);


	}

	@Then("^An inventory of (\\d+) coffee, (\\d+) Milk, (\\d+) Sugar and (\\d+) Chocolate is displayed and the mode returns to (\\w+)\\.$")
	public void an_inventory_of_coffee_Milk_Sugar_and_Chocolate_is_displayed(int coffee, int milk, int sugar, int chocolate, String mode) throws Throwable {
		StringBuffer buf = new StringBuffer();
		buf.append("Coffee: ");
		buf.append(coffee);
		buf.append("\n");
		buf.append("Milk: ");
		buf.append(milk);
		buf.append("\n");
		buf.append("Sugar: ");
		buf.append(sugar);
		buf.append("\n");
		buf.append("Chocolate: ");
		buf.append(chocolate);
		buf.append("\n");
        coffeeMakerMain.UI_Input(chooseService);
		assertEquals(buf.toString(), coffeeMaker.checkInventory());
		assertEquals(mode, coffeeMakerMain.mode.toString());

	}
	@When("^user inputs (\\d+), (\\d+), (\\d+),(\\d+),(\\d+),(\\d+), (\\w+)$")
	public void user_inputs_regular(int option, int price, int coffee, int milk, int sugar, int chocolate, String name) throws Throwable {
		chooseService = new ChooseService(option);
		coffeeMakerMain.mode = chooseService.mode;
		recipe1 = new Recipe();
		recipe1.setName(name);
		recipe1.setAmtChocolate(chocolate + "");
		recipe1.setAmtCoffee(coffee+ "");
		recipe1.setAmtMilk(milk + "");
		recipe1.setAmtSugar(sugar +"");
		recipe1.setPrice(price + "");
		DescribeRecipe describeRecipe = new DescribeRecipe(recipe1);
		coffeeMakerMain.UI_Input(describeRecipe);

	}
	@When("^user inputs (\\d+) (\\d+)$")
	public void user_inputs(int option, int recipe) throws Throwable {
		chooseService = new ChooseService(option);
		coffeeMakerMain.mode = chooseService.mode;
		ChooseRecipe chooseRecipe = new ChooseRecipe(recipe);
		coffeeMakerMain.UI_Input(chooseRecipe);
	}

	@Then("^the status is OK and the mode is WAITING$")
	public void the_status_is_OK_and_the_mode_is_WAITING() throws Throwable {
		assertEquals(CoffeeMakerUI.Status.OK, coffeeMakerMain.getStatus());
		assertEquals(CoffeeMakerUI.Mode.WAITING, coffeeMakerMain.getMode());
	}
	@Then("^the status is Out_Of_Range and the mode is WAITING$")
	public void the_status_is_Out_Of_Range_and_the_mode_is_WAITING() throws Throwable {
		assertEquals(CoffeeMakerUI.Status.OUT_OF_RANGE, coffeeMakerMain.getStatus());
		assertEquals(CoffeeMakerUI.Mode.WAITING, coffeeMakerMain.getMode());
	}
    
}
