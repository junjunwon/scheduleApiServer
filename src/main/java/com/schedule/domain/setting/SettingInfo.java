package com.schedule.domain.setting;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
public class SettingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //설정명
    @Column(nullable = false)
    private String configKey;

    //설정값
    private String configValue;

    //생성일
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdDate;

    //수정일
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Builder
    public SettingInfo(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }
}
