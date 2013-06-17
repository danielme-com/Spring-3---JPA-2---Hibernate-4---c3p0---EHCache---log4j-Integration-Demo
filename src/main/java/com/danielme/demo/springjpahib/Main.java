package com.danielme.demo.springjpahib;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
 * @author danielme.com
 * 
 */
public class Main
{
	
    private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) 
	{	
  		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
  		ICountryDao countryDao = (ICountryDao) applicationContext.getBean("countryDao");
  		
  		List<Country> countries = new LinkedList<Country>();
  		countries.add(new Country("Spain", 47265321));
  		countries.add(new Country("Mexico", 115296767));
  		countries.add(new Country("Germany", 81799600));
  		
  		countryDao.addAll(countries);
  		
  		logger.info("===COUNTRY SPAIN-population === " + countryDao.getCountryByName("Spain").getPopulation());
  		logger.info("===COUNTRY MEXICO-population === " + countryDao.getCountryByName("Mexico").getPopulation());
  		logger.info("===COUNTRY GERMANY-population === " + countryDao.getCountryByName("Germany").getPopulation());
  		
  		//debug and test EHCache for the second call ;)
  		//countryDao.getAll();
  		//countryDao.getAll();
  		
  		countryDao.deleteAll();  			
	}		

}