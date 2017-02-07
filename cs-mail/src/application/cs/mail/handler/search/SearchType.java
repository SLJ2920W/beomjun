package application.cs.mail.handler.search;

public enum SearchType {

	NONE("-"), TITLE("제목"), CONTENT("내용"), DATE("날짜"), TO("보낸사람"), FROM("받는사람");
	
	// TITLE, CONTENT, NONE;

	private String searchType;

	private SearchType(String searchType) {
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		return searchType;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

}
