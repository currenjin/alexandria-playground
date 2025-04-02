package com.currenjin.performance;

import java.util.ArrayList;
import java.util.List;

public class ListSearch {
	private List<Integer> numbers;
	private int probe;

	public ListSearch(int size) {
		numbers = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			numbers.add(i);
			probe = size / 2;
		}
	}

	public void search() {
		numbers.contains(probe);
	}
}
