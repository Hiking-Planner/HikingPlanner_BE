package com.hikingplanner.hikingplanner.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Mountain {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mountain_id")
  private Long mtid;
  @Column
  private String mountain_name;
  @Column
  private String location;
  @Column
  private Double elevation;
  @Column
  private String mountain_comment;
  @Column
  private Double latitude;
  @Column
  private Double longitude;

  @OneToMany(mappedBy = "mountain", cascade = CascadeType.ALL)
  private List<MountainImgEntity> images;

  @OneToMany(mappedBy = "mountain", fetch = FetchType.LAZY)
  private List<TrailEntity> TrailList = new ArrayList<>();
}
