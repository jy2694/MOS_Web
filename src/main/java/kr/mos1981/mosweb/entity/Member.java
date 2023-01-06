package kr.mos1981.mosweb.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private String studentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean administrator;

    @Column(nullable = false)
    private Long generation;

    @Builder
    public Member(String studentId, String name, Boolean administrator, Long generation){
        this.studentId = studentId;
        this.name = name;
        this.administrator = administrator;
        this.generation = generation;
    }
}
