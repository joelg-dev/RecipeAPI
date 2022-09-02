package io.cd21.recipeapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue
    @ReadOnlyProperty
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Column(name = "create_time")
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updateTime;

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
