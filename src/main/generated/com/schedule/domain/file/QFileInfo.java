package com.schedule.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileInfo is a Querydsl query type for FileInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileInfo extends EntityPathBase<FileInfo> {

    private static final long serialVersionUID = 1660699708L;

    public static final QFileInfo fileInfo = new QFileInfo("fileInfo");

    public final NumberPath<Integer> cost = createNumber("cost", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> joinMemberCnt = createNumber("joinMemberCnt", Integer.class);

    public final NumberPath<Integer> leaveMemberCnt = createNumber("leaveMemberCnt", Integer.class);

    public final NumberPath<Integer> payment = createNumber("payment", Integer.class);

    public final NumberPath<Integer> revenue = createNumber("revenue", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> time = createDateTime("time", java.time.LocalDateTime.class);

    public QFileInfo(String variable) {
        super(FileInfo.class, forVariable(variable));
    }

    public QFileInfo(Path<? extends FileInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileInfo(PathMetadata metadata) {
        super(FileInfo.class, metadata);
    }

}

