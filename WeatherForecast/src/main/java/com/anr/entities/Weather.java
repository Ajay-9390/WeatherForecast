package com.anr.entities;

import java.util.List;

public class Weather {

	private String city;
	private double temperature;
	private String condition;
	private List<WeatherForecast> forecast;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<WeatherForecast> getForecast() {
		return forecast;
	}

	public void setForecast(List<WeatherForecast> forecast) {
		this.forecast = forecast;
	}

}
