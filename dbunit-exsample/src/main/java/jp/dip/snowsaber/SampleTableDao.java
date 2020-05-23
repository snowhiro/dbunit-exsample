package jp.dip.snowsaber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SampleTableDao {

	private Connection con;

	public SampleTableDao(Connection con) {
		this.con = con;
	}

	/**
	 * 検索のメソッドの場合
	 *
	 * @return サンプルテーブルの全件データ
	 */
	public List<SampleTable> findSampleTableAll() {
		String selectSql = "SELECT * FROM sample_table";
		try (PreparedStatement stmt = con.prepareStatement(selectSql)) {
			ResultSet rs = stmt.executeQuery();
			List<SampleTable> list = new ArrayList<>();
			while(rs.next()) {
				SampleTable sampleTable = new SampleTable();
				sampleTable.setId(rs.getLong("id"));
				sampleTable.setDocText(rs.getString("doc_text"));
				sampleTable.setNowTime(rs.getTimestamp("now_time"));
				list.add(sampleTable);
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException("Daoの実行でエラーが発生しました。", e);
		}
	}

	/**
	 * 指定したIDのデータを取得する
	 *
	 * @param id 取得対象のID
	 * @return 指定データ
	 */
	public Optional<SampleTable> findSampleTable(Long id) {
		String selectSql = "SELECT * FROM sample_table where id = ?";
		try (PreparedStatement stmt = con.prepareStatement(selectSql)) {
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			SampleTable st = null;
			while(rs.next()) {
				st = new SampleTable();
				st.setId(rs.getLong("id"));
				st.setDocText(rs.getString("doc_text"));
				st.setNowTime(rs.getTimestamp("now_time"));
			}
			return Optional.ofNullable(st);
		} catch (Exception e) {
			throw new RuntimeException("Daoの実行でエラーが発生しました。", e);
		}
	}

	/**
	 * 登録メソッドの場合
	 * @param st 登録データ
	 * @return 登録件数
	 */
	public int insertSampleTable(SampleTable st) {
		String insertSql = "insert into sample_table (id, doc_text, now_time) values (?, ?, ?)";
		try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
			stmt.setLong(1, st.getId());
			stmt.setString(2, st.getDocText());
			stmt.setTimestamp(3, st.getNowTime());
			return stmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Daoの実行でエラーが発生しました。", e);
		}
	}

	/**
	 * 更新のメソッドの場合
	 * @param st 更新データ
	 * @return 更新件数
	 */
	public int updateSampleTable(SampleTable st) {
		String insertSql = "update sample_table set doc_text = ? , now_time = ? where id = ?";
		try (PreparedStatement stmt = con.prepareStatement(insertSql)) {

			stmt.setString(1, st.getDocText());
			stmt.setTimestamp(2, st.getNowTime());

			stmt.setLong(3, st.getId());

			return stmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Daoの実行でエラーが発生しました。", e);
		}
	}

}
