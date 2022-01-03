package de.bigamgamen.java.telegrambots.hertlhendl.controller;

import org.telegram.telegrambots.meta.api.objects.User;

import de.bigamgamen.java.telegrambots.hertlhendl.api.RightController;

public class HertlRightController implements RightController{

	private final Long adminId; 

    
	public HertlRightController(Long adminId) {
		this.adminId = adminId;
	}

	
	@Override
	public boolean isAdmin(User user) {
		return user.getId().equals(this.adminId);
	}


}
