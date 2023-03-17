package tech.asmussen;

import lombok.Data;

@Data
public class Item {
	
	private final String name;
	
	private final double price;
	private final int calories;
}
