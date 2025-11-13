package com.semicolon.backend.domain.facility.entity;

import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "tbl_facility")
@ToString(exclude = {"spaces", "dailyUses"})
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @Column(name="facility_name", nullable = false, length = 100)
    private String facilityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_Type", nullable = false, length = 10)
    private FacilityType facilityType;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "dailyUse_available", nullable = false)
    @Builder.Default
    private boolean dailyUseAvailable = false;

    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    @Builder.Default
    private List<FacilitySpace> spaces = new ArrayList<>();

    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DailyUse> dailyUses = new ArrayList<>();


}
