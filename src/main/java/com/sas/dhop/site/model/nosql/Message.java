package com.sas.dhop.site.model.nosql;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private String sender;
  private String content;
  private LocalDateTime timeStamp;
}
