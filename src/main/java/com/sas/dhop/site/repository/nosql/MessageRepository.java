package com.sas.dhop.site.repository.nosql;

import com.sas.dhop.site.model.nosql.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {}
