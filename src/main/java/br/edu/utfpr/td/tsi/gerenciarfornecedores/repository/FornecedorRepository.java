package br.edu.utfpr.td.tsi.gerenciarfornecedores.repository;

import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository
        extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByCnpj(String cnpj);
}
