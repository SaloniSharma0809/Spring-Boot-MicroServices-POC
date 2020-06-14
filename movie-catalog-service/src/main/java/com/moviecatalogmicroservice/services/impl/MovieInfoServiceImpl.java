package com.moviecatalogmicroservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.moviecatalogmicroservice.models.Movie;
import com.moviecatalogmicroservice.services.MovieInfoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MovieInfoServiceImpl implements MovieInfoService {

	@Autowired // consumer
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "getFallbackMovie")
	@Override
	public Movie getMovie(String movieId) {
		// for movie id, call movie info service to get movie details
		Movie movie = restTemplate.getForObject("http://movie-info-microservice/movie/" + movieId, Movie.class);
		return movie;
	}

	public Movie getFallbackMovie(String movieId) {
		Movie movie = new Movie();
		movie.setId(movieId);
		movie.setName("Name not found");
		movie.setDescription("Description not found");
		return movie;
	}
}
