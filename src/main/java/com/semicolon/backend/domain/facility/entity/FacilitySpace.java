package com.semicolon.backend.domain.facility.entity;

import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_facilitySpace")
@ToString(exclude = {"facility", "dailyUses"})
public class FacilitySpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "space_name",nullable = false,length = 100)
    private String spaceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "space_category",nullable = false,length = 20)
    private FacilityType facilityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id",nullable = false)
    private Facility facility;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DailyUse> dailyUses = new ArrayList<>();
}
