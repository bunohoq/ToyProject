package com.test.toy.board.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	private String seq;
	private String subject;
	private String content;
	private String id;
	private String regdate;
	private String readcount;
}
