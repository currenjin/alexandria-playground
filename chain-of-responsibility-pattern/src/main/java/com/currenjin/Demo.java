package com.currenjin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.currenjin.middleware.Middleware;
import com.currenjin.middleware.RoleCheckMiddleware;
import com.currenjin.middleware.ThrottlingMiddleware;
import com.currenjin.middleware.UserExistsMiddleware;
import com.currenjin.server.Server;

/**
 * Demo class. Everything comes together here.
 */
public class Demo {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Server server;

	private static void init() {
		server = new Server();
		server.register("admin@example.com", "admin_pass");
		server.register("user@example.com", "user_pass");

		// All checks are linked. Client can build various chains using the same
		// components.
		Middleware middleware = Middleware.link(
			new ThrottlingMiddleware(2),
			new UserExistsMiddleware(server),
			new RoleCheckMiddleware()
		);

		// Server gets a chain from client code.
		server.setMiddleware(middleware);
	}

	public static void main(String[] args) throws IOException {
		init();

		boolean success;
		do {
			System.out.print("Enter email: ");
			String email = reader.readLine();
			System.out.print("Input password: ");
			String password = reader.readLine();
			success = server.logIn(email, password);
		} while (!success);
	}
}
