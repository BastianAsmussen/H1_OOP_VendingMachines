package tech.asmussen;

import tech.asmussen.sql.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		Connection conn = MySQL.connect("localhost", 3306, "company", "root", "toor");
		
		// Load vending machines and items from the database.
		ResultSet results = MySQL.query(conn, """
				
				SELECT v.name   AS "machine_name",
				       location AS "machine_location",
				       i.name   AS "item",
				       price,
				       calories
				FROM Vending_Machines v
				         JOIN Items i on i.id = v.item""");
		
		if (results == null) {
			
			System.err.println("No data in the system, exiting...");
			
			return;
		}
		
		List<VendingMachine> machines = new ArrayList<>();
		List<Item> items = new ArrayList<>();
		
		while (results.next()) {
			
			String machineName = results.getString("machine_name");
			String machineLocation = results.getString("machine_location");
			
			String itemName = results.getString("item");
			double itemPrice = results.getDouble("price");
			int itemCalories = results.getInt("calories");
			
			// Add the machine if it doesn't exist.
			if (machines.stream().noneMatch(m -> m.getName().equals(machineName))) {
				
				machines.add(new VendingMachine(machineName, machineLocation));
			}
			
			// Add the item if it doesn't exist.
			if (items.stream().noneMatch(i -> i.getName().equals(itemName))) {
				
				items.add(new Item(itemName, itemPrice, itemCalories));
			}
			
			// Get the machine.
			VendingMachine machine =
					machines.stream().filter(m -> m.getName().equals(machineName)).findFirst().orElse(null);
			
			if (machine == null) {
				
				System.err.println("Could not find machine with name " + machineName);
				
				continue;
			}
			
			// Get the item.
			Item item = items.stream().filter(i -> i.getName().equals(itemName)).findFirst().orElse(null);
			
			if (item == null) {
				
				System.err.println("Could not find item with name " + itemName);
				
				continue;
			}
			
			// Add the item to the machine.
			machine.getItems().add(item);
		}
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			
			try {
				
				for (int i = 0; i < machines.size(); i++) {
					
					System.out.println((i + 1) + ". " + machines.get(i).getName());
				}
				
				System.out.print("Enter a vending machine name: ");
				int machineIndex = scanner.nextInt() - 1 % machines.size();
				
				VendingMachine machine = machines.get(machineIndex);
				List<Item> machineItems = machine.getItems();
				
				for (int i = 0; i < machineItems.size(); i++) {
					
					System.out.println((i + 1) + ". " + machineItems.get(i).getName());
				}
				
				System.out.print("Enter an item name: ");
				int itemIndex = scanner.nextInt() - 1 % machineItems.size();
				
				Item item = machineItems.get(itemIndex);
				
				System.out.printf("You have selected %s, which costs $%.2f and has %d calories.\n",
								  item.getName(), item.getPrice(), item.getCalories());
				
				System.out.print("Would you like to purchase this item? (y/N): ");
				boolean isBuying = scanner.next().equalsIgnoreCase("y");
				
				if (isBuying) {
					
					System.out.println("Thank you for your purchase!");
					
				} else {
					
					System.out.println("Purchase cancelled, returning to menu...");
				}
				
			} catch (Exception e) {
				
				System.err.println("An error occurred, please try again!");
				
				scanner.nextLine();
			}
		}
	}
}
