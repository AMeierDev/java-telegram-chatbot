package de.bigamgamen.java.telegrambots.hertlhendl.controller;

import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.User;

import  de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RightController;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RoleController;

public class HertlRoleController implements RoleController
{

	private static List<String> ADMIN_SET = Arrays.asList(
			HertlHendlBot.ABILITY_NAME_HELP,
			HertlHendlBot.ABILITY_NAME_LOCATION_PHOTO,
			HertlHendlBot.ABILITY_NAME_PRICES_PHOTO, 
			HertlHendlBot.ABILITY_NAME_ITEM_LIST,
			HertlHendlBot.ABILITY_NAME_NEW_ORDER, 
			HertlHendlBot.ABILITY_NAME_LIST_MY_ORDERS, 
			HertlHendlBot.ABILITY_NAME_MY_ORDERS_AS_KEYBOARD,			
			HertlHendlBot.ABILITY_NAME_MY_OPEN_ORDERS,
			HertlHendlBot.ABILITY_NAME_ADMIN_OPEN_ORDERS,
			HertlHendlBot.ABILITY_NAME_ADMIN_CLOSE_ORDERS,
			HertlHendlBot.ABILITY_NAME_ADD_POSITION,
			HertlHendlBot.ABILITY_NAME_ORDER,
			HertlHendlBot.ABILITY_NAME_COMMIT_ORDER,
			HertlHendlBot.ABILITY_NAME_CLOSE_ORDER);
	private static List<String> USER_SET = Arrays.asList(
			HertlHendlBot.ABILITY_NAME_HELP,
			HertlHendlBot.ABILITY_NAME_LOCATION_PHOTO,
			HertlHendlBot.ABILITY_NAME_PRICES_PHOTO, 
			HertlHendlBot.ABILITY_NAME_ITEM_LIST,
			HertlHendlBot.ABILITY_NAME_NEW_ORDER, 
			HertlHendlBot.ABILITY_NAME_LIST_MY_ORDERS, 
			HertlHendlBot.ABILITY_NAME_MY_ORDERS_AS_KEYBOARD,			
			HertlHendlBot.ABILITY_NAME_MY_OPEN_ORDERS,
			HertlHendlBot.ABILITY_NAME_ADD_POSITION,
			HertlHendlBot.ABILITY_NAME_ORDER,
			HertlHendlBot.ABILITY_NAME_COMMIT_ORDER,
			HertlHendlBot.ABILITY_NAME_CLOSE_ORDER);

	private RightController rigthController;

	public HertlRoleController(RightController rigthController)
	{
		this.rigthController = rigthController;
	}

	@Override
	public List<String> getAbilitiesForUser(User user)
	{
		if (rigthController.isAdmin(user))
		{
			return ADMIN_SET;
		} else
		{
			return USER_SET;
		}
	}

	@Override
	public Boolean canUseAbility(User user, String ability)
	{
		List<String> roles = getAbilitiesForUser(user);
		return roles.contains(ability);
	}

}
