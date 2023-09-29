package com.bombo.goatodo.domain.statistics;

import com.bombo.goatodo.domain.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "daily_statistics")
public class DailyStatistics extends StatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
