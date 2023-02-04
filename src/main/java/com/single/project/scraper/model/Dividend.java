package com.single.project.scraper.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Dividend {

    private LocalDateTime date;
    private String dividend;
}
