package de.bigamgamen.java.telegrambots.hertlhendl.helper;

import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramHelper
{
	private TelegramHelper()
	{
		
	}
	
	public static String getTotalUserName(User user)
	{
		return 
				(user.getFirstName() != null ? user.getFirstName() : "") 
				+" "
				+ (user.getLastName() != null ? user.getLastName() : "") 
				+"(" + (user.getUserName() != null ? user.getUserName() : "") +")";
	}
}
