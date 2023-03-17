package tech.asmussen;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VendingMachine {
	
	private final String name;
	private final String location;
	
	private List<Item> items = new ArrayList<>();
}
