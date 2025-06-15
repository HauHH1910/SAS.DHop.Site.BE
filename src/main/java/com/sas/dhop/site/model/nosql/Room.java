package com.sas.dhop.site.model.nosql;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class Room {
  @Id private String id;

  private String roomId;

  private String name;

  @Builder.Default private List<Message> messages = new ArrayList<>();
}
