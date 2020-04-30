package vn.vsd.agro.dto;

import java.util.List;

public class NewsSearchDto extends SearchDto {
	

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841253307465218951L;
	
	
	private String authorName;
	
	private List<String> authorIds;
	
	public NewsSearchDto() {
		super();
	}
	
	public NewsSearchDto(SearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
	}
	
	public NewsSearchDto(NewsSearchDto dto) {
		super(dto);
		// TODO Auto-generated constructor stub
		this.authorName = dto.getAuthorName();
		this.authorIds = dto.getAuthorIds();
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public List<String> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<String> authorIds) {
		this.authorIds = authorIds;
	}
	

	

	
}
