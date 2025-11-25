package com.semicolon.backend.domain.facility.entity;


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
@ToString(exclude = {"spaces"})
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

    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    @Builder.Default
    private List<FacilitySpace> spaces = new ArrayList<>();

}
