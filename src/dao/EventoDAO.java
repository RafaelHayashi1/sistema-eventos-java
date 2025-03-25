package dao;

import models.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
	
	// da views CriarEvento
    public void criarEvento(Evento evento) throws SQLException {
        String sql = "INSERT INTO Eventos (nome_evento, data_inicio, data_fim, descricao, id_usuario) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, evento.getNomeEvento());
            stmt.setDate(2, new java.sql.Date(evento.getDataInicio().getTime()));
            stmt.setDate(3, new java.sql.Date(evento.getDataFim().getTime()));
            stmt.setString(4, evento.getDescricao());
            stmt.setInt(5, evento.getIdUsuario());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    evento.setIdEvento(rs.getInt(1));
                }
            }
        }
    }

    // da views ListaEventos
    public List<Evento> listarEventosPorUsuario(int idUsuario) throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Eventos WHERE id_usuario = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evento evento = new Evento(
                        rs.getString("nome_evento"),
                        rs.getDate("data_inicio"),
                        rs.getDate("data_fim"),
                        rs.getString("descricao"),
                        rs.getInt("id_usuario")
                    );
                    evento.setIdEvento(rs.getInt("id_evento"));
                    eventos.add(evento);
                }
            }
        }
        return eventos;
    }
    
    public boolean excluirEvento(int idEvento) throws SQLException {
        String sql = "DELETE FROM Eventos WHERE id_evento = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEvento);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    // da views EditarEvento
    public void atualizarEvento(Evento evento) throws SQLException {
        String sql = "UPDATE Eventos SET nome_evento = ?, data_inicio = ?, data_fim = ?, descricao = ? WHERE id_evento = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, evento.getNomeEvento());
            stmt.setDate(2, new java.sql.Date(evento.getDataInicio().getTime()));
            stmt.setDate(3, new java.sql.Date(evento.getDataFim().getTime()));
            stmt.setString(4, evento.getDescricao());
            stmt.setInt(5, evento.getIdEvento());
            
            stmt.executeUpdate();
        }
    }
    
    public Evento buscarEventoPorId(int idEvento) throws SQLException {
        String sql = "SELECT * FROM Eventos WHERE id_evento = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEvento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Evento evento = new Evento(
                        rs.getString("nome_evento"),
                        rs.getDate("data_inicio"),
                        rs.getDate("data_fim"),
                        rs.getString("descricao"),
                        rs.getInt("id_usuario")
                    );
                    evento.setIdEvento(rs.getInt("id_evento"));
                    return evento;
                }
            }
        }
        return null;
    }
    
    
    
    
    
    
}