package br.edu.utfpr.td.tsi.gerenciarfornecedores.controller;

import br.edu.utfpr.td.tsi.gerenciarfornecedores.dto.FornecedorRequest;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.model.Fornecedor;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.service.FornecedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fornecedor")
@CrossOrigin
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {

        List<Fornecedor> lista = service.listar();
        model.addAttribute("fornecedores", lista);
        return "fornecedor";
    }

    @GetMapping("/pesquisarFornecedor")
    public String buscarPorCnpj(@RequestParam("cnpj") String cnpj, Model model) {
        try {
            Fornecedor f = service.buscarPorCnpj(cnpj);
            List<Fornecedor> lista = new ArrayList<>();
            if (f != null) {
                lista.add(f);
            }
            model.addAttribute("fornecedores", lista);
        } catch (Exception e) {
            model.addAttribute("erro", "Fornecedor com CNPJ " + cnpj + " não foi encontrado.");
            model.addAttribute("fornecedores", service.listar());
        }
        return "fornecedor";
    }

    @GetMapping("/novoFornecedor")
    public String novoFornecedorForm(Model model) {
        model.addAttribute("fornecedorRequest", new FornecedorRequest());
        return "formularioFornecedor";
    }

    @PostMapping("/cadastrarFornecedor")
    public String cadastrarFornecedor(@ModelAttribute("fornecedorRequest") FornecedorRequest fornecedorRequest, Model model) {
        try {
            service.salvar(fornecedorRequest);
            return "redirect:/fornecedor";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());

            return "formularioFornecedor";
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro inesperado ao salvar.");
            return "formularioFornecedor";
        }
    }

    // =========================================================================
    // NOVOS MÉTODOS DE EDIÇÃO ATUALIZADOS COM BASE NO ID DO REQUEST
    // =========================================================================

    @GetMapping("/editarFornecedor")
    public String editarFornecedorForm(@RequestParam("id") Long id, Model model) {
        try {
            // Busca o DTO pronto e mapeado pelo Service
            FornecedorRequest request = service.buscarParaEdicao(id);
            model.addAttribute("fornecedorRequest", request);
            return "editarFormularioFornecedor"; // Abre a tela de edição
        } catch (Exception e) {
            return "redirect:/fornecedor";
        }
    }

    @PostMapping("/editarCadastroFornecedor")
    public String editar(@ModelAttribute("fornecedorRequest") FornecedorRequest fornecedorRequest, Model model) {
        try {
            // O próprio objeto fornecedorRequest carrega seu ID de forma oculta da tela
            service.atualizar(fornecedorRequest);
            return "redirect:/fornecedor";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "editarFormularioFornecedor";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            return "editarFormularioFornecedor";
        }
    }

    @GetMapping("/removerFornecedor")
    public String excluir(@RequestParam("id") Long id, Model model) {
        try {
            service.excluir(id);
            return "redirect:/fornecedor";
        } catch (IllegalArgumentException e) {
            // Se cair aqui, significa que o fornecedor possui contratos e o bloqueio funcionou!
            model.addAttribute("erro", e.getMessage());

            model.addAttribute("fornecedores", service.listar()); // Recarrega a tabela de fornecedores
            return "fornecedor"; // Devolve o usuário para a página de listagem exibindo o erro
        }
    }

}
