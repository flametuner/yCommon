package br.com.battlebits.ycommon.bungee.utils.mojang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.battlebits.ycommon.common.utils.mojang.NameFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeNameFetcher extends NameFetcher {

	private JsonParser parser = new JsonParser();

	private Cache<UUID, String> uuidName = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.DAYS).build(new CacheLoader<UUID, String>() {
		@Override
		public String load(UUID id) throws Exception {
			return loadName(id);
		}
	});

	private String loadName(UUID id) {
		String name = null;
		ProxiedPlayer p = BungeeCord.getInstance().getPlayer(id);
		if (p != null) {
			name = p.getName();
			p = null;
		} else {
			name = loadFromServers(id);
		}
		return name;
	}

	@Override
	public String load(UUID id, String server) {
		String name = null;
		try {
			String[] infos = server.split("#");
			String serverUrl = infos[0].replace("%player-uuid%", id.toString().replace("-", ""));
			URL url = new URL(serverUrl);
			InputStream is = url.openStream();
			InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			JsonObject object = parser.parse(streamReader).getAsJsonObject();
			if (object.has(infos[1]) && object.has(infos[2]) && object.get(infos[2]).getAsString().equalsIgnoreCase(id.toString().replace("-", ""))) {
				name = object.get(infos[1]).getAsString();
			}
			streamReader.close();
			is.close();
			object = null;
			streamReader = null;
			is = null;
			url = null;
			serverUrl = null;
			infos = null;
			server = null;
			id = null;
		} catch (Exception ex) {
			return null;
		}
		return name;
	}

	@Override
	public String getUsername(UUID id) {
		try {
			return uuidName.get(id, new Callable<String>() {
				@Override
				public String call() throws Exception {
					return loadName(id);
				}
			});
		} catch (Exception e) {
			return null;
		}
	}

}
