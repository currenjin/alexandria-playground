package com.currenjin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class TypeInterfaceTest {
	@Test
	void type_interface() {
		List<Integer> before = Collections.<Integer>emptyList();
		List<Integer> after = Collections.emptyList();
	}

	@Test
	void type_interface_diamond_syntax() {
		Map<Integer, Map<String, String>> beforeUserLists = new HashMap<Integer, Map<String, String>>();
		Map<Integer, Map<String, String>> afterUserLists = new HashMap<>();
	}

	@Test
	void lambda() {
		Function<String, Integer> lengthFn = s -> s.length();
	}

	@Test
	void local_variable_type_interface() {
		var names = new ArrayList<String>();
	}

	// MEMO: Bad code
	public class Var {
		private static Var var = null;

		public static Var var() {
			return var;
		}

		public static void var(Var var) {
			Var.var = var;
		}
	}

	@Test
	void var_test() {
		var var = Var.var();
		if (var == null) {
			Var.var(new Var());
		}
	}

	@Test
	void fail_example() {
		// var fn = s -> s.length();
		// var n = null;
	}

	@Test
	void nondenotable_type() {
		var duck = new Object() {
			void quack() {
				System.out.println("Quack!");
			}
		};

		duck.quack();
	}
}