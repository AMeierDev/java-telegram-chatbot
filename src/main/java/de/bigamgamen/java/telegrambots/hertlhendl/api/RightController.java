package de.bigamgamen.java.telegrambots.hertlhendl.api;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.User;

public interface RightController {
	
	/**
	 * Checks if the User is the Admin
	 * @param user
	 * @return
	 */
	public boolean isAdmin(User user);

}
