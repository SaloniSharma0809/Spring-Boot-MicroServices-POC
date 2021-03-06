package com.moviecatalogmicroservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalogmicroservice.data.MovieInformationClient;
import com.moviecatalogmicroservice.data.RatingDataClient;
import com.moviecatalogmicroservice.models.Catalog;
import com.moviecatalogmicroservice.models.Movie;
import com.moviecatalogmicroservice.models.responses.RatingDTO;
import com.moviecatalogmicroservice.models.responses.UserRating;
import com.moviecatalogmicroservice.services.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	private MovieInformationClient movieInformationClient;
	
	@Autowired
	private RatingDataClient ratingDataClient;

	@Override
	public List<Catalog> getCatalogItem(Long userId) {
		// 1. get all rated movie id's
		UserRating userRating = ratingDataClient.getUserRating(userId);

		List<RatingDTO> listRatingDTO = userRating.getListRatingDTO();

		return listRatingDTO.stream().map(ratingResponse -> {

			String movieId = ratingResponse.getMovieId();

			// 2. for each movie id, call movie info service to get movie details
			Movie movie = movieInformationClient.getMovie(movieId);

			// 3. put them all together
			return new Catalog(movieId, movie.getName(), movie.getDescription(), ratingResponse.getRating());

		}).collect(Collectors.toList());
	}
}
