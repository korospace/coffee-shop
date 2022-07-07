package edu.mum.coffee.repository;

import edu.mum.coffee.domain.Diskon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskonRepository extends JpaRepository<Diskon, Long> {
    public Diskon findByCode(String code);
    public Diskon findById(Long id);

    @Query(nativeQuery = true,value = "select * from diskon where code = ?1 and id != ?2")
    public Diskon findByCodeExcept(String code,Long id);

}
