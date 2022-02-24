package com.bithumbsystems.webflux.domains;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id private String id;
    private String name;

    public Image(String id) {
        this.id = id;
    }
}