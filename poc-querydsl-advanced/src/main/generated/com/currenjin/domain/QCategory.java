package com.currenjin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 219602369L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategory category = new QCategory("category");

    public final ListPath<Category, QCategory> children = this.<Category, QCategory>createList("children", Category.class, QCategory.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QCategory parent;

    public final ListPath<Product, QProduct> products = this.<Product, QProduct>createList("products", Product.class, QProduct.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QCategory(String variable) {
        this(Category.class, forVariable(variable), INITS);
    }

    public QCategory(Path<? extends Category> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategory(PathMetadata metadata, PathInits inits) {
        this(Category.class, metadata, inits);
    }

    public QCategory(Class<? extends Category> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QCategory(forProperty("parent"), inits.get("parent")) : null;
    }

}

