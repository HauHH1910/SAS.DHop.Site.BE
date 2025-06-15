package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.payload.MessagePayload;
import com.sas.dhop.site.model.nosql.Message;

public interface ChatService {

  Message sendMessage(String roomId, MessagePayload payload);
}
