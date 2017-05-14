package me.xaanit.apparatus.objects.json;

/**
 * Created by Jacob on 4/21/2017.
 */
public class Config {

	private String token;

	private String sponsoredGuildID;

	private String logChannelID;

	public Config() {
	}

	public String getToken() {
		return this.token;
	}

	public String getLogChannelID() {
		return logChannelID;
	}

	public String getSponsoredGuildID() {
		return sponsoredGuildID;
	}
}
