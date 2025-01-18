package com.currenjin;

import java.lang.management.ManagementFactory;

public class GetPID {
	public static long getPid() {
		System.out.println("Java 8 version...");

		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		int index = jvmName.indexOf('@');

		if (index < 1) {
			return 0;
		}

		try {
			return Long.parseLong(jvmName.substring(0, index));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}