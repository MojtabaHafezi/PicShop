package at.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseDomain extends AbstractPersistable<Long> {

    @Version
    private Integer version;

}
