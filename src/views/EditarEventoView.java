package views;

import dao.EventoDAO;
import models.Evento;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarEventoView extends FormularioEventoView {
    private Evento evento;
    private Runnable callbackAtualizacao;

    public EditarEventoView(JFrame parent, Evento evento, Runnable callbackAtualizacao) {
        super(parent, "Editar Evento");
        this.evento = evento;
        this.callbackAtualizacao = callbackAtualizacao;
        preencherCampos();
        configurarAcaoPrincipal();
    }

    private void preencherCampos() {
        super.preencherCampos(
            evento.getNomeEvento(),
            evento.getDataInicio(),
            evento.getDataFim(),
            evento.getDescricao()
        );
    }

    private void configurarAcaoPrincipal() {
        btnPrincipal.setText("Salvar Alterações");
        btnPrincipal.addActionListener(this::salvarAlteracoes);
    }

    private void salvarAlteracoes(ActionEvent e) {
        if (!validarCampos()) return;

        try {
            evento.setNomeEvento(txtNome.getText().trim());
            evento.setDataInicio(parseData(txtDataInicio.getText().trim()));
            evento.setDataFim(parseData(txtDataFim.getText().trim()));
            evento.setDescricao(txtDescricao.getText().trim());

            new EventoDAO().atualizarEvento(evento);
            
            JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!");
            callbackAtualizacao.run(); 
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}