package name.hash.bookstacker.test;

import java.util.List;

import name.hash.bookstacker.BookStacker.LibraryTable;
import name.hash.bookstacker.model.Book;
import name.hash.bookstacker.model.DefaultBook;
import name.hash.bookstacker.view.Librarian;
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
					+ LibraryTable.publisher.getColumnName() + ") values ('���̑�', 1, '�X�e�B�[�u���E�L���O', '�V����')");
			db.execSQL("insert into " + LibraryTable.getTableName() + "(" + LibraryTable.title.getColumnName()
					+ ", " + LibraryTable.vol.getColumnName() + ", " + LibraryTable.author.getColumnName() + ", "
					+ LibraryTable.publisher.getColumnName() + ") values ('���f�B�[�E�W���[�J�[', 1, '�����O', '�p��')");
			db.execSQL("insert into " + LibraryTable.getTableName() + "(" + LibraryTable.title.getColumnName()
					+ ", " + LibraryTable.vol.getColumnName() + ", " + LibraryTable.author.getColumnName() + ", "
					+ LibraryTable.publisher.getColumnName() + ") values ('�J���t��', 1, '�X �G�s', '���_��')");
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
			Librarian dao = new Librarian(db);
			List<Book> books = dao.findAllBooks();
			assertEquals("������������v����", 3, books.size());

			assertEquals("1�Ԗڂ̖{�̃^�C�g������v����", "���̑�", books.get(0).getTitle());
			assertEquals("2�Ԗڂ̖{�̒��҂���v����", "�����O", books.get(1).getAuthor());
			assertEquals("3�Ԗڂ̖{�̏o�ŎЂ���v����", "���_��", books.get(2).getPublisher());
			assertEquals("3�Ԗڂ̖{�̊�������v����", 1, books.get(2).getVol());
		} finally {
			db.close();
		}
	}
	public void testFindById(){
		SQLiteDatabase db = helper.getReadableDatabase();
		try{
			Librarian dao = new Librarian(db);
			Book findBook = dao.findById(3);
			assertEquals("�{�̃^�C�g������v����", "���f�B�[�E�W���[�J�[",findBook.getTitle());
			assertEquals("�{�̒��҂���v����", "�����O",findBook.getAuthor());
		}finally{
			db.close();
		}
	}
	public void testInsertBook() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Librarian dao = new Librarian(db);
			DefaultBook book = new DefaultBook();
			dao.insertBook(book);
			List<Book> books = dao.findAllBooks();
			Book insertBook = books.get(3);
			assertEquals("������������v����", 4, books.size());
			assertEquals("�}�������{�̃^�C�g������v����", book.getTitle(), insertBook.getTitle());
			assertEquals("�}�������{�̒��҂���v����", book.getAuthor(), insertBook.getAuthor());
			assertEquals("�}�������{�̊�������v����", book.getVol(), insertBook.getVol());
			assertEquals("�}�������{�̏o�ŎЂ���v����", book.getPublisher(), insertBook.getPublisher());
		} finally {
			db.close();
		}
	}

	public void testUpdateBook() {
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Librarian dao = new Librarian(db);
			DefaultBook book = new DefaultBook();
			
			boolean updateBook = dao.updateBook(1, book);
			
			List<Book> books = dao.findAllBooks();
			for (Book book2 : books) {
				System.out.println(book2.getTitle());
			}
			Book updatedBook = books.get(0);
			assertTrue(updateBook);
			assertEquals("������������v����", 3, books.size());
			assertEquals("�}�������{�̃^�C�g������v����", book.getTitle(), updatedBook.getTitle());
			assertEquals("�}�������{�̒��҂���v����", book.getAuthor(), updatedBook.getAuthor());
			assertEquals("�}�������{�̊�������v����", book.getVol(), updatedBook.getVol());
			assertEquals("�}�������{�̏o�ŎЂ���v����", book.getPublisher(), updatedBook.getPublisher());
		} finally {
			db.close();
		}
	}
}
