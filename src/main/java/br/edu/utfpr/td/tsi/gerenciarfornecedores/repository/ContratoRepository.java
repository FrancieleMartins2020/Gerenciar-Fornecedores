package br.edu.utfpr.td.tsi.gerenciarfornecedores.repository;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository
        extends JpaRepository<Contrato, Long> {

    List<Contrato> findByFornecedorId(Long fornecedorId);
}
