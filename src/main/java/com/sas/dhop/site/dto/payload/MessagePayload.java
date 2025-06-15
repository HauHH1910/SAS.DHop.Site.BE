package com.sas.dhop.site.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
	private String sender;
	private String content;
	private String roomId;
}
