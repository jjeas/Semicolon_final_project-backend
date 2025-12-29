package com.semicolon.backend.domain.faq.entity;

import jakarta.persistence.*;
import lombok.Getter;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "tbl_category")
@Getter
public class FaqCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_category_id")
    private Long faqCategoryId;

    @Column(name = "category_name",nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "faqCategory")
    private List<Faq> faqs=new ArrayList<>();
}
