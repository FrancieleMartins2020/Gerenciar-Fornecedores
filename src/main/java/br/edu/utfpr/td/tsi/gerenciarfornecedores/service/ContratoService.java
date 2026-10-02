package br.edu.utfpr.td.tsi.gerenciarfornecedores.service;

import br.edu.utfpr.td.tsi.gerenciarfornecedores.dto.ContratoRequest;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.exception.ResourceNotFoundException;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Contrato;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Fornecedor;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.repository.ContratoRepository;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final FornecedorService fornecedorService;

    public ContratoService(
            ContratoRepository contratoRepository,
            FornecedorRepository fornecedorRepository,
            FornecedorService fornecedorService) {

        this.contratoRepository = contratoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorService = fornecedorService;
    }

    public List<Contrato> listar() {
        return contratoRepository.findAll();
    }

    public Contrato buscar(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Contrato não encontrado."));
    }

    public String validar(ContratoRequest request) {

        if (request.getFornecedorId() == null) {
            return "O fornecedor é obrigatório.";
        }

        if (request.getProdutoServico() == null || request.getProdutoServico().isBlank()) {
            return "O Produto / Serviço é obrigatório.";
        }

        if (request.getValor() == null) {
            return "O valor é obrigatório.";
        }

        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            return "O valor deve ser maior que zero.";
        }

        if (request.getDataInicio() == null) {
            return "A data de início é obrigatória.";
        }

        if (request.getDataFim() == null) {
            return "A data de fim é obrigatória.";
        }

        if (request.getDataInicio().isAfter(request.getDataFim())) {
            return "A data de início não pode ser posterior à data de fim do contrato.";
        }

        boolean fornecedorExiste = fornecedorRepository.existsById(request.getFornecedorId());
        if (!fornecedorExiste) {
            return "O fornecedor selecionado não é válido ou não existe.";
        }

        return null;
    }

    public Contrato salvar(ContratoRequest request) {
        String erroValidacao = validar(request);
        if (erroValidacao != null) {
            throw new IllegalArgumentException(erroValidacao);
        }

        Fornecedor fornecedor = fornecedorRepository.findById(request.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado."));

        Contrato contrato = new Contrato();
        contrato.setProdutoServico(request.getProdutoServico());
        contrato.setDescricao(request.getDescricao());
        contrato.setValor(request.getValor());
        contrato.setDataInicio(request.getDataInicio());
        contrato.setDataFim(request.getDataFim());
        contrato.setFornecedor(fornecedor);

        return contratoRepository.save(contrato);
    }

    public void atualizar(ContratoRequest request) {
        String erroValidacao = validar(request);
        if (erroValidacao != null) {
            throw new IllegalArgumentException(erroValidacao);
        }

        Contrato contrato = contratoRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato inválido para atualização."));

        contrato.setProdutoServico(request.getProdutoServico());
        contrato.setDescricao(request.getDescricao());
        contrato.setValor(request.getValor());
        contrato.setDataInicio(request.getDataInicio());
        contrato.setDataFim(request.getDataFim());

        Fornecedor fornecedor = fornecedorService.buscarPorId(request.getFornecedorId());
        contrato.setFornecedor(fornecedor);

        contratoRepository.save(contrato);
    }

    public void excluir(Long id) {
        Contrato contrato = buscar(id);

        if (contrato.getDataFim() != null && !contrato.getDataFim().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível excluir o contrato que está em vigor '" + contrato.getProdutoServico() +
                    "' (Vence em: " + contrato.getDataFim() + ").");
        }

        contratoRepository.delete(contrato);
    }

    public ContratoRequest buscarParaEdicao(Long id) {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado com o ID: " + id));

        ContratoRequest request = new ContratoRequest();

        request.setId(contrato.getId());

        request.setProdutoServico(contrato.getProdutoServico());
        request.setDescricao(contrato.getDescricao());
        request.setValor(contrato.getValor());
        request.setDataInicio(contrato.getDataInicio());
        request.setDataFim(contrato.getDataFim());

        if (contrato.getFornecedor() != null) {
            request.setFornecedorId(contrato.getFornecedor().getId());
        }

        return request;
    }
}
