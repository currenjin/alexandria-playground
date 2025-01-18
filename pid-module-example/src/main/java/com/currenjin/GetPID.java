package com.currenjin;

import java.lang.management.ManagementFactory;

public class GetPID {
	public static long getPid() {
		System.out.println("Java 8 version...");

		final var jvmName = ManagementFactory.getRuntimeMXBean().getPid();
		final var index = jvmName.indexOf('@');

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
