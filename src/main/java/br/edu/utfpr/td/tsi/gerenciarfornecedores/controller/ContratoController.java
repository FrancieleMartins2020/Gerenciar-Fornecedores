package br.edu.utfpr.td.tsi.gerenciarfornecedores.controller;

import br.edu.utfpr.td.tsi.gerenciarfornecedores.dto.ContratoRequest;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.service.ContratoService;
import br.edu.utfpr.td.tsi.gerenciarfornecedores.service.FornecedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contrato")
@CrossOrigin
public class ContratoController {

    private final ContratoService service;
    private final FornecedorService fornecedorService;

    public ContratoController(ContratoService service, FornecedorService fornecedorService) {
        this.service = service;
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contratos", service.listar());
        return "contrato";
    }

    @GetMapping("/novoContrato")
    public String novoContratoForm(Model model) {
        model.addAttribute("contratoRequest", new ContratoRequest());
        model.addAttribute("fornecedores", fornecedorService.listar());
        return "formularioContrato";
    }

    @PostMapping("/cadastrarContrato")
    public String salvar(@ModelAttribute("contratoRequest") ContratoRequest request, Model model) {
        try {
            service.salvar(request);
            return "redirect:/contrato";
        } catch (Exception e) {
            // CORREÇÃO: Mantém a mensagem do service e passa de volta o DTO 'request' preenchido
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("contratoRequest", request);
            model.addAttribute("fornecedores", fornecedorService.listar());
            return "formularioContrato";
        }
    }

    @GetMapping("/editarContrato")
    public String editarContratoForm(@RequestParam("id") Long id, Model model) {
        try {
            ContratoRequest request = service.buscarParaEdicao(id);
            model.addAttribute("contratoRequest", request);
            model.addAttribute("fornecedores", fornecedorService.listar());
            return "editarFormularioContrato";
        } catch (Exception e) {
            return "redirect:/contrato";
        }
    }

    @PostMapping("/editarCadastroContrato")
    public String atualizar(@ModelAttribute("contratoRequest") ContratoRequest request, Model model) {
        try {
            service.atualizar(request);
            return "redirect:/contrato";
        } catch (Exception e) {
            // CORREÇÃO: Mantém a mensagem do service e passa de volta o DTO 'request' com o ID oculto e os dados digitados
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("contratoRequest", request);
            model.addAttribute("fornecedores", fornecedorService.listar());
            return "editarFormularioContrato";
        }
    }

    @GetMapping("/removerContrato")
    public String excluir(@RequestParam("id") Long id, Model model) {
        try {
            service.excluir(id);
            return "redirect:/contrato";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("contratos", service.listar());
            return "contrato";
        }
    }
}
