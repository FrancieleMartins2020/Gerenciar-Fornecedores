package br.edu.utfpr.td.tsi.gerenciarfornecedores.service;

import br.edu.utfpr.td.tsi.gerenciarfornecedores.dto.FornecedorRequest;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.exception.ResourceNotFoundException;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Contrato;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Fornecedor;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.repository.ContratoRepository;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;
    private final ContratoRepository contratoRepository;
    public FornecedorService(FornecedorRepository repository, ContratoRepository contratoRepository) {
        this.repository = repository;
        this.contratoRepository = contratoRepository;
    }

    public List<Fornecedor> listar() {
        return repository.findAll();
    }

    public Fornecedor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fornecedor não encontrado."));
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fornecedor não encontrado para o CNPJ informado."));
    }
    public String validar(FornecedorRequest request, Long idEdicao) {

        if (request.getRazaoSocial() == null || request.getRazaoSocial().isBlank()) {
            return "Razão Social é obrigatória.";
        }

        if (request.getCnpj() == null || request.getCnpj().isBlank()) {
            return "CNPJ é obrigatório.";
        }

        String cnpjLimpo = request.getCnpj().replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() != 14) {
            return "O CNPJ deve conter exatamente 14 dígitos.";
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String regexEmail = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            if (!request.getEmail().matches(regexEmail)) {
                return "Por favor, insira um formato de e-mail válido (exemplo@dominio.com).";
            }
        }

        var fornecedorExistente = repository.findByCnpj(request.getCnpj());
        if (fornecedorExistente.isPresent()) {
            if (idEdicao == null || !fornecedorExistente.get().getId().equals(idEdicao)) {
                return "Já existe um fornecedor cadastrado com esse CNPJ.";
            }
        }

        if (request.getTelefone() == null || request.getTelefone().isBlank()) {
            return "Telefone é obrigatório.";
        }

        if (request.getEndereco() == null || request.getEndereco().isBlank()) {
            return "Endereço é obrigatório.";
        }

        String telefoneLimpo = request.getTelefone().replaceAll("[^0-9]", "");
        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            return "Por favor, insira um número de telefone válido com DDD (10 ou 11 dígitos).";
        }

        return null;
    }

    public FornecedorRequest buscarParaEdicao(Long id) {
        Fornecedor fornecedor = buscarPorId(id);

        FornecedorRequest request = new FornecedorRequest();
        request.setId(fornecedor.getId());
        request.setRazaoSocial(fornecedor.getRazaoSocial());
        request.setNomeFantasia(fornecedor.getNomeFantasia());
        request.setCnpj(fornecedor.getCnpj());
        request.setEmail(fornecedor.getEmail());
        request.setTelefone(fornecedor.getTelefone());
        request.setEndereco(fornecedor.getEndereco());

        return request;
    }

    public Fornecedor salvar(FornecedorRequest request) {

        String erroValidacao = validar(request, null);
        if (erroValidacao != null) {
            throw new IllegalArgumentException(erroValidacao);
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(request.getRazaoSocial());
        fornecedor.setNomeFantasia(request.getNomeFantasia());
        fornecedor.setCnpj(request.getCnpj());
        fornecedor.setEmail(request.getEmail());
        fornecedor.setTelefone(request.getTelefone());
        fornecedor.setEndereco(request.getEndereco());

        return repository.save(fornecedor);
    }

    public Fornecedor atualizar(FornecedorRequest request) {
        String erroValidacao = validar(request, request.getId());
        if (erroValidacao != null) {
            throw new IllegalArgumentException(erroValidacao);
        }

        Fornecedor fornecedor = buscarPorId(request.getId());
        fornecedor.setRazaoSocial(request.getRazaoSocial());
        fornecedor.setNomeFantasia(request.getNomeFantasia());
        fornecedor.setCnpj(request.getCnpj());
        fornecedor.setEmail(request.getEmail());
        fornecedor.setTelefone(request.getTelefone());
        fornecedor.setEndereco(request.getEndereco());

        return repository.save(fornecedor);
    }

    public void excluir(Long id) {
        Fornecedor fornecedor = buscarPorId(id);

        List<Contrato> todosContratos = contratoRepository.findByFornecedorId(id);

        if (!todosContratos.isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir o fornecedor '" + fornecedor.getRazaoSocial() +
                    "' porque ele possui contratos vinculados.");
        }
        repository.delete(fornecedor);
    }
}
