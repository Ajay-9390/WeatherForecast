package com.anr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.anr.entities.Weather;
import com.anr.entities.WeatherForecast;

@Service
public class WeatherService {

	@Value("${weather.api.key}")
	private String apiKey;

	private final RestTemplate restTemplate = new RestTemplate();

	public Weather getWeather(String city) {
		try {
			// Fetch current weather
			Map<String, Object> weatherResponse = fetchApiData(buildWeatherUrl(city));
			if (weatherResponse == null || "404".equals(weatherResponse.get("cod"))) {
				System.out.println("City not found in weather response");
				return null; // Return null if city not found
			}

			Weather weather = parseCurrentWeather(weatherResponse);

			// Fetch 5-day forecast
			Map<String, Object> forecastResponse = fetchApiData(buildForecastUrl(city));
			if (forecastResponse == null || "404".equals(forecastResponse.get("cod"))) {
				System.out.println("City not found in forecast response");
				return null; // Return null if city not found
			}

			List<WeatherForecast> forecast = parseWeatherForecast(forecastResponse);
			weather.setForecast(forecast);

			return weather;
		} catch (HttpClientErrorException e) {
			System.err.println("HTTP error: " + e.getMessage());
			return null; // Return null for other errors
		} catch (Exception e) {
			System.err.println("Unexpected error: " + e.getMessage());
			return null; // Handle unexpected errors
		}
	}

	private String buildWeatherUrl(String city) {
		return "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
	}

	private String buildForecastUrl(String city) {
		return "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric";
	}

	private Map<String, Object> fetchApiData(String url) {
		return restTemplate.getForObject(url, Map.class);
	}

	private Weather parseCurrentWeather(Map<String, Object> response) {
		Weather weather = new Weather();
		weather.setCity((String) response.get("name"));
		Map<String, Object> main = (Map<String, Object>) response.get("main");
		weather.setTemperature((Double) main.get("temp"));
		List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
		weather.setCondition((String) weatherList.get(0).get("description"));
		return weather;
	}

	private List<WeatherForecast> parseWeatherForecast(Map<String, Object> forecastResponse) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) forecastResponse.get("list");
		List<WeatherForecast> forecast = new ArrayList<>();
		for (Map<String, Object> item : list) {
			WeatherForecast weatherForecast = new WeatherForecast();
			weatherForecast.setDate((String) item.get("dt_txt"));
			Map<String, Object> mainMap = (Map<String, Object>) item.get("main");
			weatherForecast.setTemperature(((Number) mainMap.get("temp")).doubleValue());
			List<Map<String, Object>> weatherListMap = (List<Map<String, Object>>) item.get("weather");
			weatherForecast.setCondition((String) weatherListMap.get(0).get("description"));
			forecast.add(weatherForecast);
		}
		return forecast;
	}
}
