package com.example.jsonparsing_myattempt;

public class Club {
	String name, address, country, zip, url, number;
	long id;
	double clat, clon;

	public Club(String name, String address, String country, String zip,
			String url, String number, long id, double clat, double clon) {
		this.name = name;
		this.address = address;
		this.country = country;
		this.zip = zip;
		this.url = url;
		this.id = id;
		this.clat = clat;
		this.clon = clon;
		this.number = number;
	}

}
