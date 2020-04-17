package top.kwseeker.developkit.distributedtransaction.jta;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JTADemo {

    public static MysqlXADataSource getDataSource(String connStr, String user, String pwd) {
        MysqlXADataSource ds = new MysqlXADataSource();
        ds.setUrl(connStr);
        ds.setUser(user);
        ds.setPassword(pwd);
        return ds;
    }

    public static void main(String[] args) {
        String connStr1 = "jdbc:mysql://localhost:3306/jtaorder";
        String connStr2 = "jdbc:mysql://localhost:3306/jtastock";

        try {
            // 获取分布式数据源连接
            MysqlXADataSource ds1 = getDataSource(connStr1, "root", "123456");
            MysqlXADataSource ds2 = getDataSource(connStr2, "root", "123456");
            XAConnection xaConnection1 = ds1.getXAConnection();
            Connection connection1 = xaConnection1.getConnection();
            XAConnection xaConnection2 = ds2.getXAConnection();
            Connection connection2 = xaConnection2.getConnection();

            //
            XAResource xaResource1 = xaConnection1.getXAResource();
            Statement statement1 = connection1.createStatement();
            XAResource xaResource2 = xaConnection2.getXAResource();
            Statement statement2 = connection2.createStatement();

            //创建事务分支的xid
            Xid xid1 = new MysqlXid(new byte[] {0x01}, new byte[]{0x02}, 100);
            Xid xid2 = new MysqlXid(new byte[] {0x011}, new byte[]{0x012}, 100);

            //
            xaResource1.start(xid1, XAResource.TMNOFLAGS);
            int update1Result = statement1.executeUpdate("insert into jtaorder.orderinfo (userid, state, skuno) values('10002', 'init', '3892');");
            xaResource1.end(xid1, XAResource.TMSUCCESS);
            xaResource2.start(xid2, XAResource.TMNOFLAGS);
            int update2Result = statement2.executeUpdate("insert into jtastock.stock (skuno, realstock, prestock) values('2383', '50', '0');");
            xaResource2.end(xid2, XAResource.TMSUCCESS);
            //两阶段提交协议第一阶段
            int ret1 = xaResource1.prepare(xid1);
            int ret2 = xaResource2.prepare(xid2);
            //两阶段提交协议第二阶段, 第一阶段执行成功执行commit，否则回退
            if(XAResource.XA_OK == ret1 && XAResource.XA_OK == ret2) {
                xaResource1.commit(xid1, false);
                xaResource2.commit(xid2, false);
                System.out.println("result1:" + update1Result + ", result2:" + update2Result);
            } else {
                xaResource1.rollback(xid1);
                xaResource2.rollback(xid2);
                System.out.println("rollback all");
            }
        } catch (SQLException | XAException e) {
            e.printStackTrace();
        }
    }
}
