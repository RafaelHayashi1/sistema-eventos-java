package views;

import dao.EventoDAO;
import models.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListaEventosView extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTable tabelaEventos;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnExcluir,btnSair;
    private int idUsuarioLogado;

    public ListaEventosView(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
        configurarJanela();
        inicializarComponentes();
        carregarEventos();
    }
    
    // janela
    private void configurarJanela() {
        setTitle("Meus Eventos - Usuário ID: " + idUsuarioLogado);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    // tabelas e botoes
    private void inicializarComponentes() {
        String[] colunas = {"ID", "Nome do Evento", "Data Início", "Data Fim", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaEventos = new JTable(tableModel);
        
        btnAdicionar = new JButton("Adicionar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnSair = new JButton("Sair");
        
        configurarListeners();
        
        organizarLayout();
    }

    private void configurarListeners() {
        btnAdicionar.addActionListener(e -> adicionarEvento());
        btnEditar.addActionListener(e -> editarEvento());
        btnExcluir.addActionListener(e -> excluirEvento());
        btnSair.addActionListener(e -> sairEvento());
    }
    
    private void organizarLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        

        JPanel painelBotoesEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoesEsquerda.add(btnAdicionar);
        painelBotoesEsquerda.add(btnEditar);
        painelBotoesEsquerda.add(btnExcluir);
        

        JPanel painelBotaoDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotaoDireita.add(btnSair);
        

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelBotoesEsquerda, BorderLayout.WEST);
        painelSuperior.add(painelBotaoDireita, BorderLayout.EAST);
        

        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(new JScrollPane(tabelaEventos), BorderLayout.CENTER);
        
        add(painelPrincipal);
    }
    
    //BOTOES FUNCS
    private void adicionarEvento() {
        CriarEventoView dialog = new CriarEventoView(this, idUsuarioLogado);
        dialog.setVisible(true);
        carregarEventos(); 
    }
    
    
    private void editarEvento() {
        int linhaSelecionada = tabelaEventos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            try {
                int idEvento = (int) tableModel.getValueAt(linhaSelecionada, 0);
                EventoDAO eventoDAO = new EventoDAO();
                Evento evento = eventoDAO.buscarEventoPorId(idEvento);
                
                new EditarEventoView(
                    this, 
                    evento,
                    this::carregarEventos 
                ).setVisible(true);
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao carregar evento para edição: " + e.getMessage(),
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um evento para editar", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    

    private void excluirEvento() {
        int linhaSelecionada = tabelaEventos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int confirmacao = JOptionPane.showConfirmDialog(
                this, 
                "Tem certeza que deseja excluir este evento?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    int idEvento = (int) tableModel.getValueAt(linhaSelecionada, 0);
                   
                    EventoDAO eventoDAO = new EventoDAO();
                    boolean sucesso = eventoDAO.excluirEvento(idEvento);
                    
                    if (sucesso) {
                        tableModel.removeRow(linhaSelecionada);
                        JOptionPane.showMessageDialog(this, "Evento excluído com sucesso");
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Evento não encontrado no banco de dados",
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao excluir evento:\n" + e.getMessage(),
                        "Erro de Banco de Dados",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um evento para excluir", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void sairEvento() {
    	System.exit(0);
    }

    private void carregarEventos() {
        try {
            EventoDAO eventoDAO = new EventoDAO();
            List<Evento> eventos = eventoDAO.listarEventosPorUsuario(idUsuarioLogado);
            preencherTabela(eventos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar eventos:\n" + e.getMessage(),
                "Erro de Banco de Dados",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Evento> eventos) {
        tableModel.setRowCount(0); 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Evento evento : eventos) {
            tableModel.addRow(new Object[]{
                evento.getIdEvento(),
                evento.getNomeEvento(),
                sdf.format(evento.getDataInicio()),
                sdf.format(evento.getDataFim()),
                evento.getDescricao()
            });
        }
    }
    	
    // lembrar de tirar o parametro dps do sistema de login/registro
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListaEventosView(1).setVisible(true);
        });
    }
}