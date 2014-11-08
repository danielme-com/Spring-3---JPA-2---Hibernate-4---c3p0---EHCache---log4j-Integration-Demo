package com.danielme.demo.springjpahib;

import java.util.List;

/**
 * The DAO for the Country JPA Entity.
 * @author danielme.com
 *
 */
public interface CountryDao
{
	
	  void addAll(List<Country> list);
	 
	  Country getCountryByName(String name);
	  
	  void deleteAll();
	  
	  List<Country> getAll();	
	  
	  Country getById(Long id);

	  void clearAllEntitiesCache();

	  void clearEntityFromCache(Long id);

	  void clearHibernateCache();

}