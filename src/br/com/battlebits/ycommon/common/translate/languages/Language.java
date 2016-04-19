package br.com.battlebits.ycommon.common.translate.languages;

public enum Language {

	ENGLISH("English"), PORTUGUES("PortuguÍs");

	private String name;

	private Language(String languageName) {
		this.name = languageName;
	}
	
	public String getName() {
		return name;
	}

}
