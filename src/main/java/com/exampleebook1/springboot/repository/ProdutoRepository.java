package com.exampleebook1.springboot.repository;

import com.exampleebook1.springboot.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel,Long> {
}
