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

public class CriarEventoView extends FormularioEventoView {
    private int idUsuario;

    public CriarEventoView(JFrame parent, int idUsuario) {
        super(parent, "Adicionar Novo Evento");
        this.idUsuario = idUsuario;
        configurarAcaoPrincipal();
    }

    private void configurarAcaoPrincipal() {
        btnPrincipal.setText("Salvar");
        btnPrincipal.addActionListener(this::salvarEvento);
    }

    private void salvarEvento(ActionEvent e) {
        if (!validarCampos()) return;

        try {
            Evento novoEvento = new Evento(
                txtNome.getText().trim(),
                parseData(txtDataInicio.getText().trim()),
                parseData(txtDataFim.getText().trim()),
                txtDescricao.getText().trim(),
                idUsuario
            );

            new EventoDAO().criarEvento(novoEvento);
            JOptionPane.showMessageDialog(this, "Evento criado com sucesso!");
            dispose();
            
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, 
                "Formato de data inválido! Use dd/MM/yyyy", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao criar evento: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}