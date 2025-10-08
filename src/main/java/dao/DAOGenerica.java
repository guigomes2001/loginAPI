package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author @gui_gomes_18
 */

public abstract class DAOGenerica {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    protected Connection getConexao() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource ainda não injetado!");
        }
        return dataSource.getConnection();
    }

//    protected void fechaConexao(AutoCloseable... resources) {
//        for (AutoCloseable resource : resources) {
//            if (resource != null) {
//                try {
//                    resource.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    
    protected void fechaConexao(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void fechaConexao(Connection conn, PreparedStatement pstmt, ResultSet rs) {
    	try {
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fechaConexao(Connection conn, PreparedStatement pstmt) {
        try {
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected <T> T executar(OperacaoDAO<T> operacao) throws SQLException {
        try (Connection conn = getConexao()) {
            return operacao.executar(conn);
        }
    }

    protected <T extends DAOGenerica> T getDAO(Class<T> clazz) {
        return context.getBean(clazz);
    }

    protected UsuarioDAO getUsuarioDAO() {
        return getDAO(UsuarioDAO.class);
    }

    @FunctionalInterface
    protected interface OperacaoDAO<T> {
        T executar(Connection conn) throws SQLException;
    }
}