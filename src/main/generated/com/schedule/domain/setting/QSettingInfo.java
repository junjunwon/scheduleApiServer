package com.schedule.domain.setting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSettingInfo is a Querydsl query type for SettingInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSettingInfo extends EntityPathBase<SettingInfo> {

    private static final long serialVersionUID = 303252480L;

    public static final QSettingInfo settingInfo = new QSettingInfo("settingInfo");

    public final StringPath configKey = createString("configKey");

    public final StringPath configValue = createString("configValue");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public QSettingInfo(String variable) {
        super(SettingInfo.class, forVariable(variable));
    }

    public QSettingInfo(Path<? extends SettingInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSettingInfo(PathMetadata metadata) {
        super(SettingInfo.class, metadata);
    }

}

