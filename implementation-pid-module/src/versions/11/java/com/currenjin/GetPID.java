package com.currenjin;

public class GetPID {
	public static long getPid() {
		var ph = ProcessHandle.current();
		return ph.pid();
	}
}
