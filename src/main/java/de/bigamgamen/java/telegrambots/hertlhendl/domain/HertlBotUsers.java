package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.Optional;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import one.microstream.persistence.types.Persister;

public class HertlBotUsers {

	private final Map<Long, HertlBotUser> users = new HashMap<>();

	public HertlBotUsers() {
		super();
	}
	

	public void add(
		final HertlBotUser HertlBotUser,
		final Persister persister
	)
	{
		this.users.put(HertlBotUser.getChatId(), HertlBotUser);
		persister.store(this.users);
	}

	public void addAll(final Collection<? extends HertlBotUser> HertlBotUsers)
	{
		this.addAll(HertlBotUsers, HertlBotRootDao.storageManager());
	}
	
	public void addAll(
		final Collection<? extends HertlBotUser> users,
		final Persister persister
	)
	{
		this.users.putAll(
				users.stream().collect(
				Collectors.toMap(HertlBotUser::getChatId, Function.identity())
			)
		);
		persister.store(this.users);
	}
	
	public int HertlBotUserCount()
	{
		return this.users.size();
	}

	public List<HertlBotUser> all()
	{
		return new ArrayList<>(this.users.values());
	}

	public Optional<HertlBotUser> ofId(final long chatId)
	{
		return Optional.ofNullable(this.users.get(chatId));
	}
	
	

}
