package name.hash.bookstacker.test.importer;

import java.util.ArrayList;
import java.util.List;

import jp.group.android.atec.sf.importer.FileType;
import jp.group.android.atec.sf.unit.DatabaseTestCase;
import name.hash.bookstacker.BookStacker.LibraryTable;
import name.hash.bookstacker.model.Book;
import name.hash.bookstacker.model.BookBuilder;
import name.hash.bookstacker.view.BookStackDBHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteCsvImporterTest extends DatabaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	@Override
	protected SQLiteOpenHelper createSQLiteOpenHelper() {
		return BookStackDBHelper.getInstance(getDatabaseContext());
	}

	public void testImportData() {
		importData(FileType.Csv, "sqlitedata/" + LibraryTable.getTableName());

		List<Book> books = loadBooks();
		assertEquals(2, books.size());
		assertEquals(books.get(0).getTitle(), "‚Ð‚Æ‚Ð‚ç");
		assertEquals(books.get(1).getPublisher(), "PPP");
	}

	List<Book> loadBooks() {
		Cursor cursor = getSQLiteDatabase().query(
				LibraryTable.getTableName(),
				new String[] { LibraryTable.id.getColumnName(), LibraryTable.title.getColumnName(),
						LibraryTable.author.getColumnName(), LibraryTable.category.getColumnName(),
						LibraryTable.publisher.getColumnName() }, null, null, null, null,
				LibraryTable.id.getColumnName());

		List<Book> books = new ArrayList<Book>();
		try {
			BookBuilder builder;
			while (cursor.moveToNext()) {
				builder = BookBuilder.newBuilder();
				builder.setTitle(cursor.getString(cursor.getColumnIndex(LibraryTable.title.getColumnName())));
				builder.setAuthor(cursor.getString(cursor.getColumnIndex(LibraryTable.author.getColumnName())));
				builder.setVol(cursor.getInt(cursor.getColumnIndex(LibraryTable.vol.getColumnName())));
				builder.setPublisher(cursor.getString(cursor.getColumnIndex(LibraryTable.publisher.getColumnName())));

				books.add(builder.build());
			}
		} finally {
			cursor.close();
		}

		return books;
	}
}
