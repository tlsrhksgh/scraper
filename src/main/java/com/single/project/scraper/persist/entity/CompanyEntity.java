package com.single.project.scraper.persist.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity(name = "COMPANY")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;
    private String name;
}
