package br.com.battlebits.ycommon.bungee.utils.json;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.common.utils.json.TypeUtils;

public class TypeBungeeUtils extends TypeUtils {

	@Override
	public Type getTranslateMap() {
		return new TypeToken<HashMap<String, String>>() {
		}.getType();
	}

}
