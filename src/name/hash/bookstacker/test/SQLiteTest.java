package name.hash.bookstacker.test;

import java.util.List;

import name.hash.bookstacker.BookStacker.LibraryTable;
import name.hash.bookstacker.model.Book;
import name.hash.bookstacker.model.DefaultBook;
import name.hash.bookstacker.view.BookListDAO;
import name.hash.bookstacker.view.BookStackDbHelper;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class SQLiteTest extends AndroidTestCase {
	private BookStackDbHelper helper;

	public void setUp() {
		helper = new BookStackDbHelper(new RenamingDelegatingContext(getContext(), "test_"));
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.execSQL("insert into " + LibraryTable.getTableName() + "(" + LibraryTable.title.getColumnName()
					+ ", " + LibraryTable.vol.getColumnName() + ", " + LibraryTable.author.getColumnName() + ", "
					+ LibraryTable.publisher.getColumnName() + ") values ('骨の袋', 1, 'スティーブン・キング', '新潮社')");
			db.execSQL("insert into " + LibraryTable.getTableName() + "(" + LibraryTable.title.getColumnName()
					+ ", " + LibraryTable.vol.getColumnName() + ", " + LibraryTable.author.getColumnName() + ", "
					+ LibraryTable.publisher.getColumnName() + ") values ('レディー・ジョーカー', 1, '高村薫', '角川')");
			db.execSQL("insert into " + LibraryTable.getTableName() + "(" + LibraryTable.title.getColumnName()
					+ ", " + LibraryTable.vol.getColumnName() + ", " + LibraryTable.author.getColumnName() + ", "
					+ LibraryTable.publisher.getColumnName() + ") values ('カラフル', 1, '森 絵都', '理論社')");
		} finally {
			db.close();
		}
	}

	public void tearDown() {
		helper.close();
	}

	public void testFindAllBooks() {
		SQLiteDatabase db = helper.getReadableDatabase();
		try {
			BookListDAO dao = new BookListDAO(db);
			List<Book> books = dao.findAllBooks();
			assertEquals("検索件数が一致する", 3, books.size());

			assertEquals("1番目の本のタイトルが一致する", "骨の袋", books.get(0).getTitle());
			assertEquals("2番目の本の著者が一致する", "高村薫", books.get(1).getAuthor());
			assertEquals("3番目の本の出版社が一致する", "理論社", books.get(2).getPublisher());
			assertEquals("3番目の本の巻数が一致する", 1, books.get(2).getVol());
		} finally {
			db.close();
		}
	}

	public void testInsertBook() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			BookListDAO dao = new BookListDAO(db);
			DefaultBook book = new DefaultBook();
			dao.insertBook(book);
			List<Book> books = dao.findAllBooks();
			Book insertBook = books.get(3);
			assertEquals("検索件数が一致する", 4, books.size());
			assertEquals("挿入した本のタイトルが一致する", book.getTitle(), insertBook.getTitle());
			assertEquals("挿入した本の著者が一致する", book.getAuthor(), insertBook.getAuthor());
			assertEquals("挿入した本の巻数が一致する", book.getVol(), insertBook.getVol());
			assertEquals("挿入した本の出版社が一致する", book.getPublisher(), insertBook.getPublisher());
		} finally {
			db.close();
		}
	}

	public void testUpdateBook() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			BookListDAO dao = new BookListDAO(db);
			dao.updateBook(1, new DefaultBook());
		} finally {
			db.close();
		}
	}
}
