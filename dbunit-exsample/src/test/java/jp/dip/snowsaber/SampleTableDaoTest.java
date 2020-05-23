package jp.dip.snowsaber;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleTableDaoTest {

	private static Connection con;

	private SampleTableDao dao;

	private static final String url = "";// TODO 接続先のDB

	private static final String user = "postgres";

	private static final String passwd = "";

	@BeforeClass
	public static void setupClass() throws Exception {
		con = DriverManager.getConnection(url, user, passwd);
		con.setAutoCommit(false);
	}

	@Before
	public void setup() {
		dao = new SampleTableDao(con);
	}

	@After
	public void after() throws Exception {
		con.rollback();
	}

	@Test
	public void findSampleTableAll() throws Exception {

		IDatabaseConnection dbconn = new DatabaseConnection(con);

		// データの事前登録処理
		File dataExcel = new File(getClass().getResource("/sample_table_data.xlsx").getPath());
		IDataSet dataset = new XlsDataSet(dataExcel);

		// ExcelのデータをDBへ適用する
		DatabaseOperation.CLEAN_INSERT.execute(dbconn, dataset);

		// 処理を実行する
		List<SampleTable> list = dao.findSampleTableAll();

		// 検証処理
		assertEquals(10, list.size());
	}

	@Test
	public void findSampleTable() throws Exception {

		IDatabaseConnection dbconn = new DatabaseConnection(con);

		// データの事前登録処理
		File dataExcel = new File(getClass().getResource("/sample_table_data.xlsx").getPath());
		IDataSet dataset = new XlsDataSet(dataExcel);

		// ExcelのデータをDBへ適用する
		DatabaseOperation.CLEAN_INSERT.execute(dbconn, dataset);

		// 処理を実行する
		Long id = 10L;
		SampleTable st = dao.findSampleTable(id).get();

		// 検証処理
		assertEquals("１０番目のデータ", st.getDocText());
		LocalDateTime d1 = LocalDateTime.of(2020, 05, 05, 10, 30, 01);
		Timestamp exNowTime = new Timestamp(Date.from(d1.atZone(ZoneId.systemDefault()).toInstant()).getTime());
		assertEquals(exNowTime, st.getNowTime());

	}

	@Test
	public void insertSampleTable() throws Exception {


		SampleTable st = new SampleTable();
		st.setId(100L);
		st.setDocText("テスト用のインサートデータ");
		st.setNowTime(new Timestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).getTime()));
		int insertCount = dao.insertSampleTable(st);

		assertEquals(1, insertCount);

		String tableName = "sample_table";

        // 期待値を取得
		File dataExcel = new File(getClass().getResource("/sample_table_data_insert_result.xlsx").getPath());
		IDataSet expectedDataSet = new XlsDataSet(dataExcel);
		ITable expectedTable = expectedDataSet.getTable(tableName);

        // 実際値を取得
		IDatabaseConnection dbconn = new DatabaseConnection(con);
        ITable actualTable = dbconn.createDataSet().getTable(tableName);

        // 全てのカラムを比較する場合(今回の場合システム日時の登録のため必ずエラーとなる)
        //Assertion.assertEquals(expectedTable, actualTable);

        // システム日時の登録カラムなど除外用のカラムを追加する場合 フィルターを設定する
        String[] filterColumns = {"now_time"};
        ITable expectedFilterTable = DefaultColumnFilter.excludedColumnsTable(expectedTable, filterColumns);
        ITable actualFilterTable = DefaultColumnFilter.excludedColumnsTable(actualTable, filterColumns);
        // 比較
        Assertion.assertEquals(expectedFilterTable, actualFilterTable);
	}

	@Test
	public void updateSampleTable() throws Exception {

		IDatabaseConnection dbconn = new DatabaseConnection(con);

		// データの事前登録処理
		File dataExcel = new File(getClass().getResource("/sample_table_data.xlsx").getPath());
		IDataSet dataset = new XlsDataSet(dataExcel);

		// ExcelのデータをDBへ適用する
		DatabaseOperation.CLEAN_INSERT.execute(dbconn, dataset);

		SampleTable st = new SampleTable();
		st.setId(10L);
		st.setDocText("テスト用の更新データ");
		LocalDateTime d1 = LocalDateTime.of(2020, 05, 05, 12, 20, 52);
		st.setNowTime(new Timestamp(Date.from(d1.atZone(ZoneId.systemDefault()).toInstant()).getTime()));
		int updateCount = dao.updateSampleTable(st);

		assertEquals(1, updateCount);

		String tableName = "sample_table";

        // 期待値を取得
		File exExcel = new File(getClass().getResource("/sample_table_data_update_result.xlsx").getPath());
		IDataSet expectedDataSet = new XlsDataSet(exExcel);
		ITable expectedTable = expectedDataSet.getTable(tableName);

        // 実際値を取得
        ITable actualTable = dbconn.createDataSet().getTable(tableName);

        // 全てのカラムを比較
        Assertion.assertEquals(expectedTable, actualTable);

	}

}
