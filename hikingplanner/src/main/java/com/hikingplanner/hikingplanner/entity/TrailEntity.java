package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
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
@Entity(name="trail")
@Table(name="trail")
public class TrailEntity {
  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long trail_id;
  
  @ManyToOne
  @JoinColumn(name = "mountain_id", referencedColumnName = "mountain_id")
  private Mountain mountain;

  private String trail_name;
  private Double total_length;
  private Long up_time;
  private Long down_time;
  private String start_point;
  private String end_point;
  private String trail_comment;
  private String difficulty;
  private String traildata;
  private Long hike_count;

  public Long getMountainId() {
    return mountain.getMtid();
  }
  public TrailEntity(Long trail_id, Mountain mountain, String trail_name, String trail_comment, String start_point, String end_point, String traildata) {
    this.trail_id = trail_id;
    this.mountain = mountain;
    this.trail_name = trail_name;
    this.trail_comment = trail_comment;
    this.start_point = start_point;
    this.end_point = end_point;
    this.traildata = traildata;
  }

}