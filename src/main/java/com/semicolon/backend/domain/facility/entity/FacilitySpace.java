package com.semicolon.backend.domain.facility.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tbl_facility_space")
@ToString(exclude = {"facility"})
public class FacilitySpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "space_name",nullable = false,length = 100)
    private String spaceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id",nullable = false)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    @Column(name = "space_roomType", nullable = false, length = 10)
    private SpaceRoomType spaceRoomType;

}
