package com.schedule.testLesson;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberDetail is a Querydsl query type for MemberDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberDetail extends EntityPathBase<MemberDetail> {

    private static final long serialVersionUID = -1269437659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberDetail memberDetail = new QMemberDetail("memberDetail");

    public final StringPath desc = createString("desc");

    public final QMember member;

    public final NumberPath<Long> memberDetailId = createNumber("memberDetailId", Long.class);

    public final StringPath type = createString("type");

    public QMemberDetail(String variable) {
        this(MemberDetail.class, forVariable(variable), INITS);
    }

    public QMemberDetail(Path<? extends MemberDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberDetail(PathMetadata metadata, PathInits inits) {
        this(MemberDetail.class, metadata, inits);
    }

    public QMemberDetail(Class<? extends MemberDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

