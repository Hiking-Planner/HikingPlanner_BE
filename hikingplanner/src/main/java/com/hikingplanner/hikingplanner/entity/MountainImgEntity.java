package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity(name="mountain_img")
@Table(name="mountain_img")
public class MountainImgEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mtimg_id;
  
  @ManyToOne
  @JoinColumn(name = "mountain_id")
  private Mountain mountain;

  private String img_url;

  private String img_name;
  
}

