package gov.taxes.infra.github.base;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.taxes.infra.github.base.BaseAbstractEntity;

public interface BaseRepository<E extends BaseAbstractEntity, Long> extends JpaRepository<E, Long> {

}
