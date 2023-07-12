package com.example.voting.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "from_id")
    private String fromId;

    @Column(name = "to_id")
    private String toId;


}
