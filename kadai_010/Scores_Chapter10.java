package kadai_010;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Scores_Chapter10 {
    public static void main(String[] args) {

        Connection con = null;
        Statement statement = null;
        Statement statementForGetting = null;
        Statement statementForOrder = null;

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/challenge_java",
                    "root",
                    "Satou0913"
            );

            System.out.println("データベース接続成功：" + con.toString());

            // SQLクエリを準備
            statement = con.createStatement();
            String sql = "UPDATE scores SET score_math = 95, score_english = 80 WHERE id = 5;";
            String getting = "SELECT * FROM scores";

            // レコード更新
            System.out.println("レコード更新を実行します");
            int rowCnt = statement.executeUpdate(sql);
            System.out.println(rowCnt + "件のレコードが更新されました");

            // データ取得（全データ取得）
            statementForGetting = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet all = statementForGetting.executeQuery(getting);

            // 並べ替え
            statementForOrder = con.createStatement();
            String order = "SELECT id, score_math, score_english FROM scores ORDER BY score_math DESC, score_english DESC;";
            ResultSet result = statementForOrder.executeQuery(order);

            // 出力
            System.out.println("数学・英語の点数が高い順に並べ替えました");
            while (result.next()) {
                int id = result.getInt("id");
                int score_math = result.getInt("score_math");
                int score_english = result.getInt("score_english");

                // すべてのレコードを検索
                all.beforeFirst(); // ResultSetを最初の位置に戻す
                while(all.next()) {
                    if(all.getInt("id") == id) {
                        String name = all.getString("name");

                        System.out.println(result.getRow() + "件目：生徒ID=" + id
                                + "／氏名=" + name + "／数学=" + score_math 
                                + "／英語=" + score_english);
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("エラー発生:" + e.getMessage());

        } finally {
            // 使用したオブジェクトを解放
            try {
                if (statement != null) statement.close();
                if (statementForGetting != null) statementForGetting.close();
                if (statementForOrder != null) statementForOrder.close();
                if (con != null) con.close();
            } catch (SQLException ignore) {}
        }
    }
}