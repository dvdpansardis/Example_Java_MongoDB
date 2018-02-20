package br.com.alura.escolalura.escolalura.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

import br.com.alura.escolalura.escolalura.model.Contato;

@Service
public class GeolocalizacaoService {

	public List<Double> obterLatLongPor(Contato contato) throws Exception{
		String key = "AIzaSyCUmHkeCKoJyirlqfHTkexfz3q1kcZzv7c";
		
		GeoApiContext geoApiContext = new GeoApiContext().setApiKey(key);
		
		GeocodingApiRequest request = GeocodingApi.newRequest(geoApiContext).address(contato.getEndereco());
		
		GeocodingResult[] results = request.await();
		
		GeocodingResult result = results[0];
		
		Geometry geometry = result.geometry;
		
		LatLng location = geometry.location;
		
		return Arrays.asList(location.lat, location.lng);
	}
	
}
