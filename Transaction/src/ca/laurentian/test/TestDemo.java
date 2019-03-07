package ca.laurentian.test;

import ca.laurentian.util.JDBCUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDemo {
    @Test
    public void testTransaction() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConn();
            // 连接，事务默认就是自动提交的。 关闭自动提交。
            conn.setAutoCommit(false);
            String sql = "update account set money = money - ? where id = ?";
            ps = conn.prepareStatement(sql);
            // 扣钱， 扣ID为1的100块钱
            ps.setInt(1, 100);
            ps.setInt(2, 1);
            ps.executeUpdate();
            // 中途出现异常
            int a = 10 / 0;
            // 加钱， 给ID为2加100块钱
            ps.setInt(1, -100);
            ps.setInt(2, 2);
            ps.executeUpdate();
            // 成功： 提交事务。
            conn.commit();
        } catch (Exception e) {
            // 失败： 回滚事务
            try {
                conn.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtil.release(conn, ps, rs);
        }
    }
}
