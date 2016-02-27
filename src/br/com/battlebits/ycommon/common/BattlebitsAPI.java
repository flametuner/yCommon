package br.com.battlebits.ycommon.common;

import java.util.logging.Logger;

import br.com.battlebits.ycommon.common.manager.AccountCommon;

public class BattlebitsAPI {

	private static AccountCommon accountCommon = new AccountCommon();
	private static Logger logger = Logger.getLogger("Minecraft");
	private static boolean debugMode = true;
	public final static String FORUM_WEBSITE = "";
	public final static String WEBSITE = "";
	public final static String STORE = "";
	public final static String ADMIN_EMAIL = "";
	public final static String TWITTER = "";
	public final static String HUNGERGAMES_ADDRESS = "";
	

	public static AccountCommon getAccountCommon() {
		return accountCommon;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getBungeeChannel() {
		return "yCommon";
	}

	public static boolean debugModeEnabled() {
		return debugMode;
	}

	public static void debug(String debugStr) {
		if (debugMode)
			logger.info("[DEBUG] " + debugStr);
	}

}