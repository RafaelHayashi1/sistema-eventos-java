package views;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FormularioEventoView extends JDialog {
    protected JTextField txtNome;
    protected JTextField txtDataInicio;
    protected JTextField txtDataFim;
    protected JTextArea txtDescricao;
    protected JButton btnPrincipal;
    protected JButton btnCancelar;

    public FormularioEventoView(JFrame parent, String titulo) {
        super(parent, titulo, true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        inicializarComponentes();
    }

    protected void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelCampos = new JPanel(new GridLayout(4, 2, 10, 10));
        
        painelCampos.add(new JLabel("Nome do Evento:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Data Início (dd/MM/yyyy):"));
        txtDataInicio = new JTextField();
        painelCampos.add(txtDataInicio);

        painelCampos.add(new JLabel("Data Fim (dd/MM/yyyy):"));
        txtDataFim = new JTextField();
        painelCampos.add(txtDataFim);

        painelCampos.add(new JLabel("Descrição:"));
        txtDescricao = new JTextArea(3, 20);
        painelCampos.add(new JScrollPane(txtDescricao));


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPrincipal = new JButton();
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnPrincipal);


        btnCancelar.addActionListener(e -> dispose());

        painelPrincipal.add(painelCampos, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        add(painelPrincipal);
    }

    protected void preencherCampos(String nome, Date dataInicio, Date dataFim, String descricao) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtNome.setText(nome);
        txtDataInicio.setText(sdf.format(dataInicio));
        txtDataFim.setText(sdf.format(dataFim));
        txtDescricao.setText(descricao);
    }

    protected boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || 
            txtDataInicio.getText().trim().isEmpty() || 
            txtDataFim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Preencha todos os campos obrigatórios!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    protected Date parseData(String dataStr) throws java.text.ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
    }
}