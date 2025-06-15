package com.sas.dhop.site.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article")
public class Article extends AbstractEntity<Integer> implements Serializable {

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "author_name")
	private String authorName;

	@Lob
	@Column(name = "thumbnail")
	private String thumbnail;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
}
