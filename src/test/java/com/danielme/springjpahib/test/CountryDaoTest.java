package com.danielme.springjpahib.test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.danielme.demo.springjpahib.ApplicationContext;
import com.danielme.demo.springjpahib.Country;
import com.danielme.demo.springjpahib.CountryDao;

/**
 * Some test cases.
 * 
 * @author danielme.com
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/applicationContext.xml")
//@ContextConfiguration(classes = { ApplicationContext.class})
public class CountryDaoTest
{

	@Autowired
	private CountryDao countryDao;

	@Before
	@Transactional
	public void setUp() throws Exception
	{
		countryDao.deleteAll();

		List<Country> countries = new LinkedList<Country>();
		countries.add(new Country("Spain", 47265321));
		countries.add(new Country("Mexico", 115296767));
		countries.add(new Country("Germany", 81799600));

		countryDao.addAll(countries);
	}

	@Test
	public void simpleMethods()
	{
		assertEquals(countryDao.getAll().size(), 3);
		Country country = countryDao.getCountryByName("Spain");
		assertEquals(country.getName(), "Spain");
		assertEquals(countryDao.getById(country.getId()).getName(), "Spain");
	}

	@Test
	public void cacheTest()
	{
		assertEquals(countryDao.getAll().size(), 3);
		// check the log in debug mode: "Returning cached query results"
		assertEquals(countryDao.getAll().size(), 3);

		Country country = countryDao.getCountryByName("Spain");
		countryDao.getById(country.getId());
		// get the country from the cache because this entity is annotated with
		// @org.hibernate.annotations.Cache
		assertEquals(countryDao.getById(country.getId()).getName(), "Spain");

		countryDao.clearEntityFromCache(country.getId());
		// this time the entity is retrieved from the db
		assertEquals(countryDao.getById(country.getId()).getName(), "Spain");
	}

}
