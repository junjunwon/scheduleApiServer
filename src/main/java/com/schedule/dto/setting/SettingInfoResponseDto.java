package com.schedule.dto.setting;

import com.schedule.domain.setting.SettingInfo;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
public class SettingInfoResponseDto {

    private String configKey;
    private String configValue;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SettingInfoResponseDto(SettingInfo entity) {
        this.configKey = entity.getConfigKey();
        this.configValue = entity.getConfigValue();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
